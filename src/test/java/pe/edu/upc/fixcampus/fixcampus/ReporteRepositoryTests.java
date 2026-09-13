package pe.edu.upc.fixcampus.fixcampus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.upc.fixcampus.fixcampus.entities.Categoria;
import pe.edu.upc.fixcampus.fixcampus.entities.Ubicacion;
import pe.edu.upc.fixcampus.fixcampus.entities.Reporte;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.repositories.CategoriaRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UbicacionRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.ReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.RolRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReporteRepositoryTests {

    @Autowired
    private RolRepository roleRepository;

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private CategoriaRepository categoryRepository;

    @Autowired
    private UbicacionRepository locationRepository;

    @Autowired
    private ReporteRepository reportRepository;

    @Test
    void debeBuscarReportePorEstadoYConJoinDeCategoria() {
        Rol role = new Rol();
        role.setNombre("ESTUDIANTE");
        role.setNivelAcceso("BASICO");
        roleRepository.save(role);

        Usuario user = new Usuario();
        user.setRol(role);
        user.setNombre("Matias");
        user.setApellido("Prueba");
        user.setCorreo("matias.prueba@upc.edu.pe");
        user.setContrasenaHash("hash");
        user.setEstado("ACTIVO");
        user.setFechaRegistro(LocalDateTime.now());
        userRepository.save(user);

        Categoria category = new Categoria();
        category.setNombre("Limpieza de prueba");
        category.setDescripcion("Espacios que necesitan atención");
        categoryRepository.save(category);

        Ubicacion location = new Ubicacion();
        location.setCampus("UPC San Miguel");
        location.setEdificio("Pabellón A");
        location.setPiso(1);
        location.setZona("Aula 101");
        location.setTipo("AULA");
        locationRepository.save(location);

        Reporte report = new Reporte();
        report.setUsuarioReportante(user);
        report.setCategoria(category);
        report.setUbicacion(location);
        report.setTitulo("Luz apagada");
        report.setDescripcion("La luz del aula no enciende");
        report.setEstado("ABIERTO");
        report.setFechaCreacion(LocalDateTime.now());
        reportRepository.save(report);

        assertThat(reportRepository.findByEstadoIgnoreCase("abierto")).hasSize(1);
        assertThat(reportRepository.findByNombreCategoria("limpieza de prueba"))
                .extracting(Reporte::getTitulo)
                .containsExactly("Luz apagada");
    }
}
