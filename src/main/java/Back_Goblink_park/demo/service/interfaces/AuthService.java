package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ChangePasswordRequest;
import Back_Goblink_park.demo.dto.request.LoginRequest;
import Back_Goblink_park.demo.dto.request.RecoverPasswordRequest;
import Back_Goblink_park.demo.dto.request.RegisterRequest;
import Back_Goblink_park.demo.dto.response.AuthResponse;
import Back_Goblink_park.demo.dto.response.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
    // Obtener email del usuario autenticado (helper para el controller)
    String getAuthenticatedUserEmail();
    // Obtener datos del usuario autenticado por su correo
    UserResponse getMe(String correoUsuario);
    // Cambiar contraseña
    AuthResponse changePassword(String correoUsuario, ChangePasswordRequest request);
    // Recuperar contraseña sin autenticación (por username/correo)
    AuthResponse recoverPassword(RecoverPasswordRequest request);
}