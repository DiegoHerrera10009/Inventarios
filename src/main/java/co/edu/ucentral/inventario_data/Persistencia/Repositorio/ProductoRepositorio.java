package co.edu.ucentral.inventario_data.Persistencia.Repositorio;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
}
