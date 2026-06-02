package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ChangePasswordRequest;
import Back_Goblink_park.demo.dto.request.LoginRequest;
import Back_Goblink_park.demo.dto.request.RegisterRequest;
import Back_Goblink_park.demo.dto.response.AuthResponse;

import Back_Goblink_park.demo.service.interfaces.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // =====================================================
    // REGISTER
    // =====================================================

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return authService.register(request);
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }

    // =====================================================
    // CAMBIAR CONTRASEÑA - USUARIO AUTENTICADO
    // =====================================================
    @PostMapping("/change-password")
    public AuthResponse changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        // Obtener correo del usuario autenticado desde el token JWT
        String correoUsuario = authService.getAuthenticatedUserEmail();
        return authService.changePassword(correoUsuario, request);
    }
}