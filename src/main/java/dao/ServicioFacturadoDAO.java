/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import daoImpl.exceptions.NonexistentEntityException;
import entidades.ServicioFacturado;
import java.util.List;

/**
 *
 * @author Fede
 */
public interface ServicioFacturadoDAO {
    public void create(ServicioFacturado servicioFacturado);
    
    public void edit(ServicioFacturado servicioFacturado) throws NonexistentEntityException, Exception;
    
    public List<ServicioFacturado> findServicioFacturadoEntities();
    
    public List<ServicioFacturado> findServicioFacturadoEntities(int maxResults, int firstResult);
    
    public ServicioFacturado findServicioFacturado(Integer id);
    
    public int getServicioFacturadoCount();
    
    public void close();
}
