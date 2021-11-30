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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import misc.GroupableTableHeader;
import misc.ColumnGroup;
import misc.Dupla;
import misc.Tripleta;

/**
 *
 * @author Federico Pacheco
 */
public class PanelMostrarEstadoHabitacion extends javax.swing.JPanel {

    private boolean paraReservar; // Reservar: true; ocupar: false
    
    private GestorDeAlojamientos gesAl;
    private Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones;
    
    private List<Integer> filasSelec = null;
    private List<Integer> colsSelec = null;
    private List<Integer> idHabsTabla = null;
    private List<LocalDate> fechasTabla = null;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int ADELANTO_DIAS = 7; // i.e. cuantos dias extras se suman a fecha desde para obtener fecha hasta 
    
    /* 
        Reservas o estadias, segun corresponda.
        En una tripleta dada:
            primero: id de la habitacion
            segundo: fecha desde
            tercero: fecha hasta
    */
    private List<Tripleta<Integer, LocalDate, LocalDate>> resultado = null;
    
    
    public PanelMostrarEstadoHabitacion(boolean paraReservar) 
    {
        initComponents();
        gesAl = GestorDeAlojamientos.getInstance();
        this.paraReservar = paraReservar;
        
        armarTabla();
        dcFechaDesde.setDate(new Date(System.currentTimeMillis()));
        this.completarTabla(LocalDate.now(), LocalDate.now().plusDays(7));
    }

    private void armarTabla() 
    {
        List<Dupla<String, LinkedList<Integer>>> tiposYHabitaciones = gesAl.getTiposYHabitaciones();
        
        // Columnas "H(...)"
        idHabsTabla = new LinkedList<>();
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
        
        // Centrar fechas
        // https://stackoverflow.com/questions/7433602/how-to-center-in-jtable-cell-a-value
        DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
        rend.setHorizontalAlignment(SwingConstants.CENTER);
        tablaEstadoHabitaciones.getColumnModel().getColumn(0).setCellRenderer(rend);
    }
    
    private Object[] getFila(Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones, LocalDate fecha)
    {
        Object[] fila = new Object[idHabsTabla.size() + 1];
        
        // https://www.baeldung.com/java-datetimeformatter
        fila[0] = FORMATTER.format(fecha);
        for (int j = 0; j < idHabsTabla.size(); j++)
            fila[j + 1] = this.getColorGrilla(estadosHabitaciones.get(fecha).get(idHabsTabla.get(j)));
        
        return fila;
    }
    
    private void completarTabla(LocalDate fechaIni, LocalDate fechaFin)
    {
        fechasTabla = new LinkedList<>();
        
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
    
    /*
        En la dupla:
            primero: habitaciones y rango de fechas validos
            segundo: se taparan reservas en caso de ocupar
    */
    public Dupla<Boolean, Boolean> verificarDisponibilidad()
    {
        int i, j;
        TipoEstado est;
        Dupla<Boolean, Boolean> res = new Dupla<>();
        
        filasSelec = this.arrToList(tablaEstadoHabitaciones.getSelectedRows()); 
        colsSelec = this.arrToList(tablaEstadoHabitaciones.getSelectedColumns());
        
        // Salvar el problema de la fecha en la primera columna
        Integer colFechas = 0;
        colsSelec.remove(colFechas);
        for (j = 0; j < colsSelec.size(); j++) 
            colsSelec.set(j, colsSelec.get(j) - 1);
       
        
        if (filasSelec.size() > 0 && colsSelec.size() > 0)
        {
            i = 0;
            res.primero = true;
            res.segundo = false;
            while (i < filasSelec.size() && res.primero)
            {
                j = 0;
                while (j < colsSelec.size() && res.primero)
                {
                    est = estadosHabitaciones.get(fechasTabla.get(filasSelec.get(i))).get(idHabsTabla.get(colsSelec.get(j)));
                    
                    if (!est.equals(TipoEstado.DISPONIBLE))
                    {   
                        if (paraReservar)
                            res.primero = false;
                        else // i.e. para ocupar. Tener en cuenta reservas
                        {
                            if (!est.equals(TipoEstado.RESERVADA))
                                res.primero = false;
                            else
                                res.segundo = true;
                        }
                    }         
                        
                    j++;
                }    
                i++;
            }
        }
        else
            res.primero = false;
        
        return res;
    }

    // Llamar previamente a verificarDisponibilidad()
    private void calcularResultado()
    {
        resultado = new LinkedList<>();
        int i;
        Tripleta<Integer, LocalDate, LocalDate> auxT;
        LocalDate fechaIni;
        
        
        // Para cada habitacion seleccionada...
        for (Integer idHab : colsSelec)
        {
            if (filasSelec.size() > 1)
            {
                i = 1;
                fechaIni = fechasTabla.get(filasSelec.get(0));
                while(i < filasSelec.size())
                {
                    // Condicion de discontinuidad entre fechas seleccionadas
                    if (!filasSelec.get(i).equals(filasSelec.get(i - 1) + 1))
                    {
                        auxT = new Tripleta<>(
                            idHabsTabla.get(idHab), 
                            fechaIni, 
                            fechasTabla.get(filasSelec.get(i - 1))
                        );
                        resultado.add(auxT);
                        
                        fechaIni = fechasTabla.get(filasSelec.get(i));
                    }
                   
                    i++;
                }
                
                // Caso continuo o ultima discontinuidad
                auxT = new Tripleta<>(
                    idHabsTabla.get(idHab), 
                    fechaIni, 
                    fechasTabla.get(filasSelec.get(filasSelec.size() - 1))
                );
                resultado.add(auxT);
            }
            else // Caso base, una sola fecha
            {
                auxT = new Tripleta<>(
                    idHabsTabla.get(idHab), 
                    fechasTabla.get(filasSelec.get(0)), 
                    fechasTabla.get(filasSelec.get(0))
                );
                resultado.add(auxT);
            }
        }
        
        System.out.println("Resultado: " + resultado);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Auxiliares:
    
    // No hay otra forma sencilla. Arrays.asList() no funciona bien.
    private List<Integer> arrToList (int[] arr)
    {
        List<Integer> newArr = new ArrayList<>();
        for (int i = 0; i < arr.length; i++)
            newArr.add(arr[i]);
        return newArr;
    }

    public List<Tripleta<Integer, LocalDate, LocalDate>> getResultado() { return resultado; }

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
        tablaEstadoHabitaciones = new TablaColoreada()
        ;

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
        dcFechaDesde.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcFechaDesdePropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 85;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 12, 16, 0);
        panelRangoFechas.add(dcFechaDesde, gridBagConstraints);

        dcFechaHasta.setDateFormatString("dd/MM/yyyy"); // https://www.youtube.com/watch?v=kmZXUlp5F5Q
        dcFechaHasta.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcFechaHastaPropertyChange(evt);
            }
        });
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
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

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
        tablaEstadoHabitaciones.setShowGrid(true);
        panelDatosHabitaciones.setViewportView(tablaEstadoHabitaciones);
        tablaEstadoHabitaciones.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

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
                        .addComponent(cancelar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cuadOcupada)
                            .addComponent(lblOcupada)))
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
        
        Dupla<Boolean, Boolean> disponibilidad = this.verificarDisponibilidad();
        
        if (!disponibilidad.primero) // i.e. rango de fechas y habitaciones invalido
        {
            if (paraReservar)
                JOptionPane.showMessageDialog(
                    null, 
                    "Selección inválida. No se seleccionaron habitaciones o no todas ellas están disponibles para el rango de fechas escogido.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            else // i.e. ocupar
                JOptionPane.showMessageDialog(
                    null, 
                    "Selección inválida. No se seleccionaron habitaciones o no todas ellas están disponibles o reservadas para el rango de fechas escogido.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
        }
        else
        {
            if (paraReservar)
                this.calcularResultado();
            else
            {
                if (disponibilidad.segundo) // i.e. se tapa una reserva
                {
                    Object[] opciones = {"Ocupar igual", "Volver"};
                    int indOpcionElegida = JOptionPane.showOptionDialog(
                        null, 
                        "Se engloban una o más reservas para las habitaciones y rango de fecha escogidos. ¿Desea ocupar igualmente?",
                        "Cuidado",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        opciones,
                        opciones[1]
                    );
                
                    if (indOpcionElegida == 0) // i.e. cerrar el panel actual, volver
                    {
                        this.calcularResultado();
                        // TODO pendiente ver como conectar
                    }
                }
                else  // i.e. cerrar el panel actual, volver
                {    
                    this.calcularResultado();
                    // TODO pendiente ver como conectar
                }
            }
        }
    }//GEN-LAST:event_siguienteActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        // TODO pendiente ver como conectar
    }//GEN-LAST:event_cancelarActionPerformed

    private void dcFechaDesdePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcFechaDesdePropertyChange
        if (dcFechaHasta.getDate() == null || dcFechaHasta.getDate().before(dcFechaDesde.getDate()))
            dcFechaHasta.setDate(new Date(dcFechaDesde.getDate().getTime() + ADELANTO_DIAS * 24 * 60 * 60 * 1000));        
    }//GEN-LAST:event_dcFechaDesdePropertyChange

    private void dcFechaHastaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcFechaHastaPropertyChange
        if (dcFechaHasta.getDate() != null)
            if (dcFechaHasta.getDate().before(dcFechaDesde.getDate()))
                dcFechaHasta.setDate(new Date(dcFechaDesde.getDate().getTime() + ADELANTO_DIAS * 24 * 60 * 60 * 1000));
    }//GEN-LAST:event_dcFechaHastaPropertyChange


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