package co.edu.ucentral.inventario_data.Persistencia.Entidades;

import jakarta.persistence.*;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;              // Referencia única del repuesto (código / SKU)
    private String nombre;              // Nombre comercial del repuesto
    private String marca;               // Marca del fabricante
    private String categoria;           // Categoría (frenos, suspensión, eléctrico, etc.)
    private String tipoEquipo;          // Tipo: Automotriz, Generador eléctrico (diesel), Otro
    private String vehiculoAplicacion;  // Aplicación: vehículo, modelo de motor/generador, etc.
    private String ubicacionBodega;     // Ubicación física en la bodega
    private String imagenNombre;        // Nombre de archivo de la imagen
    @Column(length = 2000)
    private String descripcion;         // Descripción detallada del repuesto (ficha)
    private int cantidad;               // Stock actual
    private Integer stockMinimo;        // Stock mínimo recomendado
    private double precio;              // Precio unitario

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getTipoEquipo() { return tipoEquipo; }
    public void setTipoEquipo(String tipoEquipo) { this.tipoEquipo = tipoEquipo; }

    public String getVehiculoAplicacion() { return vehiculoAplicacion; }
    public void setVehiculoAplicacion(String vehiculoAplicacion) { this.vehiculoAplicacion = vehiculoAplicacion; }

    public String getUbicacionBodega() { return ubicacionBodega; }
    public void setUbicacionBodega(String ubicacionBodega) { this.ubicacionBodega = ubicacionBodega; }

    public String getImagenNombre() { return imagenNombre; }
    public void setImagenNombre(String imagenNombre) { this.imagenNombre = imagenNombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}

