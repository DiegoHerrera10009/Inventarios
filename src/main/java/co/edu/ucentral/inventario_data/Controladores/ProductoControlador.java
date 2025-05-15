package co.edu.ucentral.inventario_data.Controladores;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import co.edu.ucentral.inventario_data.Servicios.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/producto")
public class ProductoControlador {

    @Autowired
    private ProductoServicio productoServicio;

    @GetMapping("/formulario")
    public String mostrarFormulario(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        return "nuevo_Producto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoServicio.guardarProducto(producto);
        return "redirect:/producto/lista";
    }

    @GetMapping("/lista")
    public String listarProductos(Model modelo) {
        List<Producto> productos = productoServicio.obtenerTodos();
        modelo.addAttribute("productos", productos);
        return "lista_productos";
    }
}
