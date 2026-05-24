package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequest {

    @NotNull
    private Long rolId;

    @NotBlank
    @Size(max = 100)
    private String nombres;

    @NotBlank
    @Email
    @Size(max = 120)
    private String correo;

    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @Size(max = 20)
    private String telefono;

    private String fotoPerfil;

    private Boolean estado;
}