package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.UserResponse;
import Back_Goblink_park.demo.entity.Usuario;

public class UserMapper {

    public static UserResponse toResponse(Usuario usuario) {
        return UserResponse.builder()
                .id(usuario.getId())
                .nombres(usuario.getNombres())
                .correo(usuario.getCorreo())
                .username(usuario.getUsername())
                .telefono(usuario.getTelefono())
                .fotoPerfil(usuario.getFotoPerfil())  // ← Base64 o null
                .estado(usuario.getEstado())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .fechaRegistro(usuario.getFechaRegistro())
                .ultimoAcceso(usuario.getUltimoAcceso())
                .build();
    }
}