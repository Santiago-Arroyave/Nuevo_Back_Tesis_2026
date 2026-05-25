package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ProyectoObjetivoResponse;

import Back_Goblink_park.demo.entity.ProyectoObjetivo;

public class ProyectoObjetivoMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static ProyectoObjetivoResponse toResponse(
            ProyectoObjetivo objetivo
    ) {

        return ProyectoObjetivoResponse.builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        objetivo.getId()
                )

                // =================================================
                // PROYECTO
                // =================================================

                .proyectoId(

                        objetivo.getProyecto() != null
                                ? objetivo.getProyecto().getId()
                                : null
                )

                .proyectoNombre(

                        objetivo.getProyecto() != null
                                ? objetivo.getProyecto().getNombre()
                                : null
                )

                // =================================================
                // DESCRIPCIÓN
                // =================================================

                .descripcion(
                        objetivo.getDescripcion()
                )

                // =================================================
                // AUDITORÍA
                // =================================================

                .createdAt(
                        objetivo.getCreatedAt()
                )

                .updatedAt(
                        objetivo.getUpdatedAt()
                )

                .build();
    }
}