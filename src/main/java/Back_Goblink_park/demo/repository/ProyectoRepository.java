package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Proyecto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProyectoRepository
        extends JpaRepository<Proyecto, Long>,
        JpaSpecificationExecutor<Proyecto> {

    // =====================================================
    // ACTIVOS
    // =====================================================

    List<Proyecto> findByEstadoTrue();

    // =====================================================
    // ACTIVOS Y NO ELIMINADOS (OPTIMIZADO CON JOIN FETCH)
    // =====================================================

    @Query("SELECT DISTINCT p FROM Proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "LEFT JOIN FETCH p.creadoPor " +
           "WHERE p.estado = true AND p.eliminado = false")
    List<Proyecto> findByEstadoTrueAndEliminadoFalse();

    // =====================================================
    // NO ELIMINADOS (OPTIMIZADO CON JOIN FETCH)
    // =====================================================

    @Query("SELECT DISTINCT p FROM Proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "LEFT JOIN FETCH p.creadoPor " +
           "WHERE p.eliminado = false")
    List<Proyecto> findByEliminadoFalse();

    // =====================================================
    // PROYECTOS POR MIEMBRO ACTIVO (OPTIMIZADO CON JOIN FETCH)
    // =====================================================

    @Query("SELECT DISTINCT p FROM ProyectoMiembro pm " +
           "JOIN pm.proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "LEFT JOIN FETCH p.creadoPor " +
           "WHERE pm.usuario.id = :usuarioId AND pm.estado = true AND p.eliminado = false")
    List<Proyecto> findProyectosByUsuarioId(@Param("usuarioId") Long usuarioId);

    // =====================================================
    // CONTEO MASIVO DE PARTICIPANTES POR PROYECTO
    // =====================================================

    @Query("SELECT pm.proyecto.id, COUNT(pm.id) FROM ProyectoMiembro pm " +
           "WHERE pm.proyecto.id IN :proyectoIds AND pm.estado = true " +
           "GROUP BY pm.proyecto.id")
    List<Object[]> countParticipantesPorProyectos(@Param("proyectoIds") List<Long> proyectoIds);

    // =====================================================
    // POR ESTADO PROYECTO
    // =====================================================

    List<Proyecto> findByEstadoProyectoId(
            Long estadoProyectoId
    );

    // =====================================================
    // POR PRIORIDAD
    // =====================================================

    List<Proyecto> findByPrioridadId(
            Long prioridadId
    );

    // =====================================================
    // POR CATEGORÍA
    // =====================================================

    List<Proyecto> findByCategoriaId(
            Long categoriaId
    );

    // =====================================================
    // POR USUARIO CREADOR (OPTIMIZADO CON JOIN FETCH)
    // =====================================================

    @Query("SELECT DISTINCT p FROM Proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "LEFT JOIN FETCH p.creadoPor " +
           "WHERE p.creadoPor.id = :usuarioId AND p.eliminado = false")
    List<Proyecto> findByCreadoPorId(
            @Param("usuarioId") Long usuarioId
    );

    // =====================================================
    // BÚSQUEDA POR NOMBRE
    // =====================================================

    List<Proyecto> findByNombreContainingIgnoreCase(
            String nombre
    );

    // =====================================================
    // TOTAL PROYECTOS NO ELIMINADOS
    // =====================================================

    Long countByEliminadoFalse();

    // =====================================================
    // PROYECTOS ACTIVOS
    // =====================================================

    Long countByEstadoTrueAndEliminadoFalse();

    List<Proyecto> findTop5ByOrderByCreatedAtDesc();

    Long countByEstadoProyectoIdAndEliminadoFalse(long l);
}