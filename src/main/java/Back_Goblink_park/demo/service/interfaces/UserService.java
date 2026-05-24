package Back_Goblink_park.demo.service;

import Back_Goblink_park.demo.dto.request.UserRequest;
import Back_Goblink_park.demo.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse crearUsuario(UserRequest request);

    List<UserResponse> listarUsuarios();

    UserResponse obtenerUsuario(Long id);

    UserResponse actualizarUsuario(Long id, UserRequest request);

    void eliminarUsuario(Long id);
}