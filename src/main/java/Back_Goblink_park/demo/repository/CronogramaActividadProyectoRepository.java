package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CronogramaActividadProyectoRepository
        extends JpaRepository<
        CronogramaActividadProyecto,
        Long
        > {

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<CronogramaActividadProyecto>
    findByProyectoIdOrderByFechaInicioAsc(
            Long proyectoId
    );

    // =====================================================
    // LISTAR POR RESPONSABLE
    // =====================================================

    List<CronogramaActividadProyecto>
    findByResponsableId(
            Long responsableId
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
}