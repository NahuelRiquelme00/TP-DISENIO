/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="periodo_reserva")
public class PeriodoReserva implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_periodo_reserva")
    Integer idPeriodoReserva;

    @Column(name="fecha_inicio")
    LocalDate fechaInicio;

    @Column(name="fecha_fin")
    LocalDate fechaFin;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="numero_habitacion", referencedColumnName="numero")
    Habitacion habitacion;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_reserva", referencedColumnName="id_reserva")        
    Reserva reserva;

    public Integer getIdPeriodoReserva() {
        return idPeriodoReserva;
    }

    public void setIdPeriodoReserva(Integer idPeriodoReserva) {
        this.idPeriodoReserva = idPeriodoReserva;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.idPeriodoReserva);
        hash = 11 * hash + Objects.hashCode(this.fechaInicio);
        hash = 11 * hash + Objects.hashCode(this.fechaFin);
        hash = 11 * hash + Objects.hashCode(this.habitacion);
        hash = 11 * hash + Objects.hashCode(this.reserva);
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
        final PeriodoReserva other = (PeriodoReserva) obj;
        if (!Objects.equals(this.idPeriodoReserva, other.idPeriodoReserva)) {
            return false;
        }
        if (!Objects.equals(this.fechaInicio, other.fechaInicio)) {
            return false;
        }
        if (!Objects.equals(this.fechaFin, other.fechaFin)) {
            return false;
        }
        if (!Objects.equals(this.habitacion, other.habitacion)) {
            return false;
        }
        return Objects.equals(this.reserva, other.reserva);
    }

    @Override
    public String toString() {
        return "PeriodoReserva{" + "idPeriodoReserva=" + idPeriodoReserva + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + '}';
    }
    
}
