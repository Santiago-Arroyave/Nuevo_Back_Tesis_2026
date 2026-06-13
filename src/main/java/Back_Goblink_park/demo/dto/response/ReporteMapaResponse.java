package Back_Goblink_park.demo.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteMapaResponse {

    // =====================================================
    // ID Y TÍTULO
    // =====================================================
    private Long id;
    private String titulo;
    private String descripcion;

    // =====================================================
    // FECHAS
    // =====================================================
    private LocalDateTime fechaReporte;

    // =====================================================
    // UBICACIÓN
    // =====================================================
    private Double latitud;
    private Double longitud;
    private String direccionReferencia;

    // =====================================================
    // CATEGORÍA
    // =====================================================
    private Long categoriaId;
    private String categoriaNombre;
    private String colorCategoria;

    // =====================================================
    // PRIORIDAD
    // =====================================================
    private Long prioridadId;
    private String prioridadNombre;
    private String colorPrioridad;

    // =====================================================
    // ESTADO REPORTE
    // =====================================================
    private Long estadoReporteId;
    private String estadoReporteNombre;
    private String colorEstadoReporte;
    // =====================================================
    // FOTO Y USUARIO
    // =====================================================
    private String fotoPrincipalUrl;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioFotoPerfil;
    private Boolean tieneImagen;
}