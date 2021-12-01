/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import dto.PersonaFisicaDTO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Nahuel Riquelme
 */
public class PasajerosTableModel extends AbstractTableModel {

    private List<PersonaFisicaDTO> datos = new ArrayList<>();

    public List<PersonaFisicaDTO> getDatos() {
        return datos;
    }

    public void setDatos(List<PersonaFisicaDTO> datos) {
        this.datos = datos;
    }
    
    public void agregarPersona(PersonaFisicaDTO e){
        datos.add(e);
    }
    
    public void quitarPersona(int i){
        datos.remove(i);
    }
    
    public PersonaFisicaDTO personaSelecionada(int r){
        return datos.get(r);
    }
    
    @Override
    public String getColumnName(int column) {
        // TODO Auto-generated method stub
        switch (column) {
        case 0: return "Nombre(s)";
        case 1: return "Apellido(s)";
        case 2: return "Tipo de documento";
        case 3: return "Número de documento";		
        default: return "Error al cargar nombres";
        }
    }
    
    @Override
    public int getRowCount() {
        return datos.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PersonaFisicaDTO persona = datos.get(rowIndex);
        switch(columnIndex){
            case 0: return persona.getNombres();
            case 1: return persona.getApellido();
            case 2: return persona.getTipoDocumento();
            case 3: return persona.getNroDocumento();
            default: return "Error al cargar los datos";
        }
    }    
    
}
