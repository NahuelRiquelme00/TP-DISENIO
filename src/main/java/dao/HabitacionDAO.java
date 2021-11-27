/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import daoImpl.exceptions.NonexistentEntityException;
import entidades.Habitacion;
import entidades.TipoHabitacion;
import java.util.List;

/**
 *
 * @author Nahuel Riquelme
 */
public interface HabitacionDAO {
    
    public void createHabitacion(Habitacion habitacion);
    
    public void updateHabitacion(Habitacion habitacion) throws NonexistentEntityException, Exception;
    
    public void deleteHabitacion(Integer id) throws NonexistentEntityException;
    
    public List<Habitacion> getAllHabitaciones();
    
    public Habitacion findHabitacion(Integer id);
    
    public List<TipoHabitacion> getAllTiposHabitacion();
    
    public void close();
    
}
