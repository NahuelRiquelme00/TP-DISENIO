/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import daoImpl.exceptions.NonexistentEntityException;
import entidades.Factura;
import java.util.List;

/**
 *
 * @author Nahuel Riquelme
 */
public interface FacturaDAO {
    
    public void createFactura(Factura factura);
    
    public void edit(Factura factura) throws NonexistentEntityException, Exception;
    
    public List<Factura> findFacturaEntities();
    
    public List<Factura> findFacturaEntities(int maxResults, int firstResult);
    
    public Factura findFactura(Integer id);
    
    public void close();
    
    
}
