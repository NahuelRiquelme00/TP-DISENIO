/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import daoImpl.exceptions.NonexistentEntityException;
import entidades.Estadia;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Nahuel Riquelme
 */
public interface EstadiaDAO {
    
    public void createEstadia(Estadia estadia);
    
    public void updateEstadia(Estadia estadia) throws NonexistentEntityException, Exception;
    
    public void deleteEstadia(Integer id) throws NonexistentEntityException;
    
    public Estadia findEstadia(Integer id);
    
    public List<Estadia> findEstadiaEntities();
    
    public List<Estadia> getEstadiasEntreFechas(LocalDate fechaInicioGui, LocalDate fechaFinGui);
    
    public void close();
}
