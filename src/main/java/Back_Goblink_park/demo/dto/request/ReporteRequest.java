package Back_Goblink_park.demo.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteRequest {

    // =====================================================
    // RELACIONES
    // =====================================================

    private Long categoriaId;

    private Long prioridadId;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    private String titulo;

    private String descripcion;

    private LocalDateTime fechaEvento;

    // =====================================================
    // GEOLOCALIZACIÓN
    // =====================================================

    private Double latitud;

    private Double longitud;

    private String direccionReferencia;

    // =====================================================
    // EVIDENCIA PRINCIPAL
    // =====================================================

    private String fotoPrincipalUrl;

    // =====================================================
    // FUENTE
    // =====================================================

    private String fuente;
}