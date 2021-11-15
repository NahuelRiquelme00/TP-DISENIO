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
@Table(name="cheque")
public class Cheque extends MedioDePago {
	
    @Column(name="numero_cheque")
    Integer numeroCheque;

    @Column(name="fecha_cobro")
    LocalDate fechaCobro;

    @ManyToOne
    @JoinColumn(name="id_banco", referencedColumnName="id_banco")
    Banco banco;

    public Integer getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(Integer numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public LocalDate getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(LocalDate fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.numeroCheque);
        hash = 67 * hash + Objects.hashCode(this.fechaCobro);
        hash = 67 * hash + Objects.hashCode(this.banco);
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
        final Cheque other = (Cheque) obj;
        if (!Objects.equals(this.numeroCheque, other.numeroCheque)) {
            return false;
        }
        if (!Objects.equals(this.fechaCobro, other.fechaCobro)) {
            return false;
        }
        if (!Objects.equals(this.banco, other.banco)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cheque{" + "numeroCheque=" + numeroCheque + ", fechaCobro=" + fechaCobro + ", banco=" + banco + '}';
    }
    
}
