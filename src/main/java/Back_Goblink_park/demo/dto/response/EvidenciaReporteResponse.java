package Back_Goblink_park.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EvidenciaReporteResponse {

    // =====================================================
    // ID
    // =====================================================

    private Long id;

    // =====================================================
    // REPORTE
    // =====================================================

    private Long reporteId;

    // =====================================================
    // DATOS ARCHIVO
    // =====================================================

    private String tipoArchivo;

    private String urlArchivo;

    private String nombreArchivo;

    private Long tamanoArchivo;

    private String descripcion;

    private Boolean esPrincipal;

    // =====================================================
    // NUEVOS CAMPOS
    // =====================================================

    private String mimeType;

    private String storageProvider;

    private String thumbnailUrl;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDateTime fechaCarga;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}