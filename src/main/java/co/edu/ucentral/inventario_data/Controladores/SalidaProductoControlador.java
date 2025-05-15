package co.edu.ucentral.inventario_data.Controladores;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import co.edu.ucentral.inventario_data.Persistencia.Entidades.SalidaProducto;
import co.edu.ucentral.inventario_data.Persistencia.Repositorio.ProductoRepositorio;
import co.edu.ucentral.inventario_data.Servicios.SalidaProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salida")
public class SalidaProductoControlador {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private SalidaProductoServicio salidaServicio;


    @GetMapping("/formulario")
    public String mostrarFormulario(Model modelo) {
        modelo.addAttribute("salida", new SalidaProducto());
        modelo.addAttribute("productos", productoRepositorio.findAll());
        return "formulario_salida";
    }

    @PostMapping("/registrar")
    public String registrarSalida(@ModelAttribute("salida") SalidaProducto salida, Model modelo) {
        String resultado = salidaServicio.registrarSalida(salida);
        if ("ok".equals(resultado)) {
            return "redirect:/producto/lista";
        } else {
            modelo.addAttribute("error", "Stock insuficiente para realizar esta salida.");
            modelo.addAttribute("productos", productoRepositorio.findAll());
            return "formulario_salida";
        }
    }
}
