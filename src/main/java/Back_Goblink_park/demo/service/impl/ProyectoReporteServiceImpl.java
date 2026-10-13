package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ProyectoReporteMapper;

import Back_Goblink_park.demo.dto.request.ProyectoReporteRequest;

import Back_Goblink_park.demo.dto.response.ProyectoReporteResponse;

import Back_Goblink_park.demo.entity.EstadoReporte;
import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoReporte;
import Back_Goblink_park.demo.entity.Reporte;

import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.EstadoReporteRepository;
import Back_Goblink_park.demo.repository.ProyectoReporteRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.ReporteRepository;

import Back_Goblink_park.demo.service.interfaces.ProyectoReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final EstadoReporteRepository estadoReporteRepository;

    // =====================================================
    // ASOCIAR REPORTE
    // =====================================================

    @Override
    @Transactional
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
        // VALIDAR REGLAS DE NEGOCIO (RESUELTO O YA VINCULADO)
        // ==============================================

        if (reporte.getEstadoReporte() != null && reporte.getEstadoReporte().getNombre() != null) {
            String nombreEstado = reporte.getEstadoReporte().getNombre().trim().toLowerCase();
            if (nombreEstado.contains("resuelto") || nombreEstado.contains("resuelta")) {
                throw new BusinessException("El reporte '" + reporte.getTitulo() + "' ya ha sido resuelto y no puede vincularse a un nuevo proyecto.");
            }
        }

        List<ProyectoReporte> relacionesExistentes = proyectoReporteRepository.findByReporteId(request.getReporteId());
        if (relacionesExistentes != null) {
            for (ProyectoReporte rel : relacionesExistentes) {
                if (rel != null && rel.getProyecto() != null && !Boolean.TRUE.equals(rel.getProyecto().getEliminado())) {
                    throw new BusinessException("El reporte '" + reporte.getTitulo() + "' ya se encuentra vinculado al proyecto #" + rel.getProyecto().getId());
                }
            }
        }

        boolean existe = proyectoReporteRepository
                .existsByProyectoIdAndReporteId(
                        request.getProyectoId(),
                        request.getReporteId()
                );

        if (existe) {
            throw new BusinessException(
                    "El reporte '" + reporte.getTitulo() + "' ya se encuentra vinculado al proyecto #" + request.getProyectoId()
            );
        }

        // ==============================================
        // ACTUALIZAR ESTADO DEL REPORTE SEGÚN EL PROYECTO
        // ==============================================

        boolean proyectoFinalizado = (proyecto.getEstadoProyecto() != null
                && (Long.valueOf(4L).equals(proyecto.getEstadoProyecto().getId())
                || (proyecto.getEstadoProyecto().getNombre() != null
                && proyecto.getEstadoProyecto().getNombre().toLowerCase().contains("finalizado"))))
                || (proyecto.getPorcentajeAvance() != null && proyecto.getPorcentajeAvance() >= 100.0);

        EstadoReporte estadoObjetivo = proyectoFinalizado
                ? estadoReporteRepository.findByNombreIgnoreCase("Resuelto")
                        .orElseGet(() -> estadoReporteRepository.findById(3L).orElse(null))
                : estadoReporteRepository.findByNombreIgnoreCase("Convertido en proyecto")
                        .orElseGet(() -> estadoReporteRepository.findById(7L).orElse(null));

        if (estadoObjetivo != null) {
            reporte.setEstadoReporte(estadoObjetivo);
            reporteRepository.save(reporte);
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional
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