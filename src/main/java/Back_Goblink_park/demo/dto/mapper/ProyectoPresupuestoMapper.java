package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ProyectoPresupuestoResponse;

import Back_Goblink_park.demo.entity.ProyectoPresupuesto;

public class ProyectoPresupuestoMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static ProyectoPresupuestoResponse toResponse(
            ProyectoPresupuesto presupuesto
    ) {

        return ProyectoPresupuestoResponse.builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        presupuesto.getId()
                )

                // =================================================
                // PROYECTO
                // =================================================

                .proyectoId(

                        presupuesto.getProyecto() != null
                                ? presupuesto.getProyecto().getId()
                                : null
                )

                .proyectoNombre(

                        presupuesto.getProyecto() != null
                                ? presupuesto.getProyecto().getNombre()
                                : null
                )

                // =================================================
                // RUBRO
                // =================================================

                .rubro(
                        presupuesto.getRubro()
                )

                // =================================================
                // MONTO
                // =================================================

                .monto(
                        presupuesto.getMonto()
                )

                // =================================================
                // OBSERVACIONES
                // =================================================

                .observaciones(
                        presupuesto.getObservaciones()
                )

                // =================================================
                // AUDITORÍA
                // =================================================

                .createdAt(
                        presupuesto.getCreatedAt()
                )

                .updatedAt(
                        presupuesto.getUpdatedAt()
                )

                .build();
    }
}