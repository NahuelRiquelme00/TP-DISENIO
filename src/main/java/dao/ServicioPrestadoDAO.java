/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import daoImpl.exceptions.NonexistentEntityException;
import entidades.ServicioPrestado;
import java.util.List;

/**
 *
 * @author Nahuel Riquelme
 */
public interface ServicioPrestadoDAO {
    public void createServicioPrestado(ServicioPrestado servicioPrestado);
    
    public void edit(ServicioPrestado servicioPrestado) throws NonexistentEntityException, Exception;
    
    public List<ServicioPrestado> findServicioPrestadoEntities();
    
    public List<ServicioPrestado> findServicioPrestadoEntities(int maxResults, int firstResult);
    
    public ServicioPrestado findServicioPrestado(Integer id);
    
    public void close();
}
