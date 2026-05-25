package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ProyectoMetaResponse;

import Back_Goblink_park.demo.entity.ProyectoMeta;

public class ProyectoMetaMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static ProyectoMetaResponse toResponse(
            ProyectoMeta meta
    ) {

        return ProyectoMetaResponse.builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        meta.getId()
                )

                // =================================================
                // PROYECTO
                // =================================================

                .proyectoId(

                        meta.getProyecto() != null
                                ? meta.getProyecto().getId()
                                : null
                )

                .proyectoNombre(

                        meta.getProyecto() != null
                                ? meta.getProyecto().getNombre()
                                : null
                )

                // =================================================
                // DESCRIPCIÓN
                // =================================================

                .descripcion(
                        meta.getDescripcion()
                )

                // =================================================
                // AUDITORÍA
                // =================================================

                .createdAt(
                        meta.getCreatedAt()
                )

                .updatedAt(
                        meta.getUpdatedAt()
                )

                .build();
    }
}