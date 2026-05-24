package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {

    boolean existsByNombre(String nombre);
}