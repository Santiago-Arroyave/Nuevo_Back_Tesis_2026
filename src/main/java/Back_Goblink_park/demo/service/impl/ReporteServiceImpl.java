package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ReporteMapper;
import Back_Goblink_park.demo.dto.request.ActualizarEstadoReporteRequest;
import Back_Goblink_park.demo.dto.request.ActualizarPrioridadReporteRequest;
import Back_Goblink_park.demo.dto.request.ReporteRequest;
import Back_Goblink_park.demo.dto.request.ReporteUpdateRequest;
import Back_Goblink_park.demo.dto.response.ReporteMapaResponse;
import Back_Goblink_park.demo.dto.response.ReporteResponse;
import Back_Goblink_park.demo.entity.*;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.*;
import Back_Goblink_park.demo.service.interfaces.ReporteService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    // =====================================================
    // REPOSITORIES
    // =====================================================
    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final PrioridadRepository prioridadRepository;
    private final EstadoReporteRepository estadoReporteRepository;

    // ✅ NUEVO: Repositorio para evidencias
    private final EvidenciaReporteRepository evidenciaRepository;

    private byte[] convertMultipartFileToBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return file.getBytes();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error al convertir archivo a bytes: " + e.getMessage(), e);
        }
    }

    // =====================================================
    // CREAR REPORTE (CON EVIDENCIAS MÚLTIPLES)
    // =====================================================
    @Override
    @Transactional
    public ReporteResponse crearReporte(ReporteRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correo = authentication.getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        Prioridad prioridad = prioridadRepository.findById(request.getPrioridadId())
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));

        EstadoReporte estadoReporte = estadoReporteRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Estado reporte no encontrado"));

        // 1️⃣ CREAR REPORTE
        Reporte reporte = Reporte.builder()
                .usuario(usuario)
                .categoria(categoria)
                .prioridad(prioridad)
                .estadoReporte(estadoReporte)
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .fechaEvento(request.getFechaEvento())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .direccionReferencia(request.getDireccionReferencia())
                .fuente(request.getFuente())
                .publico(request.getPublico() != null ? request.getPublico() : true)
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .eliminado(false)
                .fechaReporte(LocalDateTime.now())
                .build();

        Reporte reporteGuardado = reporteRepository.save(reporte);


        // 2️⃣ GUARDAR EVIDENCIAS
        if (request.getEvidencias() != null && !request.getEvidencias().isEmpty()) {
            boolean principalAsignada = false;

            for (ReporteRequest.EvidenciaRequest evidenciaReq : request.getEvidencias()) {

                // ✅ Usar método helper
                byte[] archivoBinario = convertMultipartFileToBytes(evidenciaReq.getArchivo());

                boolean esPrincipal = evidenciaReq.getEsPrincipal() != null
                        ? evidenciaReq.getEsPrincipal()
                        : (!principalAsignada && "imagen".equalsIgnoreCase(evidenciaReq.getTipoArchivo()));

                if (esPrincipal) principalAsignada = true;

                EvidenciaReporte evidencia = EvidenciaReporte.builder()
                        .reporte(reporteGuardado)
                        .archivoBinario(archivoBinario)
                        .tipoArchivo(evidenciaReq.getTipoArchivo())
                        .nombreArchivo(evidenciaReq.getNombreArchivo())
                        .tamanoArchivo(evidenciaReq.getTamanoArchivo())
                        .mimeType(evidenciaReq.getMimeType())
                        .descripcion(evidenciaReq.getDescripcion())
                        .esPrincipal(esPrincipal)
                        .fechaCarga(LocalDateTime.now())
                        .build();

                evidenciaRepository.save(evidencia);
            }
        }


        // ✅ 3️⃣ CONSULTAR EVIDENCIAS DIRECTAMENTE (SOLUCIÓN GARANTIZADA)
        System.out.println("🔍 [SERVICE] Consultando evidencias para reporte ID: " + reporteGuardado.getId());
        List<EvidenciaReporte> evidenciasCargadas = evidenciaRepository.findByReporteId(reporteGuardado.getId());
        System.out.println("✅ [SERVICE] Evidencias encontradas: " + evidenciasCargadas.size());

        // ✅ 4️⃣ INYECTAR MANUALMENTE (bypass de Lazy Loading)
        reporteGuardado.setEvidencias(evidenciasCargadas);

        // ✅ 5️⃣ MAPEAR Y RETORNAR
        return ReporteMapper.toResponse(reporteGuardado);
    }

    // =====================================================
    // LISTAR SIMPLE
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> listarReportes() {
        return reporteRepository.findAllWithEvidencias()
                .stream()
                .map(ReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // LISTAR REPORTES CON FILTROS Y PAGINACIÓN
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public Page<ReporteResponse> listarReportesPaginados(
            String search,
            Long categoriaId,
            Long estadoReporteId,
            Long prioridadId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by("fechaReporte").descending()
        );

        Specification<Reporte> specification = construirSpecificationReportes(
                search, categoriaId, estadoReporteId, prioridadId, false);

        Page<Reporte> reportes = reporteRepository.findAll(specification, pageable);
        return reportes.map(ReporteMapper::toResponse);
    }

    // =====================================================
    // LISTAR REPORTES PARA MAPA
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<ReporteMapaResponse> listarReportesMapa(
            String search,
            Long categoriaId,
            Long estadoReporteId,
            Long prioridadId
    ) {
        Specification<Reporte> specification = construirSpecificationReportes(
                search, categoriaId, estadoReporteId, prioridadId, true);

        return reporteRepository.findAll(specification, Sort.by("fechaReporte").descending())
                .stream()
                .map(ReporteMapper::toMapaResponse)
                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public ReporteResponse obtenerReporte(Long id) {
        // ✅ Usar método con FETCH para cargar evidencias
        Reporte reporte = reporteRepository.findByIdWithEvidencias(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));

        return ReporteMapper.toResponse(reporte);
    }
    // =====================================================
    // FILTRAR CATEGORÍA
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> listarPorCategoria(Long categoriaId) {
        return reporteRepository.findByCategoriaIdAndEliminadoFalse(categoriaId)
                .stream()
                .map(ReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // FILTRAR PRIORIDAD
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> listarPorPrioridad(Long prioridadId) {
        return reporteRepository.findByPrioridadIdAndEliminadoFalse(prioridadId)
                .stream()
                .map(ReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // FILTRAR ESTADO
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> listarPorEstado(Long estadoReporteId) {
        return reporteRepository.findByEstadoReporteIdAndEliminadoFalse(estadoReporteId)
                .stream()
                .map(ReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // ACTUALIZAR ESTADO
    // =====================================================
    @Override
    @Transactional
    public ReporteResponse actualizarEstado(Long reporteId, ActualizarEstadoReporteRequest request) {
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));

        EstadoReporte estadoReporte = estadoReporteRepository.findById(request.getEstadoReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado reporte no encontrado"));

        reporte.setEstadoReporte(estadoReporte);
        reporte.setObservacionesAdmin(request.getObservacionesAdmin());

        if (estadoReporte.getNombre().equalsIgnoreCase("Validado")) {
            reporte.setFechaValidacion(LocalDateTime.now());
        }

        Reporte reporteActualizado = reporteRepository.save(reporte);
        return ReporteMapper.toResponse(reporteActualizado);
    }

    // =====================================================
    // ACTUALIZAR PRIORIDAD
    // =====================================================
    @Override
    @Transactional
    public ReporteResponse actualizarPrioridad(Long reporteId, ActualizarPrioridadReporteRequest request) {
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));

        Prioridad prioridad = prioridadRepository.findById(request.getPrioridadId())
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));

        reporte.setPrioridad(prioridad);
        Reporte reporteActualizado = reporteRepository.save(reporte);
        return ReporteMapper.toResponse(reporteActualizado);
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================
    @Override
    @Transactional
    public void eliminarReporte(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));
        reporte.setEliminado(true);
        reporteRepository.save(reporte);
    }

    // =====================================================
    // SPECIFICATION GENERAL PARA FILTROS
    // =====================================================
    private Specification<Reporte> construirSpecificationReportes(
            String search,
            Long categoriaId,
            Long estadoReporteId,
            Long prioridadId,
            boolean soloGeolocalizados
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Solo no eliminados
            predicates.add(criteriaBuilder.isFalse(root.get("eliminado")));

            // Solo reportes con coordenadas para mapa
            if (soloGeolocalizados) {
                predicates.add(criteriaBuilder.isNotNull(root.get("latitud")));
                predicates.add(criteriaBuilder.isNotNull(root.get("longitud")));
            }

            // Buscar por título o descripción
            if (search != null && !search.trim().isEmpty()) {
                String texto = "%" + search.trim().toLowerCase() + "%";
                Predicate tituloLike = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("titulo")), texto);
                Predicate descripcionLike = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("descripcion")), texto);
                predicates.add(criteriaBuilder.or(tituloLike, descripcionLike));
            }

            // Filtro categoría
            if (categoriaId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("categoria").get("id"), categoriaId));
            }

            // Filtro estado reporte
            if (estadoReporteId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("estadoReporte").get("id"), estadoReporteId));
            }

            // Filtro prioridad
            if (prioridadId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("prioridad").get("id"), prioridadId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    @Transactional
    public ReporteResponse actualizarReporte(Long id, String correoUsuario, ReporteUpdateRequest request) {

        // 1️⃣ Validar que el reporte existe y pertenece al usuario (o es admin)
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + id));

        // Verificar permisos: solo admin o el creador pueden editar
        if (!reporte.getUsuario().getCorreo().equals(correoUsuario)
                && !SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            throw new AccessDeniedException("No tienes permisos para editar este reporte");
        }

        // 2️⃣ Actualizar campos básicos (solo si vienen en el request)
        if (request.getTitulo() != null) reporte.setTitulo(request.getTitulo());
        if (request.getDescripcion() != null) reporte.setDescripcion(request.getDescripcion());
        if (request.getFechaEvento() != null) reporte.setFechaEvento(request.getFechaEvento());
        if (request.getLatitud() != null) reporte.setLatitud(request.getLatitud());
        if (request.getLongitud() != null) reporte.setLongitud(request.getLongitud());
        if (request.getDireccionReferencia() != null) reporte.setDireccionReferencia(request.getDireccionReferencia());
        if (request.getPublico() != null) reporte.setPublico(request.getPublico());
        if (request.getEstado() != null) reporte.setEstado(request.getEstado());

        // Actualizar categoría si cambia
        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            reporte.setCategoria(categoria);
        }

        // Actualizar prioridad si cambia
        if (request.getPrioridadId() != null) {
            Prioridad prioridad = prioridadRepository.findById(request.getPrioridadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));
            reporte.setPrioridad(prioridad);
        }

        // 3️⃣ Gestionar evidencias si vienen en el request
        if (request.getEvidencias() != null) {
            gestionarEvidencias(reporte, request.getEvidencias());
        }

        // 4️⃣ Guardar cambios
        Reporte actualizado = reporteRepository.save(reporte);

        // 5️⃣ Recargar con evidencias para la respuesta
        List<EvidenciaReporte> evidenciasCargadas =
                evidenciaRepository.findByReporteId(actualizado.getId());
        actualizado.setEvidencias(evidenciasCargadas);

        return ReporteMapper.toResponse(actualizado);
    }

    // =====================================================
// MÉTODO AUXILIAR: Gestionar evidencias (CRUD)
// =====================================================
    private void gestionarEvidencias(Reporte reporte, List<ReporteUpdateRequest.EvidenciaUpdateRequest> evidenciasReq) {
        boolean principalAsignada = false;

        for (ReporteUpdateRequest.EvidenciaUpdateRequest req : evidenciasReq) {

            // ➖ ELIMINAR evidencia existente
            if (Boolean.TRUE.equals(req.getEliminar()) && req.getId() != null) {
                evidenciaRepository.deleteById(req.getId());
                continue;
            }

            // ✏️ ACTUALIZAR evidencia existente
            if (req.getId() != null) {
                EvidenciaReporte evidencia = evidenciaRepository.findById(req.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Evidencia no encontrada con ID: " + req.getId()));

                // ✅ Actualizar archivo si viene nuevo (usa helper que maneja Base64 o MultipartFile)
                byte[] nuevosBytes = req.getArchivoAsBytes();
                if (nuevosBytes != null) {
                    evidencia.setArchivoBinario(nuevosBytes);
                }

                if (req.getTipoArchivo() != null) evidencia.setTipoArchivo(req.getTipoArchivo());
                if (req.getNombreArchivo() != null) evidencia.setNombreArchivo(req.getNombreArchivo());
                if (req.getTamanoArchivo() != null) evidencia.setTamanoArchivo(req.getTamanoArchivo());
                if (req.getMimeType() != null) evidencia.setMimeType(req.getMimeType());
                if (req.getDescripcion() != null) evidencia.setDescripcion(req.getDescripcion());

                // Manejar esPrincipal: solo una puede ser principal
                boolean esPrincipal = req.getEsPrincipal() != null ? req.getEsPrincipal() : false;
                if (esPrincipal && !principalAsignada) {
                    evidencia.setEsPrincipal(true);
                    principalAsignada = true;
                } else if (esPrincipal && principalAsignada) {
                    evidencia.setEsPrincipal(false);  // Desmarcar si ya hay otra principal
                }

                evidenciaRepository.save(evidencia);

                // ➕ CREAR nueva evidencia
            } else {
                // ✅ Obtener bytes del archivo (Base64 o MultipartFile)
                byte[] archivoBinario = req.getArchivoAsBytes();

                // Solo crear si hay archivo
                if (archivoBinario != null) {
                    boolean esPrincipal = req.getEsPrincipal() != null
                            ? req.getEsPrincipal()
                            : (!principalAsignada && "imagen".equalsIgnoreCase(req.getTipoArchivo()));

                    if (esPrincipal) principalAsignada = true;

                    EvidenciaReporte nueva = EvidenciaReporte.builder()
                            .reporte(reporte)
                            .archivoBinario(archivoBinario)  // ✅ Guardar bytes
                            .tipoArchivo(req.getTipoArchivo())
                            .nombreArchivo(req.getNombreArchivo())
                            .tamanoArchivo(req.getTamanoArchivo())
                            .mimeType(req.getMimeType())
                            .descripcion(req.getDescripcion())
                            .esPrincipal(esPrincipal)
                            .fechaCarga(LocalDateTime.now())
                            .build();

                    evidenciaRepository.save(nueva);
                }
            }
        }
    }
}