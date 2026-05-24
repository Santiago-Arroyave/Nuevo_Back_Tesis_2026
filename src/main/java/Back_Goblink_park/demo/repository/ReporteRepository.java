package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Reporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository
        extends JpaRepository<Reporte, Long> {

    // =====================================================
    // LISTAR SOLO ACTIVOS
    // =====================================================

    List<Reporte> findByEliminadoFalse();

    // =====================================================
    // FILTRAR POR CATEGORÍA
    // =====================================================

    List<Reporte> findByCategoriaIdAndEliminadoFalse(
            Long categoriaId
    );

    // =====================================================
    // FILTRAR POR PRIORIDAD
    // =====================================================

    List<Reporte> findByPrioridadIdAndEliminadoFalse(
            Long prioridadId
    );

    // =====================================================
    // FILTRAR POR ESTADO
    // =====================================================

    List<Reporte> findByEstadoReporteIdAndEliminadoFalse(
            Long estadoReporteId
    );

    // =====================================================
    // FILTRAR POR USUARIO
    // =====================================================

    List<Reporte> findByUsuarioIdAndEliminadoFalse(
            Long usuarioId
    );

    // =====================================================
    // BUSCAR POR TÍTULO
    // =====================================================

    List<Reporte> findByTituloContainingIgnoreCaseAndEliminadoFalse(
            String titulo
    );
}