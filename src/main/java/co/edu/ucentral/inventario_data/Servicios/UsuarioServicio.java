package co.edu.ucentral.inventario_data.Servicios;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Usuario;
import co.edu.ucentral.inventario_data.Persistencia.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public void guardarUsuario(Usuario usuario) {
        // Si aún no hay ningún ADMIN, el siguiente que se registre será ADMIN
        if (usuarioRepositorio.countByRolIgnoreCase("ADMIN") == 0) {
            usuario.setRol("ADMIN");
        } else {
            // Rol por defecto para nuevos usuarios si no se especifica
            if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
                usuario.setRol("COMERCIAL");
            }
        }
        usuarioRepositorio.save(usuario);
    }

    public Usuario obtenerPorNombre(String nombre) {
        return usuarioRepositorio.findFirstByNombreOrderByIdAsc(nombre).orElse(null);
    }

    public java.util.List<Usuario> obtenerTodos() {
        return usuarioRepositorio.findAll();
    }

    public void actualizarRol(Long id, String rol) {
        usuarioRepositorio.findById(id).ifPresent(u -> {
            String nuevoRol = (rol != null && !rol.trim().isEmpty())
                    ? rol.trim().toUpperCase()
                    : "COMERCIAL";
            u.setRol(nuevoRol);
            usuarioRepositorio.save(u);
        });
    }
}
