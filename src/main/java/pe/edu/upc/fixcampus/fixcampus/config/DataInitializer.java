package pe.edu.upc.fixcampus.fixcampus.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.repositories.RolRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner crearUsuariosDePrueba(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            Rol administrador = crearRol(rolRepository, "ADMIN", "TOTAL");
            Rol usuario = crearRol(rolRepository, "USUARIO", "BASICO");

            crearUsuario(usuarioRepository, passwordEncoder,
                    "admin@fixcampus.com", "admin123", "Administrador",
                    "FixCampus", administrador);
            crearUsuario(usuarioRepository, passwordEncoder,
                    "usuario@fixcampus.com", "usuario123", "Usuario",
                    "FixCampus", usuario);
        };
    }

    private Rol crearRol(RolRepository repository, String nombre, String nivel) {
        return repository.findByNombre(nombre).orElseGet(() -> {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rol.setNivelAcceso(nivel);
            rol.setDescripcion("Rol de prueba para FixCampus");
            return repository.save(rol);
        });
    }

    private void crearUsuario(UsuarioRepository repository,
                              PasswordEncoder passwordEncoder,
                              String correo,
                              String password,
                              String nombre,
                              String apellido,
                              Rol rol) {
        if (repository.findByCorreo(correo).isPresent()) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setContrasenaHash(passwordEncoder.encode(password));
        usuario.setEstado("ACTIVO");
        usuario.setFechaRegistro(LocalDateTime.now());
        repository.save(usuario);
    }
}
