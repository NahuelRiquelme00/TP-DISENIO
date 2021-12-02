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
import entidades.TipoHabitacion;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import misc.Dupla;

/**
 *
 * @author Nahuel Riquelme
 */
public class GestorDeAlojamientos {
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
        
        estadia.setCostoNoche(habitacion.getTipoHabitacion().getPrecioActual());
        
        //Le cambio el estado a la habitacion
        habitacion.setEstado(TipoEstado.OCUPADA);
        try {
            habitacionDAO.updateHabitacion(habitacion);
        } catch (Exception ex) {
            System.out.println("Error al actualizar el estado de la habitacion");
            Logger.getLogger(GestorDeAlojamientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Cargo la habitacion
        estadia.setHabitacion(habitacion);         
        
        //Cargo responsable
        PersonaFisica pasajeroResponsable = personaDAO.findPersonaFisica(e.getIdPasajeroResponsable());
        estadia.setPasajeroResponsable(pasajeroResponsable);
        
        //Cargo acompañantes
        e.getIdsPasajeroAcompañante().forEach(id -> {
              estadia.addPasajeroAcompañante(personaDAO.findPersonaFisica(id));
        });
        
        //Crear estadia        
        try {
            estadiaDAO.createEstadia(estadia);
            System.out.println("Estadia creada");            
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

    public void OcuparHabitacion(List<EstadiaDTO> estadiasDTO) {
    
        try {
             estadiasDTO.forEach(estadiaDTO -> {createEstadia(estadiaDTO);});
             System.out.println("Ocupacion existosa");
        } catch (Exception e) {
            System.out.println("Ocupacion fallida");
            e.printStackTrace();
        }
        
    }
    
    public Map<LocalDate, HashMap<Integer, TipoEstado>>getEstadosHabitaciones(LocalDate fechaInicioGui, LocalDate fechaFinGui) {
        // Recuperar habitaciones, estadias y periodos de reserva
        habitacionDAO = new HabitacionDAOImpl();
        estadiaDAO = new EstadiaDAOImpl();
        reservaDAO = new ReservaDAOImpl();
        
        List<Habitacion> habitaciones = habitacionDAO.getAllHabitaciones();
        List<Estadia> estadias = estadiaDAO.getEstadiasEntreFechas(fechaInicioGui, fechaFinGui);
        List<PeriodoReserva> periodosReserva = reservaDAO.getPeriodosReservaEntreFechas(fechaInicioGui, fechaFinGui);
       
        // Inicializar mapa de mapas con fechas colocadas
        int cantDias = (int) fechaInicioGui.until(fechaFinGui, ChronoUnit.DAYS) + 1;
        Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones = new HashMap<>(); 
        for (int i = 0; i < cantDias; i++)
            estadosHabitaciones.put(fechaInicioGui.plusDays(i), new HashMap());
        
        // Loop principal SD
        for (Habitacion hab : habitaciones)
        {
            this.completarEstadoEntre(estadosHabitaciones, hab, fechaInicioGui, fechaFinGui, fechaInicioGui, fechaFinGui, TipoEstado.DISPONIBLE);
            
            if (hab.getEstado() == TipoEstado.FUERA_DE_SERVICIO)
                this.completarEstadoEntre(estadosHabitaciones, hab, fechaInicioGui, fechaFinGui, LocalDate.now(), fechaFinGui, TipoEstado.FUERA_DE_SERVICIO);
            else
            {
                // Cambiar "Contiene" del SD
                for (PeriodoReserva perRes : periodosReserva)
                    //if (perRes.getHabitacion().equals(hab))
                    if (perRes.getHabitacion().getNumero().equals(hab.getNumero()))
                        this.completarEstadoEntre(estadosHabitaciones, hab, fechaInicioGui, fechaFinGui, perRes.getFechaInicio(), perRes.getFechaFin(), TipoEstado.RESERVADA);
                
                for (Estadia est : estadias)
                    //if (est.getHabitacion().equals(hab))
                    if (est.getHabitacion().getNumero().equals(hab.getNumero()))
                        this.completarEstadoEntre(estadosHabitaciones, hab, fechaInicioGui, fechaFinGui, est.getFechaInicio(), est.getFechaFin(), TipoEstado.OCUPADA);
            }
        }
        
        return /*grilla*/estadosHabitaciones;
    }
    
    private void completarEstadoEntre(Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones, Habitacion hab, LocalDate cotaInf, LocalDate cotaSup, LocalDate fechaDesde, LocalDate fechaHasta, TipoEstado estado) {
        int indIni = Math.max(
            (int) cotaInf.until(fechaDesde, ChronoUnit.DAYS),   // Si fechaDesde < cotaInf, el resultado es (-)
            0
        );
        int indFin = Math.min(                                  // No excederse del tamanio
            (int) cotaInf.until(fechaHasta, ChronoUnit.DAYS) + 1,
            (int) cotaInf.until(cotaSup, ChronoUnit.DAYS) + 1
        );
        int indHab = hab.getNumero();

        for (int i = indIni; i < indFin; i++)
            estadosHabitaciones.get(cotaInf.plusDays(i)).put(hab.getNumero(), estado);
    }
    
    public List<Dupla<String, LinkedList<Integer>>> getTiposYHabitaciones() {
        HabitacionDAO habDAO = new HabitacionDAOImpl();
        List<Habitacion> habs = habDAO.getAllHabitaciones();
        List<TipoHabitacion> tiposHab = habDAO.getAllTiposHabitacion();
        
        List<Dupla<String, LinkedList<Integer>>> tiposYHabitaciones = new LinkedList<>();
        
        for (TipoHabitacion t : tiposHab)
        {
            Dupla<String, LinkedList<Integer>> d = new Dupla<>(t.getNombre(), new LinkedList<>());
        
            for (Habitacion hab : habs)
                if (hab.getTipoHabitacion().equals(t)) 
                    d.segundo.add(hab.getNumero());
            
            tiposYHabitaciones.add(d);
        }
        
        return tiposYHabitaciones;
    }
}
