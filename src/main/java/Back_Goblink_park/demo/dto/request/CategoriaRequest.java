package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoriaRequest {

    @NotBlank(message = "El nombre es obligatorio")

    @Size(max = 50)

    private String nombre;

    @Size(max = 150)

    private String descripcion;

    private Boolean estado;
}