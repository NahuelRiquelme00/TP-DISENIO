/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.*;
import org.joda.money.Money;


@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class MedioDePago implements Serializable {

        @Id
	@Column(name="id_medio_de_pago")
	Integer idMedioDePago;
	
	@Column(name="importe", columnDefinition="bytea")
	Money importe;
	
	@Column(name="cotizacion")
	Double cotizacion;
	
	@Column(name="importe_en_pesos", columnDefinition="bytea")
	Money importeEnPesos;
}
