package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.UserRequest;
import Back_Goblink_park.demo.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponse crearUsuario(UserRequest request);
    List<UserResponse> listarUsuarios();
    UserResponse obtenerUsuario(Long id);
    UserResponse actualizarUsuario(Long id, UserRequest request);

    // ✅ AGREGAR ESTE MÉTODO
    UserResponse actualizarPerfilPorCorreo(
            String correoUsuario,
            String nombres,
            String username,
            String telefono,
            MultipartFile fotoPerfil
    );

    UserResponse cambiarEstadoUsuario(Long id, Boolean estado);
    void eliminarUsuario(Long id);
}