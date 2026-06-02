package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.*;
import Back_Goblink_park.demo.dto.request.*;
import Back_Goblink_park.demo.dto.response.ProyectoDetalleResponse;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;
import Back_Goblink_park.demo.entity.*;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.*;
import Back_Goblink_park.demo.service.interfaces.ProyectoService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl implements ProyectoService {

    // =====================================================
    // REPOSITORIES
    // =====================================================
    private final ReporteRepository reporteRepository;
    private final EstadoReporteRepository estadoReporteRepository;
    private final EstadoProyectoRepository estadoProyectoRepository;
    private final ProyectoRepository proyectoRepository;
    private final PrioridadRepository prioridadRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoReporteRepository proyectoReporteRepository;
    private final ProyectoMiembroRepository proyectoMiembroRepository;
    private final ResponsableProyectoRepository responsableProyectoRepository;
    private final ProyectoObjetivoRepository proyectoObjetivoRepository;
    private final ProyectoMetaRepository proyectoMetaRepository;
    private final ProyectoPresupuestoRepository proyectoPresupuestoRepository;
    private final CronogramaActividadProyectoRepository cronogramaActividadProyectoRepository;
    private final SeguimientoProyectoRepository seguimientoProyectoRepository;


    // =====================================================
    // CREAR PROYECTO SIMPLE
    // =====================================================
    @Override
    @Transactional
    public ProyectoResponse crearProyecto(ProyectoRequest request, String correoUsuario) {
        Proyecto proyectoGuardado = crearProyectoBase(request, correoUsuario);
        return ProyectoMapper.toResponse(proyectoGuardado);
    }

    // =====================================================
    // CREAR PROYECTO COMPLETO
    // =====================================================
    @Override
    @Transactional
    public ProyectoDetalleResponse crearProyectoCompleto(ProyectoCompletoRequest request, String correoUsuario) {
        if (request == null || request.getProyecto() == null) {
            throw new IllegalArgumentException("Los datos del proyecto son obligatorios");
        }

        Proyecto proyectoGuardado = crearProyectoBase(request.getProyecto(), correoUsuario);

        // Asociar reportes
        if (request.getReporteIds() != null && !request.getReporteIds().isEmpty()) {
            crearReportesAsociados(proyectoGuardado, request.getReporteIds());
        }

        // Crear miembros
        if (request.getMiembros() != null && !request.getMiembros().isEmpty()) {
            crearMiembrosProyecto(proyectoGuardado, request.getMiembros());
        }

        // Crear responsables
        if (request.getResponsables() != null && !request.getResponsables().isEmpty()) {
            crearResponsablesProyecto(proyectoGuardado, request.getResponsables());
        }

        // Crear objetivos
        if (request.getObjetivos() != null && !request.getObjetivos().isEmpty()) {
            crearObjetivosProyecto(proyectoGuardado, request.getObjetivos());
        }

        // Crear metas
        if (request.getMetas() != null && !request.getMetas().isEmpty()) {
            crearMetasProyecto(proyectoGuardado, request.getMetas());
        }

        // Crear presupuesto
        if (request.getPresupuesto() != null && !request.getPresupuesto().isEmpty()) {
            crearPresupuestoProyecto(proyectoGuardado, request.getPresupuesto());
        }

        // Crear cronograma de actividades
        if (request.getCronogramaActividades() != null && !request.getCronogramaActividades().isEmpty()) {
            crearCronogramaProyecto(proyectoGuardado, request.getCronogramaActividades());
        }

        return obtenerDetalleCompleto(proyectoGuardado.getId());
    }

    // =====================================================
    // LISTAR PROYECTOS
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarProyectos() {
        return proyectoRepository.findByEliminadoFalse()
                .stream()
                .map(ProyectoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProyectoResponse> listarProyectosPaginados(String search, Long estadoProyectoId, Long prioridadId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by("fechaCreacion").descending());

        Specification<Proyecto> specification = (root, query, criteriaBuilder) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isFalse(root.get("eliminado")));

            if (search != null && !search.trim().isEmpty()) {
                String texto = "%" + search.trim().toLowerCase() + "%";
                Predicate nombreLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), texto);
                Predicate descripcionLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("descripcion")), texto);
                predicates.add(criteriaBuilder.or(nombreLike, descripcionLike));
            }
            if (estadoProyectoId != null) {
                predicates.add(criteriaBuilder.equal(root.get("estadoProyecto").get("id"), estadoProyectoId));
            }
            if (prioridadId != null) {
                predicates.add(criteriaBuilder.equal(root.get("prioridad").get("id"), prioridadId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return proyectoRepository.findAll(specification, pageable).map(ProyectoMapper::toResponse);
    }

    // =====================================================
    // OBTENER PROYECTO POR ID
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public ProyectoResponse obtenerProyecto(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        return ProyectoMapper.toResponse(proyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoDetalleResponse obtenerDetalleCompleto(Long proyectoId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        return ProyectoDetalleResponse.builder()
                .proyecto(ProyectoMapper.toResponse(proyecto))
                .reportesAsociados(proyectoReporteRepository.findByProyectoId(proyectoId).stream().map(ProyectoReporteMapper::toResponse).toList())
                .miembros(proyectoMiembroRepository.findByProyectoId(proyectoId).stream().map(ProyectoMiembroMapper::toResponse).toList())
                .responsables(responsableProyectoRepository.findByProyectoId(proyectoId).stream().map(ResponsableProyectoMapper::toResponse).toList())
                .objetivos(proyectoObjetivoRepository.findByProyectoId(proyectoId).stream().map(ProyectoObjetivoMapper::toResponse).toList())
                .metas(proyectoMetaRepository.findByProyectoId(proyectoId).stream().map(ProyectoMetaMapper::toResponse).toList())
                .presupuesto(proyectoPresupuestoRepository.findByProyectoId(proyectoId).stream().map(ProyectoPresupuestoMapper::toResponse).toList())
                .cronogramaActividades(cronogramaActividadProyectoRepository.findByProyectoIdOrderByFechaInicioAsc(proyectoId).stream().map(CronogramaActividadProyectoMapper::toResponse).toList())
                .seguimientos(seguimientoProyectoRepository.findByProyectoIdOrderByFechaSeguimientoDesc(proyectoId).stream().map(SeguimientoProyectoMapper::toResponse).toList())
                .build();
    }

    // =====================================================
    // ACTUALIZAR PROYECTO SIMPLE
    // =====================================================
    @Override
    @Transactional
    public ProyectoResponse actualizarProyecto(Long id, ProyectoRequest request) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        EstadoProyecto estadoProyecto = estadoProyectoRepository.findById(request.getEstadoProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado proyecto no encontrado"));
        Prioridad prioridad = prioridadRepository.findById(request.getPrioridadId())
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));
        Categoria categoria = request.getCategoriaId() != null ? categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada")) : null;

        proyecto.setEstadoProyecto(estadoProyecto);
        proyecto.setPrioridad(prioridad);
        proyecto.setCategoria(categoria);
        proyecto.setNombre(request.getNombre());
        proyecto.setDescripcion(request.getDescripcion());
        proyecto.setPresupuestoEstimado(request.getPresupuestoEstimado());
        proyecto.setFechaInicio(request.getFechaInicio());
        proyecto.setFechaFin(request.getFechaFin());
        proyecto.setObservaciones(request.getObservaciones());
        proyecto.setLatitud(request.getLatitud());
        proyecto.setLongitud(request.getLongitud());
        proyecto.setUbicacion(request.getUbicacion());
        proyecto.setTipoProyecto(request.getTipoProyecto());
        proyecto.setPorcentajeAvance(request.getPorcentajeAvance());
        if (request.getEstado() != null) proyecto.setEstado(request.getEstado());

        return ProyectoMapper.toResponse(proyectoRepository.save(proyecto));
    }

    // =====================================================
    // ACTUALIZAR PROYECTO COMPLETO - CORREGIDO ✅
    // =====================================================
    @Override
    @Transactional
    public ProyectoDetalleResponse actualizarProyectoCompleto(Long proyectoId, ProyectoCompletoRequest request) {
        if (request == null || request.getProyecto() == null) {
            throw new IllegalArgumentException("Los datos del proyecto son obligatorios");
        }

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        // Actualizar datos generales
        actualizarDatosGeneralesProyecto(proyecto, request.getProyecto());
        Proyecto proyectoActualizado = proyectoRepository.save(proyecto);

        // Sincronizar reportes (solo si se envían explícitamente)
        if (request.getReporteIds() != null) {
            proyectoReporteRepository.eliminarPorProyectoId(proyectoId);
            proyectoReporteRepository.flush();
            crearReportesAsociados(proyectoActualizado, request.getReporteIds());
        }

        // Sincronizar miembros (solo si se envían explícitamente)
        if (request.getMiembros() != null) {
            proyectoMiembroRepository.deleteAll(proyectoMiembroRepository.findByProyectoId(proyectoId));
            proyectoMiembroRepository.flush();
            crearMiembrosProyecto(proyectoActualizado, request.getMiembros());
        }

        // Sincronizar responsables (solo si se envían explícitamente)
        if (request.getResponsables() != null) {
            responsableProyectoRepository.deleteAll(responsableProyectoRepository.findByProyectoId(proyectoId));
            crearResponsablesProyecto(proyectoActualizado, request.getResponsables());
        }

        // Sincronizar objetivos (solo si se envían explícitamente)
        if (request.getObjetivos() != null) {
            proyectoObjetivoRepository.deleteAll(proyectoObjetivoRepository.findByProyectoId(proyectoId));
            proyectoObjetivoRepository.flush();
            crearObjetivosProyecto(proyectoActualizado, request.getObjetivos());
        }

        // Sincronizar metas (solo si se envían explícitamente)
        if (request.getMetas() != null) {
            proyectoMetaRepository.deleteAll(proyectoMetaRepository.findByProyectoId(proyectoId));
            proyectoMetaRepository.flush();
            crearMetasProyecto(proyectoActualizado, request.getMetas());
        }

        // Sincronizar presupuesto (solo si se envían explícitamente)
        if (request.getPresupuesto() != null) {
            proyectoPresupuestoRepository.deleteAll(proyectoPresupuestoRepository.findByProyectoId(proyectoId));
            proyectoPresupuestoRepository.flush();
            crearPresupuestoProyecto(proyectoActualizado, request.getPresupuesto());
        }

        // ✅ CORRECCIÓN CLAVE: Solo sincronizar cronograma si se envía Y no está vacío
        // Si el frontend NO incluye cronogramaActividades, NO tocar las actividades existentes
        if (request.getCronogramaActividades() != null && !request.getCronogramaActividades().isEmpty()) {
            cronogramaActividadProyectoRepository.deleteAll(
                    cronogramaActividadProyectoRepository.findByProyectoIdOrderByFechaInicioAsc(proyectoId)
            );
            cronogramaActividadProyectoRepository.flush();
            crearCronogramaProyecto(proyectoActualizado, request.getCronogramaActividades());
        }

        return obtenerDetalleCompleto(proyectoActualizado.getId());
    }

    // =====================================================
    // ACTUALIZAR DATOS GENERALES DEL PROYECTO
    // =====================================================
    private void actualizarDatosGeneralesProyecto(Proyecto proyecto, ProyectoRequest request) {
        EstadoProyecto estadoProyecto = estadoProyectoRepository.findById(request.getEstadoProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado proyecto no encontrado"));
        Prioridad prioridad = prioridadRepository.findById(request.getPrioridadId())
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));
        Categoria categoria = request.getCategoriaId() != null ? categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada")) : null;

        proyecto.setEstadoProyecto(estadoProyecto);
        proyecto.setPrioridad(prioridad);
        proyecto.setCategoria(categoria);
        proyecto.setNombre(request.getNombre());
        proyecto.setDescripcion(request.getDescripcion());
        proyecto.setPresupuestoEstimado(request.getPresupuestoEstimado());
        proyecto.setFechaInicio(request.getFechaInicio());
        proyecto.setFechaFin(request.getFechaFin());
        proyecto.setObservaciones(request.getObservaciones());
        proyecto.setLatitud(request.getLatitud());
        proyecto.setLongitud(request.getLongitud());
        proyecto.setUbicacion(request.getUbicacion());
        proyecto.setTipoProyecto(request.getTipoProyecto());
        proyecto.setPorcentajeAvance(request.getPorcentajeAvance());
        if (request.getEstado() != null) proyecto.setEstado(request.getEstado());
    }

    // =====================================================
    // MÉTODOS AUXILIARES DE CREACIÓN
    // =====================================================
    private void crearReportesAsociados(Proyecto proyecto, List<Long> reporteIds) {
        if (reporteIds == null) return;
        reporteIds.stream().filter(id -> id != null).distinct().forEach(reporteId -> {
            Reporte reporte = reporteRepository.findById(reporteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + reporteId));
            ProyectoReporte proyectoReporte = new ProyectoReporte();
            proyectoReporte.setProyecto(proyecto);
            proyectoReporte.setReporte(reporte);
            proyectoReporteRepository.save(proyectoReporte);
        });
    }

    private void crearMiembrosProyecto(Proyecto proyecto, List<ProyectoMiembroRequest> miembros) {
        for (ProyectoMiembroRequest miembroRequest : miembros) {
            Usuario usuario = usuarioRepository.findById(miembroRequest.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario miembro no encontrado"));
            ProyectoMiembro proyectoMiembro = new ProyectoMiembro();
            proyectoMiembro.setProyecto(proyecto);
            proyectoMiembro.setUsuario(usuario);
            proyectoMiembro.setRolEnProyecto(miembroRequest.getRolEnProyecto());
            proyectoMiembro.setEstado(true);
            proyectoMiembroRepository.save(proyectoMiembro);
        }
    }

    private void crearResponsablesProyecto(Proyecto proyecto, List<ResponsableProyectoRequest> responsables) {
        for (ResponsableProyectoRequest responsableRequest : responsables) {
            Usuario usuarioResponsable = usuarioRepository.findById(responsableRequest.getUsuarioResponsableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario responsable no encontrado"));
            ResponsableProyecto responsable = new ResponsableProyecto();
            responsable.setProyecto(proyecto);
            responsable.setUsuarioResponsable(usuarioResponsable);
            responsable.setTitulo(responsableRequest.getTitulo());
            responsable.setDescripcion(responsableRequest.getDescripcion());
            responsable.setEstado(responsableRequest.getEstado());
            responsable.setPrioridad(responsableRequest.getPrioridad());
            responsable.setFechaInicio(responsableRequest.getFechaInicio());
            responsable.setFechaLimite(responsableRequest.getFechaLimite());
            responsable.setPorcentajeAvance(responsableRequest.getPorcentajeAvance());
            responsableProyectoRepository.save(responsable);
        }
    }

    private void crearObjetivosProyecto(Proyecto proyecto, List<ProyectoObjetivoRequest> objetivos) {
        for (ProyectoObjetivoRequest objetivoRequest : objetivos) {
            ProyectoObjetivo objetivo = new ProyectoObjetivo();
            objetivo.setProyecto(proyecto);
            objetivo.setDescripcion(objetivoRequest.getDescripcion());
            proyectoObjetivoRepository.save(objetivo);
        }
    }

    private void crearMetasProyecto(Proyecto proyecto, List<ProyectoMetaRequest> metas) {
        for (ProyectoMetaRequest metaRequest : metas) {
            ProyectoMeta meta = new ProyectoMeta();
            meta.setProyecto(proyecto);
            meta.setDescripcion(metaRequest.getDescripcion());
            proyectoMetaRepository.save(meta);
        }
    }

    private void crearPresupuestoProyecto(Proyecto proyecto, List<ProyectoPresupuestoRequest> presupuestoItems) {
        for (ProyectoPresupuestoRequest presupuestoRequest : presupuestoItems) {
            ProyectoPresupuesto presupuesto = new ProyectoPresupuesto();
            presupuesto.setProyecto(proyecto);
            presupuesto.setRubro(presupuestoRequest.getRubro());
            presupuesto.setMonto(presupuestoRequest.getMonto());
            presupuesto.setObservaciones(presupuestoRequest.getObservaciones());
            proyectoPresupuestoRepository.save(presupuesto);
        }
    }

    private void crearCronogramaProyecto(Proyecto proyecto, List<CronogramaActividadProyectoRequest> actividades) {
        for (CronogramaActividadProyectoRequest actividadRequest : actividades) {
            CronogramaActividadProyecto actividad = new CronogramaActividadProyecto();
            actividad.setProyecto(proyecto);
            if (actividadRequest.getResponsableId() != null) {
                ResponsableProyecto responsable = responsableProyectoRepository.findById(actividadRequest.getResponsableId())
                        .orElseThrow(() -> new ResourceNotFoundException("Responsable no encontrado"));
                actividad.setResponsable(responsable);
            }
            actividad.setNombre(actividadRequest.getNombre());
            actividad.setDescripcion(actividadRequest.getDescripcion());
            actividad.setEstado(actividadRequest.getEstado());
            actividad.setFechaInicio(actividadRequest.getFechaInicio());
            actividad.setFechaFin(actividadRequest.getFechaFin());
            actividad.setUrlEvidencia(actividadRequest.getUrlEvidencia());
            actividad.setObservaciones(actividadRequest.getObservaciones());
            cronogramaActividadProyectoRepository.save(actividad);
        }
    }

    // =====================================================
    // ELIMINAR PROYECTO (LÓGICO)
    // =====================================================
    @Override
    @Transactional
    public void eliminarProyecto(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        proyecto.setEliminado(true);
        proyectoRepository.save(proyecto);
    }

    // =====================================================
    // FILTROS
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarPorEstadoProyecto(Long estadoProyectoId) {
        return proyectoRepository.findByEstadoProyectoId(estadoProyectoId)
                .stream().map(ProyectoMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarPorPrioridad(Long prioridadId) {
        return proyectoRepository.findByPrioridadId(prioridadId)
                .stream().map(ProyectoMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarPorCategoria(Long categoriaId) {
        return proyectoRepository.findByCategoriaId(categoriaId)
                .stream().map(ProyectoMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarPorUsuario(Long usuarioId) {
        return proyectoRepository.findByCreadoPorId(usuarioId)
                .stream().map(ProyectoMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> buscarPorNombre(String nombre) {
        return proyectoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream().map(ProyectoMapper::toResponse).toList();
    }

    // =====================================================
    // CREAR PROYECTO BASE (MÉTODO PRIVADO)
    // =====================================================
    private Proyecto crearProyectoBase(ProyectoRequest request, String correoUsuario) {
        EstadoProyecto estadoProyecto = estadoProyectoRepository.findById(request.getEstadoProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado proyecto no encontrado"));
        Prioridad prioridad = prioridadRepository.findById(request.getPrioridadId())
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));
        Categoria categoria = request.getCategoriaId() != null ? categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada")) : null;
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Proyecto proyecto = Proyecto.builder()
                .estadoProyecto(estadoProyecto)
                .prioridad(prioridad)
                .categoria(categoria)
                .creadoPor(usuario)
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .presupuestoEstimado(request.getPresupuestoEstimado())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .observaciones(request.getObservaciones())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .ubicacion(request.getUbicacion())
                .tipoProyecto(request.getTipoProyecto())
                .porcentajeAvance(request.getPorcentajeAvance())
                .estado(true)
                .eliminado(false)
                .build();

        return proyectoRepository.save(proyecto);
    }

    // =====================================================
    // CONVERTIR REPORTES EN PROYECTO
    // =====================================================
    @Override
    @Transactional
    public ProyectoDetalleResponse convertirReportesEnProyecto(ProyectoCompletoRequest request, String correoUsuario) {
        if (request == null || request.getProyecto() == null) {
            throw new IllegalArgumentException("Los datos del proyecto son obligatorios");
        }
        if (request.getReporteIds() == null || request.getReporteIds().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un reporte para convertir en proyecto");
        }

        EstadoReporte estadoConvertido = estadoReporteRepository.findByNombreIgnoreCase("Convertido en proyecto")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Convertido en proyecto' no encontrado"));

        // Validar reportes antes de crear
        request.getReporteIds().stream().filter(reporteId -> reporteId != null).distinct().forEach(reporteId -> {
            Reporte reporte = reporteRepository.findById(reporteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + reporteId));
            if (reporte.getEliminado() != null && reporte.getEliminado()) {
                throw new IllegalArgumentException("El reporte con ID " + reporteId + " está eliminado");
            }
            if (reporte.getEstadoReporte() != null && "Convertido en proyecto".equalsIgnoreCase(reporte.getEstadoReporte().getNombre())) {
                throw new IllegalArgumentException("El reporte con ID " + reporteId + " ya fue convertido en proyecto");
            }
        });

        // Crear proyecto completo
        ProyectoDetalleResponse proyectoCreado = crearProyectoCompleto(request, correoUsuario);

        // Actualizar estado de los reportes
        request.getReporteIds().stream().filter(reporteId -> reporteId != null).distinct().forEach(reporteId -> {
            Reporte reporte = reporteRepository.findById(reporteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + reporteId));
            reporte.setEstadoReporte(estadoConvertido);
            reporte.setObservacionesAdmin("Reporte convertido en proyecto ambiental");
            reporteRepository.save(reporte);
        });

        return obtenerDetalleCompleto(proyectoCreado.getProyecto().getId());
    }

    // En ProyectoServiceImpl.java

    @Override
    @Transactional
    public ProyectoResponse cambiarEstadoProyecto(Long id, Boolean estado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));

        proyecto.setEstado(estado);
        Proyecto actualizado = proyectoRepository.save(proyecto);

        return ProyectoMapper.toResponse(actualizado);
    }

    @Override
    @Transactional
    public ProyectoResponse actualizarEstadoProyecto(Long id, Long estadoProyectoId) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));

        EstadoProyecto estadoProyecto = estadoProyectoRepository.findById(estadoProyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de proyecto no encontrado con ID: " + estadoProyectoId));

        proyecto.setEstadoProyecto(estadoProyecto);
        Proyecto actualizado = proyectoRepository.save(proyecto);

        return ProyectoMapper.toResponse(actualizado);
    }
}