package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.PrioridadRequest;
import Back_Goblink_park.demo.dto.response.PrioridadResponse;
import Back_Goblink_park.demo.service.interfaces.PrioridadService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prioridades")
@RequiredArgsConstructor
public class PrioridadController {

    private final PrioridadService prioridadService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrioridadResponse crear(
            @RequestBody PrioridadRequest request
    ) {

        return prioridadService.crear(request);
    }

    @GetMapping
    public List<PrioridadResponse> listar() {

        return prioridadService.listar();
    }

    @GetMapping("/{id}")
    public PrioridadResponse obtener(
            @PathVariable Long id
    ) {

        return prioridadService.obtener(id);
    }

    @PutMapping("/{id}")
    public PrioridadResponse actualizar(
            @PathVariable Long id,
            @RequestBody PrioridadRequest request
    ) {

        return prioridadService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id
    ) {

        prioridadService.eliminar(id);
    }
}