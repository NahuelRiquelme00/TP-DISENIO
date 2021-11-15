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
@Table(name="habitacion")
public class Habitacion implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="numero")
	Integer numero;
	
	@Column(name="estado")
	@Enumerated(EnumType.STRING)
	TipoEstado estado;
	
	@Column(name="capacidad")
	Integer capacidad;
        
        @OneToMany(mappedBy="habitacion")
	List<Estadia>estadias;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="tipo_habitacion", referencedColumnName="nombre")
	TipoHabitacion tipoHabitacion;
        
        @OneToMany(mappedBy="habitacion")
        List<PeriodoReserva> periodosReserva;	

}
