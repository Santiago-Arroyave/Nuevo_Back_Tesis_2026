package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.UserMapper;
import Back_Goblink_park.demo.dto.request.LoginRequest;
import Back_Goblink_park.demo.dto.request.RegisterRequest;
import Back_Goblink_park.demo.dto.response.AuthResponse;
import Back_Goblink_park.demo.entity.Rol;
import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.repository.RolRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;
import Back_Goblink_park.demo.security.JwtService;
import Back_Goblink_park.demo.service.interfaces.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}