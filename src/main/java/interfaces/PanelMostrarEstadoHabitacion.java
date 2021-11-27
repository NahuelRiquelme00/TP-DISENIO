/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package interfaces;

import entidades.TipoEstado;
import gestores.GestorDeAlojamientos;
import java.awt.Color;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import misc.GroupableTableHeader;
import misc.ColumnGroup;
import misc.Dupla;

/**
 *
 * @author Federico Pacheco
 */
public class PanelMostrarEstadoHabitacion extends javax.swing.JPanel {

    private boolean paraReservar; // Reservar: true; ocupar: false
    
    private GestorDeAlojamientos gesAl;
    Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones;
    
    private List<Integer> idHabsTabla = null;
    private List<LocalDate> fechasTabla = null;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public PanelMostrarEstadoHabitacion(boolean paraReservar) 
    {
        initComponents();
        gesAl = GestorDeAlojamientos.getInstance();
        this.paraReservar = paraReservar;
        
        armarTabla();
        dcFechaDesde.setDate(new Date(System.currentTimeMillis()));
        dcFechaHasta.setDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // Fecha de hoy + 7 dias
        this.completarTabla(LocalDate.now(), LocalDate.now().plusDays(7));
    }

    private void armarTabla() 
    {
        List<Dupla<String, LinkedList<Integer>>> tiposYHabitaciones = gesAl.getTiposYHabitaciones();
        
        //http://www.java2s.com/Code/Java/Swing-Components/GroupableGroupHeaderExample.htm    
     
        // Columnas "H(...)"
        idHabsTabla = new LinkedList<Integer>();
        modeloTabla.addColumn("Fecha");
        for (Dupla<String, LinkedList<Integer>> d : tiposYHabitaciones) // (Tristemente) deben colocarse las columnas por anticipado 
        {
            for (Integer habNro : d.segundo) 
            {
                idHabsTabla.add(habNro);
                modeloTabla.addColumn("H" + habNro);
            }
        }
        
        // Encabezados tipos de habitacion
        TableColumnModel colMod = tablaEstadoHabitaciones.getColumnModel();
        GroupableTableHeader header = (GroupableTableHeader) tablaEstadoHabitaciones.getTableHeader();
        int j = 1;  
        for (Dupla<String, LinkedList<Integer>> d : tiposYHabitaciones)
        {
            ColumnGroup colGr = new ColumnGroup(d.primero);
            if (d.segundo.size() > 0)
            {
                for (Integer habNro : d.segundo)
                {
                    colGr.add(colMod.getColumn(j));
                    j++;
                }
                header.addColumnGroup(colGr);
            }
        }
    }
    
    private Object[] getFila(Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones, LocalDate fecha)
    {
        Object[] fila = new Object[idHabsTabla.size() + 1];
        
        // https://www.baeldung.com/java-datetimeformatter
        fila[0] = formatter.format(fecha);
        for (int j = 0; j < idHabsTabla.size(); j++)
            fila[j + 1] = this.getColorGrilla(estadosHabitaciones.get(fecha).get(idHabsTabla.get(j)));
        
        return fila;
    }
    
    private void completarTabla(LocalDate fechaIni, LocalDate fechaFin)
    {
        fechasTabla = new LinkedList<LocalDate>();
        
        estadosHabitaciones = gesAl.getEstadosHabitaciones(fechaIni, fechaFin);
        int cantDias = (int) fechaIni.until(fechaFin, ChronoUnit.DAYS) + 1;
            
        modeloTabla.setRowCount(0); // Eliminar datos anteriores
        for (int i = 0; i < cantDias; i++)
        {
            modeloTabla.addRow(this.getFila(estadosHabitaciones, fechaIni.plusDays(i)));
            fechasTabla.add(fechaIni.plusDays(i));
        }
        modeloTabla.fireTableDataChanged();
    }
    
    private int getColorGrilla(TipoEstado est)
    {
        int res;
        
        switch(est)
        {
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
            default: // No se deberia llegar aca
                res = TablaColoreada.COLOR_ERROR;
                break;
        }
        
        return res;
    }
    
    public boolean verificarDisponibilidad()
    {
        boolean disponibles;
        
        int[] filasSelec = tablaEstadoHabitaciones.getSelectedRows();
        int[] colsSelec = tablaEstadoHabitaciones.getSelectedColumns(); 
        
        int i, iIni, iFin;
        int j, jIni, jFin;
      
        // No contempla selecciones no contiguas :(
        
        // REFORMULAR
        
        if (filasSelec.length == 0 || colsSelec.length == 0)
            disponibles = false;
        else if (colsSelec.length == 1 && colsSelec[0] == 0)
            disponibles = false;
        else
        {
            iIni = filasSelec[0];
            iFin = filasSelec[filasSelec.length - 1];
        
            // Evitar problemas con la col. de las fechas
            if (colsSelec.length == 1)    
                jIni = colsSelec[0];
            else
            {
                if (colsSelec[0] == 0)
                    jIni = colsSelec[1];
                else
                    jIni = colsSelec[0];
            }   
            jFin = colsSelec[colsSelec.length - 1]; 
  
            
            TipoEstado est;
            i = iIni;
            disponibles = true;
            while (i <= iFin && disponibles)
            {
                j = jIni;
                while (j <= jFin && disponibles)
                {
                    est = estadosHabitaciones.get(fechasTabla.get(i)).get(idHabsTabla.get(j));
                    if (!est.equals(TipoEstado.DISPONIBLE))
                        disponibles = false;
                    else
                        j++;
                }
            
                i++;
            }
        }
             
        return disponibles;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelRangoFechas = new javax.swing.JPanel();
        lblFechaDesde = new javax.swing.JLabel();
        lblFechaHasta = new javax.swing.JLabel();
        dcFechaDesde = new com.toedter.calendar.JDateChooser();
        dcFechaHasta = new com.toedter.calendar.JDateChooser();
        buscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cancelar = new javax.swing.JButton();
        lblOcupada = new javax.swing.JLabel();
        lblDisponible = new javax.swing.JLabel();
        lblFueraDeServicio = new javax.swing.JLabel();
        lblReservada = new javax.swing.JLabel();
        cuadOcupada = new javax.swing.JLabel();
        cuadReservada = new javax.swing.JLabel();
        cuadDisponible = new javax.swing.JLabel();
        cuadFueraDeServicio = new javax.swing.JLabel();
        siguiente = new javax.swing.JButton();
        panelDatosHabitaciones = new javax.swing.JScrollPane();
        tablaEstadoHabitaciones = new TablaColoreada() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        panelRangoFechas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelRangoFechas.setLayout(new java.awt.GridBagLayout());

        lblFechaDesde.setText("Fecha desde");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 24, 0, 0);
        panelRangoFechas.add(lblFechaDesde, gridBagConstraints);

        lblFechaHasta.setText("Fecha hasta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 110, 0, 0);
        panelRangoFechas.add(lblFechaHasta, gridBagConstraints);

        dcFechaDesde.setDateFormatString("dd/MM/yyyy"); // https://www.youtube.com/watch?v=kmZXUlp5F5Q
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 85;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 12, 16, 0);
        panelRangoFechas.add(dcFechaDesde, gridBagConstraints);

        dcFechaHasta.setDateFormatString("dd/MM/yyyy"); // https://www.youtube.com/watch?v=kmZXUlp5F5Q
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 85;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 18, 16, 0);
        panelRangoFechas.add(dcFechaHasta, gridBagConstraints);

        buscar.setText("Buscar");
        buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 181, 16, 20);
        panelRangoFechas.add(buscar, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Rango de fechas");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Datos de las habitaciones");

        cancelar.setText("Cancelar");

        lblOcupada.setText("Ocupada");

        lblDisponible.setText("Disponible");

        lblFueraDeServicio.setText("Fuera de servicio");

        lblReservada.setText("Reservada");

        cuadOcupada.setBackground(new java.awt.Color(255, 0, 0));
        cuadOcupada.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuadOcupada.setForeground(new java.awt.Color(255, 0, 0));
        cuadOcupada.setText("■");

        cuadReservada.setBackground(new java.awt.Color(255, 255, 0));
        cuadReservada.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuadReservada.setForeground(new java.awt.Color(255, 255, 0));
        cuadReservada.setText("■");

        cuadDisponible.setBackground(new java.awt.Color(99, 219, 24));
        cuadDisponible.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuadDisponible.setForeground(new java.awt.Color(99, 219, 24));
        cuadDisponible.setText("■");

        cuadFueraDeServicio.setBackground(new java.awt.Color(0, 0, 255));
        cuadFueraDeServicio.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuadFueraDeServicio.setForeground(new java.awt.Color(0, 0, 255));
        cuadFueraDeServicio.setText("■");

        siguiente.setText("Siguiente");
        siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siguienteActionPerformed(evt);
            }
        });

        modeloTabla = new DefaultTableModel() { // https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEstadoHabitaciones.setModel(modeloTabla);
        tablaEstadoHabitaciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaEstadoHabitaciones.setCellSelectionEnabled(true);
        tablaEstadoHabitaciones.setRowHeight(25);
        tablaEstadoHabitaciones.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tablaEstadoHabitaciones.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tablaEstadoHabitaciones.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tablaEstadoHabitaciones.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panelDatosHabitaciones.setViewportView(tablaEstadoHabitaciones);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(panelRangoFechas, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(panelDatosHabitaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cuadOcupada)
                        .addGap(6, 6, 6)
                        .addComponent(lblOcupada)
                        .addGap(30, 30, 30)
                        .addComponent(cuadReservada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReservada)
                        .addGap(32, 32, 32)
                        .addComponent(cuadDisponible)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDisponible)
                        .addGap(32, 32, 32)
                        .addComponent(cuadFueraDeServicio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFueraDeServicio)
                        .addGap(479, 479, 479)
                        .addComponent(siguiente)
                        .addGap(11, 11, 11)
                        .addComponent(cancelar))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(panelRangoFechas, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(6, 6, 6)
                .addComponent(panelDatosHabitaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(cuadOcupada))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblOcupada))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblReservada)
                            .addComponent(cuadReservada)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cuadDisponible)
                            .addComponent(lblDisponible)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cuadFueraDeServicio)
                            .addComponent(lblFueraDeServicio)))
                    .addComponent(siguiente)
                    .addComponent(cancelar)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarActionPerformed
        
        LocalDate ldFechaDesde = null, ldFechaHasta = null;
        
        if (dcFechaDesde.getDate() != null)
            ldFechaDesde = dcFechaDesde.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (dcFechaHasta.getDate() != null)
            ldFechaHasta = dcFechaHasta.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        if (ldFechaDesde == null || ldFechaHasta == null)
        {
            JOptionPane.showMessageDialog(null, "Complete las fechas restantes.", "", JOptionPane.INFORMATION_MESSAGE);
        
            if (ldFechaDesde == null)
                lblFechaDesde.setForeground(Color.RED);
            if (ldFechaHasta == null)
                lblFechaHasta.setForeground(Color.RED);
        }
        else if (ldFechaDesde.isBefore(LocalDate.now()))
        {
            JOptionPane.showMessageDialog(null, "\"Fecha desde\" debe ser de hoy en adelante.", "Error", JOptionPane.ERROR_MESSAGE);
            lblFechaDesde.setForeground(Color.RED);
        }
        else if (ldFechaDesde.isAfter(ldFechaHasta))
        {
            JOptionPane.showMessageDialog(null, "Rango de fechas inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            lblFechaHasta.setForeground(Color.RED);
        }
        else
        {
            lblFechaDesde.setForeground(Color.BLACK);
            lblFechaHasta.setForeground(Color.BLACK);
            
            this.completarTabla(ldFechaDesde, ldFechaHasta);
        }
    }//GEN-LAST:event_buscarActionPerformed

    private void siguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siguienteActionPerformed
        
        if (paraReservar)
            if (!this.verificarDisponibilidad())
                JOptionPane.showMessageDialog(null, "No todas las habitaciones están disponibles en el rango de fechas seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        else
        {
                    
        }       
        
    }//GEN-LAST:event_siguienteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buscar;
    private javax.swing.JButton cancelar;
    private javax.swing.JLabel cuadDisponible;
    private javax.swing.JLabel cuadFueraDeServicio;
    private javax.swing.JLabel cuadOcupada;
    private javax.swing.JLabel cuadReservada;
    private com.toedter.calendar.JDateChooser dcFechaDesde;
    private com.toedter.calendar.JDateChooser dcFechaHasta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblDisponible;
    private javax.swing.JLabel lblFechaDesde;
    private javax.swing.JLabel lblFechaHasta;
    private javax.swing.JLabel lblFueraDeServicio;
    private javax.swing.JLabel lblOcupada;
    private javax.swing.JLabel lblReservada;
    private javax.swing.JScrollPane panelDatosHabitaciones;
    private javax.swing.JPanel panelRangoFechas;
    private javax.swing.JButton siguiente;
    private javax.swing.JTable tablaEstadoHabitaciones;
    private javax.swing.table.DefaultTableModel modeloTabla;
    // End of variables declaration//GEN-END:variables
}