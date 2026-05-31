package Back_Goblink_park.demo.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrioridadResponse {

    private Long id;
    private String nombre;
    private Integer nivel;
    private String descripcion;
    private String color;
    private String imagenUrl;
    private String imagenBase64;
    private Boolean estado;
}