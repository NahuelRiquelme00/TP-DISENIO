/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.ServicioFacturadoDAO;
import daoImpl.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ServicioPrestado;
import entidades.Factura;
import entidades.ServicioFacturado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Fede
 */
public class ServicioFacturadoDAOImpl implements ServicioFacturadoDAO {

    public ServicioFacturadoDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public ServicioFacturadoDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("MiBaseDeDatos");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(ServicioFacturado servicioFacturado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServicioPrestado servicioPrestado = servicioFacturado.getServicioPrestado();
            if (servicioPrestado != null) {
                servicioPrestado = em.getReference(servicioPrestado.getClass(), servicioPrestado.getIdServicio());
                servicioFacturado.setServicioPrestado(servicioPrestado);
            }
            Factura factura = servicioFacturado.getFactura();
            if (factura != null) {
                factura = em.getReference(factura.getClass(), factura.getNumero());
                servicioFacturado.setFactura(factura);
            }
            em.persist(servicioFacturado);
            if (servicioPrestado != null) {
                servicioPrestado.getServiciosFacturados().add(servicioFacturado);
                servicioPrestado = em.merge(servicioPrestado);
            }
            if (factura != null) {
                factura.getServiciosFacturados().add(servicioFacturado);
                factura = em.merge(factura);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(ServicioFacturado servicioFacturado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServicioFacturado persistentServicioFacturado = em.find(ServicioFacturado.class, servicioFacturado.getIdServicio());
            ServicioPrestado servicioPrestadoOld = persistentServicioFacturado.getServicioPrestado();
            ServicioPrestado servicioPrestadoNew = servicioFacturado.getServicioPrestado();
            Factura facturaOld = persistentServicioFacturado.getFactura();
            Factura facturaNew = servicioFacturado.getFactura();
            if (servicioPrestadoNew != null) {
                servicioPrestadoNew = em.getReference(servicioPrestadoNew.getClass(), servicioPrestadoNew.getIdServicio());
                servicioFacturado.setServicioPrestado(servicioPrestadoNew);
            }
            if (facturaNew != null) {
                facturaNew = em.getReference(facturaNew.getClass(), facturaNew.getNumero());
                servicioFacturado.setFactura(facturaNew);
            }
            servicioFacturado = em.merge(servicioFacturado);
            if (servicioPrestadoOld != null && !servicioPrestadoOld.equals(servicioPrestadoNew)) {
                servicioPrestadoOld.getServiciosFacturados().remove(servicioFacturado);
                servicioPrestadoOld = em.merge(servicioPrestadoOld);
            }
            if (servicioPrestadoNew != null && !servicioPrestadoNew.equals(servicioPrestadoOld)) {
                servicioPrestadoNew.getServiciosFacturados().add(servicioFacturado);
                servicioPrestadoNew = em.merge(servicioPrestadoNew);
            }
            if (facturaOld != null && !facturaOld.equals(facturaNew)) {
                facturaOld.getServiciosFacturados().remove(servicioFacturado);
                facturaOld = em.merge(facturaOld);
            }
            if (facturaNew != null && !facturaNew.equals(facturaOld)) {
                facturaNew.getServiciosFacturados().add(servicioFacturado);
                facturaNew = em.merge(facturaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servicioFacturado.getIdServicio();
                if (findServicioFacturado(id) == null) {
                    throw new NonexistentEntityException("The servicioFacturado with id " + id + " no longer exists.");
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
    public List<ServicioFacturado> findServicioFacturadoEntities() {
        return findServicioFacturadoEntities(true, -1, -1);
    }

    @Override
    public List<ServicioFacturado> findServicioFacturadoEntities(int maxResults, int firstResult) {
        return findServicioFacturadoEntities(false, maxResults, firstResult);
    }

    private List<ServicioFacturado> findServicioFacturadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ServicioFacturado.class));
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
    public ServicioFacturado findServicioFacturado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ServicioFacturado.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getServicioFacturadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ServicioFacturado> rt = cq.from(ServicioFacturado.class);
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
