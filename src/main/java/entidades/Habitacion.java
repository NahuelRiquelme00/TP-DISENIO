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
@Table(name="habitacion")
public class Habitacion implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="numero")
    Integer numero;

    @Column(name="estado")
    @Enumerated(EnumType.STRING)
    TipoEstado estado;

    @Column(name="capacidad")
    Integer capacidad;

    @OneToMany(mappedBy="habitacion")
    List<Estadia>estadias;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="tipo_habitacion", referencedColumnName="nombre")
    TipoHabitacion tipoHabitacion;

    @OneToMany(mappedBy="habitacion")
    List<PeriodoReserva> periodosReserva;

    public Habitacion() {
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public TipoEstado getEstado() {
        return estado;
    }

    public void setEstado(TipoEstado estado) {
        this.estado = estado;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public List<Estadia> getEstadias() {
        return estadias;
    }

    public void setEstadias(List<Estadia> estadias) {
        this.estadias = estadias;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public List<PeriodoReserva> getPeriodosReserva() {
        return periodosReserva;
    }

    public void setPeriodosReserva(List<PeriodoReserva> periodosReserva) {
        this.periodosReserva = periodosReserva;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.numero);
        hash = 83 * hash + Objects.hashCode(this.estado);
        hash = 83 * hash + Objects.hashCode(this.capacidad);
        hash = 83 * hash + Objects.hashCode(this.estadias);
        hash = 83 * hash + Objects.hashCode(this.tipoHabitacion);
        hash = 83 * hash + Objects.hashCode(this.periodosReserva);
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
        final Habitacion other = (Habitacion) obj;
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (this.estado != other.estado) {
            return false;
        }
        if (!Objects.equals(this.capacidad, other.capacidad)) {
            return false;
        }
        if (!Objects.equals(this.estadias, other.estadias)) {
            return false;
        }
        if (!Objects.equals(this.tipoHabitacion, other.tipoHabitacion)) {
            return false;
        }
        return Objects.equals(this.periodosReserva, other.periodosReserva);
    }

    @Override
    public String toString() {
        return "Habitacion{" + "numero=" + numero + ", tipoHabitacion=" + tipoHabitacion + '}';
    }

}
