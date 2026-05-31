package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ProyectoMiembroMapper;
import Back_Goblink_park.demo.dto.request.ProyectoMiembroRequest;
import Back_Goblink_park.demo.dto.response.ProyectoMiembroResponse;
import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoMiembro;
import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.ProyectoMiembroRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;
import Back_Goblink_park.demo.service.interfaces.ProyectoMiembroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoMiembroServiceImpl implements ProyectoMiembroService {

    private final ProyectoMiembroRepository miembroRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ProyectoMiembroResponse agregarMiembro(ProyectoMiembroRequest request) {
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        boolean existe = miembroRepository.existsByProyectoIdAndUsuarioId(
                request.getProyectoId(),
                request.getUsuarioId()
        );
        if (existe) throw new RuntimeException("El usuario ya pertenece al proyecto");

        ProyectoMiembro miembro = ProyectoMiembro.builder()
                .proyecto(proyecto)
                .usuario(usuario)
                .rolEnProyecto(request.getRolEnProyecto())
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .fechaAsignacion(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProyectoMiembro guardado = miembroRepository.save(miembro);
        return ProyectoMiembroMapper.toResponse(guardado);
    }

    @Override
    public List<ProyectoMiembroResponse> listarMiembrosProyecto(Long proyectoId) {
        return miembroRepository.findByProyectoId(proyectoId)
                .stream()
                .map(ProyectoMiembroMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProyectoMiembroResponse> listarProyectosUsuario(Long usuarioId) {
        return miembroRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ProyectoMiembroMapper::toResponse)
                .toList();
    }

    @Override
    public ProyectoMiembroResponse obtenerMiembro(Long id) {
        ProyectoMiembro miembro = miembroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Miembro no encontrado"));
        return ProyectoMiembroMapper.toResponse(miembro);
    }

    @Override
    public void eliminarMiembro(Long id) {
        ProyectoMiembro miembro = miembroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Miembro no encontrado"));
        // Soft delete
        miembro.setEstado(false);
        miembro.setUpdatedAt(LocalDateTime.now());
        miembroRepository.save(miembro);
    }

    @Override
    public Page<ProyectoMiembroResponse> listarMiembrosProyectoPaginado(Long proyectoId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return miembroRepository.findByProyectoId(proyectoId, pageable)
                .map(ProyectoMiembroMapper::toResponse);
    }

    @Override
    public Page<ProyectoMiembroResponse> listarProyectosUsuarioPaginado(Long usuarioId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return miembroRepository.findByUsuarioId(usuarioId, pageable)
                .map(ProyectoMiembroMapper::toResponse);
    }
}