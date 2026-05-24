package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ComentarioReporteMapper;
import Back_Goblink_park.demo.dto.request.ComentarioReporteRequest;
import Back_Goblink_park.demo.dto.response.ComentarioReporteResponse;

import Back_Goblink_park.demo.entity.ComentarioReporte;
import Back_Goblink_park.demo.entity.Reporte;
import Back_Goblink_park.demo.entity.Usuario;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.ComentarioReporteRepository;
import Back_Goblink_park.demo.repository.ReporteRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;

import Back_Goblink_park.demo.service.interfaces.ComentarioReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioReporteServiceImpl
        implements ComentarioReporteService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ComentarioReporteRepository comentarioRepository;

    private final ReporteRepository reporteRepository;

    private final UsuarioRepository usuarioRepository;

    // =====================================================
    // CREAR COMENTARIO
    // =====================================================

    @Override
    public ComentarioReporteResponse crearComentario(
            ComentarioReporteRequest request,
            String correoUsuario
    ) {

        // =============================================
        // BUSCAR REPORTE
        // =============================================

        Reporte reporte = reporteRepository
                .findById(request.getReporteId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reporte no encontrado"
                        )
                );

        // =============================================
        // BUSCAR USUARIO AUTENTICADO
        // =============================================

        Usuario usuario = usuarioRepository
                .findByCorreo(correoUsuario)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        // =============================================
        // CREAR COMENTARIO
        // =============================================

        ComentarioReporte comentario =
                ComentarioReporte.builder()

                        .reporte(reporte)

                        .usuario(usuario)

                        .comentario(
                                request.getComentario()
                        )

                        .tipoComentario(
                                request.getTipoComentario()
                        )

                        .build();

        // =============================================
        // GUARDAR
        // =============================================

        ComentarioReporte guardado =
                comentarioRepository.save(comentario);

        // =============================================
        // RESPONSE
        // =============================================

        return ComentarioReporteMapper
                .toResponse(guardado);
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @Override
    public List<ComentarioReporteResponse> listarComentarios() {

        return comentarioRepository.findAll()

                .stream()

                .map(
                        ComentarioReporteMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public ComentarioReporteResponse obtenerComentario(
            Long id
    ) {

        ComentarioReporte comentario =
                comentarioRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Comentario no encontrado"
                                )
                        );

        return ComentarioReporteMapper
                .toResponse(comentario);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public ComentarioReporteResponse actualizarComentario(
            Long id,
            ComentarioReporteRequest request
    ) {

        ComentarioReporte comentario =
                comentarioRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Comentario no encontrado"
                                )
                        );

        comentario.setComentario(
                request.getComentario()
        );

        comentario.setTipoComentario(
                request.getTipoComentario()
        );

        ComentarioReporte actualizado =
                comentarioRepository.save(comentario);

        return ComentarioReporteMapper
                .toResponse(actualizado);
    }

    // =====================================================
    // LISTAR POR REPORTE
    // =====================================================

    @Override
    public List<ComentarioReporteResponse> listarPorReporte(
            Long reporteId
    ) {

        return comentarioRepository

                .findByReporteIdOrderByFechaComentarioAsc(
                        reporteId
                )

                .stream()

                .map(
                        ComentarioReporteMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR POR USUARIO
    // =====================================================

    @Override
    public List<ComentarioReporteResponse> listarPorUsuario(
            Long usuarioId
    ) {

        return comentarioRepository

                .findByUsuarioId(usuarioId)

                .stream()

                .map(
                        ComentarioReporteMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    public void eliminarComentario(Long id) {

        ComentarioReporte comentario =
                comentarioRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Comentario no encontrado"
                                )
                        );

        comentarioRepository.delete(comentario);
    }
}