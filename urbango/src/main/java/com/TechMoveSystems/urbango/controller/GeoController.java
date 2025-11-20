package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.repositories.BarrioRepository;
import com.TechMoveSystems.urbango.repositories.CiudadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/geo")
@RequiredArgsConstructor
public class GeoController {
    private final CiudadRepository ciudades;
    private final BarrioRepository barrios;

    @GetMapping("/ciudades")
    public List<Map<String, Object>> listCiudades() {
        return ciudades.findAll().stream()
                .map(c -> Map.<String,Object>of(
                        "id", c.getIdCiudad(),
                        "nombre", c.getNombreCiudad()
                )).toList();
    }

    @GetMapping("/ciudades/{idCiudad}/barrios")
    public List<Map<String, Object>> listBarrios(@PathVariable Integer idCiudad) {
        return barrios.findByCiudad_IdCiudadOrderByNombreBarrioAsc(idCiudad).stream()
                .map(b -> Map.<String,Object>of(
                        "id", b.getIdBarrio(),
                        "nombre", b.getNombreBarrio()
                )).toList();
    }
}

