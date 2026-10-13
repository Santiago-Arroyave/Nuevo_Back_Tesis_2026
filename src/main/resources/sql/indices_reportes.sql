-- =============================================================================
-- MIGRACIÓN DE IDEMPOTENCIA OFFLINE Y RENDIMIENTO CONCURRENTE (PostgreSQL)
-- Tabla: reportes & proyecto_reportes
-- =============================================================================

-- 1. Columna e índice único para idempotencia de sincronización offline
ALTER TABLE public.reportes
    ADD COLUMN IF NOT EXISTS client_uuid VARCHAR(100);

CREATE UNIQUE INDEX IF NOT EXISTS idx_reportes_client_uuid
    ON public.reportes USING btree (client_uuid)
    WHERE client_uuid IS NOT NULL;

-- 2. Índices B-Tree sobre reportes para consultas concurrentes (App Móvil y Panel Web)
CREATE INDEX IF NOT EXISTS idx_reportes_id_usuario
    ON public.reportes USING btree (id_usuario);

CREATE INDEX IF NOT EXISTS idx_reportes_id_estado_reporte
    ON public.reportes USING btree (id_estado_reporte);

CREATE INDEX IF NOT EXISTS idx_reportes_estado
    ON public.reportes USING btree (estado);

CREATE INDEX IF NOT EXISTS idx_reportes_id_categoria
    ON public.reportes USING btree (id_categoria);

CREATE INDEX IF NOT EXISTS idx_reportes_id_prioridad
    ON public.reportes USING btree (id_prioridad);

CREATE INDEX IF NOT EXISTS idx_reportes_eliminado_fecha
    ON public.reportes USING btree (eliminado, fecha_reporte DESC);

-- 3. Índices B-Tree sobre asociación de reportes con proyectos (proyecto_reportes)
CREATE INDEX IF NOT EXISTS idx_proyecto_reportes_id_proyecto
    ON public.proyecto_reportes USING btree (id_proyecto);

CREATE INDEX IF NOT EXISTS idx_proyecto_reportes_id_reporte
    ON public.proyecto_reportes USING btree (id_reporte);
