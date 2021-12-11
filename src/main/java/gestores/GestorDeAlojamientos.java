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
import dto.EstadiaDTO;
import dto.PersonaFisicaDTO;
import entidades.Estadia;
import entidades.Habitacion;
import entidades.PersonaFisica;
import entidades.TipoEstado;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.money.Money;

/**
 *
 * @author Nahuel Riquelme
 */
public class GestorDeAlojamientos {
    private static GestorDeAlojamientos instance;
    private final GestorDePersonas gestorPersonas;
    private EstadiaDAO estadiaDAO;
    private HabitacionDAO habitacionDAO;
    private ReservaDAO reservaDAO;
    private PersonaDAO personaDAO;
    
    private GestorDeAlojamientos (){
        this.gestorPersonas = GestorDePersonas.getInstance();
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

    
    public List<PersonaFisica> buscarOcupantes(Estadia estadia) {
        
        List<PersonaFisica> pasajeros = new ArrayList<>();
        
        pasajeros.add(estadia.getPasajeroResponsable());
        
        if(estadia.getPasajeroAcompañante()!=null){
            pasajeros.addAll(estadia.getPasajeroAcompañante());
        }
        habitacionDAO.close();
        return pasajeros;
    }

    public void calcularCostoEstadia(Integer nroHabitacion, LocalTime horaSalida) {
        estadiaDAO = new EstadiaDAOImpl();
        
        Estadia estadia = this.buscarEstadia(nroHabitacion);
        BigDecimal costoFinal = estadia.calcularCostoFinal(horaSalida);//Modificar al tipo de dato que haya quedado
        
        System.out.println("El costo final es: " + costoFinal);
        
        
        try {
            estadiaDAO.updateEstadia(estadia);
        } catch (Exception ex) {
            System.out.println("Error al crear persona");
        }
        
        estadiaDAO.close();
    }

    public Estadia buscarEstadia(Integer nroHabitacion) {
        habitacionDAO = new HabitacionDAOImpl();
        Habitacion habitacion = habitacionDAO.findHabitacion(nroHabitacion);
        habitacionDAO.close();
        
        Estadia estadia = habitacion.getEstadiaActual();
        
        return estadia;
    }

    public Integer getCantidadNoches(Estadia estadia) {
        Integer cantNoches = estadia.getCantidadNoches();
        return cantNoches;
    }

    public BigDecimal getCostoFinal(Estadia estadia) {
        estadiaDAO = new EstadiaDAOImpl();
        
        return estadia.getCostoFinal();
    }

    public BigDecimal getCostoNoche(Estadia estadia) {
        estadiaDAO = new EstadiaDAOImpl();
        
        return estadia.getCostoNoche();
    }


    
    
    
}
