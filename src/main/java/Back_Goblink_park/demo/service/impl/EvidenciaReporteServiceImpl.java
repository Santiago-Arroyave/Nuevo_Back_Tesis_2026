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
import Back_Goblink_park.demo.service.interfaces.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvidenciaReporteServiceImpl implements EvidenciaReporteService {

    // =====================================================
    // INYECCIONES
    // =====================================================
    private final EvidenciaReporteRepository evidenciaRepository;
    private final ReporteRepository reporteRepository;
    private final FileStorageService fileStorageService;

    // =====================================================
    // CREAR EVIDENCIA (CON SUPABASE STORAGE)
    // =====================================================
    @Override
    @Transactional
    public EvidenciaReporteResponse crearEvidencia(EvidenciaReporteRequest request) {

        Reporte reporte = reporteRepository.findById(request.getReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));

        String urlArchivo = request.getUrlArchivo();
        if (request.getArchivo() != null && !request.getArchivo().isEmpty()) {
            urlArchivo = fileStorageService.uploadFile(request.getArchivo(), "reportes");
        }

        EvidenciaReporte evidencia = EvidenciaReporte.builder()
                .reporte(reporte)
                .urlArchivo(urlArchivo)
                .tipoArchivo(request.getTipoArchivo())
                .nombreArchivo(request.getNombreArchivo() != null ? request.getNombreArchivo() :
                        (request.getArchivo() != null ? request.getArchivo().getOriginalFilename() : null))
                .tamanoArchivo(request.getTamanoArchivo() != null ? request.getTamanoArchivo() :
                        (request.getArchivo() != null ? request.getArchivo().getSize() : null))
                .mimeType(request.getMimeType() != null ? request.getMimeType() :
                        (request.getArchivo() != null ? request.getArchivo().getContentType() : null))
                .descripcion(request.getDescripcion())
                .esPrincipal(request.getEsPrincipal() != null ? request.getEsPrincipal() : false)
                .fechaCarga(LocalDateTime.now())
                .build();

        EvidenciaReporte evidenciaGuardada = evidenciaRepository.save(evidencia);

        // Si es principal, actualizar foto principal del reporte
        if (Boolean.TRUE.equals(evidenciaGuardada.getEsPrincipal()) && urlArchivo != null) {
            reporte.setFotoPrincipalUrl(urlArchivo);
            reporteRepository.save(reporte);
        }

        return EvidenciaReporteMapper.toResponse(evidenciaGuardada);
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
    public List<EvidenciaReporteResponse> listarPorReporte(Long reporteId) {
        return evidenciaRepository.findResponsesByReporteId(reporteId);
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    @Override
    public EvidenciaReporteResponse obtenerEvidencia(Long id) {
        EvidenciaReporte evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia no encontrada"));
        return EvidenciaReporteMapper.toResponse(evidencia);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================
    @Override
    @Transactional
    public void eliminarEvidencia(Long id) {
        EvidenciaReporte evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia no encontrada"));

        if (evidencia.getUrlArchivo() != null && !evidencia.getUrlArchivo().isBlank()) {
            fileStorageService.deleteFile(evidencia.getUrlArchivo());
        }

        evidenciaRepository.delete(evidencia);
    }
}