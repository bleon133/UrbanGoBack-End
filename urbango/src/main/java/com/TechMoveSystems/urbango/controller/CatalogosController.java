package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.services.CatalogosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalogos")
@RequiredArgsConstructor
public class CatalogosController {

    private final CatalogosService catalogosService;

    @GetMapping("/bancos")
    public List<Map<String, Object>> listarBancos() {
        return catalogosService.listarBancos();
    }
}
