/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImpl;

import dao.HabitacionDAO;
import daoImpl.exceptions.NonexistentEntityException;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Estadia;
import entidades.Habitacion;
import java.util.ArrayList;
import java.util.List;
import entidades.PeriodoReserva;
import entidades.TipoHabitacion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author Nahuel Riquelme
 */
public class HabitacionDAOImpl implements HabitacionDAO {
    
    private EntityManagerFactory emf = null;

    public HabitacionDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("postgres");//"MiBaseDeDatos");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void createHabitacion(Habitacion habitacion) {
        if (habitacion.getEstadias() == null) {
            habitacion.setEstadias(new ArrayList<Estadia>());
        }
        if (habitacion.getPeriodosReserva() == null) {
            habitacion.setPeriodosReserva(new ArrayList<PeriodoReserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estadia> attachedEstadias = new ArrayList<Estadia>();
            for (Estadia estadiasEstadiaToAttach : habitacion.getEstadias()) {
                estadiasEstadiaToAttach = em.getReference(estadiasEstadiaToAttach.getClass(), estadiasEstadiaToAttach.getIdEstadia());
                attachedEstadias.add(estadiasEstadiaToAttach);
            }
            habitacion.setEstadias(attachedEstadias);
            List<PeriodoReserva> attachedPeriodosReserva = new ArrayList<PeriodoReserva>();
            for (PeriodoReserva periodosReservaPeriodoReservaToAttach : habitacion.getPeriodosReserva()) {
                periodosReservaPeriodoReservaToAttach = em.getReference(periodosReservaPeriodoReservaToAttach.getClass(), periodosReservaPeriodoReservaToAttach.getIdPeriodoReserva());
                attachedPeriodosReserva.add(periodosReservaPeriodoReservaToAttach);
            }
            habitacion.setPeriodosReserva(attachedPeriodosReserva);
            em.persist(habitacion);
            for (Estadia estadiasEstadia : habitacion.getEstadias()) {
                Habitacion oldHabitacionOfEstadiasEstadia = estadiasEstadia.getHabitacion();
                estadiasEstadia.setHabitacion(habitacion);
                estadiasEstadia = em.merge(estadiasEstadia);
                if (oldHabitacionOfEstadiasEstadia != null) {
                    oldHabitacionOfEstadiasEstadia.getEstadias().remove(estadiasEstadia);
                    oldHabitacionOfEstadiasEstadia = em.merge(oldHabitacionOfEstadiasEstadia);
                }
            }
            for (PeriodoReserva periodosReservaPeriodoReserva : habitacion.getPeriodosReserva()) {
                Habitacion oldHabitacionOfPeriodosReservaPeriodoReserva = periodosReservaPeriodoReserva.getHabitacion();
                periodosReservaPeriodoReserva.setHabitacion(habitacion);
                periodosReservaPeriodoReserva = em.merge(periodosReservaPeriodoReserva);
                if (oldHabitacionOfPeriodosReservaPeriodoReserva != null) {
                    oldHabitacionOfPeriodosReservaPeriodoReserva.getPeriodosReserva().remove(periodosReservaPeriodoReserva);
                    oldHabitacionOfPeriodosReservaPeriodoReserva = em.merge(oldHabitacionOfPeriodosReservaPeriodoReserva);
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
    public void updateHabitacion(Habitacion habitacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
//            Habitacion persistentHabitacion = em.find(Habitacion.class, habitacion.getNumero());
//            List<Estadia> estadiasOld = persistentHabitacion.getEstadias();
//            List<Estadia> estadiasNew = habitacion.getEstadias();
//            List<PeriodoReserva> periodosReservaOld = persistentHabitacion.getPeriodosReserva();
//            List<PeriodoReserva> periodosReservaNew = habitacion.getPeriodosReserva();
//            List<Estadia> attachedEstadiasNew = new ArrayList<Estadia>();
//            for (Estadia estadiasNewEstadiaToAttach : estadiasNew) {
//                estadiasNewEstadiaToAttach = em.getReference(estadiasNewEstadiaToAttach.getClass(), estadiasNewEstadiaToAttach.getIdEstadia());
//                attachedEstadiasNew.add(estadiasNewEstadiaToAttach);
//            }
//            estadiasNew = attachedEstadiasNew;
//            habitacion.setEstadias(estadiasNew);
//            List<PeriodoReserva> attachedPeriodosReservaNew = new ArrayList<PeriodoReserva>();
//            for (PeriodoReserva periodosReservaNewPeriodoReservaToAttach : periodosReservaNew) {
//                periodosReservaNewPeriodoReservaToAttach = em.getReference(periodosReservaNewPeriodoReservaToAttach.getClass(), periodosReservaNewPeriodoReservaToAttach.getIdPeriodoReserva());
//                attachedPeriodosReservaNew.add(periodosReservaNewPeriodoReservaToAttach);
//            }
//            periodosReservaNew = attachedPeriodosReservaNew;
//            habitacion.setPeriodosReserva(periodosReservaNew);
            habitacion = em.merge(habitacion);
//            for (Estadia estadiasOldEstadia : estadiasOld) {
//                if (!estadiasNew.contains(estadiasOldEstadia)) {
//                    estadiasOldEstadia.setHabitacion(null);
//                    estadiasOldEstadia = em.merge(estadiasOldEstadia);
//                }
//            }
//            for (Estadia estadiasNewEstadia : estadiasNew) {
//                if (!estadiasOld.contains(estadiasNewEstadia)) {
//                    Habitacion oldHabitacionOfEstadiasNewEstadia = estadiasNewEstadia.getHabitacion();
//                    estadiasNewEstadia.setHabitacion(habitacion);
//                    estadiasNewEstadia = em.merge(estadiasNewEstadia);
//                    if (oldHabitacionOfEstadiasNewEstadia != null && !oldHabitacionOfEstadiasNewEstadia.equals(habitacion)) {
//                        oldHabitacionOfEstadiasNewEstadia.getEstadias().remove(estadiasNewEstadia);
//                        oldHabitacionOfEstadiasNewEstadia = em.merge(oldHabitacionOfEstadiasNewEstadia);
//                    }
//                }
//            }
//            for (PeriodoReserva periodosReservaOldPeriodoReserva : periodosReservaOld) {
//                if (!periodosReservaNew.contains(periodosReservaOldPeriodoReserva)) {
//                    periodosReservaOldPeriodoReserva.setHabitacion(null);
//                    periodosReservaOldPeriodoReserva = em.merge(periodosReservaOldPeriodoReserva);
//                }
//            }
//            for (PeriodoReserva periodosReservaNewPeriodoReserva : periodosReservaNew) {
//                if (!periodosReservaOld.contains(periodosReservaNewPeriodoReserva)) {
//                    Habitacion oldHabitacionOfPeriodosReservaNewPeriodoReserva = periodosReservaNewPeriodoReserva.getHabitacion();
//                    periodosReservaNewPeriodoReserva.setHabitacion(habitacion);
//                    periodosReservaNewPeriodoReserva = em.merge(periodosReservaNewPeriodoReserva);
//                    if (oldHabitacionOfPeriodosReservaNewPeriodoReserva != null && !oldHabitacionOfPeriodosReservaNewPeriodoReserva.equals(habitacion)) {
//                        oldHabitacionOfPeriodosReservaNewPeriodoReserva.getPeriodosReserva().remove(periodosReservaNewPeriodoReserva);
//                        oldHabitacionOfPeriodosReservaNewPeriodoReserva = em.merge(oldHabitacionOfPeriodosReservaNewPeriodoReserva);
//                    }
//                }
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = habitacion.getNumero();
                if (getById(id) == null) {
                    throw new NonexistentEntityException("The habitacion with id " + id + " no longer exists.");
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
    public void deleteHabitacion(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Habitacion habitacion;
            try {
                habitacion = em.getReference(Habitacion.class, id);
                habitacion.getNumero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The habitacion with id " + id + " no longer exists.", enfe);
            }
            List<Estadia> estadias = habitacion.getEstadias();
            for (Estadia estadiasEstadia : estadias) {
                estadiasEstadia.setHabitacion(null);
                estadiasEstadia = em.merge(estadiasEstadia);
            }
            List<PeriodoReserva> periodosReserva = habitacion.getPeriodosReserva();
            for (PeriodoReserva periodosReservaPeriodoReserva : periodosReserva) {
                periodosReservaPeriodoReserva.setHabitacion(null);
                periodosReservaPeriodoReserva = em.merge(periodosReservaPeriodoReserva);
            }
            em.remove(habitacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Habitacion> getAllHabitaciones() {
        return findHabitacionEntities(true, -1, -1);
    }

    public List<Habitacion> findHabitacionEntities(int maxResults, int firstResult) {
        return findHabitacionEntities(false, maxResults, firstResult);
    }

    private List<Habitacion> findHabitacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Habitacion.class));
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
    public Habitacion getById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Habitacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getHabitacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Habitacion> rt = cq.from(Habitacion.class);
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

    @Override
    public List<TipoHabitacion> getAllTiposHabitacion() {
        EntityManager em = getEntityManager();        
        try 
        {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<TipoHabitacion> r = cq.from(TipoHabitacion.class);
            cq.select(r);
            Query q = em.createQuery(cq);
            
            return q.getResultList();
        }
        finally {
            em.close();
        }     }
    
}
