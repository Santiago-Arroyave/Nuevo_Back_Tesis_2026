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

    // SECTOR DEL PÁRAMO
    private String sector;

    // DATOS ESPECÍFICOS POR CATEGORÍA (Flora, Fauna, Suelo, Agua, Aire)
    @com.fasterxml.jackson.annotation.JsonAlias({"datos_especificos", "datosEspecificos"})
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = DatosEspecificosDeserializer.class)
    private java.util.Map<String, Object> datosEspecificos;

    // METADATOS & IDEMPOTENCIA OFFLINE
    @com.fasterxml.jackson.annotation.JsonAlias({"client_uuid", "clientUuid", "idempotencyKey"})
    private String clientUuid;

    private String fuente;
    private Boolean publico;
    private Boolean estado;

    // FOTO PRINCIPAL DIRECTA (URL opcional si ya fue subida)
    private String fotoPrincipalUrl;

    // EVIDENCIAS
    private List<EvidenciaRequest> evidencias;

    // =====================================================
    // INNER CLASS: EvidenciaRequest
    // =====================================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvidenciaRequest {

        // Para multipart: recibe el archivo binario
        private MultipartFile archivo;

        // URL directa si ya fue subida a Supabase Storage
        private String urlArchivo;

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

    // =====================================================
    // HELPER PARA CONVERTIR STRING JSON A MAP
    // =====================================================
    public void setDatosEspecificosFromString(String jsonStr) {
        if (jsonStr == null || jsonStr.isBlank()) {
            this.datosEspecificos = null;
            return;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            this.datosEspecificos = mapper.readValue(jsonStr, new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, Object>>() {});
        } catch (Exception e) {
            this.datosEspecificos = null;
        }
    }

    // =====================================================
    // DESERIALIZADOR FLEXIBLE (Soporta JSON Object o JSON String)
    // =====================================================
    public static class DatosEspecificosDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<java.util.Map<String, Object>> {
        @Override
        public java.util.Map<String, Object> deserialize(
                com.fasterxml.jackson.core.JsonParser p,
                com.fasterxml.jackson.databind.DeserializationContext ctxt
        ) throws java.io.IOException {
            if (p.currentToken() == com.fasterxml.jackson.core.JsonToken.VALUE_STRING) {
                String text = p.getText();
                if (text == null || text.isBlank()) return null;
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                return mapper.readValue(text, new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, Object>>() {});
            }
            return p.getCodec().readValue(p, new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, Object>>() {});
        }
    }
}