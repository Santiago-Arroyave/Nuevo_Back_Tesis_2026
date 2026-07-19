package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ActividadEvidenciaResponse;
import Back_Goblink_park.demo.entity.ActividadEvidencia;

public class ActividadEvidenciaMapper {

    public static ActividadEvidenciaResponse toResponse(ActividadEvidencia evidencia) {
        if (evidencia == null) {
            return null;
        }

        return ActividadEvidenciaResponse.builder()
                .id(evidencia.getId())
                // ✅ Mapeamos el campo de la entidad (imagenBase64) al campo del DTO (archivoBase64)
                .archivoBase64(evidencia.getImagenBase64())
                .tipoArchivo(evidencia.getTipoImagen())
                .descripcion(evidencia.getDescripcion())
                .nombreArchivo(evidencia.getNombreArchivo())
                .tamanoArchivo(evidencia.getTamanoArchivo())
                .mimeType(evidencia.getMimeType())
                .fechaCarga(evidencia.getFechaCarga())
                .esPrincipal(evidencia.getEsPrincipal())
                .build();
    }
}