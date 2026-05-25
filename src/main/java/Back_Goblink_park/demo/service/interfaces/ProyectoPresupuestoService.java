package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoPresupuestoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoPresupuestoResponse;

import java.util.List;

public interface ProyectoPresupuestoService {

    // =====================================================
    // CREAR PRESUPUESTO
    // =====================================================

    ProyectoPresupuestoResponse crearPresupuesto(
            ProyectoPresupuestoRequest request
    );

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    List<ProyectoPresupuestoResponse> listarPresupuestos();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    ProyectoPresupuestoResponse obtenerPresupuesto(
            Long id
    );

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<ProyectoPresupuestoResponse> listarPorProyecto(
            Long proyectoId
    );

    // =====================================================
    // ACTUALIZAR PRESUPUESTO
    // =====================================================

    ProyectoPresupuestoResponse actualizarPresupuesto(
            Long id,
            ProyectoPresupuestoRequest request
    );

    // =====================================================
    // ELIMINAR PRESUPUESTO
    // =====================================================

    void eliminarPresupuesto(
            Long id
    );
}