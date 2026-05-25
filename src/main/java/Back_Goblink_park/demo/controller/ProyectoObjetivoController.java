package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoObjetivoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoObjetivoResponse;
import Back_Goblink_park.demo.service.interfaces.ProyectoObjetivoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyecto-objetivos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProyectoObjetivoController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final ProyectoObjetivoService
            proyectoObjetivoService;

    // =====================================================
    // CREAR OBJETIVO
    // =====================================================

    @PostMapping
    public ResponseEntity<ProyectoObjetivoResponse>
    crearObjetivo(

            @RequestBody
            ProyectoObjetivoRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(

                        proyectoObjetivoService
                                .crearObjetivo(request)
                );
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ProyectoObjetivoResponse>>
    listarObjetivos() {

        return ResponseEntity.ok(

                proyectoObjetivoService
                        .listarObjetivos()
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoObjetivoResponse>
    obtenerObjetivo(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(

                proyectoObjetivoService
                        .obtenerObjetivo(id)
        );
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<ProyectoObjetivoResponse>>
    listarPorProyecto(

            @PathVariable Long proyectoId
    ) {

        return ResponseEntity.ok(

                proyectoObjetivoService
                        .listarPorProyecto(proyectoId)
        );
    }

    // =====================================================
    // ACTUALIZAR OBJETIVO
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoObjetivoResponse>
    actualizarObjetivo(

            @PathVariable Long id,

            @RequestBody
            ProyectoObjetivoRequest request
    ) {

        return ResponseEntity.ok(

                proyectoObjetivoService
                        .actualizarObjetivo(
                                id,
                                request
                        )
        );
    }

    // =====================================================
    // ELIMINAR OBJETIVO
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminarObjetivo(

            @PathVariable Long id
    ) {

        proyectoObjetivoService
                .eliminarObjetivo(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}