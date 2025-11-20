package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.admin.dto.TarifaVehiculoDtos.*;
import com.TechMoveSystems.urbango.services.TarifaVehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/rates/vehicles")
@RequiredArgsConstructor
public class GestorTarifasVehiculoController {

    private final TarifaVehiculoService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<TarifaVehiculoSummary> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public TarifaVehiculoDetail get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public TarifaVehiculoDetail create(@Valid @RequestBody CreateOrUpdateTarifaVehiculoRequest body) {
        return service.create(body);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public TarifaVehiculoDetail update(
            @PathVariable Integer id,
            @Valid @RequestBody CreateOrUpdateTarifaVehiculoRequest body
    ) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
