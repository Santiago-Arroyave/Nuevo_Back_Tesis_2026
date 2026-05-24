package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.EstadoProyectoRequest;
import Back_Goblink_park.demo.dto.response.EstadoProyectoResponse;

import Back_Goblink_park.demo.service.interfaces.EstadoProyectoService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-proyecto")

@RequiredArgsConstructor
public class EstadoProyectoController {

    private final EstadoProyectoService service;

    // =====================================================
    // CREAR
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public EstadoProyectoResponse crear(
            @Valid
            @RequestBody
            EstadoProyectoRequest request
    ) {

        return service.crear(request);
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @GetMapping
    public List<EstadoProyectoResponse> listar() {

        return service.listar();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public EstadoProyectoResponse obtenerPorId(
            @PathVariable Long id
    ) {

        return service.obtenerPorId(id);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")
    public EstadoProyectoResponse actualizar(
            @PathVariable Long id,

            @Valid
            @RequestBody
            EstadoProyectoRequest request
    ) {

        return service.actualizar(id, request);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public void eliminar(
            @PathVariable Long id
    ) {

        service.eliminar(id);
    }
}