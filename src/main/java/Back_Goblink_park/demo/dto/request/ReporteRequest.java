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
public class ReporteRequest {

    // DATOS BÁSICOS
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    @NotNull(message = "La prioridad es obligatoria")
    private Long prioridadId;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private LocalDateTime fechaEvento;

    // GEOLOCALIZACIÓN
    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    private String direccionReferencia;

    // METADATOS
    private String fuente;
    private Boolean publico;
    private Boolean estado;

    // ✅ EVIDENCIAS MÚLTIPLES CON BASE64
    private List<EvidenciaRequest> evidencias;

    // =====================================================
    // INNER CLASS: EvidenciaRequest
    // =====================================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvidenciaRequest {

        // ✅ Para multipart: recibe el archivo binario
        private MultipartFile archivo;

        @NotBlank(message = "El tipo de archivo es obligatorio")
        private String tipoArchivo;

        private String nombreArchivo;
        private Long tamanoArchivo;
        private String mimeType;
        private String descripcion;
        private Boolean esPrincipal;

        // Helper para convertir a bytes
        public byte[] getArchivoAsBytes() {
            if (archivo == null || archivo.isEmpty()) return null;
            try {
                return archivo.getBytes();
            } catch (Exception e) {
                throw new RuntimeException("Error procesando archivo", e);
            }
        }
    }
}