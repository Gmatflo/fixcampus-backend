package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class RecomendacionIADTO {
    private Long idAnalisis;
    @NotNull private Long reporteId;
    @NotBlank @Size(max = 200) private String tituloSugerido;
    @NotBlank private String resumen;
    @NotBlank @Size(max = 30) private String prioridadSugerida;
    @NotBlank private String justificacion;
    private LocalDateTime fechaAnalisis;

    public Long getIdAnalisis() { return idAnalisis; }
    public void setIdAnalisis(Long idAnalisis) { this.idAnalisis = idAnalisis; }
    public Long getReporteId() { return reporteId; }
    public void setReporteId(Long reporteId) { this.reporteId = reporteId; }
    public String getTituloSugerido() { return tituloSugerido; }
    public void setTituloSugerido(String tituloSugerido) { this.tituloSugerido = tituloSugerido; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getPrioridadSugerida() { return prioridadSugerida; }
    public void setPrioridadSugerida(String prioridadSugerida) { this.prioridadSugerida = prioridadSugerida; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
    public LocalDateTime getFechaAnalisis() { return fechaAnalisis; }
    public void setFechaAnalisis(LocalDateTime fechaAnalisis) { this.fechaAnalisis = fechaAnalisis; }
}
