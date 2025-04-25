package co.edu.ucentral.inventario_data.Persistencia.Entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String categoria;
    private String descripcion;
    private double precio;
    private String unidad;
    private String presentacion;
    private LocalDate fechaVencimiento;
    private String codigoBarras;
}
