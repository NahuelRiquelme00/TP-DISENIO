/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */

@Entity
@Table (name="tipo_posicion_frente_IVA")
public class TipoPosicionFrenteIVA implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column (name = "id_tipo_posicion_frente_IVA")
    private Integer idTipoPosicionFrenteIVA;
    
    @Column(name="nombre")
    private String nombre;
    
    @Column (name = "tipo_factura")
    @Enumerated(EnumType.STRING)
    private TipoFactura tipoFactura; 
    
//    @OneToMany(mappedBy= "tipoPosicionFrenteIVA")
//    private List<PersonaFisica> listaPersonas;

    public TipoPosicionFrenteIVA() {
    }

    public TipoPosicionFrenteIVA(String nombre, TipoFactura tipoFactura) {
        this.nombre = nombre;
        this.tipoFactura = tipoFactura;
    }

    public Integer getIdTipoPosicionFrenteIVA() {
        return idTipoPosicionFrenteIVA;
    }

    public void setIdTipoPosicionFrenteIVA(Integer idTipoPosicionFrenteIVA) {
        this.idTipoPosicionFrenteIVA = idTipoPosicionFrenteIVA;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    @Override
    public String toString() {
        return "TipoPosicionFrenteIVA{" + "nombre=" + nombre + '}';
    }
    
    
}
