package Back_Goblink_park.demo.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponse {

    // =====================================================
    // ID & IDEMPOTENCIA
    // =====================================================
    private Long id;
    private String clientUuid;

    @com.fasterxml.jackson.annotation.JsonIgnore
    private boolean idempotentExistente;

    // =====================================================
    // USUARIO
    // =====================================================
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioCorreo;
    private String usuarioUsername;
    private String cargoRol;
    private String usuarioCargoRol;

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
    private String sector;

    // =====================================================
    // DATOS ESPECÍFICOS POR CATEGORÍA
    // =====================================================
    private Map<String, Object> datosEspecificos;

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
    // ESTADO GENERAL Y PROYECTO VINCULADO
    // =====================================================
    private Boolean publico;
    private Boolean eliminado;
    private String estado;
    private Boolean activo;
    private Boolean esProyecto;
    private Boolean convertidoEnProyecto;
    private Long proyectoId;
    private String proyectoNombre;

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