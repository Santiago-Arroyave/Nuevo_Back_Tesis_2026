package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.EvidenciaReporteResponse;
import Back_Goblink_park.demo.entity.EvidenciaReporte;

import java.util.Base64;

public class EvidenciaReporteMapper {

    // En EvidenciaReporteMapper.java
    public static EvidenciaReporteResponse toResponse(EvidenciaReporte evidencia) {

        // ✅ Convertir BYTEA a Base64 SOLO para la respuesta (frontend)
        String archivoBase64 = evidencia.getArchivoBinario() != null
                ? Base64.getEncoder().encodeToString(evidencia.getArchivoBinario())
                : null;

        return EvidenciaReporteResponse.builder()
                .id(evidencia.getId())
                .reporteId(evidencia.getReporte() != null ? evidencia.getReporte().getId() : null)
                .tipoArchivo(evidencia.getTipoArchivo())
                .archivoBase64(archivoBase64)  // ← Frontend recibe Base64
                .urlArchivo(null)
                .nombreArchivo(evidencia.getNombreArchivo())
                .tamanoArchivo(evidencia.getTamanoArchivo())
                .mimeType(evidencia.getMimeType())
                .descripcion(evidencia.getDescripcion())
                .esPrincipal(evidencia.getEsPrincipal())
                .fechaCarga(evidencia.getFechaCarga())
                .build();
    }
}