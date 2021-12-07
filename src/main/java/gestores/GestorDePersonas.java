/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import dao.PersonaDAO;
import dao.TipoPosicionFrenteIVADAO;
import daoImpl.PersonaDAOImpl;
import daoImpl.TipoPosicionFrenteIVADAOImpl;
import dto.PersonaFisicaDTO;
import entidades.Direccion;
import entidades.Localidad;
import entidades.Pais;
import entidades.PersonaFisica;
import entidades.PersonaJuridica;
import entidades.Provincia;
import entidades.TipoDocumento;
import entidades.TipoPosicionFrenteIVA;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nahuel Riquelme
 */
public class GestorDePersonas {
    private static GestorDePersonas instance;
    private PersonaDAO personaDAO;
    private TipoPosicionFrenteIVADAO posicionIVADAO;
    
    private GestorDePersonas (){
        try{
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GestorDePersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static GestorDePersonas getInstance() {
        if (instance == null) {
            instance = new GestorDePersonas();
        }
        return instance;
    }
    
    public void createPersonaFisica(PersonaFisica personaFisica){
        personaDAO = new PersonaDAOImpl();
        try {
            personaDAO.createPersonaFisica(personaFisica);
            System.out.println("Persona creada correctamente");
        } catch (Exception ex) {
            System.out.println("Error al crear persona");
        }
        personaDAO.close();
    }
    
    public void createPersonaFisica(PersonaFisicaDTO p){
        personaDAO = new PersonaDAOImpl();
        
        PersonaFisica personaFisica = new PersonaFisica(p.getApellido(), p.getNombres(), TipoDocumento.valueOf(p.getTipoDocumento()), p.getNroDocumento(), LocalDate.parse(p.getFechaNacimiento()),p.getEmail(),p.getOcupacion(),p.getNacionalidad(),p.getTelefono());
        Direccion direccion = new Direccion(p.getCalle(), p.getNumero(), p.getDepartamento(), p.getPiso(), p.getCodigoPostal());
        //La direccion se crea al crear la persona
        
        //La posicionIVA, la localidad, la provincia y el pais se tienen que relacionar entre ellos por su id;
        TipoPosicionFrenteIVA posicionIVA = personaDAO.findTipoPosicionFrenteIVA(p.getIdPosicionIVA());
        Localidad loc = personaDAO.findLocalidad(p.getIdLocalidad());
        
        /*
        Provincia prov = personaDAO.findProvincia(p.getIdProvincia());
        Pais pais = personaDAO.findPais(p.getIdPais());
        //Se podria lanzar una excepcion si no existen en la base de datos, pero al ser cargados desde una interface
        //siempre deberian existir
        prov.setPais(pais);
        loc.setProvincia(prov);
        */
        
        direccion.setLocalidad(loc);        
        personaFisica.setDireccion(direccion);
        personaFisica.setTipoPosicionFrenteIVA(posicionIVA);        
        
        try {
            personaDAO.createPersonaFisica(personaFisica);
            System.out.println("Persona creada correctamente");
        } catch (Exception ex) {
            System.out.println("Error al crear persona");
        }
        personaDAO.close();
    }
    
    public void updatePersonaFisica(PersonaFisicaDTO p, Integer id_persona){
        personaDAO = new PersonaDAOImpl();
        
        PersonaFisica personaFisica = new PersonaFisica(p.getApellido(), p.getNombres(), TipoDocumento.valueOf(p.getTipoDocumento()), p.getNroDocumento(), LocalDate.parse(p.getFechaNacimiento()),p.getEmail(),p.getOcupacion(),p.getNacionalidad(),p.getTelefono());
        Direccion direccion = new Direccion(p.getCalle(), p.getNumero(), p.getDepartamento(), p.getPiso(), p.getCodigoPostal());
        TipoPosicionFrenteIVA posicionIVA = personaDAO.findTipoPosicionFrenteIVA(p.getIdPosicionIVA());
        Localidad loc = personaDAO.findLocalidad(p.getIdLocalidad());
        Provincia prov = personaDAO.findProvincia(p.getIdProvincia());
        Pais pais = personaDAO.findPais(p.getIdPais());
        
        prov.setPais(pais);
        loc.setProvincia(prov);
        direccion.setLocalidad(loc);     
        personaFisica.setDireccion(direccion);
        personaFisica.setTipoPosicionFrenteIVA(posicionIVA);   
        personaFisica.setIdPersonaFisica(id_persona);
        
        try {
            personaDAO.updatePersonaFisica(personaFisica);
            System.out.println("Persona modificada correctamente");
        } catch (Exception ex) {
            System.out.println("Error al modificar persona");
        }
        personaDAO.close();
    }
    
    public List<PersonaFisica> getAllPersonasFisicas(){
        personaDAO = new PersonaDAOImpl();
        List<PersonaFisica> listaPersonas = personaDAO.getAllPersonasFisicas();
        personaDAO.close();
        return listaPersonas;
    }
    
    public PersonaFisica findPersonaFisica(Integer id){
        personaDAO = new PersonaDAOImpl();
        PersonaFisica persona = personaDAO.findPersonaFisica(id);
        personaDAO.close();
        return persona;
    }
    
    public List<PersonaFisicaDTO> buscarPasajero(String nombre, String apellido, String tipoDocumento, String nroDocumento){
        personaDAO = new PersonaDAOImpl();
        //Aca se deberia llamar a un metodo que busque solo por atributos
        List<PersonaFisica> listaPersonas = personaDAO.buscarPasajero(nombre, apellido, tipoDocumento, nroDocumento);
        personaDAO.close();
        return convertirADTO(listaPersonas);        
    }
            
    public List<PersonaFisicaDTO> convertirADTO(List<PersonaFisica> listaPersonas){
        List<PersonaFisicaDTO> pasajerosDTO = new ArrayList<>();
        listaPersonas.stream().map(p -> new PersonaFisicaDTO(p.getIdPersonaFisica(),p.getApellido(), p.getNombres(), p.getTipoDocumento().toString(), p.getNroDocumento())).forEachOrdered(dto -> {
            pasajerosDTO.add(dto);
        });
        return pasajerosDTO;
    }
    
    public List<Pais> getAllPaises(){
        personaDAO = new PersonaDAOImpl();
        try{
            return personaDAO.getAllPaises();
        }finally{
            personaDAO.close();
        }
    }
    
    public List<Provincia> getProvinciasWith(Integer id_pais){
        personaDAO = new PersonaDAOImpl();
        try{
            return personaDAO.getProvinciasWith(id_pais);
        }finally{
            personaDAO.close();
        }
    }

    public List<Localidad> getLocalidadesWith(Integer idProvincia) {
        personaDAO = new PersonaDAOImpl();
        try{
            return personaDAO.getLocalidadesWith(idProvincia);
        }finally{
            personaDAO.close();
        }
    }
    
    public List<TipoPosicionFrenteIVA> getAllPosicionesIVA(){
        personaDAO = new PersonaDAOImpl();
        try{
            return personaDAO.getAllPosicionesIVA();
        }finally{
            personaDAO.close();
        }
    }
    
    public Boolean tipoYNúmeroExistentes(String tipoDocumento, String númeroDocumento) {
    	return !buscarPasajero("", "", tipoDocumento, númeroDocumento).isEmpty();
    }
    
    public List<String> datosCompletos(
                String nombre, 
                String apellido, 
                String documento,
                Date fechaNac,
                String telefono, 
                String nacionalidad, 
                String calle, 
                String numero, 
                String codPostal, 
                String cuit, 
                String posIVA, 
                String ocupacion,
                String localidad,
                String provincia,
                String pais){
        
        Integer largoCuit = 14;
        List<String> hayCamposIncompletos = new ArrayList<>();
	
        // Se validan los que son obligatorios
	if(nombre.isEmpty()){
            hayCamposIncompletos.add("nombre");
	}
	if (apellido.isEmpty()){
            hayCamposIncompletos.add("apellido");
		}
	if (documento.isEmpty()){
            hayCamposIncompletos.add("documento");
	}
        if (fechaNac == null){
            hayCamposIncompletos.add("fechaNac");
	}
	if (telefono.isEmpty()){
            hayCamposIncompletos.add("telefono");
	}
	if (nacionalidad.isEmpty()){
            hayCamposIncompletos.add("nacionalidad");
	}
	if (calle.isEmpty()){
            hayCamposIncompletos.add("calle");
	}
	if (numero.isEmpty()){
            hayCamposIncompletos.add("numero");
	}	
	if (codPostal.isEmpty()){
            hayCamposIncompletos.add("codPostal");
	}	
	// El cuit no puede ser vacio si se es "responsable inscripto"
	if (posIVA.equals("RESPONSABLE INSCRIPTO (A)") ){
            if (cuit.length() < largoCuit){
		hayCamposIncompletos.add("cuit");
            }
	}
	if (ocupacion.isEmpty()){
            hayCamposIncompletos.add("ocupacion");
            
	}
	if (localidad.isEmpty()){
            hayCamposIncompletos.add("localidad");
            
	}
        if (provincia.isEmpty()){
            hayCamposIncompletos.add("provincia");
            
	}
        if (pais.isEmpty()){
            hayCamposIncompletos.add("pais");
            
	}
        return hayCamposIncompletos;
    }

   
    public void getTipoFactura(Integer idPosicionIVA) {
        posicionIVADAO = new TipoPosicionFrenteIVADAOImpl();
        
        TipoPosicionFrenteIVA posicionIVA = posicionIVADAO.findTipoPosicionFrenteIVA(idPosicionIVA);
        posicionIVADAO.close();
        
        System.out.println(posicionIVA);
    }
    
    public PersonaJuridica findPersonaJuridica (Integer id){
        personaDAO = new PersonaDAOImpl();
        PersonaJuridica persona = personaDAO.findPersonaJuridica(id);
        personaDAO.close();
        return persona;
    }
    
}
