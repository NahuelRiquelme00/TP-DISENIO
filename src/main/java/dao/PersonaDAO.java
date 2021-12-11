/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import daoImpl.exceptions.NonexistentEntityException;
import daoImpl.exceptions.PreexistingEntityException;
import entidades.Localidad;
import entidades.Pais;
import entidades.PersonaFisica;
import entidades.PersonaJuridica;
import entidades.Provincia;
import entidades.TipoPosicionFrenteIVA;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Nahuel Riquelme
 */
public interface PersonaDAO {
    
    public void createPersonaFisica(PersonaFisica personaFisica)throws PreexistingEntityException, Exception;
    
    public void deletePersonaFisica(Integer idPersonaFisica)throws NonexistentEntityException;
    
    public List<PersonaFisica> getAllPersonasFisicas();
    
    public void updatePersonaFisica(PersonaFisica personaFisica)throws NonexistentEntityException, Exception;
    
    public PersonaFisica findPersonaFisica(Integer id);
    
    public Localidad findLocalidad(Integer id);
    
    public Provincia findProvincia(Integer id);
    
    public Pais findPais(Integer id);
    
    public List<Pais> getAllPaises();
    
    public List<Provincia> getProvinciasWith(Integer id_pais);
    
    public TipoPosicionFrenteIVA findTipoPosicionFrenteIVA(Integer id);
    
    public List<PersonaFisica> buscarPasajero(String nombre, String apellido, String tipoDocumento, String nroDocumento);
    
    public void close();

    public List<Localidad> getLocalidadesWith(Integer idProvincia);

    public List<TipoPosicionFrenteIVA> getAllPosicionesIVA();

    public PersonaJuridica findPersonaJuridica(BigInteger id);
    
}
