/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import org.joda.money.Money;

@Entity
@Table(name="estadia")
public class Estadia implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_estadia")
    Integer idEstadia;

    @Column(name="fecha_inicio")
    LocalDate fechaInicio;

    @Column(name="fecha_fin")
    LocalDate fechaFin;

    @Column(name="costo", columnDefinition="bytea")
    Money costoNoche;

    @Column(name="descuento")
    Double descuento;

    @Column(name="costo_final", columnDefinition="bytea")
    Money costoFinal;

    @OneToOne
    @JoinColumn(name="numero_factura", referencedColumnName="numero")
    Factura factura;

    @ManyToOne
    @JoinColumn(name="id_persona_fisica", referencedColumnName="id_persona_fisica")
    PersonaFisica pasajeroResponsable;       

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
        name="pasajero",
        joinColumns=@JoinColumn(name="id_estadia",referencedColumnName="id_estadia"),
        inverseJoinColumns=@JoinColumn(name="id_persona_fisica",referencedColumnName="id_persona_fisica"))
    List<PersonaFisica> pasajeroAcompañante = new ArrayList<>();      

    @OneToMany
    @JoinColumn(name="id_estadia", referencedColumnName="id_estadia")
    List<ServicioPrestado> serviciosPrestados;

    @ManyToOne
    @JoinColumn(name="numero_habitacion", referencedColumnName="numero")
    Habitacion habitacion;

    public Integer getIdEstadia() {
        return idEstadia;
    }

    public void setIdEstadia(Integer idEstadia) {
        this.idEstadia = idEstadia;
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

    public Money getCostoNoche() {
        return costoNoche;
    }

    public void setCostoNoche(Money costoNoche) {
        this.costoNoche = costoNoche;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Money getCostoFinal() {
        return costoFinal;
    }

    public void setCostoFinal(Money costoFinal) {
        this.costoFinal = costoFinal;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public PersonaFisica getPasajeroResponsable() {
        return pasajeroResponsable;
    }

    public void setPasajeroResponsable(PersonaFisica pasajeroResponsable) {
        this.pasajeroResponsable = pasajeroResponsable;
    }

    public List<PersonaFisica> getPasajeroAcompañante() {
        return pasajeroAcompañante;
    }

    public void setPasajeroAcompañante(List<PersonaFisica> pasajeroAcompañante) {
        this.pasajeroAcompañante = pasajeroAcompañante;
    }
    
    public void addPasajeroAcompañante(PersonaFisica p){
        this.pasajeroAcompañante.add(p);
    }

    public List<ServicioPrestado> getServiciosPrestados() {
        return serviciosPrestados;
    }

    public void setServiciosPrestados(List<ServicioPrestado> serviciosPrestados) {
        this.serviciosPrestados = serviciosPrestados;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.idEstadia);
        hash = 67 * hash + Objects.hashCode(this.fechaInicio);
        hash = 67 * hash + Objects.hashCode(this.fechaFin);
        hash = 67 * hash + Objects.hashCode(this.costoNoche);
        hash = 67 * hash + Objects.hashCode(this.descuento);
        hash = 67 * hash + Objects.hashCode(this.costoFinal);
        hash = 67 * hash + Objects.hashCode(this.factura);
        hash = 67 * hash + Objects.hashCode(this.pasajeroResponsable);
        hash = 67 * hash + Objects.hashCode(this.pasajeroAcompañante);
        hash = 67 * hash + Objects.hashCode(this.serviciosPrestados);
        hash = 67 * hash + Objects.hashCode(this.habitacion);
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
        final Estadia other = (Estadia) obj;
        if (!Objects.equals(this.idEstadia, other.idEstadia)) {
            return false;
        }
        if (!Objects.equals(this.fechaInicio, other.fechaInicio)) {
            return false;
        }
        if (!Objects.equals(this.fechaFin, other.fechaFin)) {
            return false;
        }
        if (!Objects.equals(this.costoNoche, other.costoNoche)) {
            return false;
        }
        if (!Objects.equals(this.descuento, other.descuento)) {
            return false;
        }
        if (!Objects.equals(this.costoFinal, other.costoFinal)) {
            return false;
        }
        if (!Objects.equals(this.factura, other.factura)) {
            return false;
        }
        if (!Objects.equals(this.pasajeroResponsable, other.pasajeroResponsable)) {
            return false;
        }
        if (!Objects.equals(this.pasajeroAcompañante, other.pasajeroAcompañante)) {
            return false;
        }
        if (!Objects.equals(this.serviciosPrestados, other.serviciosPrestados)) {
            return false;
        }
        return Objects.equals(this.habitacion, other.habitacion);
    }

    @Override
    public String toString() {
        return "Estadia{" + "idEstadia=" + idEstadia + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + '}';
    }    
    
}
