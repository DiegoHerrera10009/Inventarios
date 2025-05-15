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
        usuarioRepositorio.save(usuario);
    }

    public Usuario obtenerPorNombre(String nombre) {
        return usuarioRepositorio.findByNombre(nombre);
    }
}
