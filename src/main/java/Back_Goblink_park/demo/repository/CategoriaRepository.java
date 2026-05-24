package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {

    boolean existsByNombre(String nombre);
}