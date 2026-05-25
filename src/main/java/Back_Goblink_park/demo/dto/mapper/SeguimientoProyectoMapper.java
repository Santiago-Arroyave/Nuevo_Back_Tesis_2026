package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.SeguimientoProyectoResponse;

import Back_Goblink_park.demo.entity.SeguimientoProyecto;

public class SeguimientoProyectoMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static SeguimientoProyectoResponse toResponse(
            SeguimientoProyecto seguimiento
    ) {

        return SeguimientoProyectoResponse.builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        seguimiento.getId()
                )

                // =================================================
                // PROYECTO
                // =================================================

                .proyectoId(
                        seguimiento.getProyecto().getId()
                )

                .proyectoNombre(
                        seguimiento.getProyecto().getNombre()
                )

                // =================================================
                // USUARIO
                // =================================================

                .usuarioId(
                        seguimiento.getUsuario().getId()
                )

                .usuarioNombre(
                        seguimiento.getUsuario().getNombres()
                )

                // =================================================
                // AVANCE
                // =================================================

                .descripcionAvance(
                        seguimiento.getDescripcionAvance()
                )

                // =================================================
                // PORCENTAJE
                // =================================================

                .porcentajeAvance(
                        seguimiento.getPorcentajeAvance()
                )

                // =================================================
                // FECHA
                // =================================================

                .fechaSeguimiento(
                        seguimiento.getFechaSeguimiento()
                )

                // =================================================
                // OBSERVACIONES
                // =================================================

                .observaciones(
                        seguimiento.getObservaciones()
                )

                // =================================================
                // AUDITORÍA
                // =================================================

                .createdAt(
                        seguimiento.getCreatedAt()
                )

                .updatedAt(
                        seguimiento.getUpdatedAt()
                )

                .build();
    }
}