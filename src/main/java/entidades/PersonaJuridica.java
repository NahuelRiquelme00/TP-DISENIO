/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
/**
 *
 * @author Nahuel Riquelme
 */
@Entity
@Table(name="persona_juridica")
public class PersonaJuridica extends Persona implements Serializable {

	@Id
	@Column(name="cuit")
	Integer CUIT;
	
	@Column(name="razon_social")
	String razonSocial;
	
	@Column(name="telefono")
	String telefono;
	
	@Column(name="email")
	String email;
	
	@OneToMany(mappedBy="responsableDePago")
	List<NotaDeCredito> notasDeCredito;
	
	@OneToMany(mappedBy="responsableDePagoJuridico")
	List<Factura> facturas;
}
