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
        
        System.out.println(gestorPersonas.getAllPaises().get(0).getIdPais());
             
          /* Para listar pasajeros */
//        System.out.println("Lista de personas:");
//        System.out.println(gestorPersonas.getAllPersonasFisicas());
  

          /* Para encuentrar un pasajero */
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


        /* Para crear un pasajero */
//        for (int i = 0; i < 20; i++) {
//                PersonaFisicaDTO p = new PersonaFisicaDTO("Rodriguez","Eduardo", "DNI", 40617522, "1997-09-05",
//                "email", "ocupacion", "nacionalidad", "telefono", "calle", 4782,
//                "departamento", "piso", 3000, 7,3,1,1);        
//                gestorPersonas.createPersonaFisica(p); 
//        }



        /* Para modificar un pasajero */
//        System.out.println("\nPersona con id: 9");
//        System.out.println(gestorPersonas.findPersonaFisica(9));
//        
//        PersonaFisicaDTO p = new PersonaFisicaDTO("Jose","Perez", "DNI", 40617522, "1997-09-05",
//                "email", "Ingeniero", "nacionalidad", "telefono", "calle", 4782,
//                "departamento", "piso", 3000, "Consumidor Final",3,1,1);
//        
//        gestorPersonas.updatePersonaFisica(p, 9);
//       
//        System.out.println("\nPersona con id: 9");
//        System.out.println(gestorPersonas.findPersonaFisica(9));  
    }
}
