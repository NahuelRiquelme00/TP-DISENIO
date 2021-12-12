/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author Nahuel Riquelme
 */
@Entity
@Table(name="nota_de_credito")
public class NotaDeCredito implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="numero_nota")
    Integer numeroNota;

    @Column(name="importe_neto", columnDefinition="numeric")
    BigDecimal importeNeto;

    @Column(name="iva")
    Double IVA;

    @Column(name="importe_total", columnDefinition="numeric")
    BigDecimal importeTotal;

    @ManyToOne
    @JoinColumn(name="cuit", referencedColumnName="cuit", columnDefinition="int8")
    PersonaJuridica responsableDePago;

    @OneToMany(mappedBy="notaDeCredito")
    List<Factura> facturas;

    public Integer getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Integer numeroNota) {
        this.numeroNota = numeroNota;
    }

    public BigDecimal getImporteNeto() {
        return importeNeto;
    }

    public void setImporteNeto(BigDecimal importeNeto) {
        this.importeNeto = importeNeto;
    }

    public Double getIVA() {
        return IVA;
    }

    public void setIVA(Double IVA) {
        this.IVA = IVA;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public PersonaJuridica getResponsableDePago() {
        return responsableDePago;
    }

    public void setResponsableDePago(PersonaJuridica responsableDePago) {
        this.responsableDePago = responsableDePago;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.numeroNota);
        hash = 29 * hash + Objects.hashCode(this.importeNeto);
        hash = 29 * hash + Objects.hashCode(this.IVA);
        hash = 29 * hash + Objects.hashCode(this.importeTotal);
        hash = 29 * hash + Objects.hashCode(this.responsableDePago);
        hash = 29 * hash + Objects.hashCode(this.facturas);
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
        final NotaDeCredito other = (NotaDeCredito) obj;
        if (!Objects.equals(this.numeroNota, other.numeroNota)) {
            return false;
        }
        if (!Objects.equals(this.importeNeto, other.importeNeto)) {
            return false;
        }
        if (!Objects.equals(this.IVA, other.IVA)) {
            return false;
        }
        if (!Objects.equals(this.importeTotal, other.importeTotal)) {
            return false;
        }
        if (!Objects.equals(this.responsableDePago, other.responsableDePago)) {
            return false;
        }
        return Objects.equals(this.facturas, other.facturas);
    }

    @Override
    public String toString() {
        return "NotaDeCredito{" + "numeroNota=" + numeroNota + ", importeTotal=" + importeTotal + '}';
    }
    
}
