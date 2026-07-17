package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.dto.response.EvidenciaReporteResponse;
import Back_Goblink_park.demo.entity.EvidenciaReporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvidenciaReporteRepository
        extends JpaRepository<EvidenciaReporte, Long> {

    @Query("SELECT new Back_Goblink_park.demo.dto.response.EvidenciaReporteResponse(" +
            "e.id, r.id, e.tipoArchivo, null, null, e.nombreArchivo, e.tamanoArchivo, " +
            "e.mimeType, e.descripcion, e.esPrincipal, e.fechaCarga) " +
            "FROM EvidenciaReporte e JOIN e.reporte r WHERE r.id = :reporteId")
    List<EvidenciaReporteResponse> findResponsesByReporteId(@Param("reporteId") Long reporteId);

    List<EvidenciaReporte> findByReporteId(Long reporteId);
    List<EvidenciaReporte> findByReporteIdAndEsPrincipalTrue(Long reporteId);

}