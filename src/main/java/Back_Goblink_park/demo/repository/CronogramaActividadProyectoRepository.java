package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CronogramaActividadProyectoRepository
        extends JpaRepository<CronogramaActividadProyecto, Long> {

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<CronogramaActividadProyecto>
    findByProyectoIdOrderByFechaInicioAsc(
            Long proyectoId
    );

    // =====================================================
    // LISTAR POR MIEMBRO DEL PROYECTO (ANTES RESPONSABLE)
    // =====================================================

    List<CronogramaActividadProyecto>
    findByProyectoMiembroId(
            Long proyectoMiembroId
    );

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    List<CronogramaActividadProyecto>
    findByEstado(
            String estado
    );

    // =====================================================
    // LISTAR POR PROYECTO Y ESTADO
    // =====================================================

    List<CronogramaActividadProyecto>
    findByProyectoIdAndEstado(
            Long proyectoId,
            String estado
    );

    // =====================================================
    // LISTAR POR PROYECTO Y MIEMBRO
    // =====================================================

    List<CronogramaActividadProyecto>
    findByProyectoIdAndProyectoMiembroId(
            Long proyectoId,
            Long proyectoMiembroId
    );

    // =====================================================
    // CONTAR ACTIVIDADES POR PROYECTO Y ESTADO
    // =====================================================

    Long countByProyectoIdAndEstado(
            Long proyectoId,
            String estado
    );

    // =====================================================
    // LISTAR POR PROYECTO Y PRIORIDAD
    // =====================================================

    List<CronogramaActividadProyecto>
    findByProyectoIdAndPrioridadOrderByFechaInicioAsc(
            Long proyectoId,
            String prioridad
    );
}