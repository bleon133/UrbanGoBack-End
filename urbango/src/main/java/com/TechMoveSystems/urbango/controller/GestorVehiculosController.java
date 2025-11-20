package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.admin.dto.VehicleDtos.*;
import com.TechMoveSystems.urbango.services.ImageStorageService;
import com.TechMoveSystems.urbango.services.VehiculosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/vehicles")
@RequiredArgsConstructor
public class GestorVehiculosController {

    private final VehiculosService service;
    private final ImageStorageService imageStorage;

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
        return imageStorage.storeResized(file, "vehiculos", "vehiculo");
    }
}
