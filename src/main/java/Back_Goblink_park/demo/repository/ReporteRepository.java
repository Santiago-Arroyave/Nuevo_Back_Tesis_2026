package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository
        extends JpaRepository<Reporte, Long>,
        JpaSpecificationExecutor<Reporte> {

    // =====================================================
    // LISTAR SOLO ACTIVOS
    // =====================================================
    List<Reporte> findByEliminadoFalse();

    // =====================================================
    // FILTRAR POR CATEGORÍA
    // =====================================================
    List<Reporte> findByCategoriaIdAndEliminadoFalse(Long categoriaId);

    // =====================================================
    // FILTRAR POR PRIORIDAD
    // =====================================================
    List<Reporte> findByPrioridadIdAndEliminadoFalse(Long prioridadId);

    // =====================================================
    // FILTRAR POR ESTADO
    // =====================================================
    List<Reporte> findByEstadoReporteIdAndEliminadoFalse(Long estadoReporteId);

    // =====================================================
    // FILTRAR POR USUARIO
    // =====================================================
    List<Reporte> findByUsuarioIdAndEliminadoFalse(Long usuarioId);

    // =====================================================
    // BUSCAR POR TÍTULO
    // =====================================================
    List<Reporte> findByTituloContainingIgnoreCaseAndEliminadoFalse(String titulo);

    // =====================================================
    // ÚLTIMOS REPORTES (PARA ACTIVIDAD RECIENTE - DASHBOARD)
    // =====================================================
    List<Reporte> findTop5ByOrderByFechaReporteDesc();

    // =====================================================
    // CONTADORES
    // =====================================================
    Long countByEliminadoFalse();
    Long countByEstadoReporteIdAndEliminadoFalse(Long estadoReporteId);

    // =====================================================
    // REPORTES POR CATEGORÍA (GRÁFICA)
    // =====================================================
// =====================================================
// REPORTES POR CATEGORÍA (INCLUYE COLOR)
// =====================================================
    @Query("""
    SELECT c.nombre, COUNT(r), c.color
    FROM Reporte r
    JOIN r.categoria c
    WHERE r.eliminado = false
    GROUP BY c.nombre, c.color
""")
    List<Object[]> countReportesPorCategoria();

    // =====================================================
// REPORTES POR PRIORIDAD (INCLUYE COLOR)
// =====================================================
    @Query("""
    SELECT p.nombre, COUNT(r), p.color
    FROM Reporte r
    JOIN r.prioridad p
    WHERE r.eliminado = false
    GROUP BY p.nombre, p.color
""")
    List<Object[]> countReportesPorPrioridad();

    Long countByPrioridadIdAndEliminadoFalse(long l);
}