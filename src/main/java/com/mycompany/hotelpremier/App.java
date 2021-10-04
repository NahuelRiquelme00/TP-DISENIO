/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hotelpremier;

import dto.PersonaFisicaDTO;
import entidades.PersonaFisica;
import gestores.GestorDePersonas;


/**
 *
 * @author Nahuel Riquelme
 */
public class App {
    private static final GestorDePersonas gestorPersonas = GestorDePersonas.getInstance();
    
    public static void main(String[] args) {
             
//        System.out.println("Lista de personas:");
//        System.out.println(gestorPersonas.getAllPersonasFisicas());
  


//        PersonaFisica p = gestorPersonas.findPersonaFisica(9);
//        
//        System.out.println("\nPersona con id: 9");
//        System.out.println(p);
//        
//        System.out.println("\n Localidad:");
//        System.out.println(p.getDireccion().getLocalidad());
//        
//        System.out.println("\n Provincia:");
//        System.out.println(p.getDireccion().getLocalidad().getProvincia());
//        
//        System.out.println("\n Pais:");
//        System.out.println(p.getDireccion().getLocalidad().getProvincia().getPais());


//        PersonaFisicaDTO p = new PersonaFisicaDTO("apellido","nombres", "DNI", 40617522, "1997-09-05",
//                "email", "ocupacion", "nacionalidad", "telefono", "calle", 4782,
//                "departamento", "piso", 3000, "Consumidor Final",3,1,1);        
//        gestorPersonas.createPersonaFisica(p);


        System.out.println("\nPersona con id: 9");
        System.out.println(gestorPersonas.findPersonaFisica(9));
        
        PersonaFisicaDTO p = new PersonaFisicaDTO("Jose","Perez", "DNI", 40617522, "1997-09-05",
                "email", "Ingeniero", "nacionalidad", "telefono", "calle", 4782,
                "departamento", "piso", 3000, "Consumidor Final",3,1,1);
        
        gestorPersonas.updatePersonaFisica(p, 9);
       
        System.out.println("\nPersona con id: 9");
        System.out.println(gestorPersonas.findPersonaFisica(9));
    }
}
