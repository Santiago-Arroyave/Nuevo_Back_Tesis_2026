package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.UserResponse;
import Back_Goblink_park.demo.entity.Usuario;

public class UserMapper {

    public static UserResponse toResponse(Usuario usuario) {
        if (usuario == null) return null;

        String doc = usuario.getDocumentoIdentidad();
        String cargo = usuario.getCargoRol();

        return UserResponse.builder()
                .id(usuario.getId())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .correo(usuario.getCorreo())
                .username(usuario.getUsername())
                .telefono(usuario.getTelefono())
                .documentoIdentidad(doc)
                .documento(doc)
                .entidad(usuario.getEntidad())
                .cargoRol(cargo)
                .cargo(cargo)
                .municipioResidencia(usuario.getMunicipioResidencia())
                .sobreMi(usuario.getSobreMi())
                .fotoPerfil(usuario.getFotoPerfil())   // ← Base64 o null
                .fotoPortada(usuario.getFotoPortada()) // ← Base64 o null
                .estado(usuario.getEstado())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .fechaRegistro(usuario.getFechaRegistro())
                .ultimoAcceso(usuario.getUltimoAcceso())
                .build();
    }
}