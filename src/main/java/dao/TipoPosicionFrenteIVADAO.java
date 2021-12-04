/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import daoImpl.exceptions.NonexistentEntityException;
import entidades.TipoPosicionFrenteIVA;
import java.util.List;

/**
 *
 * @author Fede
 */
public interface TipoPosicionFrenteIVADAO {
    public void create(TipoPosicionFrenteIVA tipoPosicionFrenteIVA);
    
    public void edit(TipoPosicionFrenteIVA tipoPosicionFrenteIVA) throws NonexistentEntityException, Exception;
    
    public List<TipoPosicionFrenteIVA> findTipoPosicionFrenteIVAEntities();
    
    public List<TipoPosicionFrenteIVA> findTipoPosicionFrenteIVAEntities(int maxResults, int firstResult);
    
    public TipoPosicionFrenteIVA findTipoPosicionFrenteIVA(Integer id);
    
    public int getTipoPosicionFrenteIVACount();
    
    public void close();
}
