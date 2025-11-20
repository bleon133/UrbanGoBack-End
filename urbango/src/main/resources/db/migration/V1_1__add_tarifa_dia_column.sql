-- Add missing tarifa_dia column to tarifas_vehiculo for Hibernate validation
ALTER TABLE tarifas_vehiculo
    ADD COLUMN IF NOT EXISTS tarifa_dia NUMERIC(10, 2);
