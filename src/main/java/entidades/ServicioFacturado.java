/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name="servicio_facturado")
public class ServicioFacturado implements Serializable {
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="nombre")
    String nombre;

    @Column(name="precio_unitario", columnDefinition="numeric")
    BigDecimal precioUnitario;

    @Column(name="cantidad")
    Integer cantidad;

    @Column(name="precio_total", columnDefinition="numeric")
    BigDecimal precioTotal;

    @ManyToOne
    @JoinColumn(name="id_servicio", referencedColumnName="id_servicio")
    ServicioPrestado servicioPrestado;

    @ManyToOne
    @JoinColumn(name="numero_factura", referencedColumnName="numero")
    Factura factura;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public ServicioPrestado getServicioPrestado() {
        return servicioPrestado;
    }

    public void setServicioPrestado(ServicioPrestado servicioPrestado) {
        this.servicioPrestado = servicioPrestado;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.nombre);
        hash = 31 * hash + Objects.hashCode(this.precioUnitario);
        hash = 31 * hash + Objects.hashCode(this.cantidad);
        hash = 31 * hash + Objects.hashCode(this.precioTotal);
        hash = 31 * hash + Objects.hashCode(this.servicioPrestado);
        hash = 31 * hash + Objects.hashCode(this.factura);
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
        final ServicioFacturado other = (ServicioFacturado) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.precioUnitario, other.precioUnitario)) {
            return false;
        }
        if (!Objects.equals(this.cantidad, other.cantidad)) {
            return false;
        }
        if (!Objects.equals(this.precioTotal, other.precioTotal)) {
            return false;
        }
        if (!Objects.equals(this.servicioPrestado, other.servicioPrestado)) {
            return false;
        }
        return Objects.equals(this.factura, other.factura);
    }

    @Override
    public String toString() {
        return "ServicioFacturado{" + "nombre=" + nombre + ", precioTotal=" + precioTotal + '}';
    }
    
}
