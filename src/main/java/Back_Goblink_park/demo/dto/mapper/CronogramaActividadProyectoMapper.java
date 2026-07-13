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
                // MIEMBRO DEL PROYECTO (RESPONSABLE)
                // =================================================

                .proyectoMiembroId(

                        actividad.getProyectoMiembro() != null

                                ? actividad.getProyectoMiembro()
                                .getId()

                                : null
                )

                .proyectoMiembroRol(

                        actividad.getProyectoMiembro() != null

                                ? actividad.getProyectoMiembro()
                                .getRolEnProyecto()

                                : null
                )

                // =================================================
                // USUARIO RESPONSABLE
                // =================================================

                .usuarioResponsableId(

                        actividad.getProyectoMiembro() != null

                                ? actividad.getProyectoMiembro()
                                .getUsuario()
                                .getId()

                                : null
                )

                .usuarioResponsableNombre(

                        actividad.getProyectoMiembro() != null

                                ? actividad.getProyectoMiembro()
                                .getUsuario()
                                .getNombres()

                                : null
                )

                .usuarioResponsableEmail(

                        actividad.getProyectoMiembro() != null

                                ? actividad.getProyectoMiembro()
                                .getUsuario()
                                .getCorreo()

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
                // PRIORIDAD
                // =================================================

                .prioridad(
                        actividad.getPrioridad()
                )

                // =================================================
                // AVANCE
                // =================================================

                .porcentajeAvance(
                        actividad.getPorcentajeAvance()
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
                // EVIDENCIA - URL
                // =================================================

                .urlEvidencia(
                        actividad.getUrlEvidencia()
                )

                // =================================================
                // EVIDENCIA - IMAGEN BASE64 (NUEVOS CAMPOS)
                // =================================================

                .imagenBase64(
                        actividad.getImagenBase64()
                )

                .tipoImagen(
                        actividad.getTipoImagen()
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