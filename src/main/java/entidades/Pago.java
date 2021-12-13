/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name="pago")
public class Pago implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_pago")
    Integer idPago;

    @Column(name="monto_total", columnDefinition="numeric")
    BigDecimal montoTotal;

    @Column(name="fecha_y_hora")
    LocalDateTime fechaYHora;

    @Column(name="vuelto", columnDefinition="numeric")
    BigDecimal vuelto;

    @OneToMany
    @JoinColumn(name="id_medio_de_pago", referencedColumnName="id_pago")
    List<MedioDePago>mediosDePago;

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public BigDecimal getVuelto() {
        return vuelto;
    }

    public void setVuelto(BigDecimal vuelto) {
        this.vuelto = vuelto;
    }

    public List<MedioDePago> getMediosDePago() {
        return mediosDePago;
    }

    public void setMediosDePago(List<MedioDePago> mediosDePago) {
        this.mediosDePago = mediosDePago;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.idPago);
        hash = 53 * hash + Objects.hashCode(this.montoTotal);
        hash = 53 * hash + Objects.hashCode(this.fechaYHora);
        hash = 53 * hash + Objects.hashCode(this.vuelto);
        hash = 53 * hash + Objects.hashCode(this.mediosDePago);
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
        final Pago other = (Pago) obj;
        if (!Objects.equals(this.idPago, other.idPago)) {
            return false;
        }
        if (!Objects.equals(this.montoTotal, other.montoTotal)) {
            return false;
        }
        if (!Objects.equals(this.fechaYHora, other.fechaYHora)) {
            return false;
        }
        if (!Objects.equals(this.vuelto, other.vuelto)) {
            return false;
        }
        return Objects.equals(this.mediosDePago, other.mediosDePago);
    }

    @Override
    public String toString() {
        return "Pago{" + "idPago=" + idPago + ", montoTotal=" + montoTotal + '}';
    }    
    
}