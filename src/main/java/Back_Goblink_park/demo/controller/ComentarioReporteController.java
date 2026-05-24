package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ComentarioReporteRequest;
import Back_Goblink_park.demo.dto.response.ComentarioReporteResponse;
import Back_Goblink_park.demo.service.interfaces.ComentarioReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios-reporte")
@RequiredArgsConstructor
public class ComentarioReporteController {

    // =====================================================
    // INYECCIÓN
    // =====================================================

    private final ComentarioReporteService comentarioReporteService;

    // =====================================================
    // CREAR
    // =====================================================

    @PostMapping
    public ComentarioReporteResponse crearComentario(
            @RequestBody ComentarioReporteRequest request,
            Authentication authentication
    ) {

        return comentarioReporteService.crearComentario(
                request,
                authentication.getName()
        );
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @GetMapping
    public List<ComentarioReporteResponse> listarComentarios() {

        return comentarioReporteService.listarComentarios();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ComentarioReporteResponse obtenerComentario(
            @PathVariable Long id
    ) {

        return comentarioReporteService.obtenerComentario(id);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")
    public ComentarioReporteResponse actualizarComentario(
            @PathVariable Long id,
            @RequestBody ComentarioReporteRequest request
    ) {

        return comentarioReporteService.actualizarComentario(
                id,
                request
        );
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @DeleteMapping("/{id}")
    public void eliminarComentario(
            @PathVariable Long id
    ) {

        comentarioReporteService.eliminarComentario(id);
    }
}