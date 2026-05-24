package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.RolRequest;
import Back_Goblink_park.demo.dto.response.RolResponse;
import Back_Goblink_park.demo.service.interfaces.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    // =====================================================
    // CREAR ROL
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolResponse crearRol(
            @Valid @RequestBody RolRequest request
    ) {
        return rolService.crearRol(request);
    }

    // =====================================================
    // LISTAR ROLES
    // =====================================================

    @GetMapping
    public List<RolResponse> listarRoles() {
        return rolService.listarRoles();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public RolResponse obtenerRol(
            @PathVariable Long id
    ) {
        return rolService.obtenerPorId(id);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")
    public RolResponse actualizarRol(
            @PathVariable Long id,
            @Valid @RequestBody RolRequest request
    ) {
        return rolService.actualizarRol(id, request);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarRol(
            @PathVariable Long id
    ) {
        rolService.eliminarRol(id);
    }
}