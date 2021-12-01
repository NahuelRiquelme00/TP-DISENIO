/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces.mostrarEstadoHabitacion;

import entidades.TipoEstado;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Federico Pacheco
 */
public class TablaColoreada extends JTable
{
    public static final Color COLOR_DISPONIBLE        = new Color(0x63db18);
    public static final Color COLOR_OCUPADA           = new Color(0xff0000);
    public static final Color COLOR_RESERVADA         = new Color(0xffff00);
    public static final Color COLOR_FUERA_DE_SERVICIO = new Color(0x0000ff);
    public static final Color COLOR_ERROR             = new Color(0x000000);
    
    private Color auxColor;
    
    @Override
    public Component prepareRenderer(TableCellRenderer rend, int rowInd, int colInd)
    {
        Component celda = super.prepareRenderer(rend, rowInd, colInd);
        Object val = getValueAt(rowInd, colInd);
        
        if (val != null)
        {
            auxColor = (Color) val;
            
            // https://stackoverflow.com/a/20472101
            if (this.isRowSelected(rowInd) && this.isColumnSelected(colInd))
                // https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html#darker()
                auxColor = auxColor.darker(); 
                
            celda.setBackground(auxColor);
            celda.setForeground(auxColor);
        }
        else
        {      
            celda.setBackground(Color.WHITE);
            celda.setForeground(Color.BLACK);
        }
        
        return celda;
    }
    
    @Override
    protected JTableHeader createDefaultTableHeader() {
        return new GroupableTableHeader(columnModel);
    }

    public static Color getColorGrilla(TipoEstado est) {
        Color res;
        switch (est) {
            case DISPONIBLE:
                res = TablaColoreada.COLOR_DISPONIBLE;
                break;
            case RESERVADA:
                res = TablaColoreada.COLOR_RESERVADA;
                break;
            case OCUPADA:
                res = TablaColoreada.COLOR_OCUPADA;
                break;
            case FUERA_DE_SERVICIO:
                res = TablaColoreada.COLOR_FUERA_DE_SERVICIO;
                break;
            default:
                // No se deberia llegar aca
                res = TablaColoreada.COLOR_ERROR;
                break;
        }
        return res;
    }
}
