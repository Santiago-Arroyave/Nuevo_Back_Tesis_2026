package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenciaReporteRequest {

    // =====================================================
    // RELACIÓN CON REPORTE
    // =====================================================
    @NotNull(message = "El ID del reporte es obligatorio")
    private Long reporteId;

    // =====================================================
    // TIPO DE ARCHIVO
    // =====================================================
    @NotBlank(message = "El tipo de archivo es obligatorio")
    private String tipoArchivo;  // "imagen", "video", "audio", "documento"

    // =====================================================
    // ✅ ARCHIVO BINARIO (Multipart para upload)
    // =====================================================
    private MultipartFile archivo;  // ← Recibe el archivo directamente

    // =====================================================
    // METADATOS
    // =====================================================
    private String nombreArchivo;

    private Long tamanoArchivo;  // Tamaño en bytes del archivo original

    private String mimeType;  // "image/jpeg", "video/mp4", etc.

    private String descripcion;

    private Boolean esPrincipal;  // true = esta es la foto principal del reporte

    // =====================================================
    // MÉTODO HELPER: Convertir MultipartFile a byte[]
    // =====================================================
    public byte[] getArchivoAsBytes() {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        try {
            return archivo.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir archivo a bytes: " + e.getMessage(), e);
        }
    }
}