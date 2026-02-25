package co.edu.ucentral.inventario_data.Persistencia.Repositorio;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p WHERE p.stockMinimo IS NOT NULL AND p.cantidad <= p.stockMinimo")
    List<Producto> findProductosConStockBajo();

    List<Producto> findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCase(String codigo, String nombre);

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
}
