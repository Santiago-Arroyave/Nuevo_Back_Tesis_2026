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
import Back_Goblink_park.demo.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // =====================================================
    // INYECCIONES DE DEPENDENCIAS
    // =====================================================
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // =====================================================
    // CREAR USUARIO
    // =====================================================
    @Override
    @Transactional
    public UserResponse crearUsuario(UserRequest request) {
        // Validar correo único
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessException("El correo ya está registrado");
        }

        // Validar username único (si se envía)
        if (request.getUsername() != null && !request.getUsername().isEmpty()
                && usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("El username ya existe");
        }

        // Buscar rol
        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        // Encriptar password
        String hash = passwordEncoder.encode(request.getPassword());

        // Crear entidad
        Usuario usuario = Usuario.builder()
                .rol(rol)
                .nombres(request.getNombres())
                .correo(request.getCorreo())
                .username(request.getUsername())
                .passwordHash(hash)
                .telefono(request.getTelefono())
                .fotoPerfil(request.getFotoPerfil())
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return UserMapper.toResponse(guardado);
    }

    // =====================================================
    // LISTAR USUARIOS
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER USUARIO POR ID
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public UserResponse obtenerUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return UserMapper.toResponse(usuario);
    }

    // =====================================================
    // ACTUALIZAR USUARIO (ADMIN - DATOS COMPLETOS)
    // =====================================================
    @Override
    @Transactional
    public UserResponse actualizarUsuario(Long id, UserRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        usuario.setRol(rol);
        usuario.setNombres(request.getNombres());
        usuario.setCorreo(request.getCorreo());
        usuario.setUsername(request.getUsername());
        usuario.setTelefono(request.getTelefono());
        usuario.setFotoPerfil(request.getFotoPerfil());

        if (request.getEstado() != null) {
            usuario.setEstado(request.getEstado());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return UserMapper.toResponse(actualizado);
    }

    // =====================================================
    // ACTUALIZAR PERFIL PROPIO (CON FOTO DE PERFIL) - NUEVO MÉTODO ✅
    // =====================================================
    @Override
    @Transactional
    public UserResponse actualizarPerfilPorCorreo(
            String correoUsuario,
            String nombres,
            String username,
            String telefono,
            MultipartFile fotoPerfil
    ) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Actualizar campos solo si no son null/vacíos
        if (nombres != null && !nombres.isEmpty()) {
            usuario.setNombres(nombres.trim());
        }
        if (username != null && !username.isEmpty()) {
            // Validar que el username no esté en uso por otro usuario
            if (!username.equals(usuario.getUsername()) && usuarioRepository.existsByUsername(username)) {
                throw new BusinessException("El username ya está registrado por otro usuario");
            }
            usuario.setUsername(username.trim());
        }
        if (telefono != null) {
            usuario.setTelefono(telefono.trim());
        }

        // Procesar foto de perfil si se envía nueva
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            try {
                byte[] bytes = fotoPerfil.getBytes();
                String base64Foto = Base64.getEncoder().encodeToString(bytes);
                // Limpiar saltos de línea o espacios
                usuario.setFotoPerfil(base64Foto.replaceAll("\\s", ""));
            } catch (IOException e) {
                throw new RuntimeException("Error al convertir la foto de perfil a Base64: " + e.getMessage(), e);
            }
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return UserMapper.toResponse(actualizado);
    }

    // =====================================================
    // CAMBIAR ESTADO DE USUARIO (ACTIVAR/DESACTIVAR)
    // =====================================================
    @Override
    @Transactional
    public UserResponse cambiarEstadoUsuario(Long id, Boolean estado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setEstado(estado);
        Usuario actualizado = usuarioRepository.save(usuario);
        return UserMapper.toResponse(actualizado);
    }

    // =====================================================
    // ELIMINAR USUARIO (SOFT DELETE)
    // =====================================================
    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // SOFT DELETE: Cambiar estado a false
        usuario.setEstado(false);
        usuarioRepository.save(usuario);
    }

    // =====================================================
    // VALIDAR USUARIO ACTIVO (para login)
    // =====================================================
    public void validarUsuarioActivo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!usuario.getEstado()) {
            throw new BusinessException("El usuario está inactivo. Contacte al administrador.");
        }
    }
}