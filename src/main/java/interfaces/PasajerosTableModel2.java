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
public class PasajerosTableModel2 extends AbstractTableModel {

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
    
    public PersonaFisicaDTO personaSelecionada(int r){
        return datos.get(r);
    }
    
    public void quitarPersona(int i){
        datos.remove(i);
    }
    
    @Override
    public String getColumnName(int column) {
        // TODO Auto-generated method stub
        switch (column) {
        case 0: return "Nombre(s)";
        case 1: return "Apellido(s)";
        case 2: return "Tipo de documento";
        case 3: return "Numero de documento";
        case 4: return "Categoria";
        default: return "Error al cargar nombres";
        }
    }
    
    @Override
    public int getRowCount() {
        return datos.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PersonaFisicaDTO persona = datos.get(rowIndex);
        switch(columnIndex){
            case 0: return persona.getNombres();
            case 1: return persona.getApellido();
            case 2: return persona.getTipoDocumento();
            case 3: return persona.getNroDocumento();
            case 4: return persona.getCategoria();
            default: return "Error al cargar los datos";
        }
    }    
    
}
