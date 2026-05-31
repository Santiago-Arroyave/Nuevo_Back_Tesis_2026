package Back_Goblink_park.demo.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private String color;
    private Boolean estado;
    private String imagenUrl;
    private String imagenBase64;  // ← AGREGAR ESTE CAMPO
}