/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
import org.joda.money.Money;

@Entity
@Table(name="pago")
public class Pago implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_pago")
	Integer idPago;
	
	@Column(name="monto_total", columnDefinition="bytea")
	Money montoTotal;
	
	@Column(name="fecha_y_hora")
	LocalDateTime fechaYHora;
	
        @Column(name="vuelto", columnDefinition="bytea")
	Money vuelto;
        
        @OneToMany
	@JoinColumn(name="id_medio_de_pago", referencedColumnName="id_pago")
	List<MedioDePago>mediosDePago;
}
