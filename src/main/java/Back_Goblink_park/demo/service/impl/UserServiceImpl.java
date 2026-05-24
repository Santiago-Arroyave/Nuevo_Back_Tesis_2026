package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.UserMapper;
import Back_Goblink_park.demo.dto.request.UserRequest;
import Back_Goblink_park.demo.dto.response.UserResponse;
import Back_Goblink_park.demo.entity.Rol;
import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.RolRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;
import Back_Goblink_park.demo.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // =====================================================
    // INYECCIONES
    // =====================================================

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    // =====================================================
    // CREAR USUARIO
    // =====================================================

    @Override
    public UserResponse crearUsuario(UserRequest request) {

        // VALIDAR CORREO

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {

            throw new BusinessException(
                    "El correo ya está registrado"
            );
        }

        // VALIDAR USERNAME

        if (request.getUsername() != null &&
                usuarioRepository.existsByUsername(
                        request.getUsername()
                )) {

            throw new BusinessException(
                    "El username ya existe"
            );
        }

        // BUSCAR ROL

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Rol no encontrado"
                        )
                );

        // ENCRIPTAR PASSWORD

        String hash =
                passwordEncoder.encode(
                        request.getPassword()
                );

        // CREAR USUARIO

        Usuario usuario = Usuario.builder()
                .rol(rol)
                .nombres(request.getNombres())
                .correo(request.getCorreo())
                .username(request.getUsername())
                .passwordHash(hash)
                .telefono(request.getTelefono())
                .fotoPerfil(request.getFotoPerfil())
                .estado(
                        request.getEstado() != null
                                ? request.getEstado()
                                : true
                )
                .build();

        // GUARDAR

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        // RESPUESTA

        return UserMapper.toResponse(usuarioGuardado);
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @Override
    public List<UserResponse> listarUsuarios() {

        return usuarioRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public UserResponse obtenerUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        return UserMapper.toResponse(usuario);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public UserResponse actualizarUsuario(
            Long id,
            UserRequest request
    ) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Rol no encontrado"
                        )
                );

        usuario.setRol(rol);
        usuario.setNombres(request.getNombres());
        usuario.setCorreo(request.getCorreo());
        usuario.setUsername(request.getUsername());
        usuario.setTelefono(request.getTelefono());
        usuario.setFotoPerfil(request.getFotoPerfil());
        usuario.setEstado(request.getEstado());

        // ACTUALIZAR PASSWORD SOLO SI VIENE

        if (request.getPassword() != null &&
                !request.getPassword().isBlank()) {

            usuario.setPasswordHash(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        Usuario actualizado =
                usuarioRepository.save(usuario);

        return UserMapper.toResponse(actualizado);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    public void eliminarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        usuarioRepository.delete(usuario);
    }
}