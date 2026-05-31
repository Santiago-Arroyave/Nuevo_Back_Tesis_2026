package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.PrioridadResponse;
import Back_Goblink_park.demo.entity.Prioridad;

public class PrioridadMapper {

    public static PrioridadResponse toResponse(Prioridad prioridad) {
        if (prioridad == null) {
            return null;
        }

        return PrioridadResponse.builder()
                .id(prioridad.getId())
                .nombre(prioridad.getNombre())
                .nivel(prioridad.getNivel())
                .descripcion(prioridad.getDescripcion())
                .estado(prioridad.getEstado())
                .imagenUrl(prioridad.getImagenUrl())
                .color(prioridad.getColor())  // ← ¿ESTÁ ESTA LÍNEA?
                .imagenBase64(prioridad.getImagenBase64()) // 🔑 PUNTO CLAVE: Mapear el Base64
                .build();
    }
}