/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import daoImpl.exceptions.NonexistentEntityException;
import entidades.PeriodoReserva;
import entidades.Reserva;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Nahuel Riquelme
 */
public interface ReservaDAO {
    public List<PeriodoReserva> getPeriodosReservaEntreFechas(LocalDate fechaInicioGui, LocalDate fechaFinGui);
    
    public void create(Reserva reserva);
    
    public void update(Reserva reserva) throws NonexistentEntityException, Exception;
    
    public void delete(Integer id) throws NonexistentEntityException;
    
    public List<Reserva> findReservaEntities();
    
    public Reserva findReserva(Integer id);  
    
    public int getReservaCount();

    public void close();
}
