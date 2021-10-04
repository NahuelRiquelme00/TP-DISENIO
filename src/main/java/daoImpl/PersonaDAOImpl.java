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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
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
    public PersonaFisica findPersonaFisica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PersonaFisica.class, id);
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
    
}
