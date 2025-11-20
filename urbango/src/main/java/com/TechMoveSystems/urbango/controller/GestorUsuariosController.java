package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.dto.UserDtos.*;
import com.TechMoveSystems.urbango.services.GestorUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class GestorUsuariosController {

    private final GestorUsuarioService service;

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
        String relativePhotoPath = null;
        String licenseFrontPath = null;
        String licenseBackPath = null;
        if (photo != null && !photo.isEmpty()) {
            String base = System.getProperty("app.files.base-dir", "photos");
            Path dir = Paths.get(base, "usuarios");
            Files.createDirectories(dir);
            String ext = StringUtils.getFilenameExtension(photo.getOriginalFilename());
            String filename = UUID.randomUUID() + (ext != null ? ("." + ext) : "");
            Path target = dir.resolve(filename);
            Files.copy(photo.getInputStream(), target);
            relativePhotoPath = "usuarios/" + filename;
        }
        if (licenseFront != null && !licenseFront.isEmpty()) {
            String base = System.getProperty("app.files.base-dir", "photos");
            Path dir = Paths.get(base, "licencias");
            Files.createDirectories(dir);
            String ext = StringUtils.getFilenameExtension(licenseFront.getOriginalFilename());
            String filename = "front-" + UUID.randomUUID() + (ext != null ? ("." + ext) : "");
            Path target = dir.resolve(filename);
            Files.copy(licenseFront.getInputStream(), target);
            licenseFrontPath = "licencias/" + filename;
        }
        if (licenseBack != null && !licenseBack.isEmpty()) {
            String base = System.getProperty("app.files.base-dir", "photos");
            Path dir = Paths.get(base, "licencias");
            Files.createDirectories(dir);
            String ext = StringUtils.getFilenameExtension(licenseBack.getOriginalFilename());
            String filename = "back-" + UUID.randomUUID() + (ext != null ? ("." + ext) : "");
            Path target = dir.resolve(filename);
            Files.copy(licenseBack.getInputStream(), target);
            licenseBackPath = "licencias/" + filename;
        }
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

