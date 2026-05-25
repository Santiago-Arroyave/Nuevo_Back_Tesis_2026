package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ProyectoReporteResponse;

import Back_Goblink_park.demo.entity.ProyectoReporte;

public class ProyectoReporteMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static ProyectoReporteResponse toResponse(
            ProyectoReporte proyectoReporte
    ) {

        return ProyectoReporteResponse.builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        proyectoReporte.getId()
                )

                // =================================================
                // PROYECTO
                // =================================================

                .proyectoId(

                        proyectoReporte.getProyecto() != null
                                ? proyectoReporte.getProyecto().getId()
                                : null
                )

                .proyectoNombre(

                        proyectoReporte.getProyecto() != null
                                ? proyectoReporte.getProyecto().getNombre()
                                : null
                )

                // =================================================
                // REPORTE
                // =================================================

                .reporteId(

                        proyectoReporte.getReporte() != null
                                ? proyectoReporte.getReporte().getId()
                                : null
                )

                .reporteTitulo(

                        proyectoReporte.getReporte() != null
                                ? proyectoReporte.getReporte().getTitulo()
                                : null
                )

                .reporteDescripcion(

                        proyectoReporte.getReporte() != null
                                ? proyectoReporte.getReporte().getDescripcion()
                                : null
                )

                // =================================================
                // ESTADO REPORTE
                // =================================================

                .estadoReporte(

                        proyectoReporte.getReporte() != null
                                && proyectoReporte.getReporte().getEstadoReporte() != null

                                ? proyectoReporte
                                .getReporte()
                                .getEstadoReporte()
                                .getNombre()

                                : null
                )

                // =================================================
                // PRIORIDAD
                // =================================================

                .prioridad(

                        proyectoReporte.getReporte() != null
                                && proyectoReporte.getReporte().getPrioridad() != null

                                ? proyectoReporte
                                .getReporte()
                                .getPrioridad()
                                .getNombre()

                                : null
                )

                // =================================================
                // FECHAS
                // =================================================

                .fechaAsociacion(
                        proyectoReporte.getFechaAsociacion()
                )

                .createdAt(
                        proyectoReporte.getCreatedAt()
                )

                .build();
    }
}