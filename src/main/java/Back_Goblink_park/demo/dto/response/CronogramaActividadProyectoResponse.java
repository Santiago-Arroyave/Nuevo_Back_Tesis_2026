package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List; // ✅ Agregar este import

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaActividadProyectoResponse {

    // =====================================================
    // ID
    // =====================================================

    private Long id;

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    private String proyectoNombre;

    // =====================================================
    // MIEMBRO DEL PROYECTO (RESPONSABLE)
    // =====================================================

    private Long proyectoMiembroId;

    private String proyectoMiembroRol;

    // =====================================================
    // USUARIO RESPONSABLE
    // =====================================================

    private Long usuarioResponsableId;

    private String usuarioResponsableNombre;

    private String usuarioResponsableEmail;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    private String nombre;

    private String descripcion;

    // =====================================================
    // ESTADO
    // =====================================================

    private String estado;

    // =====================================================
    // PRIORIDAD
    // =====================================================

    private String prioridad;

    // =====================================================
    // AVANCE
    // =====================================================

    private BigDecimal porcentajeAvance;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    // =====================================================
    // EVIDENCIA - URL
    // =====================================================

    private String urlEvidencia;

    // =====================================================
    // EVIDENCIA - IMAGEN BASE64 (NUEVOS CAMPOS)
    // =====================================================

    private String imagenBase64;

    private String tipoImagen;

    // =====================================================
    // OBSERVACIONES
    // =====================================================

    private String observaciones;

    private List<ActividadEvidenciaResponse> evidencias; // ✅ Este campo ahora funcionará

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}