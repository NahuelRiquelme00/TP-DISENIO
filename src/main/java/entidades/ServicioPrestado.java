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
import org.joda.money.Money;

@Entity
@Table(name="servicio_prestado")
public class ServicioPrestado implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_servicio")
	Integer idServicio;
	
	@Column(name="nombre")
	String nombre;
	
	@Column(name="precio", columnDefinition="bytea")
	Money precio;
	
	@Column(name="cantidad")
	Integer cantidad;
	
	@Column(name="fecha")
	LocalDate fecha;
        
        @Column(name="tipo")
	@Enumerated(EnumType.STRING)
	TipoServicio tipo;
        
	@OneToMany(mappedBy="servicioPrestado")
	List<ServicioFacturado>serviciosFacturados;
}