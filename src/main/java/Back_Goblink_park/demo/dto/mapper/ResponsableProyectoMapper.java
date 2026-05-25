package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ResponsableProyectoResponse;

import Back_Goblink_park.demo.entity.ResponsableProyecto;

public class ResponsableProyectoMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static ResponsableProyectoResponse toResponse(
            ResponsableProyecto responsable
    ) {

        return ResponsableProyectoResponse.builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        responsable.getId()
                )

                // =================================================
                // PROYECTO
                // =================================================

                .proyectoId(

                        responsable.getProyecto() != null
                                ? responsable.getProyecto().getId()
                                : null
                )

                .proyectoNombre(

                        responsable.getProyecto() != null
                                ? responsable.getProyecto().getNombre()
                                : null
                )

                // =================================================
                // USUARIO RESPONSABLE
                // =================================================

                .usuarioResponsableId(

                        responsable.getUsuarioResponsable() != null
                                ? responsable
                                .getUsuarioResponsable()
                                .getId()
                                : null
                )

                .usuarioResponsableNombre(

                        responsable.getUsuarioResponsable() != null
                                ? responsable
                                .getUsuarioResponsable()
                                .getNombres()
                                : null
                )

                // =================================================
                // INFORMACIÓN
                // =================================================

                .titulo(
                        responsable.getTitulo()
                )

                .descripcion(
                        responsable.getDescripcion()
                )

                // =================================================
                // ESTADO
                // =================================================

                .estado(
                        responsable.getEstado()
                )

                // =================================================
                // PRIORIDAD
                // =================================================

                .prioridad(
                        responsable.getPrioridad()
                )

                // =================================================
                // FECHAS
                // =================================================

                .fechaInicio(
                        responsable.getFechaInicio()
                )

                .fechaLimite(
                        responsable.getFechaLimite()
                )

                // =================================================
                // AVANCE
                // =================================================

                .porcentajeAvance(
                        responsable.getPorcentajeAvance()
                )

                // =================================================
                // AUDITORÍA
                // =================================================

                .createdAt(
                        responsable.getCreatedAt()
                )

                .updatedAt(
                        responsable.getUpdatedAt()
                )

                .build();
    }
}