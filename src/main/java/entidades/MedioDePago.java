/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;


@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class MedioDePago implements Serializable {

    @Id
    @Column(name="id_medio_de_pago")
    Integer idMedioDePago;

    @Column(name="importe", columnDefinition="numeric")
    BigDecimal importe;

    @Column(name="cotizacion")
    Double cotizacion;

    @Column(name="importe_en_pesos", columnDefinition="numeric")
    BigDecimal importeEnPesos;

    public Integer getIdMedioDePago() {
        return idMedioDePago;
    }

    public void setIdMedioDePago(Integer idMedioDePago) {
        this.idMedioDePago = idMedioDePago;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Double getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Double cotizacion) {
        this.cotizacion = cotizacion;
    }

    public BigDecimal getImporteEnPesos() {
        return importeEnPesos;
    }

    public void setImporteEnPesos(BigDecimal importeEnPesos) {
        this.importeEnPesos = importeEnPesos;
    }
        
}