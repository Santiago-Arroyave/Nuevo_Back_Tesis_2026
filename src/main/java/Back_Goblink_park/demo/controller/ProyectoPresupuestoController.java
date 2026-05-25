package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoPresupuestoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoPresupuestoResponse;
import Back_Goblink_park.demo.service.interfaces.ProyectoPresupuestoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyecto-presupuestos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProyectoPresupuestoController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final ProyectoPresupuestoService
            proyectoPresupuestoService;

    // =====================================================
    // CREAR PRESUPUESTO
    // =====================================================

    @PostMapping
    public ResponseEntity<ProyectoPresupuestoResponse>
    crearPresupuesto(

            @RequestBody
            ProyectoPresupuestoRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(

                        proyectoPresupuestoService
                                .crearPresupuesto(request)
                );
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ProyectoPresupuestoResponse>>
    listarPresupuestos() {

        return ResponseEntity.ok(

                proyectoPresupuestoService
                        .listarPresupuestos()
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoPresupuestoResponse>
    obtenerPresupuesto(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(

                proyectoPresupuestoService
                        .obtenerPresupuesto(id)
        );
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<ProyectoPresupuestoResponse>>
    listarPorProyecto(

            @PathVariable Long proyectoId
    ) {

        return ResponseEntity.ok(

                proyectoPresupuestoService
                        .listarPorProyecto(proyectoId)
        );
    }

    // =====================================================
    // ACTUALIZAR PRESUPUESTO
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoPresupuestoResponse>
    actualizarPresupuesto(

            @PathVariable Long id,

            @RequestBody
            ProyectoPresupuestoRequest request
    ) {

        return ResponseEntity.ok(

                proyectoPresupuestoService
                        .actualizarPresupuesto(
                                id,
                                request
                        )
        );
    }

    // =====================================================
    // ELIMINAR PRESUPUESTO
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminarPresupuesto(

            @PathVariable Long id
    ) {

        proyectoPresupuestoService
                .eliminarPresupuesto(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}