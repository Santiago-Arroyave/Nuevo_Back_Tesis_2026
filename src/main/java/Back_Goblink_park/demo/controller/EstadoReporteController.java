package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.EstadoReporteRequest;
import Back_Goblink_park.demo.dto.response.EstadoReporteResponse;
import Back_Goblink_park.demo.service.interfaces.EstadoReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-reporte")
@RequiredArgsConstructor
public class EstadoReporteController {

    private final EstadoReporteService estadoReporteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoReporteResponse crear(
            @RequestBody EstadoReporteRequest request
    ) {

        return estadoReporteService.crear(request);
    }

    @GetMapping
    public List<EstadoReporteResponse> listar() {

        return estadoReporteService.listar();
    }

    @GetMapping("/{id}")
    public EstadoReporteResponse obtener(
            @PathVariable Long id
    ) {

        return estadoReporteService.obtener(id);
    }

    @PutMapping("/{id}")
    public EstadoReporteResponse actualizar(
            @PathVariable Long id,
            @RequestBody EstadoReporteRequest request
    ) {

        return estadoReporteService.actualizar(
                id,
                request
        );
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id
    ) {

        estadoReporteService.eliminar(id);
    }
}