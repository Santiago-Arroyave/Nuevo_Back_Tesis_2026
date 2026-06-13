package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ChangePasswordRequest;
import Back_Goblink_park.demo.dto.request.LoginRequest;
import Back_Goblink_park.demo.dto.request.RecoverPasswordRequest;
import Back_Goblink_park.demo.dto.request.RegisterRequest;
import Back_Goblink_park.demo.dto.response.AuthResponse;

import Back_Goblink_park.demo.dto.response.UserResponse;
import Back_Goblink_park.demo.service.interfaces.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // =====================================================
    // OBTENER USUARIO AUTENTICADO (MI PERFIL)
    // =====================================================
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")  // Requiere que haya un token válido
    public ResponseEntity<UserResponse> getMe() {
        // Obtiene el correo del usuario logueado desde el Token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();

        return ResponseEntity.ok(authService.getMe(correoUsuario));
    }

    // =====================================================
    // RECUPERAR CONTRASEÑA (PÚBLICO - SIN TOKEN)
    // =====================================================
    @PostMapping("/recover-password")
    public ResponseEntity<AuthResponse> recoverPassword(
            @Valid @RequestBody RecoverPasswordRequest request
    ) {
        System.out.println(" [Backend] Recibida solicitud de recuperación para: " + request.getIdentifier());
        return ResponseEntity.ok(authService.recoverPassword(request));
    }

}