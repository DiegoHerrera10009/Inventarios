package co.edu.ucentral.inventario_data.Controladores;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Usuario;
import co.edu.ucentral.inventario_data.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registro(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute Usuario usuario) {
        usuario.setRol("USER");
        usuarioServicio.guardar(usuario);
        return "redirect:/login";
    }
}
