package co.edu.ucentral.inventario_data.Servicios;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import co.edu.ucentral.inventario_data.Persistencia.Entidades.SalidaProducto;
import co.edu.ucentral.inventario_data.Persistencia.Repositorio.ProductoRepositorio;
import co.edu.ucentral.inventario_data.Persistencia.Repositorio.SalidaProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalidaProductoServicio {

    @Autowired
    private SalidaProductoRepositorio salidaRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public String registrarSalida(SalidaProducto salida) {
        Producto producto = productoRepositorio.findById(salida.getProducto().getId()).orElse(null);

        if (producto != null && producto.getCantidad() >= salida.getCantidad()) {
            producto.setCantidad(producto.getCantidad() - salida.getCantidad());
            productoRepositorio.save(producto);

            salida.setFecha(LocalDateTime.now());
            salidaRepositorio.save(salida);

            return "ok";
        } else {
            return "error_stock";
        }
    }

    public List<SalidaProducto> obtenerHistorial() {
        return salidaRepositorio.findAllByOrderByFechaDesc();
    }
}
