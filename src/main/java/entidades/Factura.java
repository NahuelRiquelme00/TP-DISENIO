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
/**
 *
 * @author Nahuel Riquelme
 */
@Entity
@Table(name="factura")
public class Factura implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="numero")
	Integer numero;
	
	@Column(name="importe_neto", columnDefinition="bytea")
	Money importeNeto;
	
	@Column(name="tipo")
	@Enumerated(EnumType.STRING)
	TipoFactura tipo;
	
	@Column(name="importe_total", columnDefinition="bytea")
	Money importeTotal;
	
	@Column(name="fecha_emision")
	LocalDate fechaEmision;
        
        @ManyToOne
	@JoinColumn(name="cuit", referencedColumnName="cuit")
	PersonaJuridica responsableDePagoJuridico;
        
 	@ManyToOne
	@JoinColumn(name="numero_nota", referencedColumnName="numero_nota")
	NotaDeCredito notaDeCredito;       
	
	@OneToOne
	@JoinColumn(name="id_pago", referencedColumnName="id_pago")
	Pago pago;
	
 	@ManyToOne
	@JoinColumn(name="id_persona_fisica", referencedColumnName="id_persona_fisica")	
	PersonaFisica responsableDePagoFisico;
        
        @OneToOne(mappedBy="factura")
        Estadia estadia;
	
	@OneToMany(mappedBy="factura")
	List<ServicioFacturado>serviciosFacturados;
	
}
