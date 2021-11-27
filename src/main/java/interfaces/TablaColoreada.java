/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.awt.Color;
import java.awt.Component;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

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
        Component comp = super.prepareRenderer(rend, rowInd, colInd);
        Object val = getValueAt(rowInd, colInd);
        
        if (val != null && colInd > 0) 
        {
            color = new Color((int) val);
            comp.setBackground(color);
            comp.setForeground(color);
        }
        else
        {      
            comp.setBackground(Color.WHITE);
            comp.setForeground(Color.BLACK);
        }
        
        return comp;
    }
}
