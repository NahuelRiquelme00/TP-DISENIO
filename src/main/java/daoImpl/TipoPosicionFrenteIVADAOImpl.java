/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.TipoPosicionFrenteIVADAO;
import daoImpl.exceptions.NonexistentEntityException;
import entidades.TipoPosicionFrenteIVA;
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
 * @author Fede
 */
public class TipoPosicionFrenteIVADAOImpl implements TipoPosicionFrenteIVADAO {

    public TipoPosicionFrenteIVADAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    private EntityManagerFactory emf = null;
    
    public TipoPosicionFrenteIVADAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("MiBaseDeDatos");//"MiBaseDeDatos");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(TipoPosicionFrenteIVA tipoPosicionFrenteIVA) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoPosicionFrenteIVA);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(TipoPosicionFrenteIVA tipoPosicionFrenteIVA) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipoPosicionFrenteIVA = em.merge(tipoPosicionFrenteIVA);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoPosicionFrenteIVA.getIdTipoPosicionFrenteIVA();
                if (findTipoPosicionFrenteIVA(id) == null) {
                    throw new NonexistentEntityException("The tipoPosicionFrenteIVA with id " + id + " no longer exists.");
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
    public List<TipoPosicionFrenteIVA> findTipoPosicionFrenteIVAEntities() {
        return findTipoPosicionFrenteIVAEntities(true, -1, -1);
    }

    @Override
    public List<TipoPosicionFrenteIVA> findTipoPosicionFrenteIVAEntities(int maxResults, int firstResult) {
        return findTipoPosicionFrenteIVAEntities(false, maxResults, firstResult);
    }

    private List<TipoPosicionFrenteIVA> findTipoPosicionFrenteIVAEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPosicionFrenteIVA.class));
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
    public TipoPosicionFrenteIVA findTipoPosicionFrenteIVA(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPosicionFrenteIVA.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getTipoPosicionFrenteIVACount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPosicionFrenteIVA> rt = cq.from(TipoPosicionFrenteIVA.class);
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
