package Back_Goblink_park.demo.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponse {

    // =====================================================
    // ID
    // =====================================================
    private Long id;

    // =====================================================
    // USUARIO
    // =====================================================
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioCorreo;
    private String usuarioUsername;

    // =====================================================
    // CATEGORÍA
    // =====================================================
    private Long categoriaId;
    private String categoriaNombre;
    private String colorCategoria;  // ✅ AGREGADO

    // =====================================================
    // PRIORIDAD
    // =====================================================
    private Long prioridadId;
    private String prioridadNombre;
    private String colorPrioridad;  // ✅ YA EXISTÍA (bien ubicado)

    // =====================================================
    // ESTADO REPORTE
    // =====================================================
    private Long estadoReporteId;
    private String estadoReporteNombre;
    private String colorEstadoReporte;  // ✅ YA EXISTÍA (bien ubicado)

    // =====================================================
    // INFORMACIÓN
    // =====================================================
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaEvento;
    private LocalDateTime fechaReporte;
    private String fuente;

    // =====================================================
    // UBICACIÓN
    // =====================================================
    private Double latitud;
    private Double longitud;
    private String direccionReferencia;

    // =====================================================
    // FOTO PRINCIPAL
    // =====================================================
    private String fotoPrincipalUrl;

    // =====================================================
    // VALIDACIÓN
    // =====================================================
    private Long validadoPorId;
    private String validadoPorNombre;
    private LocalDateTime fechaValidacion;
    private String observacionesAdmin;

    // =====================================================
    // ESTADO GENERAL
    // =====================================================
    private Boolean publico;
    private Boolean eliminado;
    private Boolean estado;

    // =====================================================
    // AUDITORÍA
    // =====================================================
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // =====================================================
    // RELACIONES
    // =====================================================
    private List<EvidenciaReporteResponse> evidencias;
    private List<ComentarioReporteResponse> comentarios;
}