package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EvidenciaReporteRequest {

    // =====================================================
    // RELACIÓN REPORTE
    // =====================================================

    @NotNull(message = "El reporte es obligatorio")
    private Long reporteId;

    // =====================================================
    // DATOS ARCHIVO
    // =====================================================

    @NotBlank(message = "El tipo de archivo es obligatorio")
    private String tipoArchivo;

    @NotBlank(message = "La URL del archivo es obligatoria")
    private String urlArchivo;

    @NotBlank(message = "El nombre del archivo es obligatorio")
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
}