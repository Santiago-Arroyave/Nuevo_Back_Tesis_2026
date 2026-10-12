package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ActividadEvidenciaMapper;
import Back_Goblink_park.demo.dto.mapper.CronogramaActividadProyectoMapper;
import Back_Goblink_park.demo.dto.request.CronogramaActividadCompletarRequest;
import Back_Goblink_park.demo.dto.request.CronogramaActividadProyectoRequest;
import Back_Goblink_park.demo.dto.response.ActividadEvidenciaResponse;
import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;
import Back_Goblink_park.demo.entity.ActividadEvidencia;
import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;
import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoMiembro;
import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.ActividadEvidenciaRepository;
import Back_Goblink_park.demo.repository.CronogramaActividadProyectoRepository;
import Back_Goblink_park.demo.repository.ProyectoMiembroRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;
import Back_Goblink_park.demo.service.interfaces.CronogramaActividadProyectoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CronogramaActividadProyectoServiceImpl
        implements CronogramaActividadProyectoService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final CronogramaActividadProyectoRepository actividadRepository;
    private final ProyectoRepository proyectoRepository;
    private final ProyectoMiembroRepository proyectoMiembroRepository;
    private final ActividadEvidenciaRepository actividadEvidenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final Back_Goblink_park.demo.service.interfaces.FileStorageService fileStorageService;

    // =====================================================
    // MÉTODO AUXILIAR: BUSCAR MIEMBRO POR USUARIO O ID DIRECTO
    // =====================================================

    /**
     * Busca el ProyectoMiembro según los datos del request.
     * Prioridad:
     *   1. Si viene proyectoMiembroId → buscar por ese ID
     *   2. Si viene usuarioId → buscar por usuario + proyecto
     *   3. Si no viene ninguno → retornar null (sin responsable)
     */
    private ProyectoMiembro buscarMiembro(
            Long proyectoId,
            Long proyectoMiembroId,
            Long usuarioId) {

        // ✅ OPCIÓN 1: Si viene el ID directo del miembro
        if (proyectoMiembroId != null) {
            ProyectoMiembro miembro = proyectoMiembroRepository
                    .findById(proyectoMiembroId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Miembro del proyecto no encontrado (ID: " + proyectoMiembroId + ")"
                    ));

            if (!miembro.getProyecto().getId().equals(proyectoId)) {
                throw new IllegalArgumentException(
                        "El miembro seleccionado no pertenece a este proyecto"
                );
            }
            return miembro;
        }

        // ✅ OPCIÓN 2: Si viene el ID del usuario, buscar el miembro por usuario + proyecto
        if (usuarioId != null) {
            List<ProyectoMiembro> miembros = proyectoMiembroRepository
                    .findByProyectoId(proyectoId);

            return miembros.stream()
                    .filter(m -> m.getUsuario().getId().equals(usuarioId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "El usuario (ID: " + usuarioId + ") no es miembro de este proyecto"
                    ));
        }

        // ✅ OPCIÓN 3: Sin responsable
        return null;
    }

    // =====================================================
    // CREAR
    // =====================================================

    @Override
    public CronogramaActividadProyectoResponse crearActividad(
            CronogramaActividadProyectoRequest request) {

        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        // ✅ Buscar miembro usando el nuevo método auxiliar
        ProyectoMiembro proyectoMiembro = buscarMiembro(
                proyecto.getId(),
                request.getProyectoMiembroId(),
                request.getUsuarioId()
        );

        CronogramaActividadProyecto actividad = CronogramaActividadProyecto.builder()
                .proyecto(proyecto)
                .proyectoMiembro(proyectoMiembro)
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .estado(request.getEstado())
                .prioridad(request.getPrioridad() != null ? request.getPrioridad() : "media")
                .porcentajeAvance(request.getPorcentajeAvance() != null
                        ? request.getPorcentajeAvance()
                        : BigDecimal.ZERO)
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .urlEvidencia(request.getUrlEvidencia())
                .observaciones(request.getObservaciones())
                .build();

        CronogramaActividadProyecto guardada = actividadRepository.save(actividad);
        return CronogramaActividadProyectoMapper.toResponse(guardada);
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    @Override
    public List<CronogramaActividadProyectoResponse> listarActividades() {
        return actividadRepository.findAll().stream()
                .map(CronogramaActividadProyectoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public CronogramaActividadProyectoResponse obtenerActividad(Long id) {
        CronogramaActividadProyecto actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        return CronogramaActividadProyectoMapper.toResponse(actividad);
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @Override
    public List<CronogramaActividadProyectoResponse> listarPorProyecto(Long proyectoId) {
        return actividadRepository.findByProyectoIdOrderByFechaInicioAsc(proyectoId).stream()
                .map(CronogramaActividadProyectoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // LISTAR POR MIEMBRO DEL PROYECTO
    // =====================================================

    @Override
    public List<CronogramaActividadProyectoResponse> listarPorMiembro(Long proyectoMiembroId) {
        return actividadRepository.findByProyectoMiembroId(proyectoMiembroId).stream()
                .map(CronogramaActividadProyectoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    @Override
    public List<CronogramaActividadProyectoResponse> listarPorEstado(String estado) {
        return actividadRepository.findByEstado(estado).stream()
                .map(CronogramaActividadProyectoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public CronogramaActividadProyectoResponse actualizarActividad(
            Long id, CronogramaActividadProyectoRequest request) {

        CronogramaActividadProyecto actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        // ✅ Buscar miembro usando el nuevo método auxiliar
        ProyectoMiembro proyectoMiembro = buscarMiembro(
                proyecto.getId(),
                request.getProyectoMiembroId(),
                request.getUsuarioId()
        );

        actividad.setProyecto(proyecto);
        actividad.setProyectoMiembro(proyectoMiembro);
        actividad.setNombre(request.getNombre());
        actividad.setDescripcion(request.getDescripcion());
        actividad.setEstado(request.getEstado());
        actividad.setPrioridad(request.getPrioridad() != null
                ? request.getPrioridad()
                : actividad.getPrioridad());
        actividad.setPorcentajeAvance(request.getPorcentajeAvance() != null
                ? request.getPorcentajeAvance()
                : actividad.getPorcentajeAvance());
        actividad.setFechaInicio(request.getFechaInicio());
        actividad.setFechaFin(request.getFechaFin());
        actividad.setUrlEvidencia(request.getUrlEvidencia());
        actividad.setObservaciones(request.getObservaciones());

        CronogramaActividadProyecto actualizada = actividadRepository.save(actividad);
        return CronogramaActividadProyectoMapper.toResponse(actualizada);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    @Transactional
    public void eliminarActividad(Long id) {
        CronogramaActividadProyecto actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        if (actividad.getEvidencias() != null) {
            actividad.getEvidencias().forEach(e -> {
                if (e.getUrlArchivo() != null && !e.getUrlArchivo().isBlank()) {
                    fileStorageService.deleteFile(e.getUrlArchivo());
                }
            });
        }
        if (actividad.getUrlEvidencia() != null && !actividad.getUrlEvidencia().isBlank()) {
            fileStorageService.deleteFile(actividad.getUrlEvidencia());
        }
        actividadRepository.delete(actividad);
    }

    // =====================================================
    // MARCAR ACTIVIDAD COMO COMPLETADA (SIN EVIDENCIA)
    // =====================================================

    @Override
    @Transactional
    public CronogramaActividadProyectoResponse marcarActividadCompletada(Long actividadId) {
        CronogramaActividadProyecto actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        actividad.setEstado("completada");

        if (actividad.getFechaFin() == null) {
            actividad.setFechaFin(LocalDate.now());
        }

        CronogramaActividadProyecto actualizada = actividadRepository.save(actividad);

        if (actividad.getProyecto() != null) {
            recalcularAvanceProyecto(actividad.getProyecto().getId());
        }

        return CronogramaActividadProyectoMapper.toResponse(actualizada);
    }

    // =====================================================
    // MARCAR ACTIVIDAD COMO COMPLETADA CON EVIDENCIA (BASE64)
    // =====================================================


    // =====================================================
    // MARCAR ACTIVIDAD COMO COMPLETADA CON MÚLTIPLES EVIDENCIAS
    // =====================================================

    @Override
    @Transactional
    public CronogramaActividadProyectoResponse marcarActividadCompletadaConEvidencia(
            Long actividadId,
            List<MultipartFile> files,
            String descripcionEvidencia,
            String observaciones) {

        CronogramaActividadProyecto actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        // 1. Marcar como completada
        actividad.setEstado("completada");
        if (actividad.getFechaFin() == null) {
            actividad.setFechaFin(LocalDate.now());
        }

        // 2. Actualizar observaciones si vienen
        if (observaciones != null && !observaciones.isEmpty()) {
            String obsExistente = actividad.getObservaciones() != null ? actividad.getObservaciones() : "";
            actividad.setObservaciones(obsExistente + "\n" + observaciones);
        }

        // 3. PROCESAR MÚLTIPLES ARCHIVOS
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String url = fileStorageService.uploadFile(file, "actividades");

                    ActividadEvidencia evidencia = ActividadEvidencia.builder()
                            .actividad(actividad)
                            .urlArchivo(url)
                            .tipoImagen(file.getContentType())
                            .nombreArchivo(file.getOriginalFilename())
                            .tamanoArchivo(file.getSize())
                            .mimeType(file.getContentType())
                            .descripcion(descripcionEvidencia)
                            .build();

                    actividadEvidenciaRepository.save(evidencia);
                    actividad.getEvidencias().add(evidencia);

                    if (actividad.getUrlEvidencia() == null || actividad.getUrlEvidencia().isBlank()) {
                        actividad.setUrlEvidencia(url);
                    }
                }
            }
        }

        // 4. Guardar la actividad actualizada
        actividadRepository.save(actividad);

        // 5. Recalcular avance del proyecto
        if (actividad.getProyecto() != null) {
            recalcularAvanceProyecto(actividad.getProyecto().getId());
        }

        // ✅ 6. FORZAR CARGA DE LA COLECCIÓN LAZY
        actividad.getEvidencias().size(); // Esto fuerza a Hibernate a cargar la colección

        return CronogramaActividadProyectoMapper.toResponse(actividad);
    }

    // =====================================================
    // RECALCULAR AVANCE DEL PROYECTO
    // =====================================================

    @Override
    @Transactional
    public BigDecimal recalcularAvanceProyecto(Long proyectoId) {
        List<CronogramaActividadProyecto> actividades = actividadRepository
                .findByProyectoIdOrderByFechaInicioAsc(proyectoId);

        if (actividades.isEmpty()) {
            return BigDecimal.ZERO;
        }

        long completadas = actividades.stream()
                .filter(a -> "completada".equalsIgnoreCase(a.getEstado()))
                .count();

        BigDecimal porcentaje = BigDecimal.valueOf(completadas)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(actividades.size()), 2, RoundingMode.HALF_UP);

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        proyecto.setPorcentajeAvance(porcentaje.doubleValue());
        proyectoRepository.save(proyecto);

        return porcentaje;
    }

    // =====================================================
    // LISTADO CRONOLÓGICO Y EVIDENCIAS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CronogramaActividadProyectoResponse> listarActividadesPorProyectoCronologico(Long proyectoId) {
        List<CronogramaActividadProyecto> actividades = actividadRepository
                .findByProyectoIdOrderByFechaInicioAsc(proyectoId);

        List<CronogramaActividadProyectoResponse> responses = new java.util.ArrayList<>();
        for (int i = 0; i < actividades.size(); i++) {
            CronogramaActividadProyecto a = actividades.get(i);
            CronogramaActividadProyectoResponse resp = CronogramaActividadProyectoMapper.toResponse(a);
            resp.setNumero(i + 1);
            responses.add(resp);
        }
        return responses;
    }

    @Override
    @Transactional
    public ActividadEvidenciaResponse subirEvidencia(
            Long idActividad,
            MultipartFile file,
            String descripcion,
            String correoUsuario) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo de evidencia es obligatorio");
        }

        CronogramaActividadProyecto actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con ID: " + idActividad));

        Usuario usuario = null;
        if (correoUsuario != null && !correoUsuario.isBlank()) {
            String correoSanitizado = correoUsuario.trim().toLowerCase();
            usuario = usuarioRepository.findByCorreoIgnoreCase(correoSanitizado).orElse(null);
        }

        String url = fileStorageService.uploadFile(file, "actividades");

        boolean esPrimera = actividad.getEvidencias() == null || actividad.getEvidencias().isEmpty();

        ActividadEvidencia evidencia = ActividadEvidencia.builder()
                .actividad(actividad)
                .usuario(usuario)
                .urlArchivo(url)
                .tipoImagen(file.getContentType())
                .nombreArchivo(file.getOriginalFilename())
                .tamanoArchivo(file.getSize())
                .mimeType(file.getContentType())
                .descripcion(descripcion != null ? descripcion.trim() : null)
                .esPrincipal(esPrimera)
                .build();

        ActividadEvidencia guardada = actividadEvidenciaRepository.save(evidencia);
        if (actividad.getEvidencias() != null) {
            actividad.getEvidencias().add(guardada);
        }

        if (actividad.getUrlEvidencia() == null || actividad.getUrlEvidencia().isBlank()) {
            actividad.setUrlEvidencia(url);
            actividadRepository.save(actividad);
        }

        return ActividadEvidenciaMapper.toResponse(guardada);
    }

    @Override
    @Transactional
    public void eliminarEvidenciaActividad(Long evidenciaId, String correoUsuario) {
        ActividadEvidencia evidencia = actividadEvidenciaRepository.findById(evidenciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia de actividad no encontrada con ID: " + evidenciaId));

        if (correoUsuario != null && !correoUsuario.isBlank()) {
            String correoSanitizado = correoUsuario.trim().toLowerCase();
            Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoSanitizado)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correoUsuario));

            boolean esAdmin = usuario.getRol() != null && "ADMIN".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esPropietario = evidencia.getUsuario() != null && evidencia.getUsuario().getId().equals(usuario.getId());
            boolean esResponsable = evidencia.getActividad() != null
                    && evidencia.getActividad().getProyectoMiembro() != null
                    && evidencia.getActividad().getProyectoMiembro().getUsuario() != null
                    && evidencia.getActividad().getProyectoMiembro().getUsuario().getId().equals(usuario.getId());

            if (!esAdmin && !esPropietario && !esResponsable) {
                throw new org.springframework.security.access.AccessDeniedException("No tienes permisos para eliminar esta evidencia");
            }
        }

        // Borrar de Supabase Storage
        if (evidencia.getUrlArchivo() != null && !evidencia.getUrlArchivo().isBlank()) {
            fileStorageService.deleteFile(evidencia.getUrlArchivo());
        }

        // Si era la URL principal de la actividad, limpiar
        CronogramaActividadProyecto actividad = evidencia.getActividad();
        if (actividad != null && evidencia.getUrlArchivo() != null && evidencia.getUrlArchivo().equals(actividad.getUrlEvidencia())) {
            actividad.setUrlEvidencia(null);
            actividadRepository.save(actividad);
        }

        actividadEvidenciaRepository.delete(evidencia);
    }
}