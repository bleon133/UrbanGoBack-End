package com.TechMoveSystems.urbango.services.impl;

import com.TechMoveSystems.urbango.admin.dto.BranchDtos.*;
import com.TechMoveSystems.urbango.models.Barrio;
import com.TechMoveSystems.urbango.models.CuentaBancaria;
import com.TechMoveSystems.urbango.models.Direccion;
import com.TechMoveSystems.urbango.models.HorarioAtencion;
import com.TechMoveSystems.urbango.models.Sucursal;
import com.TechMoveSystems.urbango.repositories.BarrioRepository;
import com.TechMoveSystems.urbango.repositories.DireccionRepository;
import com.TechMoveSystems.urbango.repositories.HorarioAtencionRepository;
import com.TechMoveSystems.urbango.repositories.CuentaBancariaRepository;
import com.TechMoveSystems.urbango.repositories.SucursalRepository;
import com.TechMoveSystems.urbango.services.SucursalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SucursalesServiceImpl implements SucursalesService {

    private final SucursalRepository sucursales;
    private final DireccionRepository direcciones;
    private final BarrioRepository barrios;
    private final HorarioAtencionRepository horarios;
    private final CuentaBancariaRepository cuentasBancarias;

    @Override
    @Transactional(readOnly = true)
    public List<BranchSummary> list() {
        return sucursales.findAllByOrderByNombreAsc().stream()
                .map(this::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDetail get(Integer id) {
        var entity = sucursales.findById(id).orElseThrow();
        return toDetail(entity);
    }

    @Override
    @Transactional
    public BranchDetail create(CreateOrUpdateBranchRequest request, String photoPath) {
        var entity = new Sucursal();
        applyRequest(entity, request, photoPath);
        sucursales.save(entity);
        syncHorarios(entity, request);
        syncCuentaBancaria(entity, request);
        return toDetail(entity);
    }

    @Override
    @Transactional
    public BranchDetail update(Integer id, CreateOrUpdateBranchRequest request, String photoPath) {
        var entity = sucursales.findById(id).orElseThrow();
        applyRequest(entity, request, photoPath);
        sucursales.save(entity);
        syncHorarios(entity, request);
        syncCuentaBancaria(entity, request);
        return toDetail(entity);
    }

    @Override
    public void delete(Integer id) {
        cuentasBancarias.deleteBySucursal_IdSucursal(id);
        sucursales.deleteById(id);
    }

    private void applyRequest(Sucursal entity, CreateOrUpdateBranchRequest request, String photoPath) {
        entity.setNombre(request.nombre());
        entity.setRazonSocial(request.razonSocial());
        entity.setNit(request.nit());
        entity.setRepresentanteLegal(request.representanteLegal());
        entity.setDocumentoRepresentante(request.documentoRepresentante());
        entity.setPersonaContacto(request.personaContacto());
        entity.setTelefonoContacto(request.telefonoContacto());
        entity.setCorreoContacto(request.correoContacto());
        entity.setLatitud(request.latitud());
        entity.setLongitud(request.longitud());
        if (photoPath != null) {
            entity.setFoto(photoPath);
        }

        if (request.direccion() != null && !request.direccion().isBlank()) {
            Direccion direccion = entity.getDireccion();
            if (direccion == null) {
                direccion = new Direccion();
            }
            direccion.setDireccionCompleta(request.direccion());
            direccion.setCodigoPostal(request.codigoPostal());
            direccion.setBarrio(resolveBarrio(request.barrioId()));
            direccion.setDetalle(request.detalleDireccion());
            direcciones.save(direccion);
            entity.setDireccion(direccion);
        }
    }

    private void syncHorarios(Sucursal sucursal, CreateOrUpdateBranchRequest request) {
        if (sucursal.getIdSucursal() == null) {
            sucursales.saveAndFlush(sucursal);
        }
        horarios.deleteBySucursal_IdSucursal(sucursal.getIdSucursal());
        if (request.diasAtencion() == null || request.diasAtencion().isEmpty()) {
            return;
        }
        LocalTime apertura = parseTime(request.horarioApertura());
        LocalTime cierre = parseTime(request.horarioCierre());
        for (String dia : request.diasAtencion()) {
            if (dia == null || dia.isBlank()) continue;
            var horario = new HorarioAtencion();
            horario.setSucursal(sucursal);
            horario.setDiaSemana(dia);
            horario.setHoraApertura(apertura);
            horario.setHoraCierre(cierre);
            horarios.save(horario);
        }
    }

    private void syncCuentaBancaria(Sucursal sucursal, CreateOrUpdateBranchRequest request) {
        if (sucursal.getIdSucursal() == null) {
            sucursales.saveAndFlush(sucursal);
        }

        boolean numero = request.numeroCuenta() != null && !request.numeroCuenta().isBlank();
        boolean tipo = request.tipoCuenta() != null && !request.tipoCuenta().isBlank();
        boolean banco = request.bancoId() != null;

        if (!numero && !tipo && !banco) {
            cuentasBancarias.deleteBySucursal_IdSucursal(sucursal.getIdSucursal());
            return;
        }

        if (!numero || !tipo || !banco) {
            throw new IllegalArgumentException("La cuenta bancaria requiere número, tipo y banco.");
        }

        CuentaBancaria cuenta = cuentasBancarias.findBySucursal_IdSucursal(sucursal.getIdSucursal())
                .orElseGet(CuentaBancaria::new);
        cuenta.setSucursal(sucursal);
        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setIdBanco(request.bancoId());
        cuentasBancarias.save(cuenta);
    }

    private LocalTime parseTime(String value) {
        if (value == null || value.isBlank()) return null;
        return LocalTime.parse(value);
    }

    private Barrio resolveBarrio(Integer barrioId) {
        if (barrioId == null) return null;
        return barrios.findById(barrioId)
                .orElseThrow(() -> new IllegalArgumentException("Barrio no encontrado: " + barrioId));
    }

    private BranchSummary toSummary(Sucursal entity) {
        return new BranchSummary(
                entity.getIdSucursal(),
                entity.getNombre(),
                entity.getRazonSocial(),
                entity.getNit(),
                entity.getPersonaContacto(),
                entity.getTelefonoContacto(),
                entity.getCorreoContacto(),
                true
        );
    }

    private BranchDetail toDetail(Sucursal entity) {
        Direccion direccion = entity.getDireccion();
        Integer barrioId = null;
        Integer ciudadId = null;
        String barrioNombre = null;
        String ciudadNombre = null;
        String direccionCompleta = null;
        String codigoPostal = null;
        String detalleDireccion = null;

        if (direccion != null) {
            direccionCompleta = direccion.getDireccionCompleta();
            codigoPostal = direccion.getCodigoPostal();
            detalleDireccion = direccion.getDetalle();
            var barrio = direccion.getBarrio();
            if (barrio != null) {
                barrioId = barrio.getIdBarrio();
                barrioNombre = barrio.getNombreBarrio();
                var ciudad = barrio.getCiudad();
                if (ciudad != null) {
                    ciudadId = ciudad.getIdCiudad();
                    ciudadNombre = ciudad.getNombreCiudad();
                }
            }
        }

        List<HorarioAtencion> horariosAtencion = entity.getIdSucursal() == null
                ? List.of()
                : horarios.findBySucursal_IdSucursal(entity.getIdSucursal());
        List<String> diasAtencion = horariosAtencion.stream()
                .map(HorarioAtencion::getDiaSemana)
                .filter(Objects::nonNull)
                .toList();
        String horarioApertura = horariosAtencion.stream()
                .map(HorarioAtencion::getHoraApertura)
                .filter(Objects::nonNull)
                .map(LocalTime::toString)
                .findFirst()
                .orElse(null);
        String horarioCierre = horariosAtencion.stream()
                .map(HorarioAtencion::getHoraCierre)
                .filter(Objects::nonNull)
                .map(LocalTime::toString)
                .findFirst()
                .orElse(null);

        CuentaBancaria cuenta = entity.getIdSucursal() == null
                ? null
                : cuentasBancarias.findBySucursal_IdSucursal(entity.getIdSucursal()).orElse(null);
        Integer cuentaId = cuenta != null ? cuenta.getIdCuenta() : null;
        String numeroCuenta = cuenta != null ? cuenta.getNumeroCuenta() : null;
        String tipoCuenta = cuenta != null ? cuenta.getTipoCuenta() : null;
        Integer bancoId = cuenta != null ? cuenta.getIdBanco() : null;

        return new BranchDetail(
                entity.getIdSucursal(),
                entity.getNombre(),
                entity.getRazonSocial(),
                entity.getNit(),
                entity.getRepresentanteLegal(),
                entity.getDocumentoRepresentante(),
                entity.getPersonaContacto(),
                entity.getTelefonoContacto(),
                entity.getCorreoContacto(),
                direccionCompleta,
                barrioId,
                ciudadId,
                barrioNombre,
                ciudadNombre,
                codigoPostal,
                entity.getLatitud(),
                entity.getLongitud(),
                entity.getFoto(),
                detalleDireccion,
                horarioApertura,
                horarioCierre,
                diasAtencion,
                cuentaId,
                numeroCuenta,
                tipoCuenta,
                bancoId
        );
    }
}
