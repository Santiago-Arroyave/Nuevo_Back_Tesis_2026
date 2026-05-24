package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ReporteMapper;
import Back_Goblink_park.demo.dto.request.ReporteRequest;
import Back_Goblink_park.demo.dto.response.ReporteResponse;

import Back_Goblink_park.demo.entity.*;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.*;

import Back_Goblink_park.demo.service.interfaces.ReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

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
    public ReporteResponse crearReporte(
            ReporteRequest request
    ) {

        // =================================================
        // USUARIO JWT
        // =================================================

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

        // =================================================
        // CATEGORÍA
        // =================================================

        Categoria categoria =
                categoriaRepository
                        .findById(request.getCategoriaId())
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Categoría no encontrada"
                                )
                        );

        // =================================================
        // PRIORIDAD
        // =================================================

        Prioridad prioridad =
                prioridadRepository
                        .findById(request.getPrioridadId())
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Prioridad no encontrada"
                                )
                        );

        // =================================================
        // ESTADO DEFAULT
        // =================================================

        EstadoReporte estadoReporte =
                estadoReporteRepository
                        .findById(1L)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Estado reporte no encontrado"
                                )
                        );

        // =================================================
        // CREAR REPORTE
        // =================================================

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

        // =================================================
        // GUARDAR
        // =================================================

        Reporte reporteGuardado =
                reporteRepository.save(reporte);

        // =================================================
        // RESPONSE
        // =================================================

        return ReporteMapper.toResponse(
                reporteGuardado
        );
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @Override
    public List<ReporteResponse> listarReportes() {

        return reporteRepository
                .findByEliminadoFalse()
                .stream()
                .map(ReporteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
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
    // SOFT DELETE
    // =====================================================

    @Override
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
}