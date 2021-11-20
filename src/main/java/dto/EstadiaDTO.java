/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.List;

/**
 *
 * @author Nahuel Riquelme
 */
public class EstadiaDTO {
    
    Integer idEstadia;
    
    String fechaInicio;

    String fechaFin;

    Integer idPasajeroResponsable;       
    
    List<Integer> idsPasajeroAcompañante;      
    
    Integer idHabitacion;

    public EstadiaDTO() {
    }

    public EstadiaDTO(Integer idEstadia, String fechaInicio, String fechaFin, Integer idPasajeroResponsable, List<Integer> idsPasajeroAcompañante, Integer idHabitacion) {
        this.idEstadia = idEstadia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idPasajeroResponsable = idPasajeroResponsable;
        this.idsPasajeroAcompañante = idsPasajeroAcompañante;
        this.idHabitacion = idHabitacion;
    }

    public EstadiaDTO(String fechaInicio, String fechaFin, Integer idPasajeroResponsable, List<Integer> idsPasajeroAcompañante, Integer idHabitacion) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idPasajeroResponsable = idPasajeroResponsable;
        this.idsPasajeroAcompañante = idsPasajeroAcompañante;
        this.idHabitacion = idHabitacion;
    }

    public Integer getIdEstadia() {
        return idEstadia;
    }

    public void setIdEstadia(Integer idEstadia) {
        this.idEstadia = idEstadia;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getIdPasajeroResponsable() {
        return idPasajeroResponsable;
    }

    public void setIdPasajeroResponsable(Integer idPasajeroResponsable) {
        this.idPasajeroResponsable = idPasajeroResponsable;
    }

    public List<Integer> getIdsPasajeroAcompañante() {
        return idsPasajeroAcompañante;
    }

    public void setIdsPasajeroAcompañante(List<Integer> idsPasajeroAcompañante) {
        this.idsPasajeroAcompañante = idsPasajeroAcompañante;
    }

    public Integer getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
    }
    
}
