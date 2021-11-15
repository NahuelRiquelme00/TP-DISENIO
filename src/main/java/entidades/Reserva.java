/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="reserva")
public class Reserva implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_reserva")
	Integer idReserva;
	
	@Column(name="nombre")
	String nombre;
	
	@Column(name="apellido")
	String apellido;
	
	@Column(name="telefono")
	String telefono;
	
        @OneToMany(mappedBy="reserva")
        List<PeriodoReserva> periodosReserva;
}
