package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ComentarioReporteResponse;

import Back_Goblink_park.demo.entity.ComentarioReporte;

public class ComentarioReporteMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static ComentarioReporteResponse toResponse(
            ComentarioReporte comentario
    ) {

        return ComentarioReporteResponse.builder()

                // =====================================
                // IDS
                // =====================================

                .id(comentario.getId())

                .reporteId(
                        comentario.getReporte()
                                .getId()
                )

                // =====================================
                // USUARIO
                // =====================================

                .usuarioId(
                        comentario.getUsuario()
                                .getId()
                )

                .usuarioNombre(
                        comentario.getUsuario()
                                .getNombres()
                )

                .usuarioCorreo(
                        comentario.getUsuario()
                                .getCorreo()
                )

                // =====================================
                // COMENTARIO
                // =====================================

                .comentario(
                        comentario.getComentario()
                )

                .tipoComentario(
                        comentario.getTipoComentario()
                )

                .editado(
                        comentario.getEditado()
                )

                // =====================================
                // FECHAS
                // =====================================

                .fechaComentario(
                        comentario.getFechaComentario()
                )

                .createdAt(
                        comentario.getCreatedAt()
                )

                .updatedAt(
                        comentario.getUpdatedAt()
                )

                .build();
    }
}