package com.TechMoveSystems.urbango.services.impl;

import com.TechMoveSystems.urbango.admin.dto.VehicleDtos.*;
import com.TechMoveSystems.urbango.models.Sucursal;
import com.TechMoveSystems.urbango.models.Transporte;
import com.TechMoveSystems.urbango.repositories.SucursalRepository;
import com.TechMoveSystems.urbango.repositories.TransporteRepository;
import com.TechMoveSystems.urbango.services.VehiculosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class VehiculosServiceImpl implements VehiculosService {

    private final TransporteRepository transportes;
    private final SucursalRepository sucursales;

    @Override
    @Transactional(readOnly = true)
    public List<VehicleSummary> list(String tipo, String estado, Integer sucursalId, String search) {
        final String tipoFilter = tipo != null ? tipo.trim().toLowerCase(Locale.ROOT) : null;
        final String estadoFilter = estado != null ? estado.trim().toLowerCase(Locale.ROOT) : null;
        final String searchFilter = search != null ? search.trim().toLowerCase(Locale.ROOT) : null;

        return transportes.findAll().stream()
                .filter(t -> tipoFilter == null || (t.getTipoVehiculo() != null && t.getTipoVehiculo().equalsIgnoreCase(tipoFilter)))
                .filter(t -> estadoFilter == null || (t.getEstado() != null && t.getEstado().equalsIgnoreCase(estadoFilter)))
                .filter(t -> sucursalId == null || (t.getSucursal() != null && sucursalId.equals(t.getSucursal().getIdSucursal())))
                .filter(t -> {
                    if (searchFilter == null || searchFilter.isEmpty()) return true;
                    return (t.getMarca() != null && t.getMarca().toLowerCase(Locale.ROOT).contains(searchFilter))
                            || (t.getModelo() != null && t.getModelo().toLowerCase(Locale.ROOT).contains(searchFilter))
                            || (t.getPlaca() != null && t.getPlaca().toLowerCase(Locale.ROOT).contains(searchFilter));
                })
                .map(this::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDetail get(Integer id) {
        var entity = transportes.findById(id).orElseThrow();
        return toDetail(entity);
    }

    @Override
    @Transactional
    public VehicleDetail create(CreateVehicleRequest request, String photoPath) {
        var sucursal = getSucursal(request.sucursalId());
        var entity = new Transporte();
        applyRequest(entity, request, sucursal, photoPath);
        transportes.save(entity);
        return toDetail(entity);
    }

    @Override
    @Transactional
    public VehicleDetail update(Integer id, CreateVehicleRequest request, String photoPath) {
        var entity = transportes.findById(id).orElseThrow();
        var sucursal = getSucursal(request.sucursalId());
        applyRequest(entity, request, sucursal, photoPath);
        transportes.save(entity);
        return toDetail(entity);
    }

    @Override
    public void delete(Integer id) {
        transportes.deleteById(id);
    }

    private void applyRequest(Transporte entity, CreateVehicleRequest request, Sucursal sucursal, String photoPath) {
        entity.setSucursal(sucursal);
        entity.setTipoVehiculo(request.tipoVehiculo());
        entity.setMarca(request.marca());
        entity.setModelo(request.modelo());
        entity.setAnio(request.anio());
        entity.setPlaca(request.placa());
        entity.setPeso(request.peso());
        entity.setVelocidadMax(request.velocidadMax());
        entity.setColor(request.color());
        entity.setEstado(request.estado());
        if (photoPath != null) {
            entity.setFoto(photoPath);
        }
    }

    private VehicleSummary toSummary(Transporte transporte) {
        return new VehicleSummary(
                transporte.getIdTransporte(),
                transporte.getTipoVehiculo(),
                transporte.getMarca(),
                transporte.getModelo(),
                transporte.getAnio(),
                transporte.getPlaca(),
                transporte.getColor(),
                transporte.getEstado(),
                transporte.getSucursal() != null ? transporte.getSucursal().getIdSucursal() : null,
                transporte.getSucursal() != null ? transporte.getSucursal().getNombre() : null,
                transporte.getFoto()
        );
    }

    private VehicleDetail toDetail(Transporte transporte) {
        return new VehicleDetail(
                transporte.getIdTransporte(),
                transporte.getSucursal() != null ? transporte.getSucursal().getIdSucursal() : null,
                transporte.getSucursal() != null ? transporte.getSucursal().getNombre() : null,
                transporte.getTipoVehiculo(),
                transporte.getMarca(),
                transporte.getModelo(),
                transporte.getAnio(),
                transporte.getPlaca(),
                transporte.getPeso(),
                transporte.getVelocidadMax(),
                transporte.getColor(),
                transporte.getEstado(),
                transporte.getFoto()
        );
    }

    private Sucursal getSucursal(Integer id) {
        return sucursales.findById(id).orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada: " + id));
    }
}
