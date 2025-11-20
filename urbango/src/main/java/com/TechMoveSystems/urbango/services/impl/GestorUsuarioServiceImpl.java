package com.TechMoveSystems.urbango.services.impl;

import com.TechMoveSystems.urbango.dto.UserDtos.*;
import com.TechMoveSystems.urbango.models.*;
import com.TechMoveSystems.urbango.repositories.BarrioRepository;
import com.TechMoveSystems.urbango.repositories.DireccionRepository;
import com.TechMoveSystems.urbango.repositories.DomiciliarioRepository;
import com.TechMoveSystems.urbango.repositories.UsuarioRepository;
import com.TechMoveSystems.urbango.services.GestorUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GestorUsuarioServiceImpl implements GestorUsuarioService {
    private final UsuarioRepository usuarios;
    private final DomiciliarioRepository domiciliarios;
    private final DireccionRepository direcciones;
    private final BarrioRepository barrioRepo;
    private final PasswordEncoder encoder;

    @Override
    public List<UserSummary> list() {
        return usuarios.findAll().stream().map(this::toSummary).collect(Collectors.toList());
    }

    @Override
    public UserDetail get(Integer id) {
        var u = usuarios.findById(id).orElseThrow();
        Domiciliario d = (u.getTipoUsuario() == TipoUsuario.DOMICILIARIO) ? domiciliarios.findById(id).orElse(null) : null;
        return toDetail(u, d);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Integer create(CreateUserRequest req, String photoPath, OptionalAddress address) {
        var entity = new Usuario();
        entity.setNombre(req.firstName());
        entity.setApellidos(req.lastName());
        entity.setCorreo(req.email().trim().toLowerCase());
        entity.setContrasena(encoder.encode(req.password()));
        entity.setTipoDocumento(req.documentType());
        entity.setNumeroDocumento(req.documentNumber());
        entity.setFechaNacimiento(req.birthDate());
        entity.setGenero(req.gender());
        entity.setTelefono(req.phone());
        entity.setTipoUsuario(req.userType());
        entity.setEstado(EstadoUsuario.ACTIVO);
        entity.setFotografia(photoPath);
        usuarios.save(entity);

        if (address != null && address.direccionCompleta() != null && !address.direccionCompleta().isBlank()) {
            var dir = new Direccion();
            dir.setDireccionCompleta(address.direccionCompleta());
            dir.setCodigoPostal(address.codigoPostal());
            dir.setDetalle(address.detalle());
            if (address.barrioId() != null) {
                barrioRepo.findById(address.barrioId()).ifPresent(dir::setBarrio);
            }
            direcciones.save(dir);
            entity.setDireccion(dir);
            usuarios.save(entity);
        }

        if (req.userType() == TipoUsuario.DOMICILIARIO) {
            var dom = Domiciliario.builder()
                    .usuario(entity)
                    .disponibilidadLaboral(req.disponibilidadLaboral())
                    .contactoEmergencia(req.contactoEmergencia())
                    .numeroEmergencia(req.numeroEmergencia())
                    .numeroLicencia(req.numeroLicencia())
                    .categoriaMoto(req.categoriaMoto())
                    .experienciaPrevia(req.experienciaPrevia())
                    .build();
            domiciliarios.save(dom);
        }
        return entity.getIdUsuario();
    }

    @Override
    public void changePassword(Integer id, String newPassword) {
        var u = usuarios.findById(id).orElseThrow();
        u.setContrasena(encoder.encode(newPassword));
        usuarios.save(u);
    }

    @Override
    public void delete(Integer id) { usuarios.deleteById(id); }

    @Override
    public void setLicensePhotos(Integer idUsuario, String front, String back) {
        var u = usuarios.findById(idUsuario).orElseThrow();
        if (u.getTipoUsuario() != TipoUsuario.DOMICILIARIO) return;
        var d = domiciliarios.findById(idUsuario).orElse(null);
        if (d == null) return;
        if (front != null) d.setFotoLicenciaFrontal(front);
        if (back != null) d.setFotoLicenciaPosterior(back);
        domiciliarios.save(d);
    }

    private UserSummary toSummary(Usuario u) {
        String role;
        switch (u.getTipoUsuario()) {
            case ADMINISTRADOR -> role = "admin";
            case DOMICILIARIO -> role = "delivery";
            case MANTENIMIENTO -> role = "maintenance";
            default -> role = "client";
        }
        return new UserSummary(
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellidos(),
                u.getCorreo(),
                u.getNumeroDocumento(),
                u.getTipoDocumento() != null ? u.getTipoDocumento().name() : null,
                role,
                u.getFotografia(),
                u.getEstado() == null || u.getEstado() == EstadoUsuario.ACTIVO
        );
    }

    private UserDetail toDetail(Usuario u, Domiciliario d) {
        return new UserDetail(
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellidos(),
                u.getCorreo(),
                u.getNumeroDocumento(),
                u.getTipoDocumento(),
                u.getFechaNacimiento(),
                u.getGenero(),
                u.getTelefono(),
                u.getTipoUsuario(),
                u.getEstado(),
                u.getFotografia(),
                d != null ? d.getDisponibilidadLaboral() : null,
                d != null ? d.getContactoEmergencia() : null,
                d != null ? d.getNumeroEmergencia() : null,
                d != null ? d.getNumeroLicencia() : null,
                d != null ? d.getCategoriaMoto() : null,
                d != null ? d.getExperienciaPrevia() : null,
                d != null ? d.getFotoLicenciaFrontal() : null,
                d != null ? d.getFotoLicenciaPosterior() : null
        );
    }
}
