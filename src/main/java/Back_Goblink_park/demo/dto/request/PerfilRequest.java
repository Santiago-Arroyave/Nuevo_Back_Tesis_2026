package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilRequest {

    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres")
    private String nombres;

    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telefono;

    @Size(max = 50, message = "El username no puede exceder los 50 caracteres")
    private String username;

    // Campo para recibir la foto de perfil (opcional)
    private MultipartFile fotoPerfil;
}