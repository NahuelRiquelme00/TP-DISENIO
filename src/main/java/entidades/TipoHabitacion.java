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
@Table(name="tipo_habitacion")
public class TipoHabitacion implements Serializable {

	@Id
	@Column(name="nombre")
	String nombre;
	
	@Column(name="precio_actual", columnDefinition="bytea")
	Money precioActual;
	
}

