package Back_Goblink_park.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenciaReporteResponse {

    // =====================================================
    // ID
    // =====================================================
    private Long id;

    // =====================================================
    // RELACIÓN CON REPORTE ✅ AGREGADO
    // =====================================================
    private Long reporteId;

    // =====================================================
    // INFORMACIÓN DEL ARCHIVO
    // =====================================================
    private String tipoArchivo;  // "imagen", "video", "audio", "documento"

    // ✅ CAMPO BASE64 (única fuente de verdad)
    private String archivoBase64;

    // ⚠️ CAMPO LEGACY para compatibilidad (siempre null)
    @Deprecated
    private String urlArchivo;

    private String nombreArchivo;
    private Long tamanoArchivo;
    private String mimeType;  // "image/jpeg", "image/png", etc.
    private String descripcion;

    // =====================================================
    // MARCADORES
    // =====================================================
    private Boolean esPrincipal;

    // =====================================================
    // FECHAS
    // =====================================================
    private LocalDateTime fechaCarga;
}