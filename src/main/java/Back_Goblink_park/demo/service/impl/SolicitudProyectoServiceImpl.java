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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitudProyectoServiceImpl implements SolicitudProyectoService {

    private final SolicitudProyectoRepository solicitudRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoMiembroRepository proyectoMiembroRepository;

    // ==========================================
    // 1. CREACIÓN DE SOLICITUDES
    // ==========================================
    @Override
    @Transactional
    public SolicitudProyectoResponse crearSolicitud(SolicitudProyectoRequest request, String correoUsuario) {
        log.info("Iniciando creación de solicitud para el usuario: {} en el proyecto: {}", correoUsuario, request.getProyectoId());

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el correo: " + correoUsuario));

        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + request.getProyectoId()));

        if (proyectoMiembroRepository.existeMiembroActivo(usuario.getId(), proyecto.getId())) {
            throw new IllegalArgumentException("Ya eres miembro activo de este proyecto");
        }

        solicitudRepository.findByUsuarioIdAndProyectoIdAndEstado(usuario.getId(), proyecto.getId(), "pendiente")
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Ya tienes una solicitud pendiente para este proyecto");
                });

        SolicitudProyecto solicitud = SolicitudProyecto.builder()
                .usuario(usuario)
                .proyecto(proyecto)
                .estado("pendiente")
                .mensaje(request.getMensaje() != null ? request.getMensaje().trim() : "")
                .build();

        SolicitudProyecto guardada = solicitudRepository.save(solicitud);
        log.info("Solicitud creada exitosamente con ID: {}", guardada.getId());

        return SolicitudProyectoMapper.toResponse(guardada);
    }

    // ==========================================
    // 2. CONSULTA DE SOLICITUDES
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public SolicitudProyectoResponse obtenerSolicitud(Long id) {
        SolicitudProyecto solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con el ID: " + id));
        return SolicitudProyectoMapper.toResponse(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyectoResponse> listarSolicitudesPendientes(Long proyectoId) {
        log.debug("Listando solicitudes pendientes para el proyecto ID: {}", proyectoId);
        return solicitudRepository.findByProyectoIdAndEstadoOrderByFechaSolicitudDesc(proyectoId, "pendiente")
                .stream()
                .map(SolicitudProyectoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyectoResponse> listarSolicitudesPorProyecto(Long proyectoId) {
        return solicitudRepository.findByProyectoIdOrderByFechaSolicitudDesc(proyectoId)
                .stream()
                .map(SolicitudProyectoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudProyectoResponse> listarTodasPendientes() {
        return solicitudRepository.findByEstadoOrderByFechaSolicitudDesc("pendiente")
                .stream()
                .map(SolicitudProyectoMapper::toResponse)
                .toList();
    }

    // ==========================================
    // 3. GESTIÓN DE RESPUESTAS (ACEPTAR / RECHAZAR)
    // ==========================================
    @Override
    @Transactional
    public SolicitudProyectoResponse aceptarSolicitud(Long solicitudId, SolicitudResponderRequest request, String correoUsuario) {
        log.info("Administrador {} aceptando la solicitud ID: {}", correoUsuario, solicitudId);

        SolicitudProyecto solicitud = prepararRespuestaSolicitud(solicitudId, correoUsuario);
        solicitud.setEstado("aceptada");
        solicitud.setRespuesta(request.getRespuesta() != null ? request.getRespuesta().trim() : "");

        boolean yaEsMiembro = proyectoMiembroRepository.existeMiembroActivo(
                solicitud.getUsuario().getId(),
                solicitud.getProyecto().getId()
        );

        if (!yaEsMiembro) {
            String rolAsignado = request.getRolEnProyecto() != null ? request.getRolEnProyecto().trim().toUpperCase() : "VOLUNTARIO";

            ProyectoMiembro miembro = ProyectoMiembro.builder()
                    .proyecto(solicitud.getProyecto())
                    .usuario(solicitud.getUsuario())
                    .rolEnProyecto(rolAsignado)
                    .estado(true)
                    .build();

            proyectoMiembroRepository.save(miembro);
            log.info("Usuario {} agregado al proyecto {} como {}",
                    solicitud.getUsuario().getCorreo(), solicitud.getProyecto().getNombre(), rolAsignado);
        } else {
            log.warn("El usuario {} ya era miembro del proyecto {}. Se actualizó el estado de la solicitud, pero no se duplicó el registro.",
                    solicitud.getUsuario().getCorreo(), solicitud.getProyecto().getNombre());
        }

        SolicitudProyecto actualizada = solicitudRepository.save(solicitud);
        return SolicitudProyectoMapper.toResponse(actualizada);
    }

    @Override
    @Transactional
    public SolicitudProyectoResponse rechazarSolicitud(Long solicitudId, SolicitudResponderRequest request, String correoUsuario) {
        log.info("Administrador {} rechazando la solicitud ID: {}", correoUsuario, solicitudId);

        SolicitudProyecto solicitud = prepararRespuestaSolicitud(solicitudId, correoUsuario);
        solicitud.setEstado("rechazada");
        solicitud.setRespuesta(request.getRespuesta() != null ? request.getRespuesta().trim() : "");

        SolicitudProyecto actualizada = solicitudRepository.save(solicitud);
        return SolicitudProyectoMapper.toResponse(actualizada);
    }

    // ==========================================
    // 4. MÉTODOS PRIVADOS DE APOYO
    // ==========================================
    private SolicitudProyecto prepararRespuestaSolicitud(Long solicitudId, String correoAdmin) {
        SolicitudProyecto solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con ID: " + solicitudId));

        if (!"pendiente".equalsIgnoreCase(solicitud.getEstado())) {
            throw new IllegalArgumentException("Esta solicitud ya fue respondida previamente. Estado actual: " + solicitud.getEstado());
        }

        Usuario admin = usuarioRepository.findByCorreo(correoAdmin)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario administrador no encontrado con correo: " + correoAdmin));

        solicitud.setRespondidoPor(admin);
        solicitud.setFechaRespuesta(LocalDateTime.now());

        return solicitud;
    }
}