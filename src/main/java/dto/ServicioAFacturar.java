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
public class ServicioAFacturar {
    
    Integer idServicioPrestado;
    Integer cantidad;
    BigDecimal precioTotal;

    public ServicioAFacturar(){
        
    }

    public Integer getIdServicioPrestado() {
        return idServicioPrestado;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }
    
    public void setIdServicioPrestado(Integer idServicioPrestado) {
        this.idServicioPrestado = idServicioPrestado;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }    
    
}
