package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CronogramaActividadProyectoRepository
        extends JpaRepository<CronogramaActividadProyecto, Long> {

    // =====================================================
    // LISTAR TODAS (OPTIMIZADO CON JOIN FETCH)
    // =====================================================

    @Override
    @Query("SELECT DISTINCT a FROM CronogramaActividadProyecto a " +
           "LEFT JOIN FETCH a.proyecto p " +
           "LEFT JOIN FETCH a.proyectoMiembro pm " +
           "LEFT JOIN FETCH pm.usuario u " +
           "LEFT JOIN FETCH a.evidencias e")
    List<CronogramaActividadProyecto> findAll();

    // =====================================================
    // LISTAR POR PROYECTO (OPTIMIZADO CON JOIN FETCH)
    // =====================================================

    @Query("SELECT DISTINCT a FROM CronogramaActividadProyecto a " +
           "LEFT JOIN FETCH a.proyecto p " +
           "LEFT JOIN FETCH a.proyectoMiembro pm " +
           "LEFT JOIN FETCH pm.usuario u " +
           "LEFT JOIN FETCH a.evidencias e " +
           "WHERE a.proyecto.id = :proyectoId " +
           "ORDER BY a.fechaInicio ASC")
    List<CronogramaActividadProyecto>
    findByProyectoIdOrderByFechaInicioAsc(
            @Param("proyectoId") Long proyectoId
    );

    // =====================================================
    // LISTAR POR MIEMBRO DEL PROYECTO (OPTIMIZADO CON JOIN FETCH)
    // =====================================================

    @Query("SELECT DISTINCT a FROM CronogramaActividadProyecto a " +
           "LEFT JOIN FETCH a.proyecto p " +
           "LEFT JOIN FETCH a.proyectoMiembro pm " +
           "LEFT JOIN FETCH pm.usuario u " +
           "LEFT JOIN FETCH a.evidencias e " +
           "WHERE a.proyectoMiembro.id = :proyectoMiembroId")
    List<CronogramaActividadProyecto>
    findByProyectoMiembroId(
            @Param("proyectoMiembroId") Long proyectoMiembroId
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