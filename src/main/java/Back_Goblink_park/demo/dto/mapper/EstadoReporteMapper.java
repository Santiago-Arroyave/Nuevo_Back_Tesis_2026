package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.EstadoReporteResponse;
import Back_Goblink_park.demo.entity.EstadoReporte;

public class EstadoReporteMapper {

    public static EstadoReporteResponse toResponse(
            EstadoReporte estado
    ) {

        return EstadoReporteResponse.builder()
                .id(estado.getId())
                .nombre(estado.getNombre())
                .descripcion(estado.getDescripcion())
                .color(estado.getColor())
                .ordenVisual(estado.getOrdenVisual())
                .estado(estado.getEstado())
                .build();
    }
}