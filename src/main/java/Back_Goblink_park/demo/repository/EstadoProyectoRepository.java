package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.EstadoProyecto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoProyectoRepository
        extends JpaRepository<EstadoProyecto, Long> {

    boolean existsByNombre(String nombre);
}