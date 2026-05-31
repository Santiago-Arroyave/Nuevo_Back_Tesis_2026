package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoObjetivoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoObjetivoResponse;
import Back_Goblink_park.demo.service.interfaces.ProyectoObjetivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/proyecto-objetivos")
@RequiredArgsConstructor
public class ProyectoObjetivoController {

    private final ProyectoObjetivoService objetivoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProyectoObjetivoResponse crearObjetivo(@Valid @RequestBody ProyectoObjetivoRequest request) {
        return objetivoService.crearObjetivo(request);
    }

    @GetMapping("/proyecto/{proyectoId}")
    public List<ProyectoObjetivoResponse> listarObjetivosProyecto(@PathVariable Long proyectoId) {
        return objetivoService.listarObjetivosProyecto(proyectoId);
    }

    @GetMapping("/{id}")
    public ProyectoObjetivoResponse obtenerObjetivo(@PathVariable Long id) {
        return objetivoService.obtenerObjetivo(id);
    }

    @PutMapping("/{id}")
    public ProyectoObjetivoResponse actualizarObjetivo(@PathVariable Long id, @Valid @RequestBody ProyectoObjetivoRequest request) {
        return objetivoService.actualizarObjetivo(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarObjetivo(@PathVariable Long id) {
        objetivoService.eliminarObjetivo(id);
    }
}