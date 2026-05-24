package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ComentarioReporte;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioReporteRepository
        extends JpaRepository<ComentarioReporte, Long> {

    // =====================================================
    // LISTAR POR REPORTE
    // =====================================================

    List<ComentarioReporte> findByReporteIdOrderByFechaComentarioAsc(
            Long reporteId
    );

    // =====================================================
    // LISTAR POR USUARIO
    // =====================================================

    List<ComentarioReporte> findByUsuarioId(
            Long usuarioId
    );
}