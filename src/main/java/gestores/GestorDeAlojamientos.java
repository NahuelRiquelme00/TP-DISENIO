/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import dao.EstadiaDAO;
import dao.HabitacionDAO;
import dao.PersonaDAO;
import dao.ReservaDAO;
import daoImpl.EstadiaDAOImpl;
import daoImpl.HabitacionDAOImpl;
import daoImpl.PersonaDAOImpl;
import daoImpl.ReservaDAOImpl;
import dto.EstadiaDTO;
import entidades.Estadia;
import entidades.Habitacion;
import entidades.PeriodoReserva;
import entidades.PersonaFisica;
import entidades.TipoEstado;
import interfaces.ColorGrilla;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nahuel Riquelme
 */
public class GestorDeAlojamientos 
{
    public static final int CANT_HABITACIONES = 48;
    
    private static GestorDeAlojamientos instance;
    private EstadiaDAO estadiaDAO;
    private HabitacionDAO habitacionDAO;
    private ReservaDAO reservaDAO;
    private PersonaDAO personaDAO;
    
    private GestorDeAlojamientos (){
        try{
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GestorDePersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static GestorDeAlojamientos getInstance() {
        if (instance == null) {
            instance = new GestorDeAlojamientos();
        }
        return instance;
    }
    
    //Metodos
    public void createEstadia(EstadiaDTO e){
        estadiaDAO = new EstadiaDAOImpl();
        habitacionDAO = new HabitacionDAOImpl();
        personaDAO = new PersonaDAOImpl();
                
        Estadia estadia = new Estadia();
        
        //Cargar datos de la estadia
        
        estadia.setFechaInicio(LocalDate.parse(e.getFechaInicio()));
        
        estadia.setFechaFin(LocalDate.parse(e.getFechaFin()));
        
        Habitacion habitacion = habitacionDAO.findHabitacion(e.getIdHabitacion());
        //Le cambio el estado a la habitacion
        habitacion.setEstado(TipoEstado.OCUPADA);
        try {
            habitacionDAO.updateHabitacion(habitacion);
        } catch (Exception ex) {
            System.out.println("Error al actualizar el estado de la habitacion");
            Logger.getLogger(GestorDeAlojamientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        estadia.setHabitacion(habitacion);
        
        
        estadia.setCostoNoche(habitacion.getTipoHabitacion().getPrecioActual());
        
        PersonaFisica pasajeroResponsable = personaDAO.findPersonaFisica(e.getIdPasajeroResponsable());
        estadia.setPasajeroResponsable(pasajeroResponsable);
        
        e.getIdsPasajeroAcompañante().forEach(id -> {
              estadia.addPasajeroAcompañante(personaDAO.findPersonaFisica(id));
        });
        
        
        
        //Crear estadia        
        try {
            estadiaDAO.createEstadia(estadia);
            //System.out.println("Estadia creada");            
        } catch (Exception ex) {
            System.out.println("Error al crear la estadia, en el gestor");
            ex.printStackTrace();
        }       
        estadiaDAO.close();
        habitacionDAO.close();
        personaDAO.close();
    }
    
    public void updateEstadia(){
        
    }
    
    public void deleteEstadia(){
        
    }
    
    
    public Object[][] llenarGrilla(LocalDate fechaInicioGui, LocalDate fechaFinGui)
    {
        // Inicializar grilla con las fechas colocadas
        int rangoFechas = (int) fechaInicioGui.until(fechaFinGui, ChronoUnit.DAYS) + 1;
        Object[][] grilla = new Object[rangoFechas][CANT_HABITACIONES + 1]; 
        
        for (int i = 0; i < rangoFechas; i++)
            grilla[i][0] = fechaInicioGui.plusDays(i);
        
        // Recuperar habitaciones, estadias y periodos de reserva
        habitacionDAO = new HabitacionDAOImpl();
        estadiaDAO = new EstadiaDAOImpl();
        reservaDAO = new ReservaDAOImpl();
        
        List<Habitacion> habitaciones = habitacionDAO.getAllHabitaciones();
        List<Estadia> estadias = estadiaDAO.getEstadiasEntreFechas(fechaInicioGui, fechaFinGui);
        List<PeriodoReserva> periodosReserva = reservaDAO.getPeriodosReservaEntreFechas(fechaInicioGui, fechaFinGui);
          
        // Loop principal SD
        for (Habitacion hab : habitaciones)
        {
            this.completarEstadoEntre(grilla, hab, fechaInicioGui, fechaFinGui, fechaInicioGui, fechaFinGui, TipoEstado.DISPONIBLE);
            
            if (hab.getEstado() == TipoEstado.FUERA_DE_SERVICIO)
                this.completarEstadoEntre(grilla, hab, fechaInicioGui, fechaFinGui, LocalDate.now(), fechaFinGui, TipoEstado.FUERA_DE_SERVICIO);
            else
            {
                // "Contiene" del SD (¿cambiar el SD?)
                for (PeriodoReserva perRes : periodosReserva)
                    if (perRes.getHabitacion().equals(hab))
                        this.completarEstadoEntre(grilla, hab, fechaInicioGui, fechaFinGui, perRes.getFechaInicio(), perRes.getFechaFin(), TipoEstado.RESERVADA);
                
                for (Estadia est : estadias)
                    if (est.getHabitacion().equals(hab))
                        this.completarEstadoEntre(grilla, hab, fechaInicioGui, fechaFinGui, est.getFechaInicio(), est.getFechaFin(), TipoEstado.OCUPADA);
            }
        }
        
        return grilla;
    }
    
    private void completarEstadoEntre(Object[][] grilla, Habitacion hab, LocalDate cotaInf, LocalDate cotaSup, LocalDate fechaDesde, LocalDate fechaHasta, TipoEstado estado)
    {
        int indIni = Math.max(
            (int) cotaInf.until(fechaDesde, ChronoUnit.DAYS),   // Si fechaDesde < cotaInf, el resultado es (-)
            0
        );
        int indFin = Math.min(                                  // No excederse del tamanio de la matriz
            (int) cotaInf.until(fechaHasta, ChronoUnit.DAYS),
            (int) cotaInf.until(cotaSup, ChronoUnit.DAYS)
        );
        int indHab = hab.getNumero();
        
        for (int i = indIni; i < indFin; i++)
            grilla[i][indHab] = this.getColorGrilla(estado);
    }
    
    private int getColorGrilla(TipoEstado est)
    {
        int res;
        
        switch(est)
        {
            case DISPONIBLE: 
                res = ColorGrilla.COLOR_DISPONIBLE;
                break;
            case RESERVADA:
                res = ColorGrilla.COLOR_RESERVADA;
                break;
            case OCUPADA:
                res = ColorGrilla.COLOR_OCUPADA;
                break;
            case FUERA_DE_SERVICIO:
                res = ColorGrilla.COLOR_FUERA_DE_SERVICIO;
                break;
            default: // No se deberia llegar aca
                res = 0x000000;
                break;
        }
        
        return res;
    }
}
