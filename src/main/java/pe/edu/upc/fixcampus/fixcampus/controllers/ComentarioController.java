package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentarioDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.ComentarioRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.ReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@PreAuthorize("hasRole('ADMIN')")
public class ComentarioController {
    private final ComentarioRepository repository;
    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    public ComentarioController(ComentarioRepository repository, ReporteRepository reporteRepository,
                                UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<ComentarioDTO> listar(@RequestParam(required = false) Long reporteId) {
        List<Comentario> lista = reporteId == null ? repository.findAll()
                : repository.findByReporte_IdReporte(reporteId);
        return lista.stream().map(this::convertir).toList();
    }

    @GetMapping("/{id}")
    public ComentarioDTO buscar(@PathVariable Long id) { return convertir(buscarEntidad(id)); }

    @PostMapping
    public ResponseEntity<ComentarioDTO> crear(@Valid @RequestBody ComentarioDTO datos) {
        Comentario comentario = new Comentario();
        copiarDatos(comentario, datos);
        comentario.setFechaComentario(LocalDateTime.now());
        return ResponseEntity.status(201).body(convertir(repository.save(comentario)));
    }

    @PutMapping("/{id}")
    public ComentarioDTO actualizar(@PathVariable Long id, @Valid @RequestBody ComentarioDTO datos) {
        Comentario comentario = buscarEntidad(id);
        copiarDatos(comentario, datos);
        return convertir(repository.save(comentario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscarEntidad(id));
        return ResponseEntity.noContent().build();
    }

    private Comentario buscarEntidad(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado"));
    }

    private void copiarDatos(Comentario comentario, ComentarioDTO datos) {
        comentario.setReporte(reporteRepository.findById(datos.getReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado")));
        comentario.setUsuario(usuarioRepository.findById(datos.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")));
        comentario.setTextoComentario(datos.getTextoComentario());
    }

    private ComentarioDTO convertir(Comentario comentario) {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setIdComentario(comentario.getIdComentario());
        dto.setReporteId(comentario.getReporte().getIdReporte());
        dto.setUsuarioId(comentario.getUsuario().getIdUsuario());
        dto.setTextoComentario(comentario.getTextoComentario());
        dto.setFechaComentario(comentario.getFechaComentario());
        return dto;
    }
}
