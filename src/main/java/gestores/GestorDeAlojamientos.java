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
import daoImpl.exceptions.OcuparHabitacionException;
import dto.EstadiaDTO;
import entidades.Estadia;
import entidades.Habitacion;
import entidades.PeriodoReserva;
import entidades.PersonaFisica;
import entidades.TipoEstado;
import entidades.TipoHabitacion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    
    public void OcuparHabitacion(List<EstadiaDTO> estadiasDTO) throws OcuparHabitacionException, Exception {
        
        for(EstadiaDTO e : estadiasDTO){
        
        estadiaDAO = new EstadiaDAOImpl();
        habitacionDAO = new HabitacionDAOImpl();
        personaDAO = new PersonaDAOImpl();
                
        Estadia estadia = new Estadia();
        
        //Cargar datos de la estadia
        
        estadia.setFechaInicio(LocalDate.parse(e.getFechaInicio()));
        
        estadia.setFechaFin(LocalDate.parse(e.getFechaFin()));
        
        Habitacion habitacion = habitacionDAO.getById(e.getIdHabitacion());
        
        //Money precioActual = habitacion.getTipoHabitacion().getPrecioActual();       
        
        estadia.setCostoNoche(habitacion.getTipoHabitacion().getPrecioActual());
           
        //Cargo la habitacion
        estadia.setHabitacion(habitacion);         
        
        //Cargo responsable
        PersonaFisica pasajeroResponsable = personaDAO.findPersonaFisica(e.getIdPasajeroResponsable());
        estadia.setPasajeroResponsable(pasajeroResponsable);
        
        //Cargo acompañantes
        e.getIdsPasajeroAcompañante().forEach(id -> {
              estadia.addPasajeroAcompañante(personaDAO.findPersonaFisica(id));
        });
        
        //Le cambio el estado a la habitacion
        habitacion.setEstado(TipoEstado.OCUPADA);
        habitacionDAO.updateHabitacion(habitacion);        
            
        try{
            estadiaDAO.createEstadia(estadia);
        }catch (OcuparHabitacionException ex){
            //ex.printStackTrace();
            throw ex;
        }finally{
            estadiaDAO.close();
            habitacionDAO.close();
            personaDAO.close();
        }
        
        }

    }
    
    public void createEstadia(EstadiaDTO e){
        estadiaDAO = new EstadiaDAOImpl();
        habitacionDAO = new HabitacionDAOImpl();
        personaDAO = new PersonaDAOImpl();
                
        Estadia estadia = new Estadia();
        
        //Cargar datos de la estadia
        
        estadia.setFechaInicio(LocalDate.parse(e.getFechaInicio()));
        
        estadia.setFechaFin(LocalDate.parse(e.getFechaFin()));
        
        Habitacion habitacion = habitacionDAO.getById(e.getIdHabitacion());
        
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
    
     public void updateEstadia(Estadia estadia){
        estadiaDAO = new EstadiaDAOImpl();
        try {
            estadiaDAO.updateEstadia(estadia);
        } catch (Exception ex) {
            System.out.println("Error al cargar costo");
        }
        
        estadiaDAO.close();
    }
    
    public void deleteEstadia(){
        
    }

    
    public List<PersonaFisica> buscarOcupantes(Estadia estadia) {
        
        List<PersonaFisica> pasajeros = new ArrayList<>();
        
        pasajeros.add(estadia.getPasajeroResponsable());
        
        if(estadia.getPasajeroAcompañante()!= null){
            
            //pasajeros.addAll(estadia.getPasajeroAcompañante());
            
            for(PersonaFisica p : estadia.getPasajeroAcompañante()){
                if(!p.equals(estadia.getPasajeroResponsable())){
                    pasajeros.add(p);
                }
            }
            
        }
        
        habitacionDAO.close();
        
        return pasajeros;
    }

    public void calcularCostoEstadia(Integer nroHabitacion, LocalTime horaSalida) {
        estadiaDAO = new EstadiaDAOImpl();
        
        Estadia estadia = this.buscarEstadia(nroHabitacion);
        estadia.calcularCostoFinal(horaSalida);//Modificar al tipo de dato que haya quedado
        
        try {
            estadiaDAO.updateEstadia(estadia);
        } catch (Exception ex) {
            System.out.println("Error al cargar costo");
        }
        
        estadiaDAO.close();
    }

    public Estadia buscarEstadia(Integer nroHabitacion) {
        habitacionDAO = new HabitacionDAOImpl();
        Habitacion habitacion = habitacionDAO.getById(nroHabitacion);
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


    
    
    
    
    public Map<LocalDate, HashMap<Integer, TipoEstado>>getEstadosHabitaciones(LocalDate fechaInicioGui, LocalDate fechaFinGui) {
        // Recuperar habitaciones, estadias y periodos de reserva
        habitacionDAO = new HabitacionDAOImpl();
        estadiaDAO = new EstadiaDAOImpl();
        reservaDAO = new ReservaDAOImpl();
        
        List<Habitacion> habitaciones = habitacionDAO.getAllHabitaciones();
        List<Estadia> estadias = estadiaDAO.getEstadiasEntreFechas(fechaInicioGui, fechaFinGui);
        List<PeriodoReserva> periodosReserva = reservaDAO.getPeriodosReservaEntreFechas(fechaInicioGui, fechaFinGui);
       
        habitacionDAO.close();
        estadiaDAO.close();
        reservaDAO.close();
        
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
        
        return estadosHabitaciones;
    }
    
    private void completarEstadoEntre(Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones, Habitacion hab, LocalDate cotaInf, LocalDate cotaSup, LocalDate fechaDesde, LocalDate fechaHasta, TipoEstado estado) 
    {
        int indIni = Math.max(
            (int) cotaInf.until(fechaDesde, ChronoUnit.DAYS),   // Si fechaDesde < cotaInf, el resultado es (-)
            0
        );
        int indFin = Math.min(                                  // No excederse del tamanio
            (int) cotaInf.until(fechaHasta, ChronoUnit.DAYS) + 1,
            (int) cotaInf.until(cotaSup, ChronoUnit.DAYS) + 1
        );
        
        for (int i = indIni; i < indFin; i++)
            estadosHabitaciones.get(cotaInf.plusDays(i)).put(hab.getNumero(), estado);
    }
    
    public List<Dupla<String, ArrayList<Integer>>> getTiposYHabitaciones() {
        habitacionDAO = new HabitacionDAOImpl();
        
        List<Habitacion> habs = habitacionDAO.getAllHabitaciones();
        List<TipoHabitacion> tiposHab = habitacionDAO.getAllTiposHabitacion();
        
        habitacionDAO.close();
        
        List<Dupla<String, ArrayList<Integer>>> tiposYHabitaciones = new LinkedList<>();
        
        for (TipoHabitacion t : tiposHab)
        {
            Dupla<String, ArrayList<Integer>> d = new Dupla<>(t.getNombre(), new ArrayList<>());
        
            for (Habitacion hab : habs)
                if (hab.getTipoHabitacion().equals(t)) 
                    d.segundo.add(hab.getNumero());
            
            d.segundo.sort((Integer idH1, Integer idH2) -> idH1.compareTo(idH2));
            
            tiposYHabitaciones.add(d);
        }
        
        return tiposYHabitaciones;
    }
    
    public Habitacion getById(Integer id){
        habitacionDAO = new HabitacionDAOImpl();
        Habitacion habitacion = null;
        try {
            habitacion = habitacionDAO.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        habitacionDAO.close();
        return habitacion;
    }
    
    public Integer getCapacidadHabitacion(Integer id){
        return getById(id).getCapacidad();
    }
}
