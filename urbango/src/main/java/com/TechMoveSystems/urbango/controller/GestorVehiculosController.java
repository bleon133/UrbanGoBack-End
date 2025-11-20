package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.admin.dto.VehicleDtos.*;
import com.TechMoveSystems.urbango.services.VehiculosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/vehicles")
@RequiredArgsConstructor
public class GestorVehiculosController {

    private final VehiculosService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<VehicleSummary> list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) String search
    ) {
        return service.list(type, status, branchId, search);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public VehicleDetail get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public VehicleDetail createJson(@Valid @RequestBody CreateVehicleRequest body) {
        return service.create(body, null);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public VehicleDetail createMultipart(
            @RequestPart("data") @Valid CreateVehicleRequest body,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        return service.create(body, storePhoto(photo));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public VehicleDetail updateJson(
            @PathVariable Integer id,
            @Valid @RequestBody CreateVehicleRequest body
    ) {
        return service.update(id, body, null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public VehicleDetail updateMultipart(
            @PathVariable Integer id,
            @RequestPart("data") @Valid CreateVehicleRequest body,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        return service.update(id, body, storePhoto(photo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    private String storePhoto(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        String base = System.getProperty("app.files.base-dir", "photos");
        Path dir = Paths.get(base, "vehiculos");
        Files.createDirectories(dir);
        String extension = "";
        String original = file.getOriginalFilename();
        if (original != null) {
            int idx = original.lastIndexOf('.');
            if (idx >= 0) extension = original.substring(idx);
        }
        Path target = dir.resolve(UUID.randomUUID() + extension);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return "vehiculos/" + target.getFileName();
    }
}
