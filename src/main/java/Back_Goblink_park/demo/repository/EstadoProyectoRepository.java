package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.EstadoProyecto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoProyectoRepository
        extends JpaRepository<EstadoProyecto, Long> {

    boolean existsByNombre(String nombre);

    Optional<EstadoProyecto> findByNombreIgnoreCase(String nombre);
}