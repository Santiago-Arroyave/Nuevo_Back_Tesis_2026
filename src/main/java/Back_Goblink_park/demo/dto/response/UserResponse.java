package Back_Goblink_park.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String nombres;

    private String apellidos;

    private String correo;

    private String username;

    private String telefono;

    private String documentoIdentidad;

    private String documento; // Alias para compatibilidad con clientes móviles

    private String entidad;

    private String cargoRol;

    private String cargo; // Alias

    private String municipioResidencia;

    private String sobreMi;

    private String fotoPerfil;

    private String fotoPortada;

    private Boolean estado;

    private String rol;

    private LocalDateTime fechaRegistro;

    private LocalDateTime ultimoAcceso;
}