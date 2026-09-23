package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.fixcampus.fixcampus.entities.RecomendacionIA;

public interface RecomendacionIARepository extends JpaRepository<RecomendacionIA, Long> {
    boolean existsByReporte_IdReporte(Long idReporte);
}
