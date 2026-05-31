package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Proyecto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProyectoRepository
        extends JpaRepository<Proyecto, Long>,
        JpaSpecificationExecutor<Proyecto> {

    // =====================================================
    // ACTIVOS
    // =====================================================

    List<Proyecto> findByEstadoTrue();

    // =====================================================
    // NO ELIMINADOS
    // =====================================================

    List<Proyecto> findByEliminadoFalse();

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
    // POR USUARIO CREADOR
    // =====================================================

    List<Proyecto> findByCreadoPorId(
            Long usuarioId
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