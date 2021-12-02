/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImpl;

import dao.EstadiaDAO;
import daoImpl.exceptions.NonexistentEntityException;
import entidades.Estadia;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Factura;
import entidades.Habitacion;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author Nahuel Riquelme
 */
public class EstadiaDAOImpl implements EstadiaDAO {
    
    private EntityManagerFactory emf = null;

    public EstadiaDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("MiBaseDeDatos");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void createEstadia(Estadia estadia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura factura = estadia.getFactura();
            if (factura != null) {
                factura = em.getReference(factura.getClass(), factura.getNumero());
                estadia.setFactura(factura);
            }
            Habitacion habitacion = estadia.getHabitacion();
            if (habitacion != null) {
                habitacion = em.getReference(habitacion.getClass(), habitacion.getNumero());
                estadia.setHabitacion(habitacion);
            }
            em.persist(estadia);
            if (factura != null) {
                Estadia oldEstadiaOfFactura = factura.getEstadia();
                if (oldEstadiaOfFactura != null) {
                    oldEstadiaOfFactura.setFactura(null);
                    oldEstadiaOfFactura = em.merge(oldEstadiaOfFactura);
                }
                factura.setEstadia(estadia);
                factura = em.merge(factura);
            }
            if (habitacion != null) {
                habitacion.getEstadias().add(estadia);
                habitacion = em.merge(habitacion);
            }
            em.getTransaction().commit();
        }catch (Exception e) {
            System.out.println("Error al crear la estadia, en el dao");
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void updateEstadia(Estadia estadia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estadia persistentEstadia = em.find(Estadia.class, estadia.getIdEstadia());
            Factura facturaOld = persistentEstadia.getFactura();
            Factura facturaNew = estadia.getFactura();
            Habitacion habitacionOld = persistentEstadia.getHabitacion();
            Habitacion habitacionNew = estadia.getHabitacion();
            if (facturaNew != null) {
                facturaNew = em.getReference(facturaNew.getClass(), facturaNew.getNumero());
                estadia.setFactura(facturaNew);
            }
            if (habitacionNew != null) {
                habitacionNew = em.getReference(habitacionNew.getClass(), habitacionNew.getNumero());
                estadia.setHabitacion(habitacionNew);
            }
            estadia = em.merge(estadia);
            if (facturaOld != null && !facturaOld.equals(facturaNew)) {
                facturaOld.setEstadia(null);
                facturaOld = em.merge(facturaOld);
            }
            if (facturaNew != null && !facturaNew.equals(facturaOld)) {
                Estadia oldEstadiaOfFactura = facturaNew.getEstadia();
                if (oldEstadiaOfFactura != null) {
                    oldEstadiaOfFactura.setFactura(null);
                    oldEstadiaOfFactura = em.merge(oldEstadiaOfFactura);
                }
                facturaNew.setEstadia(estadia);
                facturaNew = em.merge(facturaNew);
            }
            if (habitacionOld != null && !habitacionOld.equals(habitacionNew)) {
                habitacionOld.getEstadias().remove(estadia);
                habitacionOld = em.merge(habitacionOld);
            }
            if (habitacionNew != null && !habitacionNew.equals(habitacionOld)) {
                habitacionNew.getEstadias().add(estadia);
                habitacionNew = em.merge(habitacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadia.getIdEstadia();
                if (findEstadia(id) == null) {
                    throw new NonexistentEntityException("The estadia with id " + id + " no longer exists.");
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
    public void deleteEstadia(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estadia estadia;
            try {
                estadia = em.getReference(Estadia.class, id);
                estadia.getIdEstadia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadia with id " + id + " no longer exists.", enfe);
            }
            Factura factura = estadia.getFactura();
            if (factura != null) {
                factura.setEstadia(null);
                factura = em.merge(factura);
            }
            Habitacion habitacion = estadia.getHabitacion();
            if (habitacion != null) {
                habitacion.getEstadias().remove(estadia);
                habitacion = em.merge(habitacion);
            }
            em.remove(estadia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Estadia> findEstadiaEntities() {
        return findEstadiaEntities(true, -1, -1);
    }

    public List<Estadia> findEstadiaEntities(int maxResults, int firstResult) {
        return findEstadiaEntities(false, maxResults, firstResult);
    }

    private List<Estadia> findEstadiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estadia.class));
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
    public Estadia findEstadia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estadia.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estadia> rt = cq.from(Estadia.class);
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

    public void persist(Object object) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Estadia> getEstadiasEntreFechas(LocalDate fechaInicioGui, LocalDate fechaFinGui) {
        EntityManager em = getEntityManager();
        
        try 
        {
            // https://www.baeldung.com/hibernate-criteria-queries
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Estadia> r = cq.from(Estadia.class);
            
            
            // https://stackoverflow.com/questions/9449003/compare-date-entities-in-jpa-criteria-api
            Predicate[] conds = new Predicate[3];
            conds[0] = cb.between(r.<LocalDate>get("fechaInicio"), fechaInicioGui, fechaFinGui);
            conds[1] = cb.between(r.<LocalDate>get("fechaFin"), fechaInicioGui, fechaFinGui);
            conds[2] = cb.and(
                cb.lessThan(r.<LocalDate>get("fechaInicio"), fechaInicioGui),
                cb.greaterThan(r.<LocalDate>get("fechaFin"), fechaFinGui)
            );

            cq.select(r).where(cb.or(conds));
            Query q = em.createQuery(cq);
            
            return q.getResultList();
        }
        finally {
            em.close();
        }      
    }
    
}
