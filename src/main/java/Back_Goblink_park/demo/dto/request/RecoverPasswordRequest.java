package Back_Goblink_park.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // ← IMPORTANTE: Para que no falle por campos extra
public class RecoverPasswordRequest {

    @NotBlank(message = "El usuario o correo es obligatorio")
    private String identifier;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String newPassword;

    @NotBlank(message = "La confirmación es obligatoria")
    private String confirmNewPassword;
}