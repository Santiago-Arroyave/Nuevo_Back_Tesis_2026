package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.UserRequest;
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
    // ACTUALIZAR PERFIL PROPIO (CON FOTO) - AUTENTICADO
    // =====================================================
    @PutMapping(value = "/perfil", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> actualizarPerfil(
            @RequestParam(required = false) String nombres,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) MultipartFile fotoPerfil
    ) {
        // Obtener usuario autenticado del SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();

        // ✅ LLAMAR AL SERVICE CORRECTAMENTE
        UserResponse updated = userService.actualizarPerfilPorCorreo(
                correoUsuario,
                nombres,
                username,
                telefono,
                fotoPerfil
        );

        return ResponseEntity.ok(updated);
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