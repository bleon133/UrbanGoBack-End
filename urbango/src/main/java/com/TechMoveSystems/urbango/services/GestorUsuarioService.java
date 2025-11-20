package com.TechMoveSystems.urbango.services;

import com.TechMoveSystems.urbango.dto.UserDtos.*;

import java.util.List;

public interface GestorUsuarioService {
    List<UserSummary> list();
    UserDetail get(Integer id);
    Integer create(CreateUserRequest req, String photoPath, OptionalAddress address);
    void setLicensePhotos(Integer idUsuario, String front, String back);
    void changePassword(Integer id, String newPassword);
    void delete(Integer id);
}

