package co.edu.ucentral.inventario_data.Persistencia.Repositorio;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
