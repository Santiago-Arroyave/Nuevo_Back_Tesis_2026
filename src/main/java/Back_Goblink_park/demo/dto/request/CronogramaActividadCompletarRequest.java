package Back_Goblink_park.demo.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaActividadCompletarRequest {

    // =====================================================
    // DESCRIPCIÓN DE LO QUE SE HIZO
    // =====================================================

    private String descripcionEvidencia;

    // =====================================================
    // URL DE LA IMAGEN (si se sube a servidor externo)
    // =====================================================

    private String urlEvidencia;

    // =====================================================
    // IMAGEN EN BASE64 (NUEVO CAMPO)
    // =====================================================

    private String imagenBase64;

    // =====================================================
    // TIPO DE IMAGEN (image/jpeg, image/png, etc.)
    // =====================================================

    private String tipoImagen;

    // =====================================================
    // OBSERVACIONES ADICIONALES
    // =====================================================

    private String observaciones;
}