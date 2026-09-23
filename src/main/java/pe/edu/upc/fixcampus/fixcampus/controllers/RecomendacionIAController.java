package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.RecomendacionIADTO;
import pe.edu.upc.fixcampus.fixcampus.entities.RecomendacionIA;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.RecomendacionIARepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.ReporteRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@PreAuthorize("hasRole('ADMIN')")
public class RecomendacionIAController {
    private final RecomendacionIARepository repository;
    private final ReporteRepository reporteRepository;

    public RecomendacionIAController(RecomendacionIARepository repository,
                                     ReporteRepository reporteRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
    }

    @GetMapping
    public List<RecomendacionIADTO> listar() {
        return repository.findAll().stream().map(this::convertir).toList();
    }

    @GetMapping("/{id}")
    public RecomendacionIADTO buscar(@PathVariable Long id) { return convertir(buscarEntidad(id)); }

    @PostMapping
    public ResponseEntity<RecomendacionIADTO> crear(@Valid @RequestBody RecomendacionIADTO datos) {
        if (repository.existsByReporte_IdReporte(datos.getReporteId())) {
            throw new IllegalArgumentException("Este reporte ya tiene una recomendación");
        }
        RecomendacionIA recomendacion = new RecomendacionIA();
        copiarDatos(recomendacion, datos);
        recomendacion.setFechaAnalisis(LocalDateTime.now());
        return ResponseEntity.status(201).body(convertir(repository.save(recomendacion)));
    }

    @PutMapping("/{id}")
    public RecomendacionIADTO actualizar(@PathVariable Long id,
                                         @Valid @RequestBody RecomendacionIADTO datos) {
        RecomendacionIA recomendacion = buscarEntidad(id);
        if (!recomendacion.getReporte().getIdReporte().equals(datos.getReporteId())
                && repository.existsByReporte_IdReporte(datos.getReporteId())) {
            throw new IllegalArgumentException("Este reporte ya tiene una recomendación");
        }
        copiarDatos(recomendacion, datos);
        return convertir(repository.save(recomendacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscarEntidad(id));
        return ResponseEntity.noContent().build();
    }

    private RecomendacionIA buscarEntidad(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recomendación no encontrada"));
    }

    private void copiarDatos(RecomendacionIA recomendacion, RecomendacionIADTO datos) {
        recomendacion.setReporte(reporteRepository.findById(datos.getReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado")));
        recomendacion.setTituloSugerido(datos.getTituloSugerido());
        recomendacion.setResumen(datos.getResumen());
        recomendacion.setPrioridadSugerida(datos.getPrioridadSugerida());
        recomendacion.setJustificacion(datos.getJustificacion());
    }

    private RecomendacionIADTO convertir(RecomendacionIA recomendacion) {
        RecomendacionIADTO dto = new RecomendacionIADTO();
        dto.setIdAnalisis(recomendacion.getIdAnalisis());
        dto.setReporteId(recomendacion.getReporte().getIdReporte());
        dto.setTituloSugerido(recomendacion.getTituloSugerido());
        dto.setResumen(recomendacion.getResumen());
        dto.setPrioridadSugerida(recomendacion.getPrioridadSugerida());
        dto.setJustificacion(recomendacion.getJustificacion());
        dto.setFechaAnalisis(recomendacion.getFechaAnalisis());
        return dto;
    }
}
