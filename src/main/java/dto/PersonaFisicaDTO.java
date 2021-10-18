/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidades.TipoDocumento;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Nahuel Riquelme
 */
public class PersonaFisicaDTO implements Comparable<PersonaFisicaDTO>{
    
    private Integer id;
    private String apellido;
    private String nombres;
    private String tipoDocumento;
    private Integer nroDocumento;
    private String fechaNacimiento;
    private String email;
    private String ocupacion;
    private String nacionalidad;
    private String telefono;
    private String calle;
    private Integer numero;
    private String departamento;
    private String piso;
    private Integer codigoPostal;
    private Integer idPosicionIVA;
    private Integer idLocalidad;
    private Integer idProvincia;
    private Integer idPais;
    private String categoria;
    
    public PersonaFisicaDTO(Integer id){
        this.id = id;
    }

    public PersonaFisicaDTO(String apellido, String nombres, String tipoDocumento, Integer nroDocumento, String fechaNacimiento, String email, String ocupacion, String nacionalidad, String telefono, String calle, Integer numero, String departamento, String piso, Integer codigoPostal, Integer posicionIVA, Integer idLocalidad, Integer idProvincia, Integer idPais) {
        this.apellido = apellido;
        this.nombres = nombres;
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.ocupacion = ocupacion;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
        this.calle = calle;
        this.numero = numero;
        this.departamento = departamento;
        this.piso = piso;
        this.codigoPostal = codigoPostal;
        this.idPosicionIVA = posicionIVA;
        this.idLocalidad = idLocalidad;
        this.idProvincia = idProvincia;
        this.idPais = idPais;
    }

    //Para la interface
    public PersonaFisicaDTO(Integer id, String apellido, String nombres, String tipoDocumento, Integer nroDocumento) {
        this.id = id;
        this.apellido = apellido;
        this.nombres = nombres;
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(Integer nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
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

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public Integer getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Integer getIdPosicionIVA() {
        return idPosicionIVA;
    }

    public void setIdPosicionIVA(Integer idPosicionIVA) {
        this.idPosicionIVA = idPosicionIVA;
    }

    public Integer getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(Integer idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public Integer getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Integer idProvincia) {
        this.idProvincia = idProvincia;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    } 

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final PersonaFisicaDTO other = (PersonaFisicaDTO) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(PersonaFisicaDTO o) {
        return o.nombres.compareToIgnoreCase(this.nombres);
    } 
    
}
