package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;

import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;

public class CronogramaActividadProyectoMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static CronogramaActividadProyectoResponse
    toResponse(
            CronogramaActividadProyecto actividad
    ) {

        return CronogramaActividadProyectoResponse
                .builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        actividad.getId()
                )

                // =================================================
                // PROYECTO
                // =================================================

                .proyectoId(
                        actividad.getProyecto()
                                .getId()
                )

                .proyectoNombre(
                        actividad.getProyecto()
                                .getNombre()
                )

                // =================================================
                // RESPONSABLE PROYECTO
                // =================================================

                .responsableId(

                        actividad.getResponsable() != null

                                ? actividad.getResponsable()
                                .getId()

                                : null
                )

                .responsableTitulo(

                        actividad.getResponsable() != null

                                ? actividad.getResponsable()
                                .getTitulo()

                                : null
                )

                // =================================================
                // USUARIO RESPONSABLE
                // =================================================

                .usuarioResponsableId(

                        actividad.getResponsable() != null

                                ? actividad.getResponsable()
                                .getUsuarioResponsable()
                                .getId()

                                : null
                )

                .usuarioResponsableNombre(

                        actividad.getResponsable() != null

                                ? actividad.getResponsable()
                                .getUsuarioResponsable()
                                .getNombres()

                                : null
                )

                // =================================================
                // INFORMACIÓN
                // =================================================

                .nombre(
                        actividad.getNombre()
                )

                .descripcion(
                        actividad.getDescripcion()
                )

                // =================================================
                // ESTADO
                // =================================================

                .estado(
                        actividad.getEstado()
                )

                // =================================================
                // FECHAS
                // =================================================

                .fechaInicio(
                        actividad.getFechaInicio()
                )

                .fechaFin(
                        actividad.getFechaFin()
                )

                // =================================================
                // EVIDENCIA
                // =================================================

                .urlEvidencia(
                        actividad.getUrlEvidencia()
                )

                // =================================================
                // OBSERVACIONES
                // =================================================

                .observaciones(
                        actividad.getObservaciones()
                )

                // =================================================
                // AUDITORÍA
                // =================================================

                .createdAt(
                        actividad.getCreatedAt()
                )

                .updatedAt(
                        actividad.getUpdatedAt()
                )

                .build();
    }
}