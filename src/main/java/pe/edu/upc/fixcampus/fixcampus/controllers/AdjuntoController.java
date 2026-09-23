package pe.edu.upc.fixcampus.fixcampus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.AdjuntoDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Adjunto;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.AdjuntoRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.ReporteRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@PreAuthorize("hasRole('ADMIN')")
public class AdjuntoController {
    private final AdjuntoRepository repository;
    private final ReporteRepository reporteRepository;

    public AdjuntoController(AdjuntoRepository repository, ReporteRepository reporteRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
    }

    @GetMapping
    @Operation(summary = "Listar adjuntos", description = "Si se indica tipoArchivo, busca adjuntos cuyo tipo contenga ese texto, sin distinguir mayúsculas. Solo para administradores.")
    public List<AdjuntoDTO> listar(@Parameter(description = "Parte del tipo de archivo, por ejemplo pdf") @RequestParam(required = false) String tipoArchivo) {
        List<Adjunto> lista = tipoArchivo == null || tipoArchivo.isBlank() ? repository.findAll()
                : repository.findByTipoArchivoContainingIgnoreCase(tipoArchivo);
        return lista.stream().map(this::convertir).toList();
    }

    @GetMapping("/{id}")
    public AdjuntoDTO buscar(@PathVariable Long id) { return convertir(buscarEntidad(id)); }

    @PostMapping
    public ResponseEntity<AdjuntoDTO> crear(@Valid @RequestBody AdjuntoDTO datos) {
        Adjunto adjunto = new Adjunto();
        copiarDatos(adjunto, datos);
        adjunto.setFechaSubida(LocalDateTime.now());
        return ResponseEntity.status(201).body(convertir(repository.save(adjunto)));
    }

    @PutMapping("/{id}")
    public AdjuntoDTO actualizar(@PathVariable Long id, @Valid @RequestBody AdjuntoDTO datos) {
        Adjunto adjunto = buscarEntidad(id);
        copiarDatos(adjunto, datos);
        return convertir(repository.save(adjunto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscarEntidad(id));
        return ResponseEntity.noContent().build();
    }

    private Adjunto buscarEntidad(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Adjunto no encontrado"));
    }

    private void copiarDatos(Adjunto adjunto, AdjuntoDTO datos) {
        adjunto.setReporte(reporteRepository.findById(datos.getReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado")));
        adjunto.setNombreArchivo(datos.getNombreArchivo());
        adjunto.setUrlArchivo(datos.getUrlArchivo());
        adjunto.setTipoArchivo(datos.getTipoArchivo());
    }

    private AdjuntoDTO convertir(Adjunto adjunto) {
        AdjuntoDTO dto = new AdjuntoDTO();
        dto.setIdAdjunto(adjunto.getIdAdjunto());
        dto.setReporteId(adjunto.getReporte().getIdReporte());
        dto.setNombreArchivo(adjunto.getNombreArchivo());
        dto.setUrlArchivo(adjunto.getUrlArchivo());
        dto.setTipoArchivo(adjunto.getTipoArchivo());
        dto.setFechaSubida(adjunto.getFechaSubida());
        return dto;
    }
}
