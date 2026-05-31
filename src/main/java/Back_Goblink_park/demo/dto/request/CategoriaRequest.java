package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    // No necesitas validaciones aquí, es opcional
    private MultipartFile imagen;
    // ✅ AGREGAR ESTO:
    private String color;

}