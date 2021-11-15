/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author usuario
 */
@MappedSuperclass
public class Persona {
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_direccion", referencedColumnName = "id_direccion")
    Direccion direccion;
    
    @ManyToOne
    @JoinColumn(name = "id_tipo_posicion_frente_IVA", referencedColumnName = "id_tipo_posicion_frente_IVA")
    TipoPosicionFrenteIVA tipoPosicionFrenteIVA;

    public TipoPosicionFrenteIVA getTipoPosicionFrenteIVA() {
        return tipoPosicionFrenteIVA;
    }

    public void setTipoPosicionFrenteIVA(TipoPosicionFrenteIVA tipoPosicionFrenteIVA) {
        this.tipoPosicionFrenteIVA = tipoPosicionFrenteIVA;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    
}
