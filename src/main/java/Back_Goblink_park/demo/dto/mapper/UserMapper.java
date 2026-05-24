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
                .fotoPerfil(usuario.getFotoPerfil())
                .estado(usuario.getEstado())
                .rol(usuario.getRol().getNombre())
                .fechaRegistro(usuario.getFechaRegistro())
                .ultimoAcceso(usuario.getUltimoAcceso())
                .build();
    }
}