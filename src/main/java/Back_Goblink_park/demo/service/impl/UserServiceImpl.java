package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.UserMapper;
import Back_Goblink_park.demo.dto.request.UserRequest;
import Back_Goblink_park.demo.dto.response.UserEstadisticasResponse;
import Back_Goblink_park.demo.dto.response.UserResponse;
import Back_Goblink_park.demo.entity.Rol;
import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.RolRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;
import Back_Goblink_park.demo.service.interfaces.FileStorageService;
import Back_Goblink_park.demo.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // =====================================================
    // INYECCIONES DE DEPENDENCIAS
    // =====================================================
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    // =====================================================
    // CREAR USUARIO
    // =====================================================
    @Override
    @Transactional
    public UserResponse crearUsuario(UserRequest request) {
        // Normalizar correo en minúsculas y sin espacios
        String correoSanitizado = request.getCorreo().trim().toLowerCase();
        if (usuarioRepository.existsByCorreoIgnoreCase(correoSanitizado)) {
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
        String doc = request.getDocumentoIdentidad() != null ? request.getDocumentoIdentidad() : request.getDocumento();

        Usuario usuario = Usuario.builder()
                .rol(rol)
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .correo(correoSanitizado)
                .username(request.getUsername())
                .passwordHash(hash)
                .telefono(request.getTelefono())
                .documentoIdentidad(doc)
                .entidad(request.getEntidad())
                .cargoRol(request.getCargoRol())
                .municipioResidencia(request.getMunicipioResidencia())
                .sobreMi(request.getSobreMi())
                .fotoPerfil(request.getFotoPerfil())
                .fotoPortada(request.getFotoPortada())
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
        if (request.getApellidos() != null) {
            usuario.setApellidos(request.getApellidos());
        }
        if (request.getCorreo() != null && !request.getCorreo().isBlank()) {
            String correoSanitizado = request.getCorreo().trim().toLowerCase();
            if (!correoSanitizado.equalsIgnoreCase(usuario.getCorreo()) && usuarioRepository.existsByCorreoIgnoreCase(correoSanitizado)) {
                throw new BusinessException("El correo ya está registrado por otro usuario");
            }
            usuario.setCorreo(correoSanitizado);
        }
        usuario.setUsername(request.getUsername());
        usuario.setTelefono(request.getTelefono());

        String doc = request.getDocumentoIdentidad() != null ? request.getDocumentoIdentidad() : request.getDocumento();
        if (doc != null) {
            usuario.setDocumentoIdentidad(doc);
        }
        if (request.getEntidad() != null) {
            usuario.setEntidad(request.getEntidad());
        }
        if (request.getCargoRol() != null) {
            usuario.setCargoRol(request.getCargoRol());
        }
        if (request.getMunicipioResidencia() != null) {
            usuario.setMunicipioResidencia(request.getMunicipioResidencia());
        }
        if (request.getSobreMi() != null) {
            usuario.setSobreMi(request.getSobreMi());
        }
        if (request.getFotoPerfil() != null) {
            usuario.setFotoPerfil(request.getFotoPerfil());
        }
        if (request.getFotoPortada() != null) {
            usuario.setFotoPortada(request.getFotoPortada());
        }

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
    // ACTUALIZAR PERFIL PROPIO (COMPLETO) - USUARIO AUTENTICADO
    // =====================================================
    @Override
    @Transactional
    public UserResponse actualizarPerfilPorCorreo(
            String correoUsuario,
            String nombres,
            String apellidos,
            String username,
            String telefono,
            String documentoIdentidad,
            String entidad,
            String cargoRol,
            String municipioResidencia,
            String sobreMi,
            MultipartFile fotoPerfil,
            MultipartFile fotoPortada
    ) {
        return actualizarPerfilPorCorreo(
                correoUsuario,
                nombres,
                apellidos,
                username,
                telefono,
                documentoIdentidad,
                entidad,
                cargoRol,
                municipioResidencia,
                sobreMi,
                fotoPerfil,
                fotoPortada,
                null,
                null
        );
    }

    @Override
    @Transactional
    public UserResponse actualizarPerfilPorCorreo(
            String correoUsuario,
            String nombres,
            String apellidos,
            String username,
            String telefono,
            String documentoIdentidad,
            String entidad,
            String cargoRol,
            String municipioResidencia,
            String sobreMi,
            MultipartFile fotoPerfil,
            MultipartFile fotoPortada,
            String fotoPerfilBase64,
            String fotoPortadaBase64
    ) {
        String correoSanitizado = correoUsuario.trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoSanitizado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correoSanitizado));

        if (nombres != null && !nombres.isBlank()) usuario.setNombres(nombres.trim());
        if (apellidos != null) usuario.setApellidos(apellidos.trim());
        if (username != null && !username.isBlank()) usuario.setUsername(username.trim());
        if (telefono != null) usuario.setTelefono(telefono.trim());
        if (documentoIdentidad != null) usuario.setDocumentoIdentidad(documentoIdentidad.trim());
        if (entidad != null) usuario.setEntidad(entidad.trim());
        if (cargoRol != null) usuario.setCargoRol(cargoRol.trim());
        if (municipioResidencia != null) usuario.setMunicipioResidencia(municipioResidencia.trim());
        if (sobreMi != null) usuario.setSobreMi(sobreMi.trim());

        // Procesar foto de perfil si se envía archivo multipart
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String url = fileStorageService.uploadFile(fotoPerfil, "avatars");
            usuario.setFotoPerfil(url);
        } else if (fotoPerfilBase64 != null && !fotoPerfilBase64.isBlank()) {
            usuario.setFotoPerfil(fotoPerfilBase64.trim());
        }

        // Procesar foto de portada si se envía archivo multipart
        if (fotoPortada != null && !fotoPortada.isEmpty()) {
            String url = fileStorageService.uploadFile(fotoPortada, "portadas");
            usuario.setFotoPortada(url);
        } else if (fotoPortadaBase64 != null && !fotoPortadaBase64.isBlank()) {
            usuario.setFotoPortada(fotoPortadaBase64.trim());
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return UserMapper.toResponse(usuarioGuardado);
    }

    // =====================================================
    // OBTENER PERFIL PROPIO POR CORREO - AUTENTICADO
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public UserResponse obtenerPerfilPorCorreo(String correoUsuario) {
        String correoSanitizado = correoUsuario.trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoSanitizado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correoSanitizado));

        return UserMapper.toResponse(usuario);
    }

    // =====================================================
    // ACTUALIZAR PERFIL PROPIO (BÁSICO) - SOBRECARGA
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
        return actualizarPerfilPorCorreo(
                correoUsuario,
                nombres,
                null,
                username,
                telefono,
                null,
                null,
                null,
                null,
                null,
                fotoPerfil,
                null
        );
    }

    // =====================================================
    // OBTENER ESTADÍSTICAS DEL USUARIO
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public UserEstadisticasResponse obtenerEstadisticas(Long idUsuario) {
        Map<String, Object> stats = usuarioRepository.obtenerEstadisticasUsuarioRaw(idUsuario);

        Long proyectos = 0L;
        Long reportes = 0L;
        Long evidencias = 0L;

        if (stats != null) {
            Object p = stats.get("totalproyectos");
            if (p == null) p = stats.get("TOTALPROYECTOS");
            Object r = stats.get("totalreportes");
            if (r == null) r = stats.get("TOTALREPORTES");
            Object e = stats.get("totalevidencias");
            if (e == null) e = stats.get("TOTALEVIDENCIAS");

            if (p instanceof Number) proyectos = ((Number) p).longValue();
            if (r instanceof Number) reportes = ((Number) r).longValue();
            if (e instanceof Number) evidencias = ((Number) e).longValue();
        }

        return UserEstadisticasResponse.builder()
                .totalProyectos(proyectos)
                .totalReportes(reportes)
                .totalEvidencias(evidencias)
                .build();
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
        String correoLimpio = (correo != null) ? correo.trim().toLowerCase() : "";
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoLimpio)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));

        if (!usuario.getEstado()) {
            throw new BusinessException("El usuario está inactivo. Contacte al administrador.");
        }
    }
}