package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoReporteRequest;

import Back_Goblink_park.demo.dto.response.ProyectoReporteResponse;

import java.util.List;

public interface ProyectoReporteService {

    // =====================================================
    // ASOCIAR REPORTE A PROYECTO
    // =====================================================

    ProyectoReporteResponse asociarReporte(
            ProyectoReporteRequest request
    );

    // =====================================================
    // LISTAR REPORTES DE UN PROYECTO
    // =====================================================

    List<ProyectoReporteResponse>
    listarReportesProyecto(
            Long proyectoId
    );

    // =====================================================
    // LISTAR PROYECTOS DE UN REPORTE
    // =====================================================

    List<ProyectoReporteResponse>
    listarProyectosReporte(
            Long reporteId
    );

    // =====================================================
    // OBTENER RELACIÓN
    // =====================================================

    ProyectoReporteResponse obtenerRelacion(
            Long id
    );

    // =====================================================
    // ELIMINAR RELACIÓN
    // =====================================================

    void eliminarRelacion(
            Long id
    );


}