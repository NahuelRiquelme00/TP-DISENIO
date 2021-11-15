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
@Table(name="servicio_facturado")
public class ServicioFacturado implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="nombre")
	String nombre;
	
	@Column(name="precio_unitario", columnDefinition="bytea")
	Money precioUnitario;
	
	@Column(name="cantidad")
	Integer cantidad;
	
	@Column(name="precio_total", columnDefinition="bytea")
	Money precioTotal;
	
	@ManyToOne
	@JoinColumn(name="id_servicio", referencedColumnName="id_servicio")
	ServicioPrestado servicioPrestado;
	
	@ManyToOne
	@JoinColumn(name="numero_factura", referencedColumnName="numero")
	Factura factura;
}
