package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.*;
import Back_Goblink_park.demo.entity.*;
import java.util.Base64;
import java.util.List;
import java.util.Collections;

public class ReporteMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================
    public static ReporteResponse toResponse(Reporte reporte) {

        // 🔍 DEBUG: Verificar evidencias
        System.out.println("🔍 [MAPPER] Reporte ID: " + reporte.getId());
        System.out.println("🔍 [MAPPER] Evidencias size: " +
                (reporte.getEvidencias() != null ? reporte.getEvidencias().size() : "NULL"));

        if (reporte.getEvidencias() != null) {
            for (EvidenciaReporte e : reporte.getEvidencias()) {
                System.out.println("  📎 Evidencia #" + e.getId() +
                        " | Principal: " + e.getEsPrincipal() +
                        " | Binario length: " + (e.getArchivoBinario() != null ? e.getArchivoBinario().length : 0));  // ✅ Sin paréntesis
            }
        }

        // ✅ CALCULAR fotoPrincipalBase64 desde evidencias (única fuente de verdad)
        String fotoPrincipalBase64 = calcularFotoPrincipalBase64(reporte.getEvidencias());

        return ReporteResponse.builder()
                // ID
                .id(reporte.getId())

                // USUARIO
                .usuarioId(reporte.getUsuario().getId())
                .usuarioNombre(reporte.getUsuario().getNombres())
                .usuarioCorreo(reporte.getUsuario().getCorreo())
                .usuarioUsername(reporte.getUsuario().getUsername())

                // CATEGORÍA
                .categoriaId(reporte.getCategoria().getId())
                .categoriaNombre(reporte.getCategoria().getNombre())
                .colorCategoria(reporte.getCategoria().getColor())

                // PRIORIDAD
                .prioridadId(reporte.getPrioridad().getId())
                .prioridadNombre(reporte.getPrioridad().getNombre())
                .colorPrioridad(reporte.getPrioridad().getColor())

                // ESTADO REPORTE
                .estadoReporteId(reporte.getEstadoReporte().getId())
                .estadoReporteNombre(reporte.getEstadoReporte().getNombre())
                .colorEstadoReporte(reporte.getEstadoReporte().getColor())

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

                // ✅ EVIDENCIA: Usar Base64 calculado desde evidencias (fallback a campo legacy si existe)
                .fotoPrincipalUrl(fotoPrincipalBase64 != null ? fotoPrincipalBase64 : reporte.getFotoPrincipalUrl())

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

    // ✅ MÉTODO AUXILIAR: Calcular foto principal Base64 desde evidencias
    private static String calcularFotoPrincipalBase64(List<EvidenciaReporte> evidencias) {
        if (evidencias == null || evidencias.isEmpty()) {
            return null;
        }

        // Buscar evidencia marcada como principal y convertir a Base64
        return evidencias.stream()
                .filter(EvidenciaReporte::getEsPrincipal)
                .findFirst()
                .map(evidencia -> {
                    // ✅ Convertir byte[] → Base64
                    byte[] binario = evidencia.getArchivoBinario();
                    return binario != null ? Base64.getEncoder().encodeToString(binario) : null;
                })
                .orElse(
                        // Fallback: si no hay principal marcada, usar la primera imagen
                        evidencias.stream()
                                .filter(e -> "imagen".equalsIgnoreCase(e.getTipoArchivo()))
                                .findFirst()
                                .map(evidencia -> {
                                    // ✅ Convertir byte[] → Base64
                                    byte[] binario = evidencia.getArchivoBinario();
                                    return binario != null ? Base64.getEncoder().encodeToString(binario) : null;
                                })
                                .orElse(null)
                );
    }

    // =====================================================
    // MAP EVIDENCIAS
    // =====================================================
    private static List<EvidenciaReporteResponse> mapEvidencias(List<EvidenciaReporte> evidencias) {
        if (evidencias == null) return Collections.emptyList();

        return evidencias.stream()
                .map(evidencia -> {
                    // ✅ Convertir byte[] → Base64 para el frontend
                    String archivoBase64 = null;
                    if (evidencia.getArchivoBinario() != null) {
                        archivoBase64 = Base64.getEncoder().encodeToString(evidencia.getArchivoBinario());
                    }

                    return EvidenciaReporteResponse.builder()
                            .id(evidencia.getId())
                            .tipoArchivo(evidencia.getTipoArchivo())
                            .archivoBase64(archivoBase64)  // ✅ Base64 para el frontend
                            .urlArchivo(null)  // ⚠️ DEJAR EN NULL (ya no usamos URLs)
                            .nombreArchivo(evidencia.getNombreArchivo())
                            .tamanoArchivo(evidencia.getTamanoArchivo())
                            .mimeType(evidencia.getMimeType())
                            .descripcion(evidencia.getDescripcion())
                            .esPrincipal(evidencia.getEsPrincipal())
                            .fechaCarga(evidencia.getFechaCarga())
                            .build();
                })
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
                        .usuarioId(comentario.getUsuario().getId())
                        .usuarioNombre(comentario.getUsuario().getNombres())
                        .build())
                .toList();
    }

    // =====================================================
    // REPORTE -> MAPA RESPONSE (Para el mapa)
    // =====================================================
    public static ReporteMapaResponse toMapaResponse(Reporte reporte) {

        // ✅ CALCULAR fotoPrincipalBase64 desde evidencias
        String fotoPrincipalBase64 = calcularFotoPrincipalBase64(reporte.getEvidencias());

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
                .colorCategoria(reporte.getCategoria().getColor())

                .prioridadId(reporte.getPrioridad().getId())
                .prioridadNombre(reporte.getPrioridad().getNombre())
                .colorPrioridad(reporte.getPrioridad().getColor())

                .estadoReporteId(reporte.getEstadoReporte().getId())
                .estadoReporteNombre(reporte.getEstadoReporte().getNombre())
                .colorEstadoReporte(reporte.getEstadoReporte().getColor())

                // ✅ NO enviar Base64, solo un booleano
                .tieneImagen(reporte.getEvidencias() != null &&
                        !reporte.getEvidencias().isEmpty() &&
                        reporte.getEvidencias().stream().anyMatch(e -> "imagen".equalsIgnoreCase(e.getTipoArchivo())))

                // ✅ O enviar URL de placeholder/thumbnail si tienes
                .fotoPrincipalUrl(null)  // ← NULL o "/assets/placeholder.png"

                .usuarioId(reporte.getUsuario().getId())
                .usuarioNombre(reporte.getUsuario().getNombres())
                .usuarioFotoPerfil(null)
                .build();
    }
}