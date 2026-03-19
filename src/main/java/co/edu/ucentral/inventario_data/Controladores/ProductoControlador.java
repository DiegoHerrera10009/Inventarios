package co.edu.ucentral.inventario_data.Controladores;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import co.edu.ucentral.inventario_data.Servicios.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                                  Model modelo) {
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            String nombreArchivo = UUID.randomUUID() + "_" + imagenArchivo.getOriginalFilename();
            Path rutaUploads = Paths.get("uploads");
            try {
                Files.createDirectories(rutaUploads);
                Files.copy(imagenArchivo.getInputStream(),
                        rutaUploads.resolve(nombreArchivo),
                        StandardCopyOption.REPLACE_EXISTING);
                producto.setImagenNombre(nombreArchivo);
            } catch (IOException e) {
                // En un proyecto real se debería registrar el error con un logger
            }
        }

        String resultado = productoServicio.guardarProducto(producto);
        if ("codigo_duplicado".equals(resultado)) {
            modelo.addAttribute("producto", producto);
            modelo.addAttribute("errorCodigo", "Ya existe un repuesto con esa referencia (código). La referencia debe ser única.");
            return "nuevo_Producto";
        }
        return "redirect:/producto/lista";
    }

    @GetMapping("/lista")
    public String listarProductos(@RequestParam(value = "q", required = false) String busqueda, Model modelo) {
        List<Producto> productos = productoServicio.buscarPorReferenciaONombre(busqueda);
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("busqueda", busqueda != null ? busqueda : "");
        modelo.addAttribute("productosConStockBajo", productoServicio.obtenerConStockBajo().size());
        return "lista_productos";
    }

    @GetMapping(value = "/exportar.csv", produces = "text/csv")
    @ResponseBody
    public String exportarProductosCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID;Codigo;Nombre;Marca;Categoria;Cantidad;StockMinimo;Precio\n");
        productoServicio.obtenerTodos().forEach(p -> {
            sb.append(p.getId() != null ? p.getId() : "").append(";");
            sb.append(p.getCodigo() != null ? p.getCodigo() : "").append(";");
            sb.append(p.getNombre() != null ? p.getNombre() : "").append(";");
            sb.append(p.getMarca() != null ? p.getMarca() : "").append(";");
            sb.append(p.getCategoria() != null ? p.getCategoria() : "").append(";");
            sb.append(p.getCantidad()).append(";");
            sb.append(p.getStockMinimo() != null ? p.getStockMinimo() : "").append(";");
            sb.append(p.getPrecio()).append("\n");
        });
        return sb.toString();
    }

    @GetMapping("/stock-bajo")
    public String listarProductosConStockBajo(Model modelo) {
        List<Producto> productos = productoServicio.obtenerConStockBajo();
        modelo.addAttribute("productos", productos);
        return "lista_stock_bajo";
    }

    @GetMapping("/ficha/{id}")
    public String verFicha(@PathVariable Long id, Model modelo) {
        return productoServicio.obtenerPorId(id)
                .map(p -> {
                    modelo.addAttribute("producto", p);
                    return "ficha_producto";
                })
                .orElse("redirect:/producto/lista");
    }

    @GetMapping("/entrada-stock/{id}")
    public String formularioEntradaStock(@PathVariable Long id, Model modelo) {
        return productoServicio.obtenerPorId(id)
                .map(p -> {
                    modelo.addAttribute("producto", p);
                    return "entrada_stock";
                })
                .orElse("redirect:/producto/lista");
    }

    @PostMapping("/entrada-stock/{id}")
    public String procesarEntradaStock(@PathVariable Long id, @RequestParam("cantidad") int cantidad) {
        if (cantidad < 1) {
            return "redirect:/producto/entrada-stock/" + id;
        }
        productoServicio.anadirStock(id, cantidad);
        return "redirect:/producto/lista";
    }
}
