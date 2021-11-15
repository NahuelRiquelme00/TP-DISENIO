/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Tarjeta extends MedioDePago {

	@Column(name="numero_tarjeta")
	Integer numeroTarjeta;
	
	@Column(name="fecha_vencimiento")
	LocalDate fechaVencimiento;
	
	@Column(name="nombre")
	String nombre;
	
	@Column(name="codigo_seguridad")
	Integer codigoSeguridad;
}
