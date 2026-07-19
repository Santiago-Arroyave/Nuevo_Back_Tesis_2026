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
public class ActividadEvidenciaResponse {
    private Long id;
    private String tipoArchivo;
    private String archivoBase64;  // ✅ Para devolver en la respuesta
    private String urlArchivo;      // ✅ Por si usas URLs externas
    private String nombreArchivo;
    private Long tamanoArchivo;
    private String mimeType;
    private String descripcion;
    private Boolean esPrincipal;

    private LocalDateTime fechaCarga;
}