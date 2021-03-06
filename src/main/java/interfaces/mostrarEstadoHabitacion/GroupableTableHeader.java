/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces.mostrarEstadoHabitacion;


import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

 

/**
  * GroupableTableHeader
  *
  * @version 1.0 10/20/98
  * @author Nobuo Tamemasa
  */

public class GroupableTableHeader extends JTableHeader 
{
    private static final String uiClassID = "GroupableTableHeaderUI";
    protected Vector columnGroups = null;

    public GroupableTableHeader(TableColumnModel model) 
    {
        super(model);
        setUI(new GroupableTableHeaderUI());
        setReorderingAllowed(false);
    }
    
    public void updateUI()
    {
        setUI(new GroupableTableHeaderUI());
        /*
        TableCellRenderer renderer = getDefaultRenderer();
            if (renderer instanceof Component) {
                SwingUtilities.updateComponentTreeUI((Component)renderer);
          }
        */
    }
  
    public void setReorderingAllowed(boolean b) {
        reorderingAllowed = false;
    }

    public void addColumnGroup(ColumnGroup g) 
    {
        if (columnGroups == null) {
            columnGroups = new Vector();
        }
        columnGroups.addElement(g);
    }

    public Enumeration getColumnGroups(TableColumn col) 
    {
        if (columnGroups == null) return null;
        Enumeration e = columnGroups.elements();
        while (e.hasMoreElements()) {
            ColumnGroup cGroup = (ColumnGroup)e.nextElement();
            Vector v_ret = (Vector)cGroup.getColumnGroups(col,new Vector());
            if (v_ret != null) { 
                return v_ret.elements();
            }
        }
        return null;
    }

    public void setColumnMargin() // Pasa los margenes a los encabezados
    {                               
        if (columnGroups == null) return;
        int columnMargin = getColumnModel().getColumnMargin();
        Enumeration e = columnGroups.elements();
        while (e.hasMoreElements()) 
        {
            ColumnGroup cGroup = (ColumnGroup)e.nextElement();
            cGroup.setColumnMargin(columnMargin);
        }
    }
  }
