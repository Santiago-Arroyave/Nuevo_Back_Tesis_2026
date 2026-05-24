package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ProyectoResponse;

import Back_Goblink_park.demo.entity.Proyecto;

public class ProyectoMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static ProyectoResponse toResponse(
            Proyecto proyecto
    ) {

        return ProyectoResponse.builder()

                // =================================================
                // ID
                // =================================================

                .id(
                        proyecto.getId()
                )

                // =================================================
                // ESTADO PROYECTO
                // =================================================

                .estadoProyectoId(

                        proyecto.getEstadoProyecto() != null
                                ? proyecto.getEstadoProyecto().getId()
                                : null
                )

                .estadoProyectoNombre(

                        proyecto.getEstadoProyecto() != null
                                ? proyecto.getEstadoProyecto().getNombre()
                                : null
                )

                // =================================================
                // PRIORIDAD
                // =================================================

                .prioridadId(

                        proyecto.getPrioridad() != null
                                ? proyecto.getPrioridad().getId()
                                : null
                )

                .prioridadNombre(

                        proyecto.getPrioridad() != null
                                ? proyecto.getPrioridad().getNombre()
                                : null
                )

                // =================================================
                // CATEGORÍA
                // =================================================

                .categoriaId(

                        proyecto.getCategoria() != null
                                ? proyecto.getCategoria().getId()
                                : null
                )

                .categoriaNombre(

                        proyecto.getCategoria() != null
                                ? proyecto.getCategoria().getNombre()
                                : null
                )

                // =================================================
                // CREADO POR
                // =================================================

                .creadoPorId(

                        proyecto.getCreadoPor() != null
                                ? proyecto.getCreadoPor().getId()
                                : null
                )

                .creadoPorNombre(

                        proyecto.getCreadoPor() != null
                                ? proyecto.getCreadoPor().getNombres()
                                : null
                )

                // =================================================
                // INFORMACIÓN
                // =================================================

                .nombre(
                        proyecto.getNombre()
                )

                .descripcion(
                        proyecto.getDescripcion()
                )

                .presupuestoEstimado(
                        proyecto.getPresupuestoEstimado()
                )

                .fechaInicio(
                        proyecto.getFechaInicio()
                )

                .fechaFin(
                        proyecto.getFechaFin()
                )

                .observaciones(
                        proyecto.getObservaciones()
                )

                // =================================================
                // UBICACIÓN
                // =================================================

                .latitud(
                        proyecto.getLatitud()
                )

                .longitud(
                        proyecto.getLongitud()
                )

                .ubicacion(
                        proyecto.getUbicacion()
                )

                // =================================================
                // EXTRA
                // =================================================

                .tipoProyecto(
                        proyecto.getTipoProyecto()
                )

                .porcentajeAvance(
                        proyecto.getPorcentajeAvance()
                )

                .estado(
                        proyecto.getEstado()
                )

                .eliminado(
                        proyecto.getEliminado()
                )

                // =================================================
                // FECHAS
                // =================================================

                .fechaCreacion(
                        proyecto.getFechaCreacion()
                )

                .createdAt(
                        proyecto.getCreatedAt()
                )

                .updatedAt(
                        proyecto.getUpdatedAt()
                )

                .build();
    }
}