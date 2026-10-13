package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.*;
import Back_Goblink_park.demo.dto.request.*;
import Back_Goblink_park.demo.dto.response.ProyectoDetalleResponse;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;
import Back_Goblink_park.demo.entity.*;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.*;
import Back_Goblink_park.demo.service.interfaces.ProyectoService;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final ProyectoObjetivoRepository proyectoObjetivoRepository;
    private final ProyectoMetaRepository proyectoMetaRepository;
    private final ProyectoPresupuestoRepository proyectoPresupuestoRepository;
    private final CronogramaActividadProyectoRepository cronogramaActividadProyectoRepository;
    private final SeguimientoProyectoRepository seguimientoProyectoRepository;
    private final SolicitudProyectoRepository solicitudProyectoRepository;
    private final Back_Goblink_park.demo.service.interfaces.CronogramaActividadProyectoService cronogramaActividadProyectoService;


    // =====================================================
    // CREAR PROYECTO SIMPLE
    // =====================================================
    @Override
    @Transactional
    public ProyectoResponse crearProyecto(ProyectoRequest request, String correoUsuario) {
        List<Long> idsReportes = obtenerIdsReportesDeProyectoRequest(request);
        if (idsReportes != null && !idsReportes.isEmpty()) {
            validarReportesParaVincular(idsReportes, null);
        }

        Proyecto proyectoGuardado = crearProyectoBase(request, correoUsuario);

        if (idsReportes != null && !idsReportes.isEmpty()) {
            vincularReportesAlProyecto(proyectoGuardado, idsReportes);
        }

        return ProyectoMapper.toResponse(proyectoGuardado);
    }

    // =====================================================
    // CREAR PROYECTO COMPLETO (Con soporte para convertir reportes)
    // =====================================================
    @Override
    @Transactional
    public ProyectoDetalleResponse crearProyectoCompleto(ProyectoCompletoRequest request, String correoUsuario) {
        if (request == null || request.getProyecto() == null) {
            throw new IllegalArgumentException("Los datos del proyecto son obligatorios");
        }

        List<Long> idsReportes = obtenerIdsReportesDeCompletoRequest(request);

        // Validar reportes ANTES de persistir el proyecto
        if (idsReportes != null && !idsReportes.isEmpty()) {
            validarReportesParaVincular(idsReportes, null);
        }

        Proyecto proyectoGuardado = crearProyectoBase(request.getProyecto(), correoUsuario);

        // Asociar reportes y ACTUALIZAR SU ESTADO si se enviaron
        if (idsReportes != null && !idsReportes.isEmpty()) {
            vincularReportesAlProyecto(proyectoGuardado, idsReportes);
        }

        // Crear miembros
        if (request.getMiembros() != null && !request.getMiembros().isEmpty()) {
            crearMiembrosProyecto(proyectoGuardado, request.getMiembros());
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

        cronogramaActividadProyectoService.recalcularAvanceProyecto(proyectoGuardado.getId());

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
            if (query != null && !Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                root.fetch("estadoProyecto", JoinType.LEFT);
                root.fetch("prioridad", JoinType.LEFT);
                root.fetch("categoria", JoinType.LEFT);
                root.fetch("creadoPor", JoinType.LEFT);
                query.distinct(true);
            }

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

        ProyectoResponse proyectoResponse = ProyectoMapper.toResponse(proyecto);
        Long countMiembros = proyectoMiembroRepository.countByProyectoIdAndEstadoTrue(proyectoId);
        proyectoResponse.setCantidadParticipantes(countMiembros != null ? countMiembros.intValue() : 0);

        return ProyectoDetalleResponse.builder()
                .proyecto(proyectoResponse)
                .reportesAsociados(proyectoReporteRepository.findByProyectoId(proyectoId).stream().map(ProyectoReporteMapper::toResponse).toList())
                .miembros(proyectoMiembroRepository.findByProyectoId(proyectoId).stream().map(ProyectoMiembroMapper::toResponse).toList())
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

        Proyecto guardado = proyectoRepository.save(proyecto);
        sincronizarReportesPorEstadoProyecto(guardado);
        return ProyectoMapper.toResponse(guardado);
    }

    // =====================================================
// ACTUALIZAR PROYECTO COMPLETO - ✅ CORREGIDO
// =====================================================
    @Override
    @Transactional
    public ProyectoDetalleResponse actualizarProyectoCompleto(Long proyectoId, ProyectoCompletoRequest request) {
        if (request == null || request.getProyecto() == null) {
            throw new IllegalArgumentException("Los datos del proyecto son obligatorios");
        }

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        List<Long> idsReportes = obtenerIdsReportesDeCompletoRequest(request);
        if (idsReportes != null && !idsReportes.isEmpty()) {
            validarReportesParaVincular(idsReportes, proyectoId);
        }

        // Actualizar datos generales
        actualizarDatosGeneralesProyecto(proyecto, request.getProyecto());
        Proyecto proyectoActualizado = proyectoRepository.save(proyecto);

        // Sincronizar reportes (solo si se envían explícitamente)
        if (idsReportes != null) {
            proyectoReporteRepository.eliminarPorProyectoId(proyectoId);
            proyectoReporteRepository.flush();
            vincularReportesAlProyecto(proyectoActualizado, idsReportes);
        }

        // ✅ CORRECCIÓN CLAVE: Antes de eliminar miembros, eliminar las actividades que los referencian
        if (request.getMiembros() != null) {
            // Primero, eliminar todas las actividades del proyecto
            List<CronogramaActividadProyecto> actividades =
                    cronogramaActividadProyectoRepository.findByProyectoIdOrderByFechaInicioAsc(proyectoId);
            if (!actividades.isEmpty()) {
                cronogramaActividadProyectoRepository.deleteAll(actividades);
                cronogramaActividadProyectoRepository.flush();
            }

            // Luego, eliminar los miembros
            proyectoMiembroRepository.deleteAll(proyectoMiembroRepository.findByProyectoId(proyectoId));
            proyectoMiembroRepository.flush();

            // Finalmente, crear los nuevos miembros
            crearMiembrosProyecto(proyectoActualizado, request.getMiembros());
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

        // ✅ CORRECCIÓN: Solo sincronizar cronograma si se envía Y no está vacío
        // Si ya eliminamos las actividades arriba, no las volvemos a eliminar
        if (request.getCronogramaActividades() != null && !request.getCronogramaActividades().isEmpty()) {
            // Si no eliminamos las actividades arriba (porque no se enviaron miembros), eliminarlas aquí
            if (request.getMiembros() == null) {
                cronogramaActividadProyectoRepository.deleteAll(
                        cronogramaActividadProyectoRepository.findByProyectoIdOrderByFechaInicioAsc(proyectoId)
                );
                cronogramaActividadProyectoRepository.flush();
            }
            crearCronogramaProyecto(proyectoActualizado, request.getCronogramaActividades());
        }

        cronogramaActividadProyectoService.recalcularAvanceProyecto(proyectoActualizado.getId());

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
    // VALIDACIÓN Y VINCULACIÓN DE REPORTES ASOCIADOS
    // =====================================================
    private List<Long> obtenerIdsReportesDeProyectoRequest(ProyectoRequest request) {
        if (request == null) return null;
        if (request.getReporteIds() != null && !request.getReporteIds().isEmpty()) {
            return request.getReporteIds();
        }
        return request.getReportesIds();
    }

    private List<Long> obtenerIdsReportesDeCompletoRequest(ProyectoCompletoRequest request) {
        if (request == null) return null;
        if (request.getReporteIds() != null) {
            return request.getReporteIds();
        }
        if (request.getReportesIds() != null) {
            return request.getReportesIds();
        }
        return obtenerIdsReportesDeProyectoRequest(request.getProyecto());
    }

    private void validarReportesParaVincular(List<Long> reporteIds, Long proyectoActualId) {
        if (reporteIds == null || reporteIds.isEmpty()) return;

        for (Long reporteId : reporteIds.stream().filter(id -> id != null).distinct().toList()) {
            Reporte reporte = reporteRepository.findById(reporteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + reporteId));

            if (Boolean.TRUE.equals(reporte.getEliminado())) {
                throw new BusinessException("El reporte '" + reporte.getTitulo() + "' está eliminado y no puede vincularse a un proyecto.");
            }

            if (esReporteResuelto(reporte)) {
                throw new BusinessException("El reporte '" + reporte.getTitulo() + "' ya ha sido resuelto y no puede vincularse a un nuevo proyecto.");
            }

            Proyecto proyectoExistente = obtenerProyectoVinculadoActivo(reporte, proyectoActualId);
            if (proyectoExistente != null) {
                throw new BusinessException("El reporte '" + reporte.getTitulo() + "' ya se encuentra vinculado al proyecto #" + proyectoExistente.getId());
            }

            if (proyectoActualId == null && esReporteConvertidoEnProyecto(reporte)) {
                throw new BusinessException("El reporte '" + reporte.getTitulo() + "' ya se encuentra vinculado a un proyecto.");
            }
        }
    }

    private boolean esReporteResuelto(Reporte reporte) {
        if (reporte == null || reporte.getEstadoReporte() == null || reporte.getEstadoReporte().getNombre() == null) {
            return false;
        }
        String n = reporte.getEstadoReporte().getNombre().trim().toLowerCase();
        return n.equals("resuelto") || n.equals("resuelta") || n.contains("resuelto");
    }

    private boolean esReporteConvertidoEnProyecto(Reporte reporte) {
        if (reporte == null || reporte.getEstadoReporte() == null || reporte.getEstadoReporte().getNombre() == null) {
            return false;
        }
        String n = reporte.getEstadoReporte().getNombre().trim().toLowerCase();
        return n.contains("convertido");
    }

    private Proyecto obtenerProyectoVinculadoActivo(Reporte reporte, Long proyectoActualId) {
        if (reporte == null || reporte.getId() == null) return null;
        List<ProyectoReporte> relaciones = proyectoReporteRepository.findByReporteId(reporte.getId());
        if (relaciones != null) {
            for (ProyectoReporte pr : relaciones) {
                if (pr != null && pr.getProyecto() != null && !Boolean.TRUE.equals(pr.getProyecto().getEliminado())) {
                    if (proyectoActualId == null || !proyectoActualId.equals(pr.getProyecto().getId())) {
                        return pr.getProyecto();
                    }
                }
            }
        }
        return null;
    }

    private void vincularReportesAlProyecto(Proyecto proyecto, List<Long> reporteIds) {
        if (reporteIds == null || reporteIds.isEmpty()) return;

        EstadoReporte estadoConvertido = estadoReporteRepository.findByNombreIgnoreCase("Convertido en proyecto")
                .orElseGet(() -> estadoReporteRepository.findById(7L)
                        .orElseThrow(() -> new ResourceNotFoundException("Estado 'Convertido en proyecto' no encontrado")));

        for (Long reporteId : reporteIds.stream().filter(id -> id != null).distinct().toList()) {
            Reporte reporte = reporteRepository.findById(reporteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + reporteId));

            ProyectoReporte proyectoReporte = new ProyectoReporte();
            proyectoReporte.setProyecto(proyecto);
            proyectoReporte.setReporte(reporte);
            proyectoReporteRepository.save(proyectoReporte);

            reporte.setEstadoReporte(estadoConvertido);
            reporte.setObservacionesAdmin("Reporte convertido en proyecto ambiental");
            reporteRepository.save(reporte);
        }
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



    // =====================================================
    // CREAR CRONOGRAMA PROYECTO - ✅ CORREGIDO
    // =====================================================
    private void crearCronogramaProyecto(Proyecto proyecto, List<CronogramaActividadProyectoRequest> actividades) {
        for (CronogramaActividadProyectoRequest actividadRequest : actividades) {
            CronogramaActividadProyecto actividad = new CronogramaActividadProyecto();
            actividad.setProyecto(proyecto);

            // ✅ 1. Intentar buscar por proyectoMiembroId (si viene)
            if (actividadRequest.getProyectoMiembroId() != null) {
                ProyectoMiembro proyectoMiembro = proyectoMiembroRepository
                        .findById(actividadRequest.getProyectoMiembroId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Miembro del proyecto no encontrado con ID: " + actividadRequest.getProyectoMiembroId()
                        ));

                if (!proyectoMiembro.getProyecto().getId().equals(proyecto.getId())) {
                    throw new IllegalArgumentException("El miembro seleccionado no pertenece a este proyecto");
                }
                actividad.setProyectoMiembro(proyectoMiembro);
            }
            // ✅ 2. Si no viene proyectoMiembroId, buscar por usuarioId (lo que envía el frontend)
            else if (actividadRequest.getUsuarioId() != null) {
                ProyectoMiembro proyectoMiembro = proyectoMiembroRepository
                        .findByProyectoIdAndUsuarioId(proyecto.getId(), actividadRequest.getUsuarioId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "El usuario con ID " + actividadRequest.getUsuarioId() + " no es miembro de este proyecto"
                        ));

                actividad.setProyectoMiembro(proyectoMiembro);
            }

            // Asignar el resto de campos
            actividad.setNombre(actividadRequest.getNombre());
            actividad.setDescripcion(actividadRequest.getDescripcion());
            actividad.setEstado(actividadRequest.getEstado() != null ? actividadRequest.getEstado() : "pendiente");
            actividad.setPrioridad(actividadRequest.getPrioridad() != null ? actividadRequest.getPrioridad() : "media");
            actividad.setPorcentajeAvance(actividadRequest.getPorcentajeAvance() != null ? actividadRequest.getPorcentajeAvance() : BigDecimal.ZERO);
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
        String correoSanitizado = (correoUsuario != null) ? correoUsuario.trim().toLowerCase() : "";
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoSanitizado)
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
        List<Long> idsReportes = obtenerIdsReportesDeCompletoRequest(request);
        if (idsReportes == null || idsReportes.isEmpty()) {
            throw new BusinessException("Debe seleccionar al menos un reporte para convertir en proyecto");
        }

        // Validar reportes ANTES de crear (lanza BusinessException si está RESUELTO o ya tiene proyecto vinculado)
        validarReportesParaVincular(idsReportes, null);

        // Crear proyecto completo (que también asocia y actualiza los reportes)
        ProyectoDetalleResponse proyectoCreado = crearProyectoCompleto(request, correoUsuario);

        return obtenerDetalleCompleto(proyectoCreado.getProyecto().getId());
    }

    // =====================================================
    // CAMBIAR ESTADO PROYECTO
    // =====================================================
    @Override
    @Transactional
    public ProyectoResponse cambiarEstadoProyecto(Long id, Boolean estado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));

        proyecto.setEstado(estado);
        Proyecto actualizado = proyectoRepository.save(proyecto);

        return ProyectoMapper.toResponse(actualizado);
    }

    // =====================================================
    // ACTUALIZAR ESTADO PROYECTO
    // =====================================================
    @Override
    @Transactional
    public ProyectoResponse actualizarEstadoProyecto(Long id, Long estadoProyectoId) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));

        EstadoProyecto estadoProyecto = estadoProyectoRepository.findById(estadoProyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de proyecto no encontrado con ID: " + estadoProyectoId));

        proyecto.setEstadoProyecto(estadoProyecto);
        if (esEstadoProyectoFinalizado(estadoProyecto)) {
            proyecto.setPorcentajeAvance(100.0);
        }
        Proyecto actualizado = proyectoRepository.save(proyecto);
        sincronizarReportesPorEstadoProyecto(actualizado);

        return ProyectoMapper.toResponse(actualizado);
    }

    private boolean esEstadoProyectoFinalizado(EstadoProyecto ep) {
        if (ep == null) return false;
        if (Long.valueOf(4L).equals(ep.getId())) return true;
        if (ep.getNombre() != null) {
            String n = ep.getNombre().trim().toLowerCase();
            return n.contains("finalizado") || n.contains("completado");
        }
        return false;
    }

    private void sincronizarReportesPorEstadoProyecto(Proyecto proyecto) {
        if (proyecto == null || proyecto.getId() == null) return;
        boolean finalizado = esEstadoProyectoFinalizado(proyecto.getEstadoProyecto())
                || (proyecto.getPorcentajeAvance() != null && proyecto.getPorcentajeAvance() >= 100.0);

        List<ProyectoReporte> relaciones = proyectoReporteRepository.findByProyectoId(proyecto.getId());
        if (relaciones == null || relaciones.isEmpty()) return;

        EstadoReporte estadoObjetivo = finalizado
                ? estadoReporteRepository.findByNombreIgnoreCase("Resuelto")
                        .orElseGet(() -> estadoReporteRepository.findById(3L).orElse(null))
                : estadoReporteRepository.findByNombreIgnoreCase("Convertido en proyecto")
                        .orElseGet(() -> estadoReporteRepository.findById(7L).orElse(null));

        if (estadoObjetivo == null) return;

        for (ProyectoReporte rel : relaciones) {
            Reporte rep = rel.getReporte();
            if (rep != null && !Boolean.TRUE.equals(rep.getEliminado())) {
                if (rep.getEstadoReporte() == null || rep.getEstadoReporte().getId() != estadoObjetivo.getId()) {
                    rep.setEstadoReporte(estadoObjetivo);
                    if (finalizado) {
                        rep.setObservacionesAdmin("Reporte resuelto tras finalización del proyecto ambiental asociado");
                    }
                    reporteRepository.save(rep);
                }
            }
        }
    }

    // =====================================================
    // MÓDULO MÓVIL / EXPLORACIÓN (OPTIMIZADO)
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarMisProyectos(String correoUsuario) {
        String correoSanitizado = (correoUsuario != null) ? correoUsuario.trim().toLowerCase() : "";
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoSanitizado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correoUsuario));

        return listarProyectosDondeParticipaUsuario(usuario.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarProyectosDondeParticipaUsuario(Long usuarioId) {
        List<Proyecto> proyectos = proyectoRepository.findProyectosByUsuarioId(usuarioId);
        if (proyectos == null || proyectos.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        List<Long> proyectoIds = proyectos.stream().map(Proyecto::getId).toList();
        java.util.Map<Long, Integer> participantesMap = obtenerMapaParticipantes(proyectoIds);

        return proyectos.stream()
                .map(p -> {
                    ProyectoResponse dto = ProyectoMapper.toResponse(p);
                    dto.setCantidadParticipantes(participantesMap.getOrDefault(p.getId(), 0));
                    return dto;
                })
                .toList();
    }

    private java.util.Map<Long, Integer> obtenerMapaParticipantes(List<Long> proyectoIds) {
        if (proyectoIds == null || proyectoIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        List<Object[]> counts = proyectoRepository.countParticipantesPorProyectos(proyectoIds);
        java.util.Map<Long, Integer> map = new java.util.HashMap<>();
        for (Object[] row : counts) {
            Long projId = (Long) row[0];
            Number count = (Number) row[1];
            map.put(projId, count != null ? count.intValue() : 0);
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> explorarProyectos(String correoUsuario, String categoriaFiltro) {
        Set<Long> proyectosExcluidos = new java.util.HashSet<>();

        if (correoUsuario != null && !correoUsuario.isBlank()) {
            String correoSanitizado = correoUsuario.trim().toLowerCase();
            usuarioRepository.findByCorreoIgnoreCase(correoSanitizado).ifPresent(usuario -> {
                // Proyectos donde ya es miembro activo
                List<ProyectoMiembro> membresias = proyectoMiembroRepository.findByUsuarioIdAndEstadoTrue(usuario.getId());
                membresias.stream()
                        .filter(pm -> pm.getProyecto() != null)
                        .map(pm -> pm.getProyecto().getId())
                        .forEach(proyectosExcluidos::add);

                // Proyectos donde tiene solicitud aprobada / aceptada
                List<SolicitudProyecto> solicitudes = solicitudProyectoRepository.findByUsuarioId(usuario.getId());
                solicitudes.stream()
                        .filter(s -> ("aceptada".equalsIgnoreCase(s.getEstado()) || "aprobada".equalsIgnoreCase(s.getEstado())) && s.getProyecto() != null)
                        .map(s -> s.getProyecto().getId())
                        .forEach(proyectosExcluidos::add);
            });
        }

        List<Proyecto> proyectosActivos = proyectoRepository.findByEstadoTrueAndEliminadoFalse();

        List<Proyecto> proyectosFiltrados = proyectosActivos.stream()
                .filter(p -> !proyectosExcluidos.contains(p.getId()))
                .filter(p -> {
                    if (categoriaFiltro == null || categoriaFiltro.isBlank()) {
                        return true;
                    }
                    String catFiltro = categoriaFiltro.trim().toLowerCase();
                    if (p.getCategoria() != null) {
                        if (p.getCategoria().getNombre() != null && p.getCategoria().getNombre().toLowerCase().contains(catFiltro)) {
                            return true;
                        }
                        if (String.valueOf(p.getCategoria().getId()).equals(catFiltro)) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();

        List<Long> ids = proyectosFiltrados.stream().map(Proyecto::getId).toList();
        java.util.Map<Long, Integer> participantesMap = obtenerMapaParticipantes(ids);

        return proyectosFiltrados.stream()
                .map(p -> {
                    ProyectoResponse dto = ProyectoMapper.toResponse(p);
                    dto.setCantidadParticipantes(participantesMap.getOrDefault(p.getId(), 0));
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoDetalleResponse obtenerResumenCompleto(Long proyectoId) {
        return obtenerDetalleCompleto(proyectoId);
    }
}