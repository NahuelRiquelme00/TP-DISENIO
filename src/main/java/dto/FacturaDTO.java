/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;


import entidades.TipoFactura;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Fede
 */
public class FacturaDTO {
    
    String fechaEmision;//Listo
    BigDecimal importeNeto;//Listo
    BigDecimal importeFinal;//Listo
    Integer idResponsablePago;//Listo
    BigInteger cuitResponsableJuridico;
    List<ServicioAFacturar> serviciosAFacturar;
    TipoFactura tipoFactura;//Listo
    Integer idEstadia;//Listo
    
    public FacturaDTO(){
    }

    public FacturaDTO(String fechaEmision, BigDecimal costoFactura, BigDecimal importeTotal, Integer idResponsable, TipoFactura tipoF) {
        this.fechaEmision = fechaEmision;
        this.importeNeto = costoFactura;
        this.importeFinal = importeTotal;
        this.idResponsablePago = idResponsable;
        this.tipoFactura = tipoF;
    }

    public FacturaDTO(String fechaEmision, BigDecimal costoFactura, BigDecimal importeTotal, BigInteger idResponsableJ, TipoFactura tipoF) {
        this.fechaEmision = fechaEmision;
        this.importeNeto = costoFactura;
        this.importeFinal = importeTotal;
        this.cuitResponsableJuridico = idResponsableJ;
        this.tipoFactura = tipoF;
    }

    //Getters
    public String getFechaEmision() {
        return fechaEmision;
    }
    
    public BigDecimal getImporteNeto() {
        return importeNeto;
    }

    public BigDecimal getImporteTotal() {
        return importeFinal;
    }
    
    public Integer getIdResponsable() {
        return idResponsablePago;
    }
    
    public BigInteger getCuitResponsableJuridico() {
        return cuitResponsableJuridico;
    }

    public List<ServicioAFacturar> getServiciosAFacturar() {
        return serviciosAFacturar;
    }
    
    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }
    
    public Integer getIdEstadia() {
        return idEstadia;
    }

    //Setters
    public void setServiciosAFacturar(List<ServicioAFacturar> serviciosAFacturar) {
        this.serviciosAFacturar = serviciosAFacturar;
    }
    
    public void setIdEstadia(Integer idEstadia) {
        this.idEstadia = idEstadia;
    }
    
}
