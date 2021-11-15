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
@Table(name="banco")
public class Banco implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_banco")
	Integer idBanco;
        
	@Column(name="nombre_banco")
	String nombre;
        
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="plaza",
	joinColumns=@JoinColumn(name="id_banco",referencedColumnName="id_banco"),
	inverseJoinColumns=@JoinColumn(name="id_localidad",referencedColumnName="id_localidad"))
	List <Localidad> localidades;
}
