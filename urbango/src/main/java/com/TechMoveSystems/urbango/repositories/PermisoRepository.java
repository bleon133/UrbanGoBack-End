package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.Permiso;
import com.TechMoveSystems.urbango.models.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    List<Permiso> findByTipoUsuario(TipoUsuario tipoUsuario);
}

