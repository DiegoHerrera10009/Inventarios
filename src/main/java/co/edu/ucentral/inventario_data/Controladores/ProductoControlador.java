package co.edu.ucentral.inventario_data.Controladores;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import co.edu.ucentral.inventario_data.Servicios.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/productos")
public class ProductoControlador {

    @Autowired
    private ProductoServicio productoServicio;

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("listaProductos", productoServicio.listarTodos());
        return "lista_productos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("producto", new Producto());
        return "formulario_producto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        productoServicio.guardar(producto);
        return "redirect:/admin/productos";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoServicio.buscarPorId(id));
        return "formulario_producto";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoServicio.eliminar(id);
        return "redirect:/admin/productos";
    }
}
