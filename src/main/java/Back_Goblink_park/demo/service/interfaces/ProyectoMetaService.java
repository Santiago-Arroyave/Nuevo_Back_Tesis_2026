package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoMetaRequest;
import Back_Goblink_park.demo.dto.response.ProyectoMetaResponse;

import java.util.List;

public interface ProyectoMetaService {

    // =====================================================
    // CREAR META
    // =====================================================

    ProyectoMetaResponse crearMeta(
            ProyectoMetaRequest request
    );

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    List<ProyectoMetaResponse> listarMetas();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    ProyectoMetaResponse obtenerMeta(
            Long id
    );

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<ProyectoMetaResponse> listarPorProyecto(
            Long proyectoId
    );

    // =====================================================
    // ACTUALIZAR META
    // =====================================================

    ProyectoMetaResponse actualizarMeta(
            Long id,
            ProyectoMetaRequest request
    );

    // =====================================================
    // ELIMINAR META
    // =====================================================

    void eliminarMeta(
            Long id
    );
}