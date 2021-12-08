/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author Fede
 */
public class ServicioPrestadoDTO {
    
    String nombreConsumo;
    BigDecimal precioUnitario;
    int unidadesAPagar;
    int unidadesTotales;
    BigDecimal costoTotal;
    String descripcion;
    
    
    
    public ServicioPrestadoDTO(){
        
    }

    public void setNombreConsumo(String nombre) {
        this.nombreConsumo = nombre;
    }

    public void setPrecioUnitario(BigDecimal precioU) {
        this.precioUnitario = precioU;
    }

    public void setUnidadesAPagar(int uPagar) {
        this.unidadesAPagar = uPagar;
    }

    public void setUnidadesTotales(int uTotales) {
        this.unidadesTotales = uTotales;
    }

    public void setCostoTotal(BigDecimal costo) {
        this.costoTotal = costo;
    }

    public void setDescripcion(String desc) {
        this.descripcion = desc;
    }

    public String getNombreConsumo() {
        return nombreConsumo;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public Integer getUnidadesAPagar() {
        return unidadesAPagar;
    }

    public Integer getUnidadesTotales() {
        return unidadesTotales;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
}
