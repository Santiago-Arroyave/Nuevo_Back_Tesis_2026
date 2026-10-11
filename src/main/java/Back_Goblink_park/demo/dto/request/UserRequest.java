package Back_Goblink_park.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private Long rolId;

    private String nombres;

    private String apellidos;

    private String correo;

    private String username;

    private String password;

    private String telefono;

    @JsonAlias({"documento_identidad", "documento", "cedula", "numeroDocumento"})
    private String documentoIdentidad;

    private String documento;

    private String entidad;

    @JsonAlias({"cargo_rol", "cargo"})
    private String cargoRol;

    private String cargo_rol;

    @JsonAlias({"municipio_residencia", "municipio"})
    private String municipioResidencia;

    private String municipio_residencia;

    @JsonAlias({"sobre_mi", "descripcion"})
    private String sobreMi;

    private String sobre_mi;

    @JsonAlias({"foto_perfil"})
    private String fotoPerfil;

    @JsonAlias({"foto_portada"})
    private String fotoPortada;

    private Boolean estado;

    public String resolverDocumento() {
        if (documentoIdentidad != null && !documentoIdentidad.isBlank()) return documentoIdentidad.trim();
        if (documento != null && !documento.isBlank()) return documento.trim();
        return null;
    }

    public String resolverCargo() {
        if (cargoRol != null && !cargoRol.isBlank()) return cargoRol.trim();
        if (cargo_rol != null && !cargo_rol.isBlank()) return cargo_rol.trim();
        return null;
    }

    public String resolverMunicipio() {
        if (municipioResidencia != null && !municipioResidencia.isBlank()) return municipioResidencia.trim();
        if (municipio_residencia != null && !municipio_residencia.isBlank()) return municipio_residencia.trim();
        return null;
    }

    public String resolverSobreMi() {
        if (sobreMi != null && !sobreMi.isBlank()) return sobreMi.trim();
        if (sobre_mi != null && !sobre_mi.isBlank()) return sobre_mi.trim();
        return null;
    }
}