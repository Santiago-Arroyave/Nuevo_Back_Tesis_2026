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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =====================================================
    // SOLO ADMIN
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    @PreAuthorize("hasRole('ADMIN')")

    public UserResponse crearUsuario(
            @RequestBody UserRequest request
    ) {

        return userService.crearUsuario(request);
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping

    @PreAuthorize("hasRole('ADMIN')")

    public List<UserResponse> listarUsuarios() {

        return userService.listarUsuarios();
    }

    // =====================================================
    // USER O ADMIN
    // =====================================================

    @GetMapping("/{id}")

    @PreAuthorize(
            "hasAnyRole('ADMIN','USER')"
    )

    public UserResponse obtenerUsuario(
            @PathVariable Long id
    ) {

        return userService.obtenerUsuario(id);
    }

    // =====================================================
    // SOLO ADMIN
    // =====================================================

    @PutMapping("/{id}")

    @PreAuthorize("hasRole('ADMIN')")

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
    // SOLO ADMIN
    // =====================================================

    @DeleteMapping("/{id}")

    @PreAuthorize("hasRole('ADMIN')")

    public void eliminarUsuario(
            @PathVariable Long id
    ) {

        userService.eliminarUsuario(id);
    }
}