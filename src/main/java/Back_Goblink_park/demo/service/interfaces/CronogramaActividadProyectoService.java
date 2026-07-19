package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.CronogramaActividadCompletarRequest;
import Back_Goblink_park.demo.dto.request.CronogramaActividadProyectoRequest;
import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface CronogramaActividadProyectoService {

    // =====================================================
    // CREAR
    // =====================================================

    CronogramaActividadProyectoResponse
    crearActividad(
            CronogramaActividadProyectoRequest request
    );

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    List<CronogramaActividadProyectoResponse>
    listarActividades();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    CronogramaActividadProyectoResponse
    obtenerActividad(
            Long id
    );

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<CronogramaActividadProyectoResponse>
    listarPorProyecto(
            Long proyectoId
    );

    // =====================================================
    // LISTAR POR MIEMBRO DEL PROYECTO
    // =====================================================

    List<CronogramaActividadProyectoResponse>
    listarPorMiembro(
            Long proyectoMiembroId
    );

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    List<CronogramaActividadProyectoResponse>
    listarPorEstado(
            String estado
    );

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    CronogramaActividadProyectoResponse
    actualizarActividad(
            Long id,
            CronogramaActividadProyectoRequest request
    );

    // =====================================================
    // ELIMINAR
    // =====================================================

    void eliminarActividad(
            Long id
    );

    // =====================================================
    // MARCAR ACTIVIDAD COMO COMPLETADA  ← NUEVO
    // =====================================================

    CronogramaActividadProyectoResponse
    marcarActividadCompletada(
            Long actividadId
    );

    // =====================================================
    // RECALCULAR AVANCE DEL PROYECTO  ← NUEVO
    // =====================================================

    BigDecimal recalcularAvanceProyecto(
            Long proyectoId
    );

    // =====================================================
    // MARCAR ACTIVIDAD COMO COMPLETADA CON EVIDENCIA
    // =====================================================

    CronogramaActividadProyectoResponse
    marcarActividadCompletadaConEvidencia(
            Long actividadId,
            List<MultipartFile> files,
            String descripcionEvidencia,
            String observaciones
    );
}