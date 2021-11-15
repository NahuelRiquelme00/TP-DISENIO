/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
//import org.javamoney.moneta.Money;
import org.joda.money.Money;

@Entity
@Table(name="estadia")
public class Estadia implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_estadia")
	Integer idEstadia;
	
	@Column(name="fecha_inicio")
	LocalDate fechaInicio;
	
	@Column(name="fecha_fin")
	LocalDate fechaFin;
	
        @Column(name="costo", columnDefinition="bytea")
        Money costoNoche;
	
	@Column(name="descuento")
	Double descuento;
	
	@Column(name="costo_final", columnDefinition="bytea")
	Money costoFinal;
        
 	@OneToOne
	@JoinColumn(name="numero_factura", referencedColumnName="numero")
        Factura factura;
        
	@ManyToOne
	@JoinColumn(name="id_persona_fisica", referencedColumnName="id_persona_fisica")
 	PersonaFisica pasajeroResponsable;       
        
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="pasajero",
	joinColumns=@JoinColumn(name="id_estadia",referencedColumnName="id_estadia"),
	inverseJoinColumns=@JoinColumn(name="id_persona_fisica",referencedColumnName="id_persona_fisica"))
	List<PersonaFisica> pasajeroAcompañante;      
   
	@OneToMany
	@JoinColumn(name="id_estadia", referencedColumnName="id_estadia")
	List<ServicioPrestado> serviciosPrestados;
	
	@ManyToOne
	@JoinColumn(name="numero_habitacion", referencedColumnName="numero")
	Habitacion habitacion;
}
