package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoMiembroRequest;
import Back_Goblink_park.demo.dto.response.ProyectoMiembroResponse;
import Back_Goblink_park.demo.service.interfaces.ProyectoMiembroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/proyecto-miembros")
@RequiredArgsConstructor
public class ProyectoMiembroController {

    private final ProyectoMiembroService miembroService;

    // =====================================================
    // AGREGAR MIEMBRO
    // =====================================================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProyectoMiembroResponse agregarMiembro(
            @Valid @RequestBody ProyectoMiembroRequest request
    ) {
        return miembroService.agregarMiembro(request);
    }

    // =====================================================
    // LISTAR MIEMBROS DE UN PROYECTO (completo)
    // =====================================================
    @GetMapping("/proyecto/{proyectoId}")
    public List<ProyectoMiembroResponse> listarMiembrosProyecto(
            @PathVariable Long proyectoId
    ) {
        return miembroService.listarMiembrosProyecto(proyectoId);
    }

    // =====================================================
    // LISTAR MIEMBROS DE UN PROYECTO (paginado)
    // =====================================================
    @GetMapping("/proyecto/{proyectoId}/page")
    public ResponseEntity<Page<ProyectoMiembroResponse>> listarMiembrosProyectoPaginado(
            @PathVariable Long proyectoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                miembroService.listarMiembrosProyectoPaginado(proyectoId, page, size)
        );
    }

    // =====================================================
    // LISTAR PROYECTOS DE UN USUARIO (completo)
    // =====================================================
    @GetMapping("/usuario/{usuarioId}")
    public List<ProyectoMiembroResponse> listarProyectosUsuario(
            @PathVariable Long usuarioId
    ) {
        return miembroService.listarProyectosUsuario(usuarioId);
    }

    // =====================================================
    // LISTAR PROYECTOS DE UN USUARIO (paginado)
    // =====================================================
    @GetMapping("/usuario/{usuarioId}/page")
    public ResponseEntity<Page<ProyectoMiembroResponse>> listarProyectosUsuarioPaginado(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                miembroService.listarProyectosUsuarioPaginado(usuarioId, page, size)
        );
    }

    // =====================================================
    // OBTENER MIEMBRO POR ID
    // =====================================================
    @GetMapping("/{id}")
    public ProyectoMiembroResponse obtenerMiembro(
            @PathVariable Long id
    ) {
        return miembroService.obtenerMiembro(id);
    }

    // =====================================================
    // ELIMINAR MIEMBRO (SOFT DELETE)
    // =====================================================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMiembro(@PathVariable Long id) {
        miembroService.eliminarMiembro(id);
    }
}