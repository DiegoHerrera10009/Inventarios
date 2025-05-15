package co.edu.ucentral.inventario_data.Controladores;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Usuario;
import co.edu.ucentral.inventario_data.Servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String inicio() {
        return "inicio"; // Página con botones para iniciar sesión o registrarse
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        usuarioServicio.guardarUsuario(usuario);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute Usuario usuario, Model modelo, HttpSession session) {
        Usuario encontrado = usuarioServicio.obtenerPorNombre(usuario.getNombre());
        if (encontrado != null && encontrado.getContrasena().equals(usuario.getContrasena())) {
            session.setAttribute("usuarioLogueado", encontrado); // Guardar usuario en sesión
            return "redirect:/menu";
        } else {
            modelo.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }

    @GetMapping("/menu")
    public String mostrarMenu(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("nombreUsuario", usuario.getNombre());
        return "menu";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Elimina la sesión
        return "redirect:/login";
    }
}
