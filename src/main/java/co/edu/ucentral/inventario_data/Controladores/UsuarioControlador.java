package co.edu.ucentral.inventario_data.Controladores;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Usuario;
import co.edu.ucentral.inventario_data.Servicios.ProductoServicio;
import co.edu.ucentral.inventario_data.Servicios.SalidaProductoServicio;
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

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private SalidaProductoServicio salidaProductoServicio;

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
        model.addAttribute("rolUsuario", usuario.getRol());
        // Métricas básicas para el dashboard
        long totalProductos = productoServicio.obtenerTodos().size();
        long productosConStockBajo = productoServicio.obtenerConStockBajo().size();
        long totalMovimientos = salidaProductoServicio.obtenerHistorial().size();

        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("productosConStockBajo", productosConStockBajo);
        model.addAttribute("totalMovimientos", totalMovimientos);
        model.addAttribute("salidasRecientes", salidaProductoServicio.obtenerHistorial()
                .stream()
                .limit(5)
                .toList());
        return "menu";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Elimina la sesión
        return "redirect:/login";
    }

    // --- Gestión básica de usuarios (solo vista simple), pensada para rol ADMIN ---

    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioActual == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioActual", usuarioActual);
        model.addAttribute("usuarios", usuarioServicio.obtenerTodos());
        return "usuarios";
    }

    @PostMapping("/usuarios/{id}/rol")
    public String actualizarRolUsuario(@PathVariable Long id,
                                       @RequestParam("rol") String rol) {
        usuarioServicio.actualizarRol(id, rol);
        return "redirect:/usuarios";
    }
}
