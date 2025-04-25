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

    public List<Producto> listarTodos() {
        return productoRepositorio.findAll();
    }

    public void guardar(Producto producto) {
        productoRepositorio.save(producto);
    }

    public Producto buscarPorId(Long id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        productoRepositorio.deleteById(id);
    }
}
