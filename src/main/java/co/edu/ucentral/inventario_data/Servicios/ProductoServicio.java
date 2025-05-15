package co.edu.ucentral.inventario_data.Servicios;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import co.edu.ucentral.inventario_data.Persistencia.Repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public void guardarProducto(Producto producto) {
        productoRepositorio.save(producto);
    }

    public List<Producto> obtenerTodos() {
        return productoRepositorio.findAll();
    }
}
