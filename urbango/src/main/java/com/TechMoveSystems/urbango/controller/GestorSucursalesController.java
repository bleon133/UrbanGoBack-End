package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.admin.dto.BranchDtos.*;
import com.TechMoveSystems.urbango.services.ImageStorageService;
import com.TechMoveSystems.urbango.services.SucursalesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
public class GestorSucursalesController {

    private final SucursalesService service;
    private final ImageStorageService imageStorage;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<BranchSummary> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public BranchDetail get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public BranchDetail createJson(@Valid @RequestBody CreateOrUpdateBranchRequest body) {
        return service.create(body, null);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public BranchDetail createMultipart(
            @RequestPart("data") @Valid CreateOrUpdateBranchRequest body,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        return service.create(body, storePhoto(photo));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public BranchDetail updateJson(
            @PathVariable Integer id,
            @Valid @RequestBody CreateOrUpdateBranchRequest body
    ) {
        return service.update(id, body, null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public BranchDetail updateMultipart(
            @PathVariable Integer id,
            @RequestPart("data") @Valid CreateOrUpdateBranchRequest body,
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
        return imageStorage.storeResized(file, "sucursales", "branch");
    }
}
