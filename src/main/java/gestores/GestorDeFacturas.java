/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import daoImpl.EstadiaDAOImpl;
import daoImpl.FacturaDAOImpl;
import daoImpl.HabitacionDAOImpl;
import daoImpl.PersonaDAOImpl;
import daoImpl.ServicioPrestadoDAOImpl;
import dto.FacturaDTO;
import entidades.Estadia;
import entidades.Factura;
import entidades.Habitacion;
import entidades.PersonaFisica;
import entidades.ServicioPrestado;
import entidades.TipoEstado;
import entidades.TipoFactura;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import dto.ServicioAFacturar;
import entidades.ServicioFacturado;

/**
 *
 * @author Nahuel Riquelme
 */
public class GestorDeFacturas {
    private static GestorDeFacturas instance;
    
    private FacturaDAOImpl facturaDAO;
    private EstadiaDAOImpl estadiaDAO;
    private HabitacionDAOImpl habitacionDAO;
    private PersonaDAOImpl personaDAO;
    private ServicioPrestadoDAOImpl servicioPrestadoDAO;
        
    public static GestorDeFacturas getInstance() {
        if (instance == null) {
            instance = new GestorDeFacturas();
        }
        return instance;
    }
    
    
    
    public void createFactura(FacturaDTO f){
        facturaDAO = new FacturaDAOImpl();
        estadiaDAO = new EstadiaDAOImpl();
        habitacionDAO = new HabitacionDAOImpl();
        personaDAO = new PersonaDAOImpl();
        servicioPrestadoDAO = new ServicioPrestadoDAOImpl();
        
        
        Factura factura = new Factura();
        
        //Cargar datos de la Factura
        
        factura.setTipo(f.getTipoFactura());
        factura.setFechaEmision(LocalDate.parse(f.getFechaEmision()));
        
        //CARGAR SERVICIOS
        
        List<ServicioAFacturar> serviciosAFacturar = f.getServiciosAFacturar();
        
        if(serviciosAFacturar != null){
            Integer tamServicios = serviciosAFacturar.size();
            List<ServicioFacturado> serviciosFacturados = null;
            
            //Por cada servicio a facturar
            for(int i = 0; i<tamServicios; i++){
                ServicioPrestado servicioPrestado = servicioPrestadoDAO.findServicioPrestado(serviciosAFacturar.get(i).getIdServicioPrestado());
                ServicioAFacturar servicioAFacturarI = serviciosAFacturar.get(i);

                ServicioFacturado servicioFacturado = new ServicioFacturado();
                servicioFacturado.setNombre(servicioPrestado.getNombre());
                servicioFacturado.setPrecioUnitario(servicioPrestado.getPrecio());
                servicioFacturado.setCantidad(servicioAFacturarI.getCantidad());
                servicioFacturado.setPrecioTotal(servicioAFacturarI.getPrecioTotal());
                servicioFacturado.setServicioPrestado(servicioPrestado);

                serviciosFacturados.add(servicioFacturado);
            }
            factura.setServiciosFacturados(serviciosFacturados);
        }
        //Cargar Estadia
        
        Estadia estadia = estadiaDAO.findEstadia(f.getIdEstadia());
        if (f.getIdEstadia() != null){
            factura.setEstadia(estadia);
        }
        
        factura.setImporteNeto(f.getImporteNeto());
        
        factura.setImporteTotal(f.getImporteTotal());
        
        PersonaFisica responsablePago = personaDAO.findPersonaFisica(f.getIdResponsable());
        factura.setResponsableDePagoFisico(responsablePago);
        
        //Crear la factura
        
        try {
            facturaDAO.createFactura(factura);
            System.out.println("Factura creada");            
        } catch (Exception ex) {
            System.out.println("Error al crear la factura, en el gestor");
            ex.printStackTrace();
        }
        
        //Le cambio el estado a la habitacion
        
        Habitacion habitacion = estadia.getHabitacion();
        habitacion.setEstado(TipoEstado.DISPONIBLE);
        try {
            habitacionDAO.updateHabitacion(habitacion);
        } catch (Exception ex) {
            System.out.println("Error al actualizar el estado de la habitacion");
            Logger.getLogger(GestorDeAlojamientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        estadiaDAO.close();
        habitacionDAO.close();
        personaDAO.close();
        facturaDAO.close();
        servicioPrestadoDAO.close();
    }

    
}

    

