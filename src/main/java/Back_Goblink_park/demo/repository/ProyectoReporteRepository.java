package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ProyectoReporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProyectoReporteRepository
        extends JpaRepository<ProyectoReporte, Long> {

    // =====================================================
    // LISTAR REPORTES POR PROYECTO (OPTIMIZADO CON JOIN FETCH)
    // =====================================================

    @Query("SELECT pr FROM ProyectoReporte pr " +
           "JOIN FETCH pr.proyecto p " +
           "JOIN FETCH pr.reporte r " +
           "LEFT JOIN FETCH r.categoria " +
           "LEFT JOIN FETCH r.estadoReporte " +
           "LEFT JOIN FETCH r.prioridad " +
           "LEFT JOIN FETCH r.usuario " +
           "WHERE pr.proyecto.id = :proyectoId")
    List<ProyectoReporte> findByProyectoId(
            @Param("proyectoId") Long proyectoId
    );

    // =====================================================
    // LISTAR PROYECTOS POR REPORTE
    // =====================================================

    List<ProyectoReporte> findByReporteId(
            Long reporteId
    );

    // =====================================================
    // VALIDAR DUPLICADO
    // =====================================================

    Optional<ProyectoReporte>
    findByProyectoIdAndReporteId(
            Long proyectoId,
            Long reporteId
    );

    // =====================================================
    // EXISTE RELACIÓN
    // =====================================================

    boolean existsByProyectoIdAndReporteId(
            Long proyectoId,
            Long reporteId
    );

    // =====================================================
// ELIMINAR RELACIONES POR PROYECTO
// =====================================================

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM ProyectoReporte pr
    WHERE pr.proyecto.id = :proyectoId
""")
    void eliminarPorProyectoId(
            @Param("proyectoId") Long proyectoId
    );
}