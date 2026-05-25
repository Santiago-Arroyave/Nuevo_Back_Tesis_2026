package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoReporteRequest;

import Back_Goblink_park.demo.dto.response.ProyectoReporteResponse;

import Back_Goblink_park.demo.service.interfaces.ProyectoReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/proyecto-reportes")

@RequiredArgsConstructor

@CrossOrigin("*")
public class ProyectoReporteController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final ProyectoReporteService proyectoReporteService;

    // =====================================================
    // ASOCIAR REPORTE A PROYECTO
    // =====================================================

    @PostMapping
    public ResponseEntity<ProyectoReporteResponse>
    asociarReporte(

            @RequestBody
            ProyectoReporteRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)

                .body(

                        proyectoReporteService
                                .asociarReporte(request)
                );
    }

    // =====================================================
    // LISTAR REPORTES DE UN PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")

    public ResponseEntity<List<ProyectoReporteResponse>>
    listarReportesProyecto(

            @PathVariable
            Long proyectoId
    ) {

        return ResponseEntity.ok(

                proyectoReporteService
                        .listarReportesProyecto(
                                proyectoId
                        )
        );
    }

    // =====================================================
    // LISTAR PROYECTOS DE UN REPORTE
    // =====================================================

    @GetMapping("/reporte/{reporteId}")

    public ResponseEntity<List<ProyectoReporteResponse>>
    listarProyectosReporte(

            @PathVariable
            Long reporteId
    ) {

        return ResponseEntity.ok(

                proyectoReporteService
                        .listarProyectosReporte(
                                reporteId
                        )
        );
    }

    // =====================================================
    // OBTENER RELACIÓN
    // =====================================================

    @GetMapping("/{id}")

    public ResponseEntity<ProyectoReporteResponse>
    obtenerRelacion(

            @PathVariable
            Long id
    ) {

        return ResponseEntity.ok(

                proyectoReporteService
                        .obtenerRelacion(id)
        );
    }

    // =====================================================
    // ELIMINAR RELACIÓN
    // =====================================================

    @DeleteMapping("/{id}")

    public ResponseEntity<Void>
    eliminarRelacion(

            @PathVariable
            Long id
    ) {

        proyectoReporteService
                .eliminarRelacion(id);

        return ResponseEntity.noContent().build();
    }
}