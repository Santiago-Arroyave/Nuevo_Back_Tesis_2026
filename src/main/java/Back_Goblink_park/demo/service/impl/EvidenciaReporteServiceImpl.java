package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.EvidenciaReporteMapper;
import Back_Goblink_park.demo.dto.request.EvidenciaReporteRequest;
import Back_Goblink_park.demo.dto.response.EvidenciaReporteResponse;

import Back_Goblink_park.demo.entity.EvidenciaReporte;
import Back_Goblink_park.demo.entity.Reporte;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.EvidenciaReporteRepository;
import Back_Goblink_park.demo.repository.ReporteRepository;

import Back_Goblink_park.demo.service.interfaces.EvidenciaReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvidenciaReporteServiceImpl
        implements EvidenciaReporteService {

    // =====================================================
    // INYECCIONES
    // =====================================================

    private final EvidenciaReporteRepository evidenciaRepository;

    private final ReporteRepository reporteRepository;

    // =====================================================
    // CREAR EVIDENCIA
    // =====================================================

    @Override
    public EvidenciaReporteResponse crearEvidencia(
            EvidenciaReporteRequest request
    ) {

        // =========================================
        // BUSCAR REPORTE
        // =========================================

        Reporte reporte = reporteRepository.findById(
                request.getReporteId()
        ).orElseThrow(() ->

                new ResourceNotFoundException(
                        "Reporte no encontrado"
                )
        );

        // =========================================
        // CREAR EVIDENCIA
        // =========================================

        EvidenciaReporte evidencia = EvidenciaReporte.builder()

                .reporte(reporte)

                .tipoArchivo(
                        request.getTipoArchivo()
                )

                .urlArchivo(
                        request.getUrlArchivo()
                )

                .nombreArchivo(
                        request.getNombreArchivo()
                )

                .tamanoArchivo(
                        request.getTamanoArchivo()
                )

                .descripcion(
                        request.getDescripcion()
                )

                .esPrincipal(
                        request.getEsPrincipal()
                )

                .mimeType(
                        request.getMimeType()
                )

                .storageProvider(
                        request.getStorageProvider()
                )

                .thumbnailUrl(
                        request.getThumbnailUrl()
                )

                .build();

        // =========================================
        // GUARDAR
        // =========================================

        EvidenciaReporte evidenciaGuardada =
                evidenciaRepository.save(evidencia);

        // =========================================
        // SI ES PRINCIPAL
        // ACTUALIZAR FOTO PRINCIPAL
        // =========================================

        if (Boolean.TRUE.equals(
                evidenciaGuardada.getEsPrincipal()
        )) {

            reporte.setFotoPrincipalUrl(
                    evidenciaGuardada.getUrlArchivo()
            );

            reporteRepository.save(reporte);
        }

        // =========================================
        // RESPUESTA
        // =========================================

        return EvidenciaReporteMapper.toResponse(
                evidenciaGuardada
        );
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    @Override
    public List<EvidenciaReporteResponse> listarEvidencias() {

        return evidenciaRepository.findAll()
                .stream()
                .map(EvidenciaReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // LISTAR POR REPORTE
    // =====================================================

    @Override
    public List<EvidenciaReporteResponse> listarPorReporte(
            Long reporteId
    ) {

        return evidenciaRepository
                .findByReporteId(reporteId)
                .stream()
                .map(EvidenciaReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public EvidenciaReporteResponse obtenerEvidencia(
            Long id
    ) {

        EvidenciaReporte evidencia =
                evidenciaRepository.findById(id)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Evidencia no encontrada"
                                )
                        );

        return EvidenciaReporteMapper.toResponse(
                evidencia
        );
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    public void eliminarEvidencia(
            Long id
    ) {

        EvidenciaReporte evidencia =
                evidenciaRepository.findById(id)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Evidencia no encontrada"
                                )
                        );

        evidenciaRepository.delete(evidencia);
    }
}