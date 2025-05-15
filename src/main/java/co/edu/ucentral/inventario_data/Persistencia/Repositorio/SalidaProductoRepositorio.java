package co.edu.ucentral.inventario_data.Persistencia.Repositorio;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.SalidaProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalidaProductoRepositorio extends JpaRepository<SalidaProducto, Long> {
}
