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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvidenciaReporteServiceImpl implements EvidenciaReporteService {

    // =====================================================
    // INYECCIONES
    // =====================================================
    private final EvidenciaReporteRepository evidenciaRepository;
    private final ReporteRepository reporteRepository;

    // =====================================================
    // CREAR EVIDENCIA (CON BASE64)
    // =====================================================
    @Override
    @Transactional
    public EvidenciaReporteResponse crearEvidencia(EvidenciaReporteRequest request) {

        Reporte reporte = reporteRepository.findById(request.getReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));

        // ✅ Obtener bytes del archivo
        byte[] archivoBinario = request.getArchivoAsBytes();

        EvidenciaReporte evidencia = EvidenciaReporte.builder()
                .reporte(reporte)
                .archivoBinario(archivoBinario)  // ✅ Usar archivoBinario
                .tipoArchivo(request.getTipoArchivo())
                .nombreArchivo(request.getNombreArchivo())
                .tamanoArchivo(request.getTamanoArchivo())
                .mimeType(request.getMimeType())
                .descripcion(request.getDescripcion())
                .esPrincipal(request.getEsPrincipal() != null ? request.getEsPrincipal() : false)
                .fechaCarga(LocalDateTime.now())
                .build();

        EvidenciaReporte evidenciaGuardada = evidenciaRepository.save(evidencia);

        // Si es principal, actualizar foto principal del reporte
        if (Boolean.TRUE.equals(evidenciaGuardada.getEsPrincipal())) {
            // ✅ Convertir byte[] a Base64 para guardar en reporte.fotoPrincipalUrl
            String base64 = Base64.getEncoder().encodeToString(evidenciaGuardada.getArchivoBinario());
            reporte.setFotoPrincipalUrl(base64);
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
        return evidenciaRepository.findByReporteId(reporteId)
                .stream()
                .map(EvidenciaReporteMapper::toResponse)
                .toList();
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
        evidenciaRepository.delete(evidencia);
    }
}