/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
/**
 *
 * @author Nahuel Riquelme
 */
@Entity
@Table(name="persona_juridica")
public class PersonaJuridica extends Persona implements Serializable {

    @Id
    @Column(name="cuit", columnDefinition="int8")
    BigInteger CUIT;

    @Column(name="razon_social")
    String razonSocial;

    @Column(name="telefono")
    String telefono;

    @Column(name="email")
    String email;

    @OneToMany(mappedBy="responsableDePago")
    List<NotaDeCredito> notasDeCredito;

    @OneToMany(mappedBy="responsableDePagoJuridico")
    List<Factura> facturas;

    public BigInteger getCUIT() {
        return CUIT;
    }

    public void setCUIT(BigInteger CUIT) {
        this.CUIT = CUIT;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<NotaDeCredito> getNotasDeCredito() {
        return notasDeCredito;
    }

    public void setNotasDeCredito(List<NotaDeCredito> notasDeCredito) {
        this.notasDeCredito = notasDeCredito;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.CUIT);
        hash = 59 * hash + Objects.hashCode(this.razonSocial);
        hash = 59 * hash + Objects.hashCode(this.telefono);
        hash = 59 * hash + Objects.hashCode(this.email);
        hash = 59 * hash + Objects.hashCode(this.notasDeCredito);
        hash = 59 * hash + Objects.hashCode(this.facturas);
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
        final PersonaJuridica other = (PersonaJuridica) obj;
        if (!Objects.equals(this.razonSocial, other.razonSocial)) {
            return false;
        }
        if (!Objects.equals(this.telefono, other.telefono)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.CUIT, other.CUIT)) {
            return false;
        }
        if (!Objects.equals(this.notasDeCredito, other.notasDeCredito)) {
            return false;
        }
        return Objects.equals(this.facturas, other.facturas);
    }

    @Override
    public String toString() {
        return "PersonaJuridica{" + "CUIT=" + CUIT + ", razonSocial=" + razonSocial + '}';
    }
    
}
