package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.UserMapper;
import Back_Goblink_park.demo.dto.request.LoginRequest;
import Back_Goblink_park.demo.dto.request.RegisterRequest;
import Back_Goblink_park.demo.dto.response.AuthResponse;

import Back_Goblink_park.demo.entity.Rol;
import Back_Goblink_park.demo.entity.Usuario;

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

    @Override
    public AuthResponse register(
            RegisterRequest request
    ) {

        if (usuarioRepository.existsByCorreo(
                request.getCorreo()
        )) {

            throw new RuntimeException(
                    "El correo ya está registrado"
            );
        }

        Rol rolUser = rolRepository
                .findByNombre("USER")
                .orElseThrow(() ->
                        new RuntimeException(
                                "Rol USER no encontrado"
                        )
                );

        Usuario usuario = Usuario.builder()
                .rol(rolUser)
                .nombres(request.getNombres())
                .correo(request.getCorreo())
                .passwordHash(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .estado(true)
                .build();

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        String token =
                jwtService.generateToken(
                        usuarioGuardado.getCorreo()
                );

        return AuthResponse.builder()
                .token(token)
                .usuario(
                        UserMapper.toResponse(
                                usuarioGuardado
                        )
                )
                .build();
    }

    @Override
    public AuthResponse login(
            LoginRequest request
    ) {

        Usuario usuario = usuarioRepository
                .findByCorreo(request.getCorreo())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Credenciales inválidas"
                        )
                );

        boolean passwordCorrecta =
                passwordEncoder.matches(
                        request.getPassword(),
                        usuario.getPasswordHash()
                );

        if (!passwordCorrecta) {

            throw new RuntimeException(
                    "Credenciales inválidas"
            );
        }

        String token =
                jwtService.generateToken(
                        usuario.getCorreo()
                );

        return AuthResponse.builder()
                .token(token)
                .usuario(
                        UserMapper.toResponse(usuario)
                )
                .build();
    }
}