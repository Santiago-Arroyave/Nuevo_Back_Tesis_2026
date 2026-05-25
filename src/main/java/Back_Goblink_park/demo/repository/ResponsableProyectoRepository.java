package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ResponsableProyecto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponsableProyectoRepository
        extends JpaRepository<ResponsableProyecto, Long> {

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<ResponsableProyecto>
    findByProyectoId(
            Long proyectoId
    );

    // =====================================================
    // LISTAR POR USUARIO RESPONSABLE
    // =====================================================

    List<ResponsableProyecto>
    findByUsuarioResponsableId(
            Long usuarioId
    );

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    List<ResponsableProyecto>
    findByEstado(
            String estado
    );

    // =====================================================
    // LISTAR POR PRIORIDAD
    // =====================================================

    List<ResponsableProyecto>
    findByPrioridad(
            String prioridad
    );

    // =====================================================
    // LISTAR TAREAS POR PROYECTO Y ESTADO
    // =====================================================

    List<ResponsableProyecto>
    findByProyectoIdAndEstado(
            Long proyectoId,
            String estado
    );

    // =====================================================
    // LISTAR TAREAS POR RESPONSABLE Y ESTADO
    // =====================================================

    List<ResponsableProyecto>
    findByUsuarioResponsableIdAndEstado(
            Long usuarioId,
            String estado
    );
}