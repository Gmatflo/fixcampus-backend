package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByEstadoIgnoreCase(String estado);

    @Query("select r from Reporte r join r.categoria c where lower(c.nombre) = lower(:nombreCategoria)")
    List<Reporte> findByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);
}
