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
import org.joda.money.Money;

@Entity
@Table(name="tipo_habitacion")
public class TipoHabitacion implements Serializable {

    @Id
    @Column(name="nombre")
    String nombre;

    @Column(name="precio_actual", columnDefinition="numeric")
    BigDecimal precioActual;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioActual() {
        return precioActual;
    }

    public void setPrecioActual(BigDecimal precioActual) {
        this.precioActual = precioActual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.nombre);
        hash = 59 * hash + Objects.hashCode(this.precioActual);
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
        final TipoHabitacion other = (TipoHabitacion) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return Objects.equals(this.precioActual, other.precioActual);
    }

    @Override
    public String toString() {
        return "TipoHabitacion{" + "nombre=" + nombre + ", precioActual=" + precioActual + '}';
    }
    
    
	
}

