/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;


import entidades.TipoFactura;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Fede
 */
public class FacturaDTO {
    
    Integer numero;//No es necesario me parece
    String fechaEmision;//Listo
    BigDecimal importeNeto;//Listo
    BigDecimal importeFinal;//Listo
    Integer idResponsablePago;//Listo
    Integer cuitResponsableJuridico;
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

    public void setIdEstadia(Integer idEstadia) {
        this.idEstadia = idEstadia;
    }

    public Integer getIdEstadia() {
        return idEstadia;
    }

    public Integer getIdResponsable() {
        return idResponsablePago;
    }

    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }

    public List<ServicioAFacturar> getServiciosAFacturar() {
        return serviciosAFacturar;
    }

    public BigDecimal getImporteNeto() {
        return importeNeto;
    }

    public BigDecimal getImporteTotal() {
        return importeFinal;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setServiciosAFacturar(List<ServicioAFacturar> serviciosAFacturar) {
        this.serviciosAFacturar = serviciosAFacturar;
    }
    
}
