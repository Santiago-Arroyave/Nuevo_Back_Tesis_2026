package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ProyectoMiembroResponse;

import Back_Goblink_park.demo.entity.ProyectoMiembro;

public class ProyectoMiembroMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static ProyectoMiembroResponse toResponse(
            ProyectoMiembro miembro
    ) {

        return ProyectoMiembroResponse.builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        miembro.getId()
                )

                // =================================================
                // PROYECTO
                // =================================================

                .proyectoId(

                        miembro.getProyecto() != null
                                ? miembro.getProyecto().getId()
                                : null
                )

                .proyectoNombre(

                        miembro.getProyecto() != null
                                ? miembro.getProyecto().getNombre()
                                : null
                )

                // =================================================
                // USUARIO
                // =================================================

                .usuarioId(

                        miembro.getUsuario() != null
                                ? miembro.getUsuario().getId()
                                : null
                )

                .usuarioNombre(

                        miembro.getUsuario() != null
                                ? miembro.getUsuario().getNombres()
                                : null
                )

                .usuarioCorreo(

                        miembro.getUsuario() != null
                                ? miembro.getUsuario().getCorreo()
                                : null
                )

                // =================================================
                // ROL
                // =================================================

                .rolEnProyecto(
                        miembro.getRolEnProyecto()
                )

                // =================================================
                // ESTADO
                // =================================================

                .estado(
                        miembro.getEstado()
                )

                // =================================================
                // FECHAS
                // =================================================

                .fechaAsignacion(
                        miembro.getFechaAsignacion()
                )

                .createdAt(
                        miembro.getCreatedAt()
                )

                .updatedAt(
                        miembro.getUpdatedAt()
                )

                .build();
    }
}