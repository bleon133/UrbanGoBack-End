package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.dto.UserDtos.*;
import com.TechMoveSystems.urbango.services.GestorUsuarioService;
import com.TechMoveSystems.urbango.services.ImageStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class GestorUsuariosController {

    private final GestorUsuarioService service;
    private final ImageStorageService imageStorage;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<UserSummary> list() { return service.list(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UserDetail get(@PathVariable Integer id) { return service.get(id); }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserDetail> create(
            @RequestPart("data") @Valid CreateUserRequest data,
            @RequestPart(value = "address", required = false) OptionalAddress address,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "licenseFront", required = false) MultipartFile licenseFront,
            @RequestPart(value = "licenseBack", required = false) MultipartFile licenseBack
    ) throws IOException {
        String relativePhotoPath = imageStorage.storeResized(photo, "usuarios", "user");
        String licenseFrontPath = imageStorage.storeResized(licenseFront, "licencias", "front");
        String licenseBackPath = imageStorage.storeResized(licenseBack, "licencias", "back");
        Integer id = service.create(data, relativePhotoPath, address);
        if ((licenseFrontPath != null || licenseBackPath != null) && data.userType().name().equals("DOMICILIARIO")) {
            service.setLicensePhotos(id, licenseFrontPath, licenseBackPath);
        }
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id, @RequestBody @Valid ChangePasswordRequest body) {
        service.changePassword(id, body.newPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
