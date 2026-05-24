package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.EvidenciaReporteResponse;
import Back_Goblink_park.demo.entity.EvidenciaReporte;

public class EvidenciaReporteMapper {

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    public static EvidenciaReporteResponse toResponse(
            EvidenciaReporte evidencia
    ) {

        return EvidenciaReporteResponse.builder()

                // =====================================
                // ID
                // =====================================

                .id(evidencia.getId())

                // =====================================
                // REPORTE
                // =====================================

                .reporteId(
                        evidencia.getReporte().getId()
                )

                // =====================================
                // DATOS ARCHIVO
                // =====================================

                .tipoArchivo(
                        evidencia.getTipoArchivo()
                )

                .urlArchivo(
                        evidencia.getUrlArchivo()
                )

                .nombreArchivo(
                        evidencia.getNombreArchivo()
                )

                .tamanoArchivo(
                        evidencia.getTamanoArchivo()
                )

                .descripcion(
                        evidencia.getDescripcion()
                )

                .esPrincipal(
                        evidencia.getEsPrincipal()
                )

                // =====================================
                // NUEVOS CAMPOS
                // =====================================

                .mimeType(
                        evidencia.getMimeType()
                )

                .storageProvider(
                        evidencia.getStorageProvider()
                )

                .thumbnailUrl(
                        evidencia.getThumbnailUrl()
                )

                // =====================================
                // FECHAS
                // =====================================

                .fechaCarga(
                        evidencia.getFechaCarga()
                )

                .createdAt(
                        evidencia.getCreatedAt()
                )

                .updatedAt(
                        evidencia.getUpdatedAt()
                )

                .build();
    }
}