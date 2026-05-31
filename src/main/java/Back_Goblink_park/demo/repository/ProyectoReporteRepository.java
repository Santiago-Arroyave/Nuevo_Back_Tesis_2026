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
    // LISTAR REPORTES POR PROYECTO
    // =====================================================

    List<ProyectoReporte> findByProyectoId(
            Long proyectoId
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