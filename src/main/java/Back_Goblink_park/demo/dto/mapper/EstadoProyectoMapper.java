package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.EstadoProyectoResponse;
import Back_Goblink_park.demo.entity.EstadoProyecto;

public class EstadoProyectoMapper {

    public static EstadoProyectoResponse toResponse(
            EstadoProyecto estadoProyecto
    ) {

        return EstadoProyectoResponse.builder()
                .id(estadoProyecto.getId())
                .nombre(estadoProyecto.getNombre())
                .descripcion(estadoProyecto.getDescripcion())
                .color(estadoProyecto.getColor())
                .ordenVisual(estadoProyecto.getOrdenVisual())
                .estado(estadoProyecto.getEstado())
                .build();
    }
}