package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOInsert;
import pe.edu.upc.fixcampus.fixcampus.dtos.ReporteDTOList;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.servicesinterfaces.ReporteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReporteController {

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<ReporteDTOList>> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String categoria) {

        List<Reporte> reportes;
        if (estado != null && !estado.isBlank()) {
            reportes = service.buscarPorEstado(estado);
        } else if (categoria != null && !categoria.isBlank()) {
            reportes = service.buscarPorCategoria(categoria);
        } else {
            reportes = service.listar();
        }

        return ResponseEntity.ok(reportes.stream().map(this::convertirDto).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertirDto(service.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> registrar(@Valid @RequestBody ReporteDTOInsert dto) {
        Reporte guardado = service.registrar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getIdReporte())
                .toUri();
        return ResponseEntity.created(location).body(convertirDto(guardado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ReporteDTOList> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReporteDTOInsert dto) {
        return ResponseEntity.ok(convertirDto(service.actualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ReporteDTOList convertirDto(Reporte reporte) {
        ReporteDTOList dto = new ReporteDTOList();
        dto.setIdReporte(reporte.getIdReporte());
        dto.setUsuarioReportanteId(reporte.getUsuarioReportante().getIdUsuario());
        dto.setTecnicoAsignadoId(reporte.getTecnicoAsignado() == null ? null : reporte.getTecnicoAsignado().getIdUsuario());
        dto.setCategoriaId(reporte.getCategoria().getIdCategoria());
        dto.setCategoriaNombre(reporte.getCategoria().getNombre());
        dto.setUbicacionId(reporte.getUbicacion().getIdUbicacion());
        dto.setTitulo(reporte.getTitulo());
        dto.setDescripcion(reporte.getDescripcion());
        dto.setDetalleUbicacion(reporte.getDetalleUbicacion());
        dto.setPrioridad(reporte.getPrioridad());
        dto.setEstado(reporte.getEstado());
        dto.setFechaCreacion(reporte.getFechaCreacion());
        dto.setFechaAsignacion(reporte.getFechaAsignacion());
        dto.setFechaResolucion(reporte.getFechaResolucion());
        return dto;
    }
}
