package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.CategoriaResponse;
import Back_Goblink_park.demo.entity.Categoria;

public class CategoriaMapper {

    public static CategoriaResponse toResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .color(categoria.getColor())
                .estado(categoria.getEstado())
                .imagenUrl(categoria.getImagenUrl())
                .imagenBase64(categoria.getImagenBase64())
                .build();
    }
}