package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.service.interfaces.CronogramaActividadProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evidencias-actividad")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EvidenciaActividadController {

    private final CronogramaActividadProyectoService actividadService;

    // =====================================================
    // ELIMINAR EVIDENCIA DE ACTIVIDAD
    // =====================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvidencia(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String correoUsuario = authentication != null ? authentication.getName() : null;
        actividadService.eliminarEvidenciaActividad(id, correoUsuario);
        return ResponseEntity.noContent().build();
    }
}
