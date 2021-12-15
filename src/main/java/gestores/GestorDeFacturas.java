/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import dao.EstadiaDAO;
import dao.FacturaDAO;
import dao.HabitacionDAO;
import dao.PersonaDAO;
import dao.ServicioFacturadoDAO;
import dao.ServicioPrestadoDAO;
import daoImpl.EstadiaDAOImpl;
import daoImpl.FacturaDAOImpl;
import daoImpl.HabitacionDAOImpl;
import daoImpl.PersonaDAOImpl;
import daoImpl.ServicioFacturadoDAOImpl;
import daoImpl.ServicioPrestadoDAOImpl;
import dto.FacturaDTO;
import entidades.Estadia;
import entidades.Factura;
import entidades.Habitacion;
import entidades.PersonaFisica;
import entidades.ServicioPrestado;
import entidades.TipoEstado;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import dto.ServicioAFacturar;
import entidades.PersonaJuridica;
import entidades.ServicioFacturado;
import java.util.ArrayList;

/**
 *
 * @author Nahuel Riquelme
 */
public class GestorDeFacturas {
    private static GestorDeFacturas instance;
    
    private FacturaDAO facturaDAO;
    private EstadiaDAO estadiaDAO;
    private HabitacionDAO habitacionDAO;
    private PersonaDAO personaDAO;
    private ServicioPrestadoDAO servicioPrestadoDAO;
    private ServicioFacturadoDAO servicioFacturadoDAO;
        
    public static GestorDeFacturas getInstance() {
        if (instance == null) {
            instance = new GestorDeFacturas();
        }
        return instance;
    }
    
    
    
    public void Facturar(FacturaDTO f){
        facturaDAO = new FacturaDAOImpl();
        estadiaDAO = new EstadiaDAOImpl();
        habitacionDAO = new HabitacionDAOImpl();
        personaDAO = new PersonaDAOImpl();
        servicioPrestadoDAO = new ServicioPrestadoDAOImpl();
        servicioFacturadoDAO = new ServicioFacturadoDAOImpl();
        
        
        Factura factura = new Factura();
        
        //Cargar datos de la Factura
        factura.setTipo(f.getTipoFactura());
        factura.setFechaEmision(LocalDate.parse(f.getFechaEmision()));
        
        //CARGAR SERVICIOS
        List<ServicioAFacturar> serviciosAFacturar = f.getServiciosAFacturar();
        
        if(serviciosAFacturar != null){
            Integer tamServicios = serviciosAFacturar.size();
            List<ServicioFacturado> serviciosFacturados = new ArrayList<>();
            
            //Por cada servicio a facturar
            for(int i = 0; i<tamServicios; i++){
                ServicioAFacturar servicioAFacturarI = serviciosAFacturar.get(i);
                ServicioPrestado servicioPrestado = servicioPrestadoDAO.findServicioPrestado(servicioAFacturarI.getIdServicioPrestado());
                
                ServicioFacturado servicioFacturado = new ServicioFacturado();
                
                servicioFacturado.setNombre(servicioPrestado.getNombre());
                servicioFacturado.setPrecioUnitario(servicioPrestado.getPrecio());
                servicioFacturado.setPrecioTotal(servicioAFacturarI.getPrecioTotal());
                servicioFacturado.setCantidad(servicioAFacturarI.getCantidad());
                servicioFacturado.setServicioPrestado(servicioPrestado);
//servicioFacturado.setFactura(factura);
                
//No se deberia crear el servicio facturado, ya que se deberia crear solo al crear la factura
                try {
                    servicioFacturadoDAO.create(servicioFacturado);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                
                serviciosFacturados.add(servicioFacturado);
            }
            factura.setServiciosFacturados(serviciosFacturados);
        }
        
        //Cargar Estadia
        Estadia estadia = null;
        
        if (f.getIdEstadia() != null){
            estadia= estadiaDAO.findEstadia(f.getIdEstadia());
            factura.setEstadia(estadia);
        }
        
        factura.setImporteNeto(f.getImporteNeto());
        
        factura.setImporteTotal(f.getImporteTotal());
        
        //Cargar Datos responsable de pago
        if(f.getIdResponsable() != null){
            PersonaFisica responsablePago = personaDAO.findPersonaFisica(f.getIdResponsable());
            factura.setResponsableDePagoFisico(responsablePago);
        }else{
            PersonaJuridica responsablePago = personaDAO.findPersonaJuridica(f.getCuitResponsableJuridico());
            factura.setResponsableDePagoJuridico(responsablePago);
        }
        
        
        //CREAR LA FACTURA
        
        try {
            facturaDAO.createFactura(factura);
            System.out.println("Factura creada");
            
        } catch (Exception ex) {
            System.out.println("Error al crear la factura, en el gestor");
            ex.printStackTrace();
        }
        
//No deberia ser necesario        
        //Le agrego la factura al servicio facturado
        if(serviciosAFacturar != null){
            List<ServicioFacturado> serviciosFacturados = factura.getServiciosFacturados();
            Integer tamServicios = serviciosFacturados.size();
            
            //Por cada servicio a facturar
            for(int i = 0; i<tamServicios; i++){
                ServicioFacturado servicio = serviciosFacturados.get(i);
                
                servicio.setFactura(factura);
                try {
                    servicioFacturadoDAO.edit(servicio);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        //Le cambio el estado a la habitacion y le agrego la factura a la estadía
        if(f.getIdEstadia() != null){
            Habitacion habitacion = estadia.getHabitacion();
            habitacion.setEstado(TipoEstado.DISPONIBLE);
            //estadia.setFactura(factura);
            
            try {
                habitacionDAO.updateHabitacion(habitacion);
                //estadiaDAO.updateEstadia(estadia);
                System.out.println("Habitación modificada");
            } catch (Exception ex) {
                Logger.getLogger(GestorDeAlojamientos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        estadiaDAO.close();
        System.out.println("Factura estadía: " + estadia.getFactura() + "\n");
        habitacionDAO.close();
        personaDAO.close();
        facturaDAO.close();
        servicioPrestadoDAO.close();
    }

    
}