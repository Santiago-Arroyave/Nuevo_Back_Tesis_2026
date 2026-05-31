package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.*;
import Back_Goblink_park.demo.entity.*;
import java.util.List;

public class ReporteMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================
    public static ReporteResponse toResponse(Reporte reporte) {
        return ReporteResponse.builder()

                // =================================================
                // ID
                // =================================================
                .id(reporte.getId())

                // =================================================
                // USUARIO
                // =================================================
                .usuarioId(reporte.getUsuario().getId())
                .usuarioNombre(reporte.getUsuario().getNombres())
                .usuarioCorreo(reporte.getUsuario().getCorreo())
                .usuarioUsername(reporte.getUsuario().getUsername())

                // =================================================
                // CATEGORÍA
                // =================================================
                .categoriaId(reporte.getCategoria().getId())
                .categoriaNombre(reporte.getCategoria().getNombre())
                .colorCategoria(reporte.getCategoria().getColor())  // ✅ AGREGADO

                // =================================================
                // PRIORIDAD
                // =================================================
                .prioridadId(reporte.getPrioridad().getId())
                .prioridadNombre(reporte.getPrioridad().getNombre())
                .colorPrioridad(reporte.getPrioridad().getColor())  // ✅ YA EXISTÍA

                // =================================================
                // ESTADO REPORTE
                // =================================================
                .estadoReporteId(reporte.getEstadoReporte().getId())
                .estadoReporteNombre(reporte.getEstadoReporte().getNombre())
                .colorEstadoReporte(reporte.getEstadoReporte().getColor())  // ✅ YA EXISTÍA

                // =================================================
                // INFORMACIÓN
                // =================================================
                .titulo(reporte.getTitulo())
                .descripcion(reporte.getDescripcion())
                .fechaEvento(reporte.getFechaEvento())
                .fechaReporte(reporte.getFechaReporte())
                .fuente(reporte.getFuente())

                // =================================================
                // GEOLOCALIZACIÓN
                // =================================================
                .latitud(reporte.getLatitud())
                .longitud(reporte.getLongitud())
                .direccionReferencia(reporte.getDireccionReferencia())

                // =================================================
                // EVIDENCIA
                // =================================================
                .fotoPrincipalUrl(reporte.getFotoPrincipalUrl())

                // =================================================
                // VALIDACIÓN
                // =================================================
                .validadoPorId(reporte.getValidadoPor() != null ? reporte.getValidadoPor().getId() : null)
                .validadoPorNombre(reporte.getValidadoPor() != null ? reporte.getValidadoPor().getNombres() : null)
                .fechaValidacion(reporte.getFechaValidacion())
                .observacionesAdmin(reporte.getObservacionesAdmin())

                // =================================================
                // ESTADO GENERAL
                // =================================================
                .publico(reporte.getPublico())
                .eliminado(reporte.getEliminado())
                .estado(reporte.getEstado())

                // =================================================
                // AUDITORÍA
                // =================================================
                .createdAt(reporte.getCreatedAt())
                .updatedAt(reporte.getUpdatedAt())

                // =================================================
                // RELACIONES
                // =================================================
                .evidencias(mapEvidencias(reporte.getEvidencias()))
                .comentarios(mapComentarios(reporte.getComentarios()))

                .build();
    }

    // =====================================================
    // MAP EVIDENCIAS
    // =====================================================
    private static List<EvidenciaReporteResponse> mapEvidencias(List<EvidenciaReporte> evidencias) {
        if (evidencias == null) return List.of();

        return evidencias.stream()
                .map(evidencia -> EvidenciaReporteResponse.builder()
                        .id(evidencia.getId())
                        .tipoArchivo(evidencia.getTipoArchivo())
                        .urlArchivo(evidencia.getUrlArchivo())
                        .nombreArchivo(evidencia.getNombreArchivo())
                        .tamanoArchivo(evidencia.getTamanoArchivo())
                        .mimeType(evidencia.getMimeType())
                        .storageProvider(evidencia.getStorageProvider())
                        .thumbnailUrl(evidencia.getThumbnailUrl())
                        .descripcion(evidencia.getDescripcion())
                        .esPrincipal(evidencia.getEsPrincipal())
                        .build())
                .toList();
    }

    // =====================================================
    // MAP COMENTARIOS
    // =====================================================
    private static List<ComentarioReporteResponse> mapComentarios(List<ComentarioReporte> comentarios) {
        if (comentarios == null) return List.of();

        return comentarios.stream()
                .map(comentario -> ComentarioReporteResponse.builder()
                        .id(comentario.getId())
                        .comentario(comentario.getComentario())
                        .tipoComentario(comentario.getTipoComentario())
                        .editado(comentario.getEditado())
                        .fechaComentario(comentario.getFechaComentario())
                        .usuarioId(comentario.getUsuario().getId())
                        .usuarioNombre(comentario.getUsuario().getNombres())
                        .build())
                .toList();
    }

    // =====================================================
    // REPORTE -> MAPA RESPONSE
    // =====================================================
    public static ReporteMapaResponse toMapaResponse(Reporte reporte) {
        return ReporteMapaResponse.builder()
                .id(reporte.getId())
                .titulo(reporte.getTitulo())
                .descripcion(reporte.getDescripcion())
                .fechaReporte(reporte.getFechaReporte())
                .latitud(reporte.getLatitud())
                .longitud(reporte.getLongitud())
                .direccionReferencia(reporte.getDireccionReferencia())

                .categoriaId(reporte.getCategoria().getId())
                .categoriaNombre(reporte.getCategoria().getNombre())
                .colorCategoria(reporte.getCategoria().getColor())  // ✅ AGREGADO

                .prioridadId(reporte.getPrioridad().getId())
                .prioridadNombre(reporte.getPrioridad().getNombre())
                .colorPrioridad(reporte.getPrioridad().getColor())  // ✅ AGREGADO

                .estadoReporteId(reporte.getEstadoReporte().getId())
                .estadoReporteNombre(reporte.getEstadoReporte().getNombre())
                .colorEstadoReporte(reporte.getEstadoReporte().getColor())  // ✅ YA EXISTÍA

                .fotoPrincipalUrl(reporte.getFotoPrincipalUrl())
                .usuarioId(reporte.getUsuario().getId())
                .usuarioNombre(reporte.getUsuario().getNombres())
                .usuarioFotoPerfil(reporte.getUsuario().getFotoPerfil())
                .build();
    }
}