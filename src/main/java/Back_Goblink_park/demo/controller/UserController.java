package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.UserRequest;
import Back_Goblink_park.demo.dto.response.UserResponse;
import Back_Goblink_park.demo.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =====================================================
    // CREAR USUARIO
    // SOLO ADMIN
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse crearUsuario(
            @RequestBody UserRequest request
    ) {

        return userService.crearUsuario(request);
    }

    // =====================================================
    // LISTAR USUARIOS
    // SOLO ADMIN
    // =====================================================

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponse> listarUsuarios() {

        return userService.listarUsuarios();
    }

    // =====================================================
    // OBTENER USUARIO
    // ADMIN O USER
    // =====================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public UserResponse obtenerUsuario(
            @PathVariable Long id
    ) {

        return userService.obtenerUsuario(id);
    }

    // =====================================================
    // ACTUALIZAR USUARIO
    // SOLO ADMIN
    // =====================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse actualizarUsuario(

            @PathVariable Long id,

            @RequestBody UserRequest request
    ) {

        return userService.actualizarUsuario(
                id,
                request
        );
    }

    // =====================================================
    // ELIMINAR USUARIO
    // SOLO ADMIN
    // =====================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminarUsuario(
            @PathVariable Long id
    ) {

        userService.eliminarUsuario(id);
    }
}