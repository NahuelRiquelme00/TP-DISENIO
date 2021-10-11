/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImpl;

import dao.PersonaDAO;
import daoImpl.exceptions.NonexistentEntityException;
import daoImpl.exceptions.PreexistingEntityException;
import entidades.Localidad;
import entidades.Pais;
import entidades.PersonaFisica;
import entidades.Provincia;
import entidades.TipoDocumento;
import entidades.TipoPosicionFrenteIVA;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Nahuel Riquelme
 */
public class PersonaDAOImpl implements PersonaDAO {

    private EntityManagerFactory emf = null;
    
    public PersonaDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("MiBaseDeDatos");
    }   

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void createPersonaFisica(PersonaFisica personaFisica) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(personaFisica);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonaFisica(personaFisica.getIdPersonaFisica()) != null) {
                throw new PreexistingEntityException("PersonaFisica " + personaFisica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void updatePersonaFisica(PersonaFisica personaFisica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            personaFisica = em.merge(personaFisica);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = personaFisica.getIdPersonaFisica();
                if (findPersonaFisica(id) == null) {
                    throw new NonexistentEntityException("The personaFisica with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void deletePersonaFisica(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PersonaFisica personaFisica;
            try {
                personaFisica = em.getReference(PersonaFisica.class, id);
                personaFisica.getIdPersonaFisica();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personaFisica with id " + id + " no longer exists.", enfe);
            }
            em.remove(personaFisica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<PersonaFisica> getAllPersonasFisicas() {
        return findPersonaFisicaEntities(true, -1, -1);
    }

    public List<PersonaFisica> findPersonaFisicaEntities(int maxResults, int firstResult) {
        return findPersonaFisicaEntities(false, maxResults, firstResult);
    }

    private List<PersonaFisica> findPersonaFisicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PersonaFisica.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<PersonaFisica> buscarPasajero(String nombre, String apellido, String tipoDocumento, String nroDocumento) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            
            CriteriaQuery<PersonaFisica> configPersonas = cb.createQuery(PersonaFisica.class);
            Root<PersonaFisica> raizPersona = configPersonas.from(PersonaFisica.class);
            
//            configPersonas.select(raizPersona);            
//            Predicate[] predicates = new Predicate[4];
//            predicates[0] = cb.equal(raizPersona.get("nombres"),nombre);
//            predicates[1] = cb.equal(raizPersona.get("apellido"),apellido);
//            predicates[2] = cb.equal(raizPersona.get("tipoDocumento"),TipoDocumento.valueOf(tipoDocumento));
//            predicates[3] = cb.equal(raizPersona.get("nroDocumento"),nroDocumento);
//            configPersonas.where(predicates);

            if(!nombre.isBlank() && !apellido.isBlank() && !tipoDocumento.isBlank() && !nroDocumento.isBlank()){
                //Busca por todos los atributos
                Integer nro = Integer.parseInt(nroDocumento);
                Predicate predicateNombre = cb.like(raizPersona.get("nombres"),nombre+"%");
                Predicate predicateApellido = cb.like(raizPersona.get("apellido"),apellido+"%");
                Predicate predicateTipoDoc = cb.equal(raizPersona.get("tipoDocumento"),TipoDocumento.valueOf(tipoDocumento));
                Predicate predicateNumDoc = cb.equal(raizPersona.get("nroDocumento"),nro);
                configPersonas.select(raizPersona).where(cb.and(predicateNombre,predicateApellido,predicateTipoDoc,predicateNumDoc));
            } else if (tipoDocumento.isBlank() && !nombre.isBlank() && !apellido.isBlank() ){
                //Busca por nombre y apellido
                Predicate predicateNombre = cb.like(raizPersona.get("nombres"),nombre+"%");
                Predicate predicateApellido = cb.like(raizPersona.get("apellido"),apellido+"%");
                configPersonas.select(raizPersona).where(cb.and(predicateNombre,predicateApellido));
            } else if (apellido.isBlank() && !nombre.isBlank()){
                //Busca por nombre
                //Predicate predicateNombre = cb.equal(raizPersona.get("nombres"),nombre);
                Predicate predicateNombre = cb.like(raizPersona.get("nombres"),nombre+"%");
                configPersonas.select(raizPersona).where(predicateNombre);
            } else if (nombre.isBlank() && !apellido.isBlank()){
                //Busca por apellido
                Predicate predicateApellido = cb.like(raizPersona.get("apellido"),apellido+"%");
                configPersonas.select(raizPersona).where(predicateApellido);
            } else if (nombre.isBlank() && apellido.isBlank() && !tipoDocumento.isBlank() && !nroDocumento.isBlank() ){
                //Buscar por tipo y nro
                Integer nro = Integer.parseInt(nroDocumento);
                Predicate predicateTipoDoc = cb.equal(raizPersona.get("tipoDocumento"),TipoDocumento.valueOf(tipoDocumento));
                Predicate predicateNumDoc = cb.equal(raizPersona.get("nroDocumento"),nro);
                configPersonas.select(raizPersona).where(cb.and(predicateTipoDoc,predicateNumDoc));
            }else configPersonas.select(raizPersona); //Carga todas las personas 
            //Faltarian otras combinaciones
 
            configPersonas.orderBy(cb.desc(raizPersona.get("nombres")));
            
            List<PersonaFisica> resultado = em.createQuery(configPersonas).getResultList();
            
//            resultado.forEach(personaFisica -> {
//                System.out.println(personaFisica);
//            });
//            
//            if (resultado.isEmpty()) System.out.println("Lista vacia");
//            System.out.println(nombre + " " + apellido + " " + " " + tipoDocumento + " " + nroDocumento);
            
            return resultado;
            
            
        } finally {
            em.close();
        }
    }

    @Override
    public PersonaFisica findPersonaFisica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PersonaFisica.class, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public TipoPosicionFrenteIVA findTipoPosicionFrenteIVA(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPosicionFrenteIVA.class, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Localidad findLocalidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localidad.class, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Provincia findProvincia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Provincia.class, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Pais findPais(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaFisicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PersonaFisica> rt = cq.from(PersonaFisica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void close(){
        emf.close();
    }

    @Override
    public List<Pais> getAllPaises() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();          
            CriteriaQuery<Pais> configPaises = cb.createQuery(Pais.class);
            Root<Pais> raizPais = configPaises.from(Pais.class);
            configPaises.select(raizPais);
            configPaises.orderBy(cb.asc(raizPais.get("nombre")));      
            List<Pais> resultado = em.createQuery(configPaises).getResultList();
            return resultado;
        } finally {
            em.close();
        }  
    }
    
    @Override
    public List<Provincia> getProvinciasWith(Integer id_pais){
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();          
            CriteriaQuery<Provincia> configProvincias = cb.createQuery(Provincia.class);
            Root<Provincia> raizProvincia = configProvincias.from(Provincia.class);
            
            Predicate predicatePais = cb.equal(raizProvincia.get("pais").get("idPais"),id_pais);
            configProvincias.select(raizProvincia).where(predicatePais);            
            configProvincias.orderBy(cb.asc(raizProvincia.get("nombre")));      
            List<Provincia> resultado = em.createQuery(configProvincias).getResultList();
            return resultado;
        } finally {
            em.close();
        }  
    }

    @Override
    public List<Localidad> getLocalidadesWith(Integer idProvincia) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();          
            CriteriaQuery<Localidad> configLocalidad = cb.createQuery(Localidad.class);
            Root<Localidad> raizLocalidad = configLocalidad.from(Localidad.class);
            
            Predicate predicateProvincia = cb.equal(raizLocalidad.get("provincia").get("idProvincia"),idProvincia);
            configLocalidad.select(raizLocalidad).where(predicateProvincia);            
            configLocalidad.orderBy(cb.asc(raizLocalidad.get("nombre")));      
            List<Localidad> resultado = em.createQuery(configLocalidad).getResultList();
            return resultado;
        } finally {
            em.close();
        } 
    }

    @Override
    public List<TipoPosicionFrenteIVA> getAllPosicionesIVA() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();          
            CriteriaQuery<TipoPosicionFrenteIVA> configPosicion = cb.createQuery(TipoPosicionFrenteIVA.class);
            Root<TipoPosicionFrenteIVA> raizPosicion = configPosicion.from(TipoPosicionFrenteIVA.class);
            configPosicion.select(raizPosicion);
            configPosicion.orderBy(cb.asc(raizPosicion.get("nombre")));      
            List<TipoPosicionFrenteIVA> resultado = em.createQuery(configPosicion).getResultList();
            return resultado;
        } finally {
            em.close();
        }  
    }
    
}
