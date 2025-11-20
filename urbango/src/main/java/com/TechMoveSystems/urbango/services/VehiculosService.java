package com.TechMoveSystems.urbango.services;

import com.TechMoveSystems.urbango.admin.dto.VehicleDtos.*;

import java.util.List;

public interface VehiculosService {
    List<VehicleSummary> list(String tipo, String estado, Integer sucursalId, String search);
    VehicleDetail get(Integer id);
    VehicleDetail create(CreateVehicleRequest request, String photoPath);
    VehicleDetail update(Integer id, CreateVehicleRequest request, String photoPath);
    void delete(Integer id);
}
