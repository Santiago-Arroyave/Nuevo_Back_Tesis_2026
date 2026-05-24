package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

public class EstadoProyectoRequest {

    @NotBlank
    private String nombre;

    private String descripcion;

    private String color;

    @NotNull
    private Short ordenVisual;

    private Boolean estado;
}