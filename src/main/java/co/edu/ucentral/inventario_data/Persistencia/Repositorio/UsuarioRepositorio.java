package co.edu.ucentral.inventario_data.Persistencia.Repositorio;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Usuario findByNombre(String nombre);
    long countByRolIgnoreCase(String rol);
    java.util.Optional<Usuario> findFirstByNombreOrderByIdAsc(String nombre);
}
