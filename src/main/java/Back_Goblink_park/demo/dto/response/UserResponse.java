package Back_Goblink_park.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String nombres;

    private String correo;

    private String username;

    private String telefono;

    private String fotoPerfil;

    private Boolean estado;

    private String rol;

    private LocalDateTime fechaRegistro;

    private LocalDateTime ultimoAcceso;
}