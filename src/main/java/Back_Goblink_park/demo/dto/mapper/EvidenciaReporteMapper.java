package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.EvidenciaReporteResponse;
import Back_Goblink_park.demo.entity.EvidenciaReporte;

import java.util.Base64;

public class EvidenciaReporteMapper {

    public static EvidenciaReporteResponse toResponse(EvidenciaReporte evidencia) {
        if (evidencia == null) {
            return null;
        }

        return EvidenciaReporteResponse.builder()
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
                .build();
    }
}