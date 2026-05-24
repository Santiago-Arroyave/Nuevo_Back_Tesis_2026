package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.request.RolRequest;
import Back_Goblink_park.demo.dto.response.RolResponse;
import Back_Goblink_park.demo.entity.Rol;

public class RolMapper {

    public static Rol toEntity(RolRequest request) {

        return Rol.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .estado(
                        request.getEstado() != null
                                ? request.getEstado()
                                : true
                )
                .build();
    }

    public static RolResponse toResponse(Rol rol) {

        return RolResponse.builder()
                .id(rol.getId())
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .estado(rol.getEstado())
                .createdAt(rol.getCreatedAt())
                .updatedAt(rol.getUpdatedAt())
                .build();
    }
}