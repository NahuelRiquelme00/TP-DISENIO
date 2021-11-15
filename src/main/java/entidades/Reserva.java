/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name="reserva")
public class Reserva implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_reserva")
    Integer idReserva;

    @Column(name="nombre")
    String nombre;

    @Column(name="apellido")
    String apellido;

    @Column(name="telefono")
    String telefono;

    @OneToMany(mappedBy="reserva")
    List<PeriodoReserva> periodosReserva;

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<PeriodoReserva> getPeriodosReserva() {
        return periodosReserva;
    }

    public void setPeriodosReserva(List<PeriodoReserva> periodosReserva) {
        this.periodosReserva = periodosReserva;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.idReserva);
        hash = 67 * hash + Objects.hashCode(this.nombre);
        hash = 67 * hash + Objects.hashCode(this.apellido);
        hash = 67 * hash + Objects.hashCode(this.telefono);
        hash = 67 * hash + Objects.hashCode(this.periodosReserva);
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
        final Reserva other = (Reserva) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.apellido, other.apellido)) {
            return false;
        }
        if (!Objects.equals(this.telefono, other.telefono)) {
            return false;
        }
        if (!Objects.equals(this.idReserva, other.idReserva)) {
            return false;
        }
        return Objects.equals(this.periodosReserva, other.periodosReserva);
    }

    @Override
    public String toString() {
        return "Reserva{" + "idReserva=" + idReserva + ", apellido=" + apellido + '}';
    }
    
}
