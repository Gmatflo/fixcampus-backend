package pe.edu.upc.fixcampus.fixcampus.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recomendacion_ia")
public class RecomendacionIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_analisis")
    private Long idAnalisis;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_reporte", nullable = false, unique = true)
    private Reporte reporte;

    @Column(name = "titulo_sugerido", nullable = false, length = 200)
    private String tituloSugerido;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String resumen;

    @Column(name = "prioridad_sugerida", nullable = false, length = 30)
    private String prioridadSugerida;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificacion;

    @Column(name = "fecha_analisis", nullable = false)
    private LocalDateTime fechaAnalisis;

    public Long getIdAnalisis() { return idAnalisis; }
    public void setIdAnalisis(Long idAnalisis) { this.idAnalisis = idAnalisis; }
    public Reporte getReporte() { return reporte; }
    public void setReporte(Reporte reporte) { this.reporte = reporte; }
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
