/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.joda.money.Money;
/**
 *
 * @author Nahuel Riquelme
 */
@Entity
@Table(name="nota_de_credito")
public class NotaDeCredito implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="numero_nota")
	Integer numeroNota;
	
	@Column(name="importe_neto", columnDefinition="bytea")
	Money importeNeto;
	
	@Column(name="iva")
	Double IVA;
	
	@Column(name="importe_total", columnDefinition="bytea")
	Money importeTotal;
	
	@ManyToOne
	@JoinColumn(name="cuit", referencedColumnName="cuit")
	PersonaJuridica responsableDePago;
        
	@OneToMany(mappedBy="notaDeCredito")
	List<Factura> facturas;
}
