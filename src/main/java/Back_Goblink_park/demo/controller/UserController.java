package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.UserRequest;
import Back_Goblink_park.demo.dto.response.UserEstadisticasResponse;
import Back_Goblink_park.demo.dto.response.UserResponse;
import Back_Goblink_park.demo.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =====================================================
    // CREAR USUARIO - SOLO ADMIN
    // =====================================================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse crearUsuario(@Valid @RequestBody UserRequest request) {
        return userService.crearUsuario(request);
    }

    // =====================================================
    // LISTAR USUARIOS - ADMIN / USER
    // =====================================================
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<UserResponse> listarUsuarios() {
        return userService.listarUsuarios();
    }

    // =====================================================
    // OBTENER POR ID - SOLO ADMIN
    // =====================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse obtenerUsuario(@PathVariable Long id) {
        return userService.obtenerUsuario(id);
    }

    // =====================================================
    // ACTUALIZAR DATOS COMPLETOS - SOLO ADMIN
    // =====================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request
    ) {
        return userService.actualizarUsuario(id, request);
    }

    // =====================================================
    // OBTENER PERFIL PROPIO - AUTENTICADO
    // =====================================================
    @GetMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> obtenerMiPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();
        UserResponse response = userService.obtenerPerfilPorCorreo(correoUsuario);
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ACTUALIZAR PERFIL PROPIO (CON FOTO) - AUTENTICADO
    // =====================================================
    @PutMapping(value = "/perfil", consumes = {"multipart/form-data", "application/x-www-form-urlencoded"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> actualizarPerfil(
            @RequestParam(required = false) String nombres,
            @RequestParam(required = false) String apellidos,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String documentoIdentidad,
            @RequestParam(required = false) String entidad,
            @RequestParam(required = false) String cargoRol,
            @RequestParam(required = false) String cargo_rol,
            @RequestParam(required = false) String municipioResidencia,
            @RequestParam(required = false) String municipio_residencia,
            @RequestParam(required = false) String sobreMi,
            @RequestParam(required = false) String sobre_mi,
            @RequestPart(value = "fotoPerfil", required = false) MultipartFile fotoPerfil,
            @RequestPart(value = "fotoPortada", required = false) MultipartFile fotoPortada
    ) {
        // Obtener usuario autenticado del SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();

        String docFinal = (documentoIdentidad != null && !documentoIdentidad.isBlank()) ? documentoIdentidad : documento;
        String cargoFinal = (cargoRol != null && !cargoRol.isBlank()) ? cargoRol : cargo_rol;
        String municipioFinal = (municipioResidencia != null && !municipioResidencia.isBlank()) ? municipioResidencia : municipio_residencia;
        String sobreMiFinal = (sobreMi != null && !sobreMi.isBlank()) ? sobreMi : sobre_mi;

        UserResponse updated = userService.actualizarPerfilPorCorreo(
                correoUsuario,
                nombres,
                apellidos,
                username,
                telefono,
                docFinal,
                entidad,
                cargoFinal,
                municipioFinal,
                sobreMiFinal,
                fotoPerfil,
                fotoPortada
        );

        return ResponseEntity.ok(updated);
    }

    // =====================================================
    // ACTUALIZAR PERFIL PROPIO (JSON) - AUTENTICADO
    // =====================================================
    @PutMapping(value = "/perfil", consumes = "application/json")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> actualizarPerfilJson(
            @RequestBody UserRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();

        UserResponse updated = userService.actualizarPerfilPorCorreo(
                correoUsuario,
                request.getNombres(),
                request.getApellidos(),
                request.getUsername(),
                request.getTelefono(),
                request.resolverDocumento(),
                request.getEntidad(),
                request.resolverCargo(),
                request.resolverMunicipio(),
                request.resolverSobreMi(),
                null,
                null,
                request.getFotoPerfil(),
                request.getFotoPortada()
        );

        return ResponseEntity.ok(updated);
    }

    // =====================================================
    // OBTENER ESTADÍSTICAS DEL USUARIO - ADMIN / USER
    // =====================================================
    @GetMapping("/{id}/estadisticas")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<UserEstadisticasResponse> obtenerEstadisticas(@PathVariable Long id) {
        UserEstadisticasResponse response = userService.obtenerEstadisticas(id);
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ACTIVAR / DESACTIVAR USUARIO - SOLO ADMIN
    // =====================================================
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse cambiarEstadoUsuario(
            @PathVariable Long id,
            @RequestParam Boolean estado
    ) {
        return userService.cambiarEstadoUsuario(id, estado);
    }

    // =====================================================
    // ELIMINAR / DESACTIVAR (SOFT DELETE) - SOLO ADMIN
    // =====================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable Long id) {
        userService.eliminarUsuario(id);
    }
}