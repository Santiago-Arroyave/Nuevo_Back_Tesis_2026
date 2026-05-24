package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ProyectoMapper;

import Back_Goblink_park.demo.dto.request.ProyectoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;

import Back_Goblink_park.demo.entity.Categoria;
import Back_Goblink_park.demo.entity.EstadoProyecto;
import Back_Goblink_park.demo.entity.Prioridad;
import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.Usuario;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.CategoriaRepository;
import Back_Goblink_park.demo.repository.EstadoProyectoRepository;
import Back_Goblink_park.demo.repository.PrioridadRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;

import Back_Goblink_park.demo.service.interfaces.ProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl
        implements ProyectoService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ProyectoRepository proyectoRepository;

    private final EstadoProyectoRepository estadoProyectoRepository;

    private final PrioridadRepository prioridadRepository;

    private final CategoriaRepository categoriaRepository;

    private final UsuarioRepository usuarioRepository;

    // =====================================================
    // CREAR
    // =====================================================

    @Override
    public ProyectoResponse crearProyecto(
            ProyectoRequest request,
            String correoUsuario
    ) {

        // =============================================
        // ESTADO PROYECTO
        // =============================================

        EstadoProyecto estadoProyecto =
                estadoProyectoRepository
                        .findById(request.getEstadoProyectoId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estado proyecto no encontrado"
                                )
                        );

        // =============================================
        // PRIORIDAD
        // =============================================

        Prioridad prioridad =
                prioridadRepository
                        .findById(request.getPrioridadId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prioridad no encontrada"
                                )
                        );

        // =============================================
        // CATEGORÍA
        // =============================================

        Categoria categoria = null;

        if (request.getCategoriaId() != null) {

            categoria = categoriaRepository
                    .findById(request.getCategoriaId())

                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Categoría no encontrada"
                            )
                    );
        }

        // =============================================
        // USUARIO AUTENTICADO
        // =============================================

        Usuario usuario =
                usuarioRepository
                        .findByCorreo(correoUsuario)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuario no encontrado"
                                )
                        );

        // =============================================
        // CREAR ENTITY
        // =============================================

        Proyecto proyecto = Proyecto.builder()

                // =====================================
                // RELACIONES
                // =====================================

                .estadoProyecto(
                        estadoProyecto
                )

                .prioridad(
                        prioridad
                )

                .categoria(
                        categoria
                )

                .creadoPor(
                        usuario
                )

                // =====================================
                // INFORMACIÓN
                // =====================================

                .nombre(
                        request.getNombre()
                )

                .descripcion(
                        request.getDescripcion()
                )

                .presupuestoEstimado(
                        request.getPresupuestoEstimado()
                )

                .fechaInicio(
                        request.getFechaInicio()
                )

                .fechaFin(
                        request.getFechaFin()
                )

                .observaciones(
                        request.getObservaciones()
                )

                // =====================================
                // UBICACIÓN
                // =====================================

                .latitud(
                        request.getLatitud()
                )

                .longitud(
                        request.getLongitud()
                )

                .ubicacion(
                        request.getUbicacion()
                )

                // =====================================
                // EXTRA
                // =====================================

                .tipoProyecto(
                        request.getTipoProyecto()
                )

                .porcentajeAvance(
                        request.getPorcentajeAvance()
                )

                .estado(true)

                .eliminado(false)

                .build();

        // =============================================
        // GUARDAR
        // =============================================

        Proyecto guardado =
                proyectoRepository.save(proyecto);

        // =============================================
        // RESPONSE
        // =============================================

        return ProyectoMapper.toResponse(
                guardado
        );
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @Override
    public List<ProyectoResponse> listarProyectos() {

        return proyectoRepository

                .findByEliminadoFalse()

                .stream()

                .map(
                        ProyectoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public ProyectoResponse obtenerProyecto(
            Long id
    ) {

        Proyecto proyecto =
                proyectoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Proyecto no encontrado"
                                )
                        );

        return ProyectoMapper.toResponse(
                proyecto
        );
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public ProyectoResponse actualizarProyecto(
            Long id,
            ProyectoRequest request
    ) {

        Proyecto proyecto =
                proyectoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Proyecto no encontrado"
                                )
                        );

        // =============================================
        // ESTADO PROYECTO
        // =============================================

        EstadoProyecto estadoProyecto =
                estadoProyectoRepository
                        .findById(request.getEstadoProyectoId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estado proyecto no encontrado"
                                )
                        );

        // =============================================
        // PRIORIDAD
        // =============================================

        Prioridad prioridad =
                prioridadRepository
                        .findById(request.getPrioridadId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prioridad no encontrada"
                                )
                        );

        // =============================================
        // CATEGORÍA
        // =============================================

        Categoria categoria = null;

        if (request.getCategoriaId() != null) {

            categoria = categoriaRepository
                    .findById(request.getCategoriaId())

                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Categoría no encontrada"
                            )
                    );
        }

        // =============================================
        // ACTUALIZAR
        // =============================================

        proyecto.setEstadoProyecto(
                estadoProyecto
        );

        proyecto.setPrioridad(
                prioridad
        );

        proyecto.setCategoria(
                categoria
        );

        proyecto.setNombre(
                request.getNombre()
        );

        proyecto.setDescripcion(
                request.getDescripcion()
        );

        proyecto.setPresupuestoEstimado(
                request.getPresupuestoEstimado()
        );

        proyecto.setFechaInicio(
                request.getFechaInicio()
        );

        proyecto.setFechaFin(
                request.getFechaFin()
        );

        proyecto.setObservaciones(
                request.getObservaciones()
        );

        proyecto.setLatitud(
                request.getLatitud()
        );

        proyecto.setLongitud(
                request.getLongitud()
        );

        proyecto.setUbicacion(
                request.getUbicacion()
        );

        proyecto.setTipoProyecto(
                request.getTipoProyecto()
        );

        proyecto.setPorcentajeAvance(
                request.getPorcentajeAvance()
        );

        proyecto.setEstado(
                request.getEstado()
        );

        Proyecto actualizado =
                proyectoRepository.save(proyecto);

        return ProyectoMapper.toResponse(
                actualizado
        );
    }

    // =====================================================
    // ELIMINADO LÓGICO
    // =====================================================

    @Override
    public void eliminarProyecto(
            Long id
    ) {

        Proyecto proyecto =
                proyectoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Proyecto no encontrado"
                                )
                        );

        proyecto.setEliminado(true);

        proyectoRepository.save(proyecto);
    }

    // =====================================================
    // FILTRO ESTADO PROYECTO
    // =====================================================

    @Override
    public List<ProyectoResponse> listarPorEstadoProyecto(
            Long estadoProyectoId
    ) {

        return proyectoRepository

                .findByEstadoProyectoId(
                        estadoProyectoId
                )

                .stream()

                .map(
                        ProyectoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // FILTRO PRIORIDAD
    // =====================================================

    @Override
    public List<ProyectoResponse> listarPorPrioridad(
            Long prioridadId
    ) {

        return proyectoRepository

                .findByPrioridadId(
                        prioridadId
                )

                .stream()

                .map(
                        ProyectoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // FILTRO CATEGORÍA
    // =====================================================

    @Override
    public List<ProyectoResponse> listarPorCategoria(
            Long categoriaId
    ) {

        return proyectoRepository

                .findByCategoriaId(
                        categoriaId
                )

                .stream()

                .map(
                        ProyectoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // FILTRO USUARIO
    // =====================================================

    @Override
    public List<ProyectoResponse> listarPorUsuario(
            Long usuarioId
    ) {

        return proyectoRepository

                .findByCreadoPorId(usuarioId)

                .stream()

                .map(
                        ProyectoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // BUSCAR NOMBRE
    // =====================================================

    @Override
    public List<ProyectoResponse> buscarPorNombre(
            String nombre
    ) {

        return proyectoRepository

                .findByNombreContainingIgnoreCase(
                        nombre
                )

                .stream()

                .map(
                        ProyectoMapper::toResponse
                )

                .toList();
    }
}