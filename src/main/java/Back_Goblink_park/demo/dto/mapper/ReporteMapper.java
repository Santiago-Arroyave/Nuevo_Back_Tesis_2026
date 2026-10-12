package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.*;
import Back_Goblink_park.demo.entity.*;

import java.util.Collections;
import java.util.List;

public class ReporteMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================
    public static ReporteResponse toResponse(Reporte reporte) {
        if (reporte == null) {
            return null;
        }

        String fotoPrincipalUrl = calcularFotoPrincipalUrl(reporte);

        return ReporteResponse.builder()
                // ID
                .id(reporte.getId())

                // USUARIO
                .usuarioId(reporte.getUsuario() != null ? reporte.getUsuario().getId() : null)
                .usuarioNombre(reporte.getUsuario() != null ? reporte.getUsuario().getNombres() : null)
                .usuarioCorreo(reporte.getUsuario() != null ? reporte.getUsuario().getCorreo() : null)
                .usuarioUsername(reporte.getUsuario() != null ? reporte.getUsuario().getUsername() : null)
                .cargoRol(reporte.getUsuario() != null ? reporte.getUsuario().getCargoRol() : null)
                .usuarioCargoRol(reporte.getUsuario() != null ? reporte.getUsuario().getCargoRol() : null)

                // CATEGORÍA
                .categoriaId(reporte.getCategoria() != null ? reporte.getCategoria().getId() : null)
                .categoriaNombre(reporte.getCategoria() != null ? reporte.getCategoria().getNombre() : null)
                .colorCategoria(reporte.getCategoria() != null ? reporte.getCategoria().getColor() : null)

                // PRIORIDAD
                .prioridadId(reporte.getPrioridad() != null ? reporte.getPrioridad().getId() : null)
                .prioridadNombre(reporte.getPrioridad() != null ? reporte.getPrioridad().getNombre() : null)
                .colorPrioridad(reporte.getPrioridad() != null ? reporte.getPrioridad().getColor() : null)

                // ESTADO REPORTE
                .estadoReporteId(reporte.getEstadoReporte() != null ? reporte.getEstadoReporte().getId() : null)
                .estadoReporteNombre(reporte.getEstadoReporte() != null ? reporte.getEstadoReporte().getNombre() : null)
                .colorEstadoReporte(reporte.getEstadoReporte() != null ? reporte.getEstadoReporte().getColor() : null)

                // INFORMACIÓN
                .titulo(reporte.getTitulo())
                .descripcion(reporte.getDescripcion())
                .fechaEvento(reporte.getFechaEvento())
                .fechaReporte(reporte.getFechaReporte())
                .fuente(reporte.getFuente())

                // GEOLOCALIZACIÓN
                .latitud(reporte.getLatitud())
                .longitud(reporte.getLongitud())
                .direccionReferencia(reporte.getDireccionReferencia())
                .sector(reporte.getSector())

                // DATOS ESPECÍFICOS POR CATEGORÍA
                .datosEspecificos(reporte.getDatosEspecificos())

                // EVIDENCIA: URL pública de Supabase Storage
                .fotoPrincipalUrl(fotoPrincipalUrl)

                // VALIDACIÓN
                .validadoPorId(reporte.getValidadoPor() != null ? reporte.getValidadoPor().getId() : null)
                .validadoPorNombre(reporte.getValidadoPor() != null ? reporte.getValidadoPor().getNombres() : null)
                .fechaValidacion(reporte.getFechaValidacion())
                .observacionesAdmin(reporte.getObservacionesAdmin())

                // ESTADO GENERAL
                .publico(reporte.getPublico())
                .eliminado(reporte.getEliminado())
                .estado(reporte.getEstado())

                // AUDITORÍA
                .createdAt(reporte.getCreatedAt())
                .updatedAt(reporte.getUpdatedAt())

                // RELACIONES
                .evidencias(mapEvidencias(reporte.getEvidencias()))
                .comentarios(mapComentarios(reporte.getComentarios()))
                .build();
    }

    // =====================================================
    // MÉTODO AUXILIAR: Calcular foto principal URL
    // =====================================================
    private static String calcularFotoPrincipalUrl(Reporte reporte) {
        if (reporte == null) {
            return null;
        }

        if (reporte.getFotoPrincipalUrl() != null && !reporte.getFotoPrincipalUrl().isBlank()) {
            return reporte.getFotoPrincipalUrl();
        }

        if (reporte.getEvidencias() != null && !reporte.getEvidencias().isEmpty()) {
            return reporte.getEvidencias().stream()
                    .filter(e -> Boolean.TRUE.equals(e.getEsPrincipal()) && e.getUrlArchivo() != null && !e.getUrlArchivo().isBlank())
                    .findFirst()
                    .map(EvidenciaReporte::getUrlArchivo)
                    .orElseGet(() -> reporte.getEvidencias().stream()
                            .filter(e -> e.getUrlArchivo() != null && !e.getUrlArchivo().isBlank())
                            .findFirst()
                            .map(EvidenciaReporte::getUrlArchivo)
                            .orElse(null));
        }

        return null;
    }

    // =====================================================
    // MAP EVIDENCIAS
    // =====================================================
    private static List<EvidenciaReporteResponse> mapEvidencias(List<EvidenciaReporte> evidencias) {
        if (evidencias == null) return Collections.emptyList();

        return evidencias.stream()
                .map(evidencia -> EvidenciaReporteResponse.builder()
                        .id(evidencia.getId())
                        .reporteId(evidencia.getReporte() != null ? evidencia.getReporte().getId() : null)
                        .tipoArchivo(evidencia.getTipoArchivo())
                        .urlArchivo(evidencia.getUrlArchivo())
                        .archivoBase64(null)
                        .nombreArchivo(evidencia.getNombreArchivo())
                        .tamanoArchivo(evidencia.getTamanoArchivo())
                        .mimeType(evidencia.getMimeType())
                        .descripcion(evidencia.getDescripcion())
                        .esPrincipal(evidencia.getEsPrincipal())
                        .fechaCarga(evidencia.getFechaCarga())
                        .build())
                .toList();
    }

    // =====================================================
    // MAP COMENTARIOS
    // =====================================================
    private static List<ComentarioReporteResponse> mapComentarios(List<ComentarioReporte> comentarios) {
        if (comentarios == null) return Collections.emptyList();

        return comentarios.stream()
                .map(comentario -> ComentarioReporteResponse.builder()
                        .id(comentario.getId())
                        .comentario(comentario.getComentario())
                        .tipoComentario(comentario.getTipoComentario())
                        .editado(comentario.getEditado())
                        .fechaComentario(comentario.getFechaComentario())
                        .usuarioId(comentario.getUsuario() != null ? comentario.getUsuario().getId() : null)
                        .usuarioNombre(comentario.getUsuario() != null ? comentario.getUsuario().getNombres() : null)
                        .build())
                .toList();
    }

    // =====================================================
    // REPORTE -> MAPA RESPONSE (Para el mapa)
    // =====================================================
    public static ReporteMapaResponse toMapaResponse(Reporte reporte) {
        if (reporte == null) {
            return null;
        }

        String fotoPrincipalUrl = calcularFotoPrincipalUrl(reporte);
        String usuarioFotoPerfil = (reporte.getUsuario() != null) ? reporte.getUsuario().getFotoPerfil() : null;

        boolean tieneImagen = (fotoPrincipalUrl != null && !fotoPrincipalUrl.isBlank()) ||
                (reporte.getEvidencias() != null && reporte.getEvidencias().stream()
                        .anyMatch(e -> e.getUrlArchivo() != null && !e.getUrlArchivo().isBlank()));

        return ReporteMapaResponse.builder()
                .id(reporte.getId())
                .titulo(reporte.getTitulo())
                .descripcion(reporte.getDescripcion())
                .fechaReporte(reporte.getFechaReporte())
                .latitud(reporte.getLatitud())
                .longitud(reporte.getLongitud())
                .direccionReferencia(reporte.getDireccionReferencia())
                .sector(reporte.getSector())
                .datosEspecificos(reporte.getDatosEspecificos())

                .categoriaId(reporte.getCategoria() != null ? reporte.getCategoria().getId() : null)
                .categoriaNombre(reporte.getCategoria() != null ? reporte.getCategoria().getNombre() : null)
                .colorCategoria(reporte.getCategoria() != null ? reporte.getCategoria().getColor() : null)

                .prioridadId(reporte.getPrioridad() != null ? reporte.getPrioridad().getId() : null)
                .prioridadNombre(reporte.getPrioridad() != null ? reporte.getPrioridad().getNombre() : null)
                .colorPrioridad(reporte.getPrioridad() != null ? reporte.getPrioridad().getColor() : null)

                .estadoReporteId(reporte.getEstadoReporte() != null ? reporte.getEstadoReporte().getId() : null)
                .estadoReporteNombre(reporte.getEstadoReporte() != null ? reporte.getEstadoReporte().getNombre() : null)
                .colorEstadoReporte(reporte.getEstadoReporte() != null ? reporte.getEstadoReporte().getColor() : null)

                .tieneImagen(tieneImagen)
                .fotoPrincipalUrl(fotoPrincipalUrl)

                .usuarioId(reporte.getUsuario() != null ? reporte.getUsuario().getId() : null)
                .usuarioNombre(reporte.getUsuario() != null ? reporte.getUsuario().getNombres() : null)
                .usuarioFotoPerfil(usuarioFotoPerfil)
                .cargoRol(reporte.getUsuario() != null ? reporte.getUsuario().getCargoRol() : null)
                .usuarioCargoRol(reporte.getUsuario() != null ? reporte.getUsuario().getCargoRol() : null)
                .build();
    }
}