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

    @GetMapping("/historial")
    public String verHistorial(Model modelo) {
        modelo.addAttribute("salidas", salidaServicio.obtenerHistorial());
        return "historial_salidas";
    }

    @GetMapping(value = "/exportar.csv", produces = "text/csv")
    @ResponseBody
    public String exportarHistorialCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID;Fecha;CodigoProducto;NombreProducto;Marca;Cantidad;Motivo\n");
        salidaServicio.obtenerHistorial().forEach(s -> {
            sb.append(s.getId() != null ? s.getId() : "").append(";");
            sb.append(s.getFecha() != null ? s.getFecha() : "").append(";");
            Producto p = s.getProducto();
            sb.append(p != null && p.getCodigo() != null ? p.getCodigo() : "").append(";");
            sb.append(p != null && p.getNombre() != null ? p.getNombre() : "").append(";");
            sb.append(p != null && p.getMarca() != null ? p.getMarca() : "").append(";");
            sb.append(s.getCantidad()).append(";");
            sb.append(s.getMotivo() != null ? s.getMotivo() : "").append("\n");
        });
        return sb.toString();
    }
}
