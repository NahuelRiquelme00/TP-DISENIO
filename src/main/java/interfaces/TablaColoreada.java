/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import misc.GroupableTableHeader;

/**
 *
 * @author Federico Pacheco
 */
public class TablaColoreada extends JTable
{
    public static final int COLOR_DISPONIBLE = 0x63db18;
    public static final int COLOR_OCUPADA = 0xff0000;
    public static final int COLOR_RESERVADA = 0xffff00;
    public static final int COLOR_FUERA_DE_SERVICIO = 0x0000ff;
    public static final int COLOR_ERROR = 0x000000;
    
    private Color color;
    
    
    @Override
    public Component prepareRenderer(TableCellRenderer rend, int rowInd, int colInd)
    {
        Component celda = super.prepareRenderer(rend, rowInd, colInd);
        Object val = getValueAt(rowInd, colInd);
        
        if (val != null && colInd > 0) 
        {
            color = new Color((int) val);
            // https://stackoverflow.com/a/20472101
            if (this.isRowSelected(rowInd) && this.isColumnSelected(colInd))
                // https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html#darker()
                color = color.darker(); 
                
            celda.setBackground(color);
            celda.setForeground(color);
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
}
