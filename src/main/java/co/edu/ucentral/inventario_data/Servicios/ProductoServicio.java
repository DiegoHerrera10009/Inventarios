package co.edu.ucentral.inventario_data.Servicios;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import co.edu.ucentral.inventario_data.Persistencia.Repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public String sugerirSiguienteUbicacion() {
        long n = productoRepositorio.count();
        return "Estante A - Posición " + (n + 1);
    }

    /** Guarda el producto. Devuelve "ok" o "codigo_duplicado" si ya existe otro con la misma referencia. */
    public String guardarProducto(Producto producto) {
        if (producto.getCantidad() < 1) {
            producto.setCantidad(1);
        }
        String codigo = producto.getCodigo() != null ? producto.getCodigo().trim() : "";
        if (codigo.isEmpty()) {
            productoRepositorio.save(producto);
            return "ok";
        }
        boolean esNuevo = producto.getId() == null;
        if (esNuevo && productoRepositorio.existsByCodigoIgnoreCase(codigo)) {
            return "codigo_duplicado";
        }
        if (!esNuevo && productoRepositorio.existsByCodigoIgnoreCaseAndIdNot(codigo, producto.getId())) {
            return "codigo_duplicado";
        }
        producto.setCodigo(codigo);
        if (producto.getUbicacionBodega() == null || producto.getUbicacionBodega().trim().isEmpty()) {
            producto.setUbicacionBodega(sugerirSiguienteUbicacion());
        }
        productoRepositorio.save(producto);
        return "ok";
    }

    public boolean anadirStock(Long idProducto, int cantidadAnadir) {
        if (cantidadAnadir < 1) return false;
        return productoRepositorio.findById(idProducto)
                .map(p -> {
                    p.setCantidad(p.getCantidad() + cantidadAnadir);
                    productoRepositorio.save(p);
                    return true;
                })
                .orElse(false);
    }

    public List<Producto> obtenerTodos() {
        return productoRepositorio.findAll();
    }

    public List<Producto> buscarPorReferenciaONombre(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return productoRepositorio.findAll();
        }
        String t = termino.trim();
        return productoRepositorio.findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCase(t, t);
    }

    public List<Producto> obtenerConStockBajo() {
        return productoRepositorio.findProductosConStockBajo();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepositorio.findById(id);
    }
}
