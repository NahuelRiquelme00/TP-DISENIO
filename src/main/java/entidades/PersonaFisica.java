/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */
@Entity
@Table (name="persona_fisica")
public class PersonaFisica extends Persona implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_persona_fisica")
    private Integer idPersonaFisica;
    
    @Column(name="apellido")
    private String apellido;
    
    @Column(name="nombres")
    private String nombres;
    
    @Column(name="tipo_documento")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;
    
    @Column(name="numero_documento")
    private Integer nroDocumento;
    
    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(name="email")
    private String email;
    
    @Column(name="ocupacion")
    private String ocupacion;
    
    @Column(name="nacionalidad")
    private String nacionalidad;
    
    @Column(name="telefono")
    private String telefono;

    public PersonaFisica() {
    }

    public PersonaFisica(String apellido, String nombres, TipoDocumento tipoDocumento, Integer nroDocumento, LocalDate fechaNacimiento, String email, String ocupacion, String nacionalidad, String telefono) {
        this.apellido = apellido;
        this.nombres = nombres;
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.ocupacion = ocupacion;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
    }

    public Integer getIdPersonaFisica() {
        return idPersonaFisica;
    }

    public void setIdPersonaFisica(Integer idPersonaFisica) {
        this.idPersonaFisica = idPersonaFisica;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(Integer nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "PersonaFisica{" + "apellido=" + apellido + ", nombres=" + nombres + ", direccion=" + direccion + ", tipoPosicionFrenteIVA=" + tipoPosicionFrenteIVA + '}';
    }

}
