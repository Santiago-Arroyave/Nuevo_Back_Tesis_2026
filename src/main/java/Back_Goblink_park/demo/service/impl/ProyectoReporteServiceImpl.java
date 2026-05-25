package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ProyectoReporteMapper;

import Back_Goblink_park.demo.dto.request.ProyectoReporteRequest;

import Back_Goblink_park.demo.dto.response.ProyectoReporteResponse;

import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoReporte;
import Back_Goblink_park.demo.entity.Reporte;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.ProyectoReporteRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.ReporteRepository;

import Back_Goblink_park.demo.service.interfaces.ProyectoReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoReporteServiceImpl
        implements ProyectoReporteService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ProyectoReporteRepository proyectoReporteRepository;

    private final ProyectoRepository proyectoRepository;

    private final ReporteRepository reporteRepository;

    // =====================================================
    // ASOCIAR REPORTE
    // =====================================================

    @Override
    public ProyectoReporteResponse asociarReporte(
            ProyectoReporteRequest request
    ) {

        // ==============================================
        // VALIDAR PROYECTO
        // ==============================================

        Proyecto proyecto = proyectoRepository
                .findById(request.getProyectoId())

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Proyecto no encontrado"
                        )
                );

        // ==============================================
        // VALIDAR REPORTE
        // ==============================================

        Reporte reporte = reporteRepository
                .findById(request.getReporteId())

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Reporte no encontrado"
                        )
                );

        // ==============================================
        // VALIDAR DUPLICADO
        // ==============================================

        boolean existe = proyectoReporteRepository

                .existsByProyectoIdAndReporteId(
                        request.getProyectoId(),
                        request.getReporteId()
                );

        if (existe) {

            throw new RuntimeException(
                    "El reporte ya está asociado al proyecto"
            );
        }

        // ==============================================
        // CREAR RELACIÓN
        // ==============================================

        ProyectoReporte proyectoReporte =
                ProyectoReporte.builder()

                        .proyecto(proyecto)

                        .reporte(reporte)

                        .build();

        // ==============================================
        // GUARDAR
        // ==============================================

        ProyectoReporte guardado =
                proyectoReporteRepository.save(
                        proyectoReporte
                );

        // ==============================================
        // RESPONSE
        // ==============================================

        return ProyectoReporteMapper
                .toResponse(guardado);
    }

    // =====================================================
    // LISTAR REPORTES POR PROYECTO
    // =====================================================

    @Override
    public List<ProyectoReporteResponse>
    listarReportesProyecto(
            Long proyectoId
    ) {

        return proyectoReporteRepository

                .findByProyectoId(proyectoId)

                .stream()

                .map(
                        ProyectoReporteMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR PROYECTOS POR REPORTE
    // =====================================================

    @Override
    public List<ProyectoReporteResponse>
    listarProyectosReporte(
            Long reporteId
    ) {

        return proyectoReporteRepository

                .findByReporteId(reporteId)

                .stream()

                .map(
                        ProyectoReporteMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER RELACIÓN
    // =====================================================

    @Override
    public ProyectoReporteResponse obtenerRelacion(
            Long id
    ) {

        ProyectoReporte relacion =
                proyectoReporteRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Relación no encontrada"
                                )
                        );

        return ProyectoReporteMapper
                .toResponse(relacion);
    }

    // =====================================================
    // ELIMINAR RELACIÓN
    // =====================================================

    @Override
    public void eliminarRelacion(
            Long id
    ) {

        ProyectoReporte relacion =
                proyectoReporteRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Relación no encontrada"
                                )
                        );

        proyectoReporteRepository.delete(relacion);
    }
}