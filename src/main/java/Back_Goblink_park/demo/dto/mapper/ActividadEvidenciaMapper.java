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
                .urlArchivo(evidencia.getUrlArchivo() != null ? evidencia.getUrlArchivo() : evidencia.getImagenBase64())
                .archivoBase64(null)
                .tipoArchivo(evidencia.getTipoImagen())
                .descripcion(evidencia.getDescripcion())
                .nombreArchivo(evidencia.getNombreArchivo())
                .tamanoArchivo(evidencia.getTamanoArchivo())
                .mimeType(evidencia.getMimeType())
                .fechaCarga(evidencia.getFechaCarga())
                .esPrincipal(evidencia.getEsPrincipal())
                .usuarioId(evidencia.getUsuario() != null ? evidencia.getUsuario().getId() : null)
                .usuarioNombre(evidencia.getUsuario() != null ? evidencia.getUsuario().getNombres() : null)
                .build();
    }
}