package com.TechMoveSystems.urbango.services;

import com.TechMoveSystems.urbango.admin.dto.TarifaVehiculoDtos.*;

import java.util.List;

public interface TarifaVehiculoService {
    List<TarifaVehiculoSummary> list();
    TarifaVehiculoDetail get(Integer id);
    TarifaVehiculoDetail create(CreateOrUpdateTarifaVehiculoRequest request);
    TarifaVehiculoDetail update(Integer id, CreateOrUpdateTarifaVehiculoRequest request);
    void delete(Integer id);
}
