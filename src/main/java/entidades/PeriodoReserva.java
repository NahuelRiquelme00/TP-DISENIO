/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="periodo_reserva")
public class PeriodoReserva implements Serializable {
    	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_periodo_reserva")
        Integer idPeriodoReserva;
        
        @Column(name="fecha_inicio")
	LocalDate fechaInicio;
        
        @Column(name="fecha_fin")
	LocalDate fechaFin;
        
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="numero_habitacion", referencedColumnName="numero")
        Habitacion habitacion;
        
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_reserva", referencedColumnName="id_reserva")        
        Reserva reserva;
}
