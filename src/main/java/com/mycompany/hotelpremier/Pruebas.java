/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hotelpremier;

import dto.EstadiaDTO;
import dto.PersonaFisicaDTO;
import entidades.PersonaFisica;
import entidades.TipoHabitacion;
import gestores.GestorDeAlojamientos;
import gestores.GestorDePersonas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.joda.money.Money;


/**
 *
 * @author Nahuel Riquelme
 */
public class Pruebas {
    private static final GestorDePersonas gestorPersonas = GestorDePersonas.getInstance();
    private static final GestorDeAlojamientos gestorAlojamientos = GestorDeAlojamientos.getInstance();
    public static void main(String[] args) {
        
             
          /* Para listar pasajeros */
//        System.out.println("Lista de personas:");
//        System.out.println(gestorPersonas.getAllPersonasFisicas());
  

          /* Para encuentrar un pasajero */
//        PersonaFisica p = gestorPersonas.findPersonaFisica(9);
//        
//        System.out.println("\nPersona con id: 9");
//        System.out.println(p);


        /* Para crear un pasajero */
        for (int i = 0; i < 30; i++) {
                PersonaFisicaDTO p = new PersonaFisicaDTO("Rodriguez","Eduardo", "DNI", 40617522, "1997-09-05",
                "email", "ocupacion", "nacionalidad", "telefono", "calle", 4782,
                "departamento", "piso", 3000, 7,3,1,1);        
                gestorPersonas.createPersonaFisica(p); 
        }
        
//        EstadiaDTO e = new EstadiaDTO();
//        e.setFechaInicio(LocalDate.now().toString());
//        e.setFechaFin(LocalDate.of(2021, 11, 29).toString());
//        e.setIdHabitacion(1);
//        e.setIdPasajeroResponsable(3);
//        List<Integer> idsPasajeroAcompañante = new ArrayList<>();
//        idsPasajeroAcompañante.add(4);
//        idsPasajeroAcompañante.add(5);
//        e.setIdsPasajeroAcompañante(idsPasajeroAcompañante);
//        gestorAlojamientos.createEstadia(e);

//         TipoHabitacion t = new TipoHabitacion();
//         t.setNombre("Personalizada");
//         t.setPrecioActual(Money.parse("ARG 5000"));
//         
//         System.out.println(t);
//
//           if(gestorPersonas.NoExisteAcompañante(7)){
//               System.out.println("Acompañante valiado");
//           }else{
//               System.out.println("Acompañante invalido");
//           }


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
