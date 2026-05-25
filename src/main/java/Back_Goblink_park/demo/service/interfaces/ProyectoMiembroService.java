package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoMiembroRequest;
import Back_Goblink_park.demo.dto.response.ProyectoMiembroResponse;

import java.util.List;

public interface ProyectoMiembroService {

    // =====================================================
    // AGREGAR MIEMBRO
    // =====================================================

    ProyectoMiembroResponse agregarMiembro(
            ProyectoMiembroRequest request
    );

    // =====================================================
    // LISTAR MIEMBROS POR PROYECTO
    // =====================================================

    List<ProyectoMiembroResponse> listarMiembrosProyecto(
            Long proyectoId
    );

    // =====================================================
    // LISTAR PROYECTOS DEL USUARIO
    // =====================================================

    List<ProyectoMiembroResponse> listarProyectosUsuario(
            Long usuarioId
    );

    // =====================================================
    // OBTENER MIEMBRO
    // =====================================================

    ProyectoMiembroResponse obtenerMiembro(
            Long id
    );

    // =====================================================
    // ELIMINAR MIEMBRO
    // =====================================================

    void eliminarMiembro(
            Long id
    );
}