/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(List<Localidad> localidades) {
        this.localidades = localidades;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.idBanco);
        hash = 17 * hash + Objects.hashCode(this.nombre);
        hash = 17 * hash + Objects.hashCode(this.localidades);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Banco other = (Banco) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.idBanco, other.idBanco)) {
            return false;
        }
        if (!Objects.equals(this.localidades, other.localidades)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Banco{" + "idBanco=" + idBanco + ", nombre=" + nombre + '}';
    }    
        
}
