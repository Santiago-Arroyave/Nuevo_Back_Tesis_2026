package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.PrioridadResponse;
import Back_Goblink_park.demo.entity.Prioridad;

public class PrioridadMapper {

    public static PrioridadResponse toResponse(Prioridad prioridad) {

        return PrioridadResponse.builder()
                .id(prioridad.getId())
                .nombre(prioridad.getNombre())
                .nivel(prioridad.getNivel())
                .descripcion(prioridad.getDescripcion())
                .estado(prioridad.getEstado())
                .build();
    }
}