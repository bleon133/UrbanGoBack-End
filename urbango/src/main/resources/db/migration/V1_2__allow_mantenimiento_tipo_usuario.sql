-- Ensure the tipo_usuario check constraint includes MANTENIMIENTO
ALTER TABLE usuarios
    DROP CONSTRAINT IF EXISTS usuarios_tipo_usuario_check;

ALTER TABLE usuarios
    ADD CONSTRAINT usuarios_tipo_usuario_check
        CHECK (tipo_usuario IN ('CLIENTE', 'DOMICILIARIO', 'ADMINISTRADOR', 'MANTENIMIENTO'));
