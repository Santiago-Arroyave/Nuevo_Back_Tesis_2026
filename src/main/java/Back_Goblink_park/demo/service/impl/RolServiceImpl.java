package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.RolMapper;
import Back_Goblink_park.demo.dto.request.RolRequest;
import Back_Goblink_park.demo.dto.response.RolResponse;
import Back_Goblink_park.demo.entity.Rol;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.RolRepository;
import Back_Goblink_park.demo.service.interfaces.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public RolResponse crearRol(RolRequest request) {

        if (rolRepository.existsByNombre(request.getNombre())) {
            throw new BusinessException("El rol ya existe");
        }

        Rol rol = RolMapper.toEntity(request);

        Rol rolGuardado = rolRepository.save(rol);

        return RolMapper.toResponse(rolGuardado);
    }

    @Override
    public RolResponse obtenerPorId(Long id) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado")
                );

        return RolMapper.toResponse(rol);
    }

    @Override
    public List<RolResponse> listarRoles() {

        return rolRepository.findAll()
                .stream()
                .map(RolMapper::toResponse)
                .toList();
    }

    @Override
    public RolResponse actualizarRol(Long id, RolRequest request) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado")
                );

        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        rol.setEstado(request.getEstado());

        Rol actualizado = rolRepository.save(rol);

        return RolMapper.toResponse(actualizado);
    }

    @Override
    public void eliminarRol(Long id) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado")
                );

        rolRepository.delete(rol);
    }
}