/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="servicio_prestado")
public class ServicioPrestado implements Serializable {
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_servicio")
    Integer idServicio;

    @Column(name="nombre")
    String nombre;

    @Column(name="precio", columnDefinition="numeric")
    BigDecimal precio;

    @Column(name="cantidad")
    Integer cantidad;

    @Column(name="fecha")
    LocalDate fecha;

    @Column(name="tipo")
    @Enumerated(EnumType.STRING)
    TipoServicio tipo;

    @OneToMany(mappedBy="servicioPrestado")
    @LazyCollection(LazyCollectionOption.FALSE)
    List<ServicioFacturado>serviciosFacturados;

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoServicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoServicio tipo) {
        this.tipo = tipo;
    }

    public List<ServicioFacturado> getServiciosFacturados() {
        return serviciosFacturados;
    }

    public void setServiciosFacturados(List<ServicioFacturado> serviciosFacturados) {
        this.serviciosFacturados = serviciosFacturados;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.idServicio);
        hash = 67 * hash + Objects.hashCode(this.nombre);
        hash = 67 * hash + Objects.hashCode(this.precio);
        hash = 67 * hash + Objects.hashCode(this.cantidad);
        hash = 67 * hash + Objects.hashCode(this.fecha);
        hash = 67 * hash + Objects.hashCode(this.tipo);
        hash = 67 * hash + Objects.hashCode(this.serviciosFacturados);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServicioPrestado other = (ServicioPrestado) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.idServicio, other.idServicio)) {
            return false;
        }
        if (!Objects.equals(this.precio, other.precio)) {
            return false;
        }
        if (!Objects.equals(this.cantidad, other.cantidad)) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        return Objects.equals(this.serviciosFacturados, other.serviciosFacturados);
    }

    @Override
    public String toString() {
        return "ServicioPrestado{" + "idServicio=" + idServicio + ", nombre=" + nombre + ", precio=" + precio + '}';
    }
    
}