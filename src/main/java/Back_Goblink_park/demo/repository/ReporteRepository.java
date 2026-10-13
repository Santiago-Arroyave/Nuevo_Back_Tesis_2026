package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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


    // ✅ MÉTODO NUEVO: Cargar reporte CON evidencias y usuario (JOIN FETCH)
    @Query("SELECT r FROM Reporte r LEFT JOIN FETCH r.evidencias LEFT JOIN FETCH r.usuario WHERE r.id = :id")
    Optional<Reporte> findByIdWithEvidencias(@Param("id") Long id);

    // ✅ IDEMPOTENCIA OFFLINE: Buscar reporte por client_uuid con sus relaciones cargadas
    @Query("""
        SELECT r FROM Reporte r
        LEFT JOIN FETCH r.evidencias
        LEFT JOIN FETCH r.usuario
        LEFT JOIN FETCH r.categoria
        LEFT JOIN FETCH r.prioridad
        LEFT JOIN FETCH r.estadoReporte
        WHERE r.clientUuid = :clientUuid
    """)
    Optional<Reporte> findByClientUuidWithEvidencias(@Param("clientUuid") String clientUuid);

    // ✅ MÉTODO NUEVO: Listar reportes CON evidencias y usuario
    @Query("SELECT r FROM Reporte r LEFT JOIN FETCH r.evidencias LEFT JOIN FETCH r.usuario WHERE r.eliminado = false")
    List<Reporte> findAllWithEvidencias();

    @Query("""
        SELECT COUNT(r) FROM Reporte r
        JOIN r.estadoReporte er
        WHERE r.eliminado = false
          AND LOWER(TRIM(er.nombre)) IN ('pendiente', 'pendientes')
    """)
    Long countReportesPendientesDashboard();

    @Query("""
        SELECT COUNT(r) FROM Reporte r
        JOIN r.estadoReporte er
        WHERE r.eliminado = false
          AND LOWER(TRIM(er.nombre)) IN ('en revisión', 'en revision', 'en_revision', 'validado', 'en proceso')
    """)
    Long countReportesEnRevisionDashboard();

    @Query("""
        SELECT COUNT(r) FROM Reporte r
        JOIN r.estadoReporte er
        WHERE r.eliminado = false
          AND LOWER(TRIM(er.nombre)) IN ('resuelto', 'resuelta')
    """)
    Long countReportesResueltosDashboard();

    @Query("""
        SELECT COUNT(DISTINCT r.id) FROM Reporte r
        LEFT JOIN r.proyectosAsociados pr
        LEFT JOIN pr.proyecto p
        LEFT JOIN r.estadoReporte er
        WHERE r.eliminado = false
          AND (
              (pr IS NOT NULL AND (p IS NULL OR p.eliminado = false))
              OR (er IS NOT NULL AND LOWER(TRIM(er.nombre)) IN ('convertido en proyecto', 'convertido_en_proyecto'))
          )
    """)
    Long countReportesConvertidosEnProyecto();
}

