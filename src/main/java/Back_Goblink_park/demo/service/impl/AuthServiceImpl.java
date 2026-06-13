package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.UserMapper;
import Back_Goblink_park.demo.dto.request.ChangePasswordRequest;
import Back_Goblink_park.demo.dto.request.LoginRequest;
import Back_Goblink_park.demo.dto.request.RecoverPasswordRequest;
import Back_Goblink_park.demo.dto.request.RegisterRequest;
import Back_Goblink_park.demo.dto.response.AuthResponse;

import Back_Goblink_park.demo.dto.response.UserResponse;
import Back_Goblink_park.demo.entity.Rol;
import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.RolRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;
import Back_Goblink_park.demo.security.JwtService;
import Back_Goblink_park.demo.service.interfaces.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // =====================================================
    // REGISTRO DE USUARIO
    // =====================================================
    @Override
    public AuthResponse register(RegisterRequest request) {
        // Validar correo único
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessException("El correo ya está registrado");
        }

        // Buscar rol USER
        Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new BusinessException("Rol USER no encontrado"));

        // Crear usuario
        Usuario usuario = Usuario.builder()
                .rol(rolUser)
                .nombres(request.getNombres())
                .correo(request.getCorreo())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .estado(true) // Nuevo usuario siempre activo
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Generar token JWT
        String token = jwtService.generateToken(usuarioGuardado.getCorreo());

        return AuthResponse.builder()
                .token(token)
                .usuario(UserMapper.toResponse(usuarioGuardado))
                .build();
    }

    // =====================================================
    // LOGIN DE USUARIO
    // =====================================================
    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        // 2. 🔒 VALIDAR QUE EL USUARIO ESTÉ ACTIVO
        if (!usuario.getEstado()) {
            throw new BusinessException(
                    "El usuario está inactivo. Contacte al administrador para reactivarlo."
            );
        }

        // 3. Validar password
        boolean passwordCorrecta = passwordEncoder.matches(
                request.getPassword(),
                usuario.getPasswordHash()
        );

        if (!passwordCorrecta) {
            throw new BusinessException("Credenciales inválidas");
        }

        // 4. Generar token JWT
        String token = jwtService.generateToken(usuario.getCorreo());

        return AuthResponse.builder()
                .token(token)
                .usuario(UserMapper.toResponse(usuario))
                .build();
    }

    // =====================================================
    // OBTENER EMAIL DEL USUARIO AUTENTICADO
    // =====================================================
    @Override
    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Usuario no autenticado");
        }

        // El "name" en nuestro JWT es el correo del usuario
        return authentication.getName();
    }

    // =====================================================
    // CAMBIAR CONTRASEÑA
    // =====================================================
    @Override
    @Transactional
    public AuthResponse changePassword(String correoUsuario, ChangePasswordRequest request) {
        // Validar que las nuevas contraseñas coincidan
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BusinessException("Las nuevas contraseñas no coinciden");
        }

        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Validar contraseña actual
        if (!passwordEncoder.matches(request.getCurrentPassword(), usuario.getPasswordHash())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }

        // Validar que la nueva contraseña sea diferente a la actual
        if (passwordEncoder.matches(request.getNewPassword(), usuario.getPasswordHash())) {
            throw new BusinessException("La nueva contraseña debe ser diferente a la actual");
        }

        // Encriptar y actualizar contraseña
        String nuevaPasswordHash = passwordEncoder.encode(request.getNewPassword());
        usuario.setPasswordHash(nuevaPasswordHash);

        // Actualizar fecha de último acceso/modificación (opcional)
        usuario.setUltimoAcceso(LocalDateTime.now());

        usuarioRepository.save(usuario);

        // Retornar respuesta con datos actualizados (sin incluir password)
        return AuthResponse.builder()
                .mensaje("Contraseña actualizada exitosamente")
                .usuario(UserMapper.toResponse(usuario))
                .build();
    }

    // =====================================================
    // OBTENER DATOS DEL USUARIO AUTENTICADO (MI PERFIL) ✅ AGREGAR ESTO
    // =====================================================
    @Override
    public UserResponse getMe(String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con correo: " + correoUsuario));

        return UserMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public AuthResponse recoverPassword(RecoverPasswordRequest request) {
        // 1. Validar que las contraseñas coincidan
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BusinessException("Las nuevas contraseñas no coinciden");
        }

        // 2. Buscar usuario por Correo O por Username (lo que venga en 'identifier')
        Usuario usuario = usuarioRepository.findByCorreo(request.getIdentifier())
                .orElseGet(() -> {
                    // Si no lo encontró por correo, intenta por username
                    System.out.println(" [Backend] No encontrado por correo, buscando por username: " + request.getIdentifier());
                    return usuarioRepository.findByUsername(request.getIdentifier())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "No se encontró ningún usuario con: " + request.getIdentifier()));
                });

        // 3. Validar que la nueva contraseña sea diferente
        if (passwordEncoder.matches(request.getNewPassword(), usuario.getPasswordHash())) {
            throw new BusinessException("La nueva contraseña debe ser diferente a la actual");
        }

        // 4. Encriptar y guardar
        usuario.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(usuario);

        System.out.println("✅ [Backend] Contraseña recuperada exitosamente para: " + usuario.getCorreo());

        // 5. Retornar respuesta (sin token, el usuario aún no hace login)
        return AuthResponse.builder()
                .mensaje("Contraseña recuperada exitosamente. Ahora puedes iniciar sesión.")
                .usuario(UserMapper.toResponse(usuario))
                .build();
    }
}