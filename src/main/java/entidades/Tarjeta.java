/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Tarjeta extends MedioDePago {

    @Column(name="numero_tarjeta")
    Integer numeroTarjeta;

    @Column(name="fecha_vencimiento")
    LocalDate fechaVencimiento;

    @Column(name="nombre")
    String nombre;

    @Column(name="codigo_seguridad")
    Integer codigoSeguridad;

    public Integer getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(Integer numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCodigoSeguridad() {
        return codigoSeguridad;
    }

    public void setCodigoSeguridad(Integer codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.numeroTarjeta);
        hash = 59 * hash + Objects.hashCode(this.fechaVencimiento);
        hash = 59 * hash + Objects.hashCode(this.nombre);
        hash = 59 * hash + Objects.hashCode(this.codigoSeguridad);
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
        final Tarjeta other = (Tarjeta) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.numeroTarjeta, other.numeroTarjeta)) {
            return false;
        }
        if (!Objects.equals(this.fechaVencimiento, other.fechaVencimiento)) {
            return false;
        }
        return Objects.equals(this.codigoSeguridad, other.codigoSeguridad);
    }

    @Override
    public String toString() {
        return "Tarjeta{" + "numeroTarjeta=" + numeroTarjeta + ", nombre=" + nombre + '}';
    }
    
}
