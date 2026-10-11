package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.UserRequest;
import Back_Goblink_park.demo.dto.response.UserEstadisticasResponse;
import Back_Goblink_park.demo.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponse crearUsuario(UserRequest request);
    List<UserResponse> listarUsuarios();
    UserResponse obtenerUsuario(Long id);
    UserResponse actualizarUsuario(Long id, UserRequest request);

    UserResponse actualizarPerfilPorCorreo(
            String correoUsuario,
            String nombres,
            String username,
            String telefono,
            MultipartFile fotoPerfil
    );

    UserResponse actualizarPerfilPorCorreo(
            String correoUsuario,
            String nombres,
            String apellidos,
            String username,
            String telefono,
            String documentoIdentidad,
            String entidad,
            String cargoRol,
            String municipioResidencia,
            String sobreMi,
            MultipartFile fotoPerfil,
            MultipartFile fotoPortada
    );

    UserResponse actualizarPerfilPorCorreo(
            String correoUsuario,
            String nombres,
            String apellidos,
            String username,
            String telefono,
            String documentoIdentidad,
            String entidad,
            String cargoRol,
            String municipioResidencia,
            String sobreMi,
            MultipartFile fotoPerfil,
            MultipartFile fotoPortada,
            String fotoPerfilBase64,
            String fotoPortadaBase64
    );

    UserEstadisticasResponse obtenerEstadisticas(Long idUsuario);

    UserResponse obtenerPerfilPorCorreo(String correoUsuario);

    UserResponse cambiarEstadoUsuario(Long id, Boolean estado);
    void eliminarUsuario(Long id);
}