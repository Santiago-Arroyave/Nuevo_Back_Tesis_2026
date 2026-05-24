package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.EvidenciaReporte;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenciaReporteRepository
        extends JpaRepository<EvidenciaReporte, Long> {

    // =====================================================
    // LISTAR POR REPORTE
    // =====================================================

    List<EvidenciaReporte> findByReporteId(Long reporteId);

    // =====================================================
    // OBTENER EVIDENCIA PRINCIPAL
    // =====================================================

    List<EvidenciaReporte> findByReporteIdAndEsPrincipalTrue(
            Long reporteId
    );
}