/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Nahuel Riquelme
 */
public class GestorDeFacturas {
    private static GestorDeFacturas instance;
        
    public static GestorDeFacturas getInstance() {
        if (instance == null) {
            instance = new GestorDeFacturas();
        }
        return instance;
    }
}

    

