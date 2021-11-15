/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name="cheque")
public class Cheque extends MedioDePago {
	
	@Column(name="numero_cheque")
	Integer numeroCheque;
	
	@Column(name="fecha_cobro")
	LocalDate fechaCobro;
	
	@ManyToOne
	@JoinColumn(name="id_banco", referencedColumnName="id_banco")
	Banco banco;
	
}
