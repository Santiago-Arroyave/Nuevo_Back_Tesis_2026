package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoMiembroRequest;
import Back_Goblink_park.demo.dto.response.ProyectoMiembroResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProyectoMiembroService {

    // =====================================================
    // AGREGAR MIEMBRO
    // =====================================================
    ProyectoMiembroResponse agregarMiembro(ProyectoMiembroRequest request);

    // =====================================================
    // LISTAR MIEMBROS POR PROYECTO
    // =====================================================
    List<ProyectoMiembroResponse> listarMiembrosProyecto(Long proyectoId);

    // =====================================================
    // LISTAR PROYECTOS DEL USUARIO
    // =====================================================
    List<ProyectoMiembroResponse> listarProyectosUsuario(Long usuarioId);

    // =====================================================
    // OBTENER MIEMBRO POR ID
    // =====================================================
    ProyectoMiembroResponse obtenerMiembro(Long id);

    // =====================================================
    // ELIMINAR MIEMBRO (SOFT DELETE)
    // =====================================================
    void eliminarMiembro(Long id);

    Page<ProyectoMiembroResponse> listarMiembrosProyectoPaginado(Long proyectoId, int page, int size);
    Page<ProyectoMiembroResponse> listarProyectosUsuarioPaginado(Long usuarioId, int page, int size);
}