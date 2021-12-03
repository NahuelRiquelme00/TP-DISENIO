package daoImpl;

import dao.ReservaDAO;
import daoImpl.exceptions.NonexistentEntityException;
import entidades.PeriodoReserva;
import entidades.Reserva;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Federico Pacheco
 */
public class ReservaDAOImpl implements ReservaDAO {
    private EntityManagerFactory emf = null;

    public ReservaDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("MiBaseDeDatos");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Pendiente implementar el resto de operaciones. No se como hacerlo. - Fede P.
    
    @Override
    public List<PeriodoReserva> getPeriodosReservaEntreFechas(LocalDate fechaInicioGui, LocalDate fechaFinGui) 
    {
        EntityManager em = getEntityManager();
        
        try
        {
            // https://www.baeldung.com/hibernate-criteria-queries
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<PeriodoReserva> r = cq.from(PeriodoReserva.class);
            
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

    @Override
    public void close() {
        emf.close();
    }
    
    @Override
    public void create(Reserva reserva) {
        if (reserva.getPeriodosReserva() == null) {
            reserva.setPeriodosReserva(new ArrayList<PeriodoReserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PeriodoReserva> attachedPeriodosReserva = new ArrayList<PeriodoReserva>();
            for (PeriodoReserva periodosReservaPeriodoReservaToAttach : reserva.getPeriodosReserva()) {
                periodosReservaPeriodoReservaToAttach = em.getReference(periodosReservaPeriodoReservaToAttach.getClass(), periodosReservaPeriodoReservaToAttach.getIdPeriodoReserva());
                attachedPeriodosReserva.add(periodosReservaPeriodoReservaToAttach);
            }
            reserva.setPeriodosReserva(attachedPeriodosReserva);
            em.persist(reserva);
            for (PeriodoReserva periodosReservaPeriodoReserva : reserva.getPeriodosReserva()) {
                Reserva oldReservaOfPeriodosReservaPeriodoReserva = periodosReservaPeriodoReserva.getReserva();
                periodosReservaPeriodoReserva.setReserva(reserva);
                periodosReservaPeriodoReserva = em.merge(periodosReservaPeriodoReserva);
                if (oldReservaOfPeriodosReservaPeriodoReserva != null) {
                    oldReservaOfPeriodosReservaPeriodoReserva.getPeriodosReserva().remove(periodosReservaPeriodoReserva);
                    oldReservaOfPeriodosReservaPeriodoReserva = em.merge(oldReservaOfPeriodosReservaPeriodoReserva);
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
    public void update(Reserva reserva) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reserva persistentReserva = em.find(Reserva.class, reserva.getIdReserva());
            List<PeriodoReserva> periodosReservaOld = persistentReserva.getPeriodosReserva();
            List<PeriodoReserva> periodosReservaNew = reserva.getPeriodosReserva();
            List<PeriodoReserva> attachedPeriodosReservaNew = new ArrayList<PeriodoReserva>();
            for (PeriodoReserva periodosReservaNewPeriodoReservaToAttach : periodosReservaNew) {
                periodosReservaNewPeriodoReservaToAttach = em.getReference(periodosReservaNewPeriodoReservaToAttach.getClass(), periodosReservaNewPeriodoReservaToAttach.getIdPeriodoReserva());
                attachedPeriodosReservaNew.add(periodosReservaNewPeriodoReservaToAttach);
            }
            periodosReservaNew = attachedPeriodosReservaNew;
            reserva.setPeriodosReserva(periodosReservaNew);
            reserva = em.merge(reserva);
            for (PeriodoReserva periodosReservaOldPeriodoReserva : periodosReservaOld) {
                if (!periodosReservaNew.contains(periodosReservaOldPeriodoReserva)) {
                    periodosReservaOldPeriodoReserva.setReserva(null);
                    periodosReservaOldPeriodoReserva = em.merge(periodosReservaOldPeriodoReserva);
                }
            }
            for (PeriodoReserva periodosReservaNewPeriodoReserva : periodosReservaNew) {
                if (!periodosReservaOld.contains(periodosReservaNewPeriodoReserva)) {
                    Reserva oldReservaOfPeriodosReservaNewPeriodoReserva = periodosReservaNewPeriodoReserva.getReserva();
                    periodosReservaNewPeriodoReserva.setReserva(reserva);
                    periodosReservaNewPeriodoReserva = em.merge(periodosReservaNewPeriodoReserva);
                    if (oldReservaOfPeriodosReservaNewPeriodoReserva != null && !oldReservaOfPeriodosReservaNewPeriodoReserva.equals(reserva)) {
                        oldReservaOfPeriodosReservaNewPeriodoReserva.getPeriodosReserva().remove(periodosReservaNewPeriodoReserva);
                        oldReservaOfPeriodosReservaNewPeriodoReserva = em.merge(oldReservaOfPeriodosReservaNewPeriodoReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reserva.getIdReserva();
                if (findReserva(id) == null) {
                    throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.");
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
    public void delete(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reserva reserva;
            try {
                reserva = em.getReference(Reserva.class, id);
                reserva.getIdReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            List<PeriodoReserva> periodosReserva = reserva.getPeriodosReserva();
            for (PeriodoReserva periodosReservaPeriodoReserva : periodosReserva) {
                periodosReservaPeriodoReserva.setReserva(null);
                periodosReservaPeriodoReserva = em.merge(periodosReservaPeriodoReserva);
            }
            em.remove(reserva);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Reserva> findReservaEntities() {
        return findReservaEntities(true, -1, -1);
    }

    public List<Reserva> findReservaEntities(int maxResults, int firstResult) {
        return findReservaEntities(false, maxResults, firstResult);
    }

    private List<Reserva> findReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reserva.class));
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
    public Reserva findReserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reserva> rt = cq.from(Reserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
