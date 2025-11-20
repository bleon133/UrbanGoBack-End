package com.TechMoveSystems.urbango.services.impl;

import com.TechMoveSystems.urbango.admin.dto.TarifaVehiculoDtos.*;
import com.TechMoveSystems.urbango.models.TarifaVehiculo;
import com.TechMoveSystems.urbango.repositories.TarifaVehiculoRepository;
import com.TechMoveSystems.urbango.services.TarifaVehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarifaVehiculoServiceImpl implements TarifaVehiculoService {

    private final TarifaVehiculoRepository tarifas;

    @Override
    @Transactional(readOnly = true)
    public List<TarifaVehiculoSummary> list() {
        return tarifas.findAllOrdered().stream().map(this::toSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TarifaVehiculoDetail get(Integer id) {
        var entity = tarifas.findById(id).orElseThrow();
        return toDetail(entity);
    }

    @Override
    @Transactional
    public TarifaVehiculoDetail create(CreateOrUpdateTarifaVehiculoRequest request) {
        var entity = new TarifaVehiculo();
        apply(entity, request);
        tarifas.save(entity);
        return toDetail(entity);
    }

    @Override
    @Transactional
    public TarifaVehiculoDetail update(Integer id, CreateOrUpdateTarifaVehiculoRequest request) {
        var entity = tarifas.findById(id).orElseThrow();
        apply(entity, request);
        tarifas.save(entity);
        return toDetail(entity);
    }

    @Override
    public void delete(Integer id) {
        tarifas.deleteById(id);
    }

    private void apply(TarifaVehiculo entity, CreateOrUpdateTarifaVehiculoRequest req) {
        entity.setTipoVehiculo(req.tipoVehiculo());
        entity.setTarifaHora(req.tarifaHora());
        entity.setTarifaSemana(req.tarifaSemana());
        entity.setDepositoGarantia(req.depositoGarantia());
    }

    private TarifaVehiculoSummary toSummary(TarifaVehiculo entity) {
        return new TarifaVehiculoSummary(
                entity.getIdTarifaVehiculo(),
                entity.getTipoVehiculo(),
                entity.getTarifaHora(),
                entity.getTarifaSemana(),
                entity.getDepositoGarantia()
        );
    }

    private TarifaVehiculoDetail toDetail(TarifaVehiculo entity) {
        return new TarifaVehiculoDetail(
                entity.getIdTarifaVehiculo(),
                entity.getTipoVehiculo(),
                entity.getTarifaHora(),
                entity.getTarifaSemana(),
                entity.getDepositoGarantia()
        );
    }
}
