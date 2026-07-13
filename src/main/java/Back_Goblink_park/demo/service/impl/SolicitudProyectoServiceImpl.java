package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.SolicitudProyectoMapper;
import Back_Goblink_park.demo.dto.request.SolicitudProyectoRequest;
import Back_Goblink_park.demo.dto.request.SolicitudResponderRequest;
import Back_Goblink_park.demo.dto.response.SolicitudProyectoResponse;
import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoMiembro;
import Back_Goblink_park.demo.entity.SolicitudProyecto;
import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.ProyectoMiembroRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.SolicitudProyectoRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;
import Back_Goblink_park.demo.service.interfaces.SolicitudProyectoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudProyectoServiceImpl implements SolicitudProyectoService {

    private final SolicitudProyectoRepository solicitudRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoMiembroRepository proyectoMiembroRepository;

    // =====================================================
    // CREAR SOLICITUD (App Móvil)
    // =====================================================

    @Override
    @Transactional
    public SolicitudProyectoResponse crearSolicitud(
            SolicitudProyectoRequest request,
            String correoUsuario) {

        // Buscar usuario solicitante
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Buscar proyecto
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        // Verificar si ya es miembro del proyecto
        if (solicitudRepository.existeMiembroActivo(usuario.getId(), proyecto.getId())) {
            throw new IllegalArgumentException("Ya eres miembro de este proyecto");
        }

        // Verificar si ya existe una solicitud pendiente
        solicitudRepository
                .findByUsuarioIdAndProyectoIdAndEstado(usuario.getId(), proyecto.getId(), "pendiente")
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Ya tienes una solicitud pendiente para este proyecto");
                });

        // Crear solicitud
        SolicitudProyecto solicitud = SolicitudProyecto.builder()
                .usuario(usuario)
                .proyecto(proyecto)
                .estado("pendiente")
                .mensaje(request.getMensaje())
                .build();

        SolicitudProyecto guardada = solicitudRepository.save(solicitud);

        return SolicitudProyectoMapper.toResponse(guardada);
    }

    // =====================================================
    // LISTAR SOLICITUDES PENDIENTES DE UN PROYECTO (Admin)
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyectoResponse> listarSolicitudesPendientes(Long proyectoId) {
        return solicitudRepository
                .findByProyectoIdAndEstadoOrderByFechaSolicitudDesc(proyectoId, "pendiente")
                .stream()
                .map(SolicitudProyectoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // LISTAR TODAS LAS SOLICITUDES DE UN PROYECTO
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyectoResponse> listarSolicitudesPorProyecto(Long proyectoId) {
        return solicitudRepository
                .findByProyectoIdOrderByFechaSolicitudDesc(proyectoId)
                .stream()
                .map(SolicitudProyectoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // LISTAR TODAS LAS SOLICITUDES PENDIENTES (Dashboard)
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyectoResponse> listarTodasPendientes() {
        return solicitudRepository
                .findByEstadoOrderByFechaSolicitudDesc("pendiente")
                .stream()
                .map(SolicitudProyectoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER SOLICITUD POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public SolicitudProyectoResponse obtenerSolicitud(Long id) {
        SolicitudProyecto solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));
        return SolicitudProyectoMapper.toResponse(solicitud);
    }

    // =====================================================
    // RESPONDER SOLICITUD (Admin acepta o rechaza)
    // =====================================================

    @Override
    @Transactional
    public SolicitudProyectoResponse responderSolicitud(
            Long solicitudId,
            SolicitudResponderRequest request,
            String correoAdmin) {

        // Buscar solicitud
        SolicitudProyecto solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        // Verificar que esté pendiente
        if (!"pendiente".equalsIgnoreCase(solicitud.getEstado())) {
            throw new IllegalArgumentException("Esta solicitud ya fue respondida");
        }

        // Validar estado
        String nuevoEstado = request.getEstado();
        if (!"aceptada".equalsIgnoreCase(nuevoEstado) && !"rechazada".equalsIgnoreCase(nuevoEstado)) {
            throw new IllegalArgumentException("Estado inválido. Debe ser 'aceptada' o 'rechazada'");
        }

        // Buscar admin que responde
        Usuario admin = usuarioRepository.findByCorreo(correoAdmin)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario admin no encontrado"));

        // Actualizar solicitud
        solicitud.setEstado(nuevoEstado.toLowerCase());
        solicitud.setRespuesta(request.getRespuesta());
        solicitud.setRespondidoPor(admin);
        solicitud.setFechaRespuesta(LocalDateTime.now());

        // ✅ SI ES ACEPTADA: crear el ProyectoMiembro automáticamente
        if ("aceptada".equalsIgnoreCase(nuevoEstado)) {
            ProyectoMiembro miembro = ProyectoMiembro.builder()
                    .proyecto(solicitud.getProyecto())
                    .usuario(solicitud.getUsuario())
                    .rolEnProyecto("VOLUNTARIO")  // Rol por defecto
                    .estado(true)
                    .build();

            proyectoMiembroRepository.save(miembro);
        }

        SolicitudProyecto actualizada = solicitudRepository.save(solicitud);

        return SolicitudProyectoMapper.toResponse(actualizada);
    }
}