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
import entidades.PersonaJuridica;
import entidades.Provincia;
import entidades.TipoDocumento;
import entidades.TipoPosicionFrenteIVA;
import java.math.BigInteger;
import java.util.ArrayList;
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
        this.emf = Persistence.createEntityManagerFactory("postgres");//"MiBaseDeDatos");
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

            Predicate predicateNombre;
            Predicate predicateApellido;
            Predicate predicateTipoDoc;
            Predicate predicateNumDoc;
            ArrayList<Predicate> condiciones = new ArrayList<>();

            if(!nombre.isBlank()) {
                predicateNombre = cb.like(raizPersona.get("nombres"),nombre+"%");
                condiciones.add(predicateNombre);
            }

            if(!apellido.isBlank()) {
                predicateApellido = cb.like(raizPersona.get("apellido"),apellido+"%");
                condiciones.add(predicateApellido);
            }

            if(!tipoDocumento.isBlank()) {
                predicateTipoDoc = cb.equal(raizPersona.get("tipoDocumento"),TipoDocumento.valueOf(tipoDocumento));
                condiciones.add(predicateTipoDoc);
            }

            if(!nroDocumento.isBlank() && !tipoDocumento.isBlank()) {
                Integer nro = Integer.parseInt(nroDocumento);
                predicateNumDoc = cb.equal(raizPersona.get("numeroDocumento"),nro);
                condiciones.add(predicateNumDoc);
            }

            configPersonas.select(raizPersona).where(cb.and(condiciones.toArray(new Predicate[condiciones.size()])));

            configPersonas.orderBy(cb.asc(raizPersona.get("nombres")));
            
            List<PersonaFisica> resultado = em.createQuery(configPersonas).getResultList();
            
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

    @Override
    public PersonaJuridica findPersonaJuridica(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PersonaJuridica.class, id);
        } finally {
            em.close();
        }    
    }

    public Boolean NoExisteAcompañante(Integer id) {
        /*
        Esta consulta debe devolver si existe una relacion entre la persona del id y una estadia
        en proceso, es decir, cuya fecha de finalizacion se superior a la fecha actual.
        */
        EntityManager em = getEntityManager();
        
        try{         
            String hql = "SELECT pf.idPersonaFisica from Estadia es JOIN es.pasajeroAcompañante pf WHERE es.fechaFin > CURRENT_DATE AND pf.idPersonaFisica = :idPersona";
            Query query = em.createQuery(hql);
            query.setParameter("idPersona", id);       
            List result = query.getResultList();            
            if(result.isEmpty()){
                return true;
            }
            
        }finally{
            em.close();
        }        
        
        return false;
    }
    
    @Override
    public Long countAll(String nombre, String apellido, String tipoDocumento, String nroDocumento){
        EntityManager em = getEntityManager();
        try{
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
            Root<PersonaFisica> root = criteriaQuery.from(PersonaFisica.class);
            
            Predicate predicateNombre;
            Predicate predicateApellido;
            Predicate predicateTipoDoc;
            Predicate predicateNumDoc;
            ArrayList<Predicate> condiciones = new ArrayList<>();

            if(!nombre.isBlank()) {
                predicateNombre = cb.like(root.get("nombres"),nombre+"%");
                condiciones.add(predicateNombre);
            }

            if(!apellido.isBlank()) {
                predicateApellido = cb.like(root.get("apellido"),apellido+"%");
                condiciones.add(predicateApellido);
            }

            if(!tipoDocumento.isBlank()) {
                predicateTipoDoc = cb.equal(root.get("tipoDocumento"),TipoDocumento.valueOf(tipoDocumento));
                condiciones.add(predicateTipoDoc);
            }

            if(!nroDocumento.isBlank() && !tipoDocumento.isBlank()) {
                Integer nro = Integer.parseInt(nroDocumento);
                predicateNumDoc = cb.equal(root.get("nroDocumento"),nro);
                condiciones.add(predicateNumDoc);
            }
            
            criteriaQuery.select(cb.count(root)).where(cb.and(condiciones.toArray(new Predicate[condiciones.size()])));
            
            //criteriaQuery.select(cb.count(root));
            
            Long result = em.createQuery(criteriaQuery).getSingleResult();
            
            return result;          
            
        } finally {
            em.close();
        }
    }
     
}
