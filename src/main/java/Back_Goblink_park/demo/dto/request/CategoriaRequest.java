package Back_Goblink_park.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
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

    // Archivo binario (para peticiones multipart/form-data)
    private MultipartFile imagen;

    // Color temático (#HEX)
    private String color;

    // URL pública generada desde Supabase Storage (para peticiones JSON)
    @JsonAlias({"imagen_url", "imagenUrl"})
    private String imagenUrl;
}