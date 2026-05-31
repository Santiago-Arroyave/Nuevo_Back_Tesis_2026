package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ReporteMapper;

import Back_Goblink_park.demo.dto.request.ActualizarEstadoReporteRequest;
import Back_Goblink_park.demo.dto.request.ActualizarPrioridadReporteRequest;
import Back_Goblink_park.demo.dto.request.ReporteRequest;

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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl
        implements ReporteService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ReporteRepository reporteRepository;

    private final UsuarioRepository usuarioRepository;

    private final CategoriaRepository categoriaRepository;

    private final PrioridadRepository prioridadRepository;

    private final EstadoReporteRepository estadoReporteRepository;

    // =====================================================
    // CREAR REPORTE
    // =====================================================

    @Override
    @Transactional
    public ReporteResponse crearReporte(
            ReporteRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String correo = authentication.getName();

        Usuario usuario =
                usuarioRepository
                        .findByCorreo(correo)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuario no encontrado"
                                )
                        );

        Categoria categoria =
                categoriaRepository
                        .findById(request.getCategoriaId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Categoría no encontrada"
                                )
                        );

        Prioridad prioridad =
                prioridadRepository
                        .findById(request.getPrioridadId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prioridad no encontrada"
                                )
                        );

        EstadoReporte estadoReporte =
                estadoReporteRepository
                        .findById(1L)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estado reporte no encontrado"
                                )
                        );

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

                .direccionReferencia(
                        request.getDireccionReferencia()
                )

                .fotoPrincipalUrl(
                        request.getFotoPrincipalUrl()
                )

                .fuente(request.getFuente())

                .build();

        Reporte reporteGuardado =
                reporteRepository.save(reporte);

        return ReporteMapper.toResponse(
                reporteGuardado
        );
    }

    // =====================================================
    // LISTAR SIMPLE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> listarReportes() {

        return reporteRepository
                .findByEliminadoFalse()
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

        Specification<Reporte> specification =
                construirSpecificationReportes(
                        search,
                        categoriaId,
                        estadoReporteId,
                        prioridadId,
                        false
                );

        Page<Reporte> reportes =
                reporteRepository.findAll(
                        specification,
                        pageable
                );

        return reportes.map(
                ReporteMapper::toResponse
        );
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

        Specification<Reporte> specification =
                construirSpecificationReportes(
                        search,
                        categoriaId,
                        estadoReporteId,
                        prioridadId,
                        true
                );

        return reporteRepository
                .findAll(
                        specification,
                        Sort.by("fechaReporte").descending()
                )
                .stream()
                .map(ReporteMapper::toMapaResponse)
                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ReporteResponse obtenerReporte(
            Long id
    ) {

        Reporte reporte =
                reporteRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reporte no encontrado"
                                )
                        );

        return ReporteMapper.toResponse(
                reporte
        );
    }

    // =====================================================
    // FILTRAR CATEGORÍA
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> listarPorCategoria(
            Long categoriaId
    ) {

        return reporteRepository
                .findByCategoriaIdAndEliminadoFalse(
                        categoriaId
                )
                .stream()
                .map(ReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // FILTRAR PRIORIDAD
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> listarPorPrioridad(
            Long prioridadId
    ) {

        return reporteRepository
                .findByPrioridadIdAndEliminadoFalse(
                        prioridadId
                )
                .stream()
                .map(ReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // FILTRAR ESTADO
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> listarPorEstado(
            Long estadoReporteId
    ) {

        return reporteRepository
                .findByEstadoReporteIdAndEliminadoFalse(
                        estadoReporteId
                )
                .stream()
                .map(ReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // ACTUALIZAR ESTADO
    // =====================================================

    @Override
    @Transactional
    public ReporteResponse actualizarEstado(
            Long reporteId,
            ActualizarEstadoReporteRequest request
    ) {

        Reporte reporte =
                reporteRepository.findById(reporteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reporte no encontrado"
                                )
                        );

        EstadoReporte estadoReporte =
                estadoReporteRepository.findById(
                                request.getEstadoReporteId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estado reporte no encontrado"
                                )
                        );

        reporte.setEstadoReporte(
                estadoReporte
        );

        reporte.setObservacionesAdmin(
                request.getObservacionesAdmin()
        );

        if (estadoReporte.getNombre()
                .equalsIgnoreCase("Validado")) {

            reporte.setFechaValidacion(
                    LocalDateTime.now()
            );
        }

        Reporte reporteActualizado =
                reporteRepository.save(reporte);

        return ReporteMapper.toResponse(
                reporteActualizado
        );
    }

    // =====================================================
    // ACTUALIZAR PRIORIDAD
    // =====================================================

    @Override
    @Transactional
    public ReporteResponse actualizarPrioridad(
            Long reporteId,
            ActualizarPrioridadReporteRequest request
    ) {

        Reporte reporte =
                reporteRepository.findById(reporteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reporte no encontrado"
                                )
                        );

        Prioridad prioridad =
                prioridadRepository.findById(
                                request.getPrioridadId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prioridad no encontrada"
                                )
                        );

        reporte.setPrioridad(
                prioridad
        );

        Reporte reporteActualizado =
                reporteRepository.save(reporte);

        return ReporteMapper.toResponse(
                reporteActualizado
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    @Transactional
    public void eliminarReporte(
            Long id
    ) {

        Reporte reporte =
                reporteRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reporte no encontrado"
                                )
                        );

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

            List<Predicate> predicates =
                    new ArrayList<>();

            // =============================================
            // SOLO NO ELIMINADOS
            // =============================================

            predicates.add(
                    criteriaBuilder.isFalse(
                            root.get("eliminado")
                    )
            );

            // =============================================
            // SOLO REPORTES CON COORDENADAS PARA MAPA
            // =============================================

            if (soloGeolocalizados) {

                predicates.add(
                        criteriaBuilder.isNotNull(
                                root.get("latitud")
                        )
                );

                predicates.add(
                        criteriaBuilder.isNotNull(
                                root.get("longitud")
                        )
                );
            }

            // =============================================
            // BUSCAR POR TÍTULO O DESCRIPCIÓN
            // =============================================

            if (search != null && !search.trim().isEmpty()) {

                String texto =
                        "%" + search.trim().toLowerCase() + "%";

                Predicate tituloLike =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("titulo")
                                ),
                                texto
                        );

                Predicate descripcionLike =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("descripcion")
                                ),
                                texto
                        );

                predicates.add(
                        criteriaBuilder.or(
                                tituloLike,
                                descripcionLike
                        )
                );
            }

            // =============================================
            // FILTRO CATEGORÍA
            // =============================================

            if (categoriaId != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("categoria")
                                        .get("id"),
                                categoriaId
                        )
                );
            }

            // =============================================
            // FILTRO ESTADO REPORTE
            // =============================================

            if (estadoReporteId != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("estadoReporte")
                                        .get("id"),
                                estadoReporteId
                        )
                );
            }

            // =============================================
            // FILTRO PRIORIDAD
            // =============================================

            if (prioridadId != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("prioridad")
                                        .get("id"),
                                prioridadId
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }


}