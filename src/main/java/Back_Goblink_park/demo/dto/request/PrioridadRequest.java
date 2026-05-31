package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrioridadRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 20, message = "El nombre no puede exceder los 20 caracteres")
    private String nombre;

    @NotNull(message = "El nivel es obligatorio")
    @Min(value = 1, message = "El nivel debe ser entre 1 y 4")
    @Max(value = 4, message = "El nivel debe ser entre 1 y 4")
    private Integer nivel;

    @Size(max = 100, message = "La descripción no puede exceder los 100 caracteres")
    private String descripcion;

    private Boolean estado;

    // Campo para recibir la imagen (opcional)
    private MultipartFile imagen;

    // ✅ AGREGAR ESTO:
    private String color;
}