package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT c FROM Categoria c WHERE lower(c.descripcion) LIKE lower(concat('%', :palabraClave, '%'))")
    List<Categoria> buscarPorDescripcion(@Param("palabraClave") String palabraClave);
}