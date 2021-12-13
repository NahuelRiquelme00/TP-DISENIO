/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.FacturaDAO;
import daoImpl.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.PersonaJuridica;
import entidades.NotaDeCredito;
import entidades.Estadia;
import entidades.Factura;
import entidades.ServicioFacturado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Fede
 */
public class FacturaDAOImpl implements FacturaDAO {

    public FacturaDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public FacturaDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("MiBaseDeDatos");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void createFactura(Factura factura) {
        if (factura.getServiciosFacturados() == null) {
            factura.setServiciosFacturados(new ArrayList<>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PersonaJuridica responsableDePagoJuridico = factura.getResponsableDePagoJuridico();
            if (responsableDePagoJuridico != null) {
                responsableDePagoJuridico = em.merge(responsableDePagoJuridico);
                factura.setResponsableDePagoJuridico(responsableDePagoJuridico);
            }
            NotaDeCredito notaDeCredito = factura.getNotaDeCredito();
            if (notaDeCredito != null) {
                notaDeCredito = em.getReference(notaDeCredito.getClass(), notaDeCredito.getNumeroNota());
                factura.setNotaDeCredito(notaDeCredito);
            }
            Estadia estadia = factura.getEstadia();
            if (estadia != null) {
                estadia = em.getReference(estadia.getClass(), estadia.getIdEstadia());
                factura.setEstadia(estadia);
            }
            List<ServicioFacturado> attachedServiciosFacturados = new ArrayList<>();
            for (ServicioFacturado serviciosFacturadosServicioFacturadoToAttach : factura.getServiciosFacturados()) {
                serviciosFacturadosServicioFacturadoToAttach = em.getReference(serviciosFacturadosServicioFacturadoToAttach.getClass(), serviciosFacturadosServicioFacturadoToAttach.getNombre());
                attachedServiciosFacturados.add(serviciosFacturadosServicioFacturadoToAttach);
            }
            factura.setServiciosFacturados(attachedServiciosFacturados);
            em.persist(factura);
            if (responsableDePagoJuridico != null) {
                responsableDePagoJuridico.getFacturas().add(factura);
                responsableDePagoJuridico = em.merge(responsableDePagoJuridico);
            }
            if (notaDeCredito != null) {
                notaDeCredito.getFacturas().add(factura);
                notaDeCredito = em.merge(notaDeCredito);
            }
            if (estadia != null) {
                Factura oldFacturaOfEstadia = estadia.getFactura();
                if (oldFacturaOfEstadia != null) {
                    oldFacturaOfEstadia.setEstadia(null);
                    oldFacturaOfEstadia = em.merge(oldFacturaOfEstadia);
                }
                estadia.setFactura(factura);
                estadia = em.merge(estadia);
            }
            for (ServicioFacturado serviciosFacturadosServicioFacturado : factura.getServiciosFacturados()) {
                Factura oldFacturaOfServiciosFacturadosServicioFacturado = serviciosFacturadosServicioFacturado.getFactura();
                serviciosFacturadosServicioFacturado.setFactura(factura);
                serviciosFacturadosServicioFacturado = em.merge(serviciosFacturadosServicioFacturado);
                if (oldFacturaOfServiciosFacturadosServicioFacturado != null) {
                    oldFacturaOfServiciosFacturadosServicioFacturado.getServiciosFacturados().remove(serviciosFacturadosServicioFacturado);
                    oldFacturaOfServiciosFacturadosServicioFacturado = em.merge(oldFacturaOfServiciosFacturadosServicioFacturado);
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
    public void edit(Factura factura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getNumero());
            PersonaJuridica responsableDePagoJuridicoOld = persistentFactura.getResponsableDePagoJuridico();
            PersonaJuridica responsableDePagoJuridicoNew = factura.getResponsableDePagoJuridico();
            NotaDeCredito notaDeCreditoOld = persistentFactura.getNotaDeCredito();
            NotaDeCredito notaDeCreditoNew = factura.getNotaDeCredito();
            Estadia estadiaOld = persistentFactura.getEstadia();
            Estadia estadiaNew = factura.getEstadia();
            List<ServicioFacturado> serviciosFacturadosOld = persistentFactura.getServiciosFacturados();
            List<ServicioFacturado> serviciosFacturadosNew = factura.getServiciosFacturados();
            if (responsableDePagoJuridicoNew != null) {
                responsableDePagoJuridicoNew = em.merge(responsableDePagoJuridicoNew);
                factura.setResponsableDePagoJuridico(responsableDePagoJuridicoNew);
            }
            if (notaDeCreditoNew != null) {
                notaDeCreditoNew = em.getReference(notaDeCreditoNew.getClass(), notaDeCreditoNew.getNumeroNota());
                factura.setNotaDeCredito(notaDeCreditoNew);
            }
            if (estadiaNew != null) {
                estadiaNew = em.getReference(estadiaNew.getClass(), estadiaNew.getIdEstadia());
                factura.setEstadia(estadiaNew);
            }
            List<ServicioFacturado> attachedServiciosFacturadosNew = new ArrayList<ServicioFacturado>();
            for (ServicioFacturado serviciosFacturadosNewServicioFacturadoToAttach : serviciosFacturadosNew) {
                serviciosFacturadosNewServicioFacturadoToAttach = em.getReference(serviciosFacturadosNewServicioFacturadoToAttach.getClass(), serviciosFacturadosNewServicioFacturadoToAttach.getNombre());
                attachedServiciosFacturadosNew.add(serviciosFacturadosNewServicioFacturadoToAttach);
            }
            serviciosFacturadosNew = attachedServiciosFacturadosNew;
            factura.setServiciosFacturados(serviciosFacturadosNew);
            factura = em.merge(factura);
            if (responsableDePagoJuridicoOld != null && !responsableDePagoJuridicoOld.equals(responsableDePagoJuridicoNew)) {
                responsableDePagoJuridicoOld.getFacturas().remove(factura);
                responsableDePagoJuridicoOld = em.merge(responsableDePagoJuridicoOld);
            }
            if (responsableDePagoJuridicoNew != null && !responsableDePagoJuridicoNew.equals(responsableDePagoJuridicoOld)) {
                responsableDePagoJuridicoNew.getFacturas().add(factura);
                responsableDePagoJuridicoNew = em.merge(responsableDePagoJuridicoNew);
            }
            if (notaDeCreditoOld != null && !notaDeCreditoOld.equals(notaDeCreditoNew)) {
                notaDeCreditoOld.getFacturas().remove(factura);
                notaDeCreditoOld = em.merge(notaDeCreditoOld);
            }
            if (notaDeCreditoNew != null && !notaDeCreditoNew.equals(notaDeCreditoOld)) {
                notaDeCreditoNew.getFacturas().add(factura);
                notaDeCreditoNew = em.merge(notaDeCreditoNew);
            }
            if (estadiaOld != null && !estadiaOld.equals(estadiaNew)) {
                estadiaOld.setFactura(null);
                estadiaOld = em.merge(estadiaOld);
            }
            if (estadiaNew != null && !estadiaNew.equals(estadiaOld)) {
                Factura oldFacturaOfEstadia = estadiaNew.getFactura();
                if (oldFacturaOfEstadia != null) {
                    oldFacturaOfEstadia.setEstadia(null);
                    oldFacturaOfEstadia = em.merge(oldFacturaOfEstadia);
                }
                estadiaNew.setFactura(factura);
                estadiaNew = em.merge(estadiaNew);
            }
            for (ServicioFacturado serviciosFacturadosOldServicioFacturado : serviciosFacturadosOld) {
                if (!serviciosFacturadosNew.contains(serviciosFacturadosOldServicioFacturado)) {
                    serviciosFacturadosOldServicioFacturado.setFactura(null);
                    serviciosFacturadosOldServicioFacturado = em.merge(serviciosFacturadosOldServicioFacturado);
                }
            }
            for (ServicioFacturado serviciosFacturadosNewServicioFacturado : serviciosFacturadosNew) {
                if (!serviciosFacturadosOld.contains(serviciosFacturadosNewServicioFacturado)) {
                    Factura oldFacturaOfServiciosFacturadosNewServicioFacturado = serviciosFacturadosNewServicioFacturado.getFactura();
                    serviciosFacturadosNewServicioFacturado.setFactura(factura);
                    serviciosFacturadosNewServicioFacturado = em.merge(serviciosFacturadosNewServicioFacturado);
                    if (oldFacturaOfServiciosFacturadosNewServicioFacturado != null && !oldFacturaOfServiciosFacturadosNewServicioFacturado.equals(factura)) {
                        oldFacturaOfServiciosFacturadosNewServicioFacturado.getServiciosFacturados().remove(serviciosFacturadosNewServicioFacturado);
                        oldFacturaOfServiciosFacturadosNewServicioFacturado = em.merge(oldFacturaOfServiciosFacturadosNewServicioFacturado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = factura.getNumero();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
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
    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    @Override
    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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
    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
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