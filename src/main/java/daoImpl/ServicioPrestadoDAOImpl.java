/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.ServicioPrestadoDAO;
import daoImpl.exceptions.NonexistentEntityException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ServicioFacturado;
import entidades.ServicioPrestado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Fede
 */
public class ServicioPrestadoDAOImpl implements ServicioPrestadoDAO {

    public ServicioPrestadoDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public ServicioPrestadoDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("MiBaseDeDatos");//"MiBaseDeDatos");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void createServicioPrestado(ServicioPrestado servicioPrestado) {
        if (servicioPrestado.getServiciosFacturados() == null) {
            servicioPrestado.setServiciosFacturados(new ArrayList<ServicioFacturado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ServicioFacturado> attachedServiciosFacturados = new ArrayList<ServicioFacturado>();
            for (ServicioFacturado serviciosFacturadosServicioFacturadoToAttach : servicioPrestado.getServiciosFacturados()) {
                serviciosFacturadosServicioFacturadoToAttach = em.getReference(serviciosFacturadosServicioFacturadoToAttach.getClass(), serviciosFacturadosServicioFacturadoToAttach.getNombre());
                attachedServiciosFacturados.add(serviciosFacturadosServicioFacturadoToAttach);
            }
            servicioPrestado.setServiciosFacturados(attachedServiciosFacturados);
            em.persist(servicioPrestado);
            for (ServicioFacturado serviciosFacturadosServicioFacturado : servicioPrestado.getServiciosFacturados()) {
                ServicioPrestado oldServicioPrestadoOfServiciosFacturadosServicioFacturado = serviciosFacturadosServicioFacturado.getServicioPrestado();
                serviciosFacturadosServicioFacturado.setServicioPrestado(servicioPrestado);
                serviciosFacturadosServicioFacturado = em.merge(serviciosFacturadosServicioFacturado);
                if (oldServicioPrestadoOfServiciosFacturadosServicioFacturado != null) {
                    oldServicioPrestadoOfServiciosFacturadosServicioFacturado.getServiciosFacturados().remove(serviciosFacturadosServicioFacturado);
                    oldServicioPrestadoOfServiciosFacturadosServicioFacturado = em.merge(oldServicioPrestadoOfServiciosFacturadosServicioFacturado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(ServicioPrestado servicioPrestado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServicioPrestado persistentServicioPrestado = em.find(ServicioPrestado.class, servicioPrestado.getIdServicio());
            List<ServicioFacturado> serviciosFacturadosOld = persistentServicioPrestado.getServiciosFacturados();
            List<ServicioFacturado> serviciosFacturadosNew = servicioPrestado.getServiciosFacturados();
            List<ServicioFacturado> attachedServiciosFacturadosNew = new ArrayList<ServicioFacturado>();
            for (ServicioFacturado serviciosFacturadosNewServicioFacturadoToAttach : serviciosFacturadosNew) {
                serviciosFacturadosNewServicioFacturadoToAttach = em.getReference(serviciosFacturadosNewServicioFacturadoToAttach.getClass(), serviciosFacturadosNewServicioFacturadoToAttach.getNombre());
                attachedServiciosFacturadosNew.add(serviciosFacturadosNewServicioFacturadoToAttach);
            }
            serviciosFacturadosNew = attachedServiciosFacturadosNew;
            servicioPrestado.setServiciosFacturados(serviciosFacturadosNew);
            servicioPrestado = em.merge(servicioPrestado);
            for (ServicioFacturado serviciosFacturadosOldServicioFacturado : serviciosFacturadosOld) {
                if (!serviciosFacturadosNew.contains(serviciosFacturadosOldServicioFacturado)) {
                    serviciosFacturadosOldServicioFacturado.setServicioPrestado(null);
                    serviciosFacturadosOldServicioFacturado = em.merge(serviciosFacturadosOldServicioFacturado);
                }
            }
            for (ServicioFacturado serviciosFacturadosNewServicioFacturado : serviciosFacturadosNew) {
                if (!serviciosFacturadosOld.contains(serviciosFacturadosNewServicioFacturado)) {
                    ServicioPrestado oldServicioPrestadoOfServiciosFacturadosNewServicioFacturado = serviciosFacturadosNewServicioFacturado.getServicioPrestado();
                    serviciosFacturadosNewServicioFacturado.setServicioPrestado(servicioPrestado);
                    serviciosFacturadosNewServicioFacturado = em.merge(serviciosFacturadosNewServicioFacturado);
                    if (oldServicioPrestadoOfServiciosFacturadosNewServicioFacturado != null && !oldServicioPrestadoOfServiciosFacturadosNewServicioFacturado.equals(servicioPrestado)) {
                        oldServicioPrestadoOfServiciosFacturadosNewServicioFacturado.getServiciosFacturados().remove(serviciosFacturadosNewServicioFacturado);
                        oldServicioPrestadoOfServiciosFacturadosNewServicioFacturado = em.merge(oldServicioPrestadoOfServiciosFacturadosNewServicioFacturado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servicioPrestado.getIdServicio();
                if (findServicioPrestado(id) == null) {
                    throw new NonexistentEntityException("The servicioPrestado with id " + id + " no longer exists.");
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
    public List<ServicioPrestado> findServicioPrestadoEntities() {
        return findServicioPrestadoEntities(true, -1, -1);
    }

    @Override
    public List<ServicioPrestado> findServicioPrestadoEntities(int maxResults, int firstResult) {
        return findServicioPrestadoEntities(false, maxResults, firstResult);
    }

    private List<ServicioPrestado> findServicioPrestadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ServicioPrestado.class));
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
    public ServicioPrestado findServicioPrestado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ServicioPrestado.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicioPrestadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ServicioPrestado> rt = cq.from(ServicioPrestado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void close() {
        emf.close();
    }

    
}
