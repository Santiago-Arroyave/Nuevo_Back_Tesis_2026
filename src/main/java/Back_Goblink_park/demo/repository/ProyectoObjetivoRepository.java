package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ProyectoObjetivo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoObjetivoRepository
        extends JpaRepository<ProyectoObjetivo, Long> {

    // =====================================================
    // LISTAR OBJETIVOS POR PROYECTO
    // =====================================================

    List<ProyectoObjetivo> findByProyectoId(
            Long proyectoId
    );
}