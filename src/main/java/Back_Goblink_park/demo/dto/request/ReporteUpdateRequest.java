package Back_Goblink_park.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteUpdateRequest {

    // =====================================================
    // CAMPOS ACTUALIZABLES DEL REPORTE
    // =====================================================
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaEvento;
    private Double latitud;
    private Double longitud;
    private String direccionReferencia;
    private Long categoriaId;
    private Long prioridadId;
    private Boolean publico;
    private Boolean estado;

    // =====================================================
    // ✅ EVIDENCIAS: Lista para gestionar (CRUD)
    // =====================================================
    private List<EvidenciaUpdateRequest> evidencias;

    // =====================================================
    // INNER CLASS: EvidenciaUpdateRequest
    // =====================================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvidenciaUpdateRequest {

        // ID de la evidencia existente (null = crear nueva)
        private Long id;

        // ✅ Para NUEVAS evidencias: recibe archivo como MultipartFile
        private MultipartFile archivo;

        // ✅ Para ACTUALIZAR evidencias existentes: recibe Base64 (frontend ya tiene la imagen)
        private String archivoBase64;

        @NotBlank(message = "El tipo de archivo es obligatorio")
        private String tipoArchivo;  // "imagen", "video", "audio", "documento"

        private String nombreArchivo;
        private Long tamanoArchivo;
        private String mimeType;
        private String descripcion;
        private Boolean esPrincipal;

        // ✅ Marcar para eliminar (soft delete)
        private Boolean eliminar;

        // =====================================================
        // MÉTODO HELPER: Obtener bytes del archivo (prioriza Base64 para updates)
        // =====================================================
        public byte[] getArchivoAsBytes() {
            // Prioridad 1: Si viene Base64 (común en updates desde frontend)
            if (archivoBase64 != null && !archivoBase64.isEmpty()) {
                try {
                    return java.util.Base64.getDecoder().decode(archivoBase64);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Base64 inválido: " + e.getMessage(), e);
                }
            }
            // Prioridad 2: Si viene MultipartFile (común en creaciones)
            if (archivo != null && !archivo.isEmpty()) {
                try {
                    return archivo.getBytes();
                } catch (Exception e) {
                    throw new RuntimeException("Error al leer archivo: " + e.getMessage(), e);
                }
            }
            return null;
        }
    }
}