/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.ReservaDAO;
import entidades.Estadia;
import entidades.PeriodoReserva;
import entidades.Reserva;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
public class ReservaDAOImpl implements ReservaDAO
{
    private EntityManagerFactory emf = null;

    public ReservaDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("postgres");//"MiBaseDeDatos");
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
}
