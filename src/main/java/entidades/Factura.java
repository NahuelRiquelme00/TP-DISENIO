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
//import org.joda.money.Money;
/**
 *
 * @author Nahuel Riquelme
 */
@Entity
@Table(name="factura")
public class Factura implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="numero")
    Integer numero;

    @Column(name="importe_neto", columnDefinition="numeric")
    BigDecimal importeNeto;

    @Column(name="tipo")
    @Enumerated(EnumType.STRING)
    TipoFactura tipo;

    @Column(name="importe_total", columnDefinition="numeric")
    BigDecimal importeTotal;

    @Column(name="fecha_emision")
    LocalDate fechaEmision;

    @ManyToOne
    @JoinColumn(name="cuit", referencedColumnName="cuit")
    PersonaJuridica responsableDePagoJuridico;

    @ManyToOne
    @JoinColumn(name="numero_nota", referencedColumnName="numero_nota")
    NotaDeCredito notaDeCredito;       

    @OneToOne
    @JoinColumn(name="id_pago", referencedColumnName="id_pago")
    Pago pago;

    @ManyToOne
    @JoinColumn(name="id_persona_fisica", referencedColumnName="id_persona_fisica")	
    PersonaFisica responsableDePagoFisico;

    @OneToOne(mappedBy="factura")
    Estadia estadia;

    @OneToMany(mappedBy="factura")
    List<ServicioFacturado>serviciosFacturados;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public BigDecimal getImporteNeto() {
        return importeNeto;
    }

    public void setImporteNeto(BigDecimal importeNeto) {
        this.importeNeto = importeNeto;
    }

    public TipoFactura getTipo() {
        return tipo;
    }

    public void setTipo(TipoFactura tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public PersonaJuridica getResponsableDePagoJuridico() {
        return responsableDePagoJuridico;
    }

    public void setResponsableDePagoJuridico(PersonaJuridica responsableDePagoJuridico) {
        this.responsableDePagoJuridico = responsableDePagoJuridico;
    }

    public NotaDeCredito getNotaDeCredito() {
        return notaDeCredito;
    }

    public void setNotaDeCredito(NotaDeCredito notaDeCredito) {
        this.notaDeCredito = notaDeCredito;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public PersonaFisica getResponsableDePagoFisico() {
        return responsableDePagoFisico;
    }

    public void setResponsableDePagoFisico(PersonaFisica responsableDePagoFisico) {
        this.responsableDePagoFisico = responsableDePagoFisico;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
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
        hash = 59 * hash + Objects.hashCode(this.numero);
        hash = 59 * hash + Objects.hashCode(this.importeNeto);
        hash = 59 * hash + Objects.hashCode(this.tipo);
        hash = 59 * hash + Objects.hashCode(this.importeTotal);
        hash = 59 * hash + Objects.hashCode(this.fechaEmision);
        hash = 59 * hash + Objects.hashCode(this.responsableDePagoJuridico);
        hash = 59 * hash + Objects.hashCode(this.notaDeCredito);
        hash = 59 * hash + Objects.hashCode(this.pago);
        hash = 59 * hash + Objects.hashCode(this.responsableDePagoFisico);
        hash = 59 * hash + Objects.hashCode(this.estadia);
        hash = 59 * hash + Objects.hashCode(this.serviciosFacturados);
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
        final Factura other = (Factura) obj;
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.importeNeto, other.importeNeto)) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        if (!Objects.equals(this.importeTotal, other.importeTotal)) {
            return false;
        }
        if (!Objects.equals(this.fechaEmision, other.fechaEmision)) {
            return false;
        }
        if (!Objects.equals(this.responsableDePagoJuridico, other.responsableDePagoJuridico)) {
            return false;
        }
        if (!Objects.equals(this.notaDeCredito, other.notaDeCredito)) {
            return false;
        }
        if (!Objects.equals(this.pago, other.pago)) {
            return false;
        }
        if (!Objects.equals(this.responsableDePagoFisico, other.responsableDePagoFisico)) {
            return false;
        }
        if (!Objects.equals(this.estadia, other.estadia)) {
            return false;
        }
        return Objects.equals(this.serviciosFacturados, other.serviciosFacturados);
    }

    @Override
    public String toString() {
        return "Factura{" + "numero=" + numero + ", importeTotal=" + importeTotal + '}';
    }
    	
}
