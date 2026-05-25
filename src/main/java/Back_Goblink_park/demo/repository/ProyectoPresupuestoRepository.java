package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ProyectoPresupuesto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoPresupuestoRepository
        extends JpaRepository<ProyectoPresupuesto, Long> {

    // =====================================================
    // LISTAR PRESUPUESTOS POR PROYECTO
    // =====================================================

    List<ProyectoPresupuesto> findByProyectoId(
            Long proyectoId
    );
}