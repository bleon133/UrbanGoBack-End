package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.repositories.TipoTransporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transport-types")
@RequiredArgsConstructor
public class TransportTypeController {

    private final TipoTransporteRepository tipos;

    @GetMapping
    public List<Map<String, Object>> list() {
        return tipos.findAll().stream()
                .map(t -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", t.getIdTipoTransporte());
                    map.put("nombre", t.getNombre());
                    return map;
                })
                .toList();
    }
}
