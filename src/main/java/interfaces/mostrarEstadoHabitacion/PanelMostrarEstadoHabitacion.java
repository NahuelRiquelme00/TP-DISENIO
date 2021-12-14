package interfaces.mostrarEstadoHabitacion;

import dao.ReservaDAO;
import daoImpl.ReservaDAOImpl;
import dto.EstadiaDTO;
import entidades.TipoEstado;
import gestores.GestorDeAlojamientos;
import interfaces.PanelOcuparHabitacion;
import interfaces.VentanaPrincipal;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import misc.Dupla;
import misc.Tripleta;

/**
 *
 * @author Federico Pacheco
 */
public class PanelMostrarEstadoHabitacion extends javax.swing.JPanel 
{
    private final VentanaPrincipal frame;
    private boolean paraReservar; // Reservar: true; ocupar: false
    private static List<Tripleta<Integer, LocalDate, LocalDate>> reservasUOcupacionesAdicionales = new LinkedList<>(); // Mismo formato que "resultado"
    
    private GestorDeAlojamientos gesAl;
    private Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones;
    
    FixedColumnTable tablaFechaYEstadoHabitaciones;
    private List<Integer> filasSelec;
    private List<Integer> colsSelec;
    private List<Integer> idHabsTabla;
    private List<LocalDate> fechasTabla;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int ADELANTO_DIAS = 7; // i.e. cuantos dias extras se suman a fecha desde para obtener fecha hasta 
    
    /* 
        Reservas o estadias, segun corresponda.
        En una tripleta dada:
            primero: id de la habitacion
            segundo: fecha desde
            tercero: fecha hasta
    */
    private List<Tripleta<Integer, LocalDate, LocalDate>> resultado;
    
    
    public PanelMostrarEstadoHabitacion(VentanaPrincipal frame, boolean paraReservar) 
    {
        this.frame = frame;
        this.paraReservar = paraReservar;
        gesAl = GestorDeAlojamientos.getInstance();
        
        initComponents();
        this.configurarSeleccion();
        this.configurarTabla();
        dcFechaDesde.setDate(new Date(System.currentTimeMillis()));
        this.pintarTabla(LocalDate.now(), LocalDate.now().plusDays(7));
    }
    
    private void configurarSeleccion()
    {
        // Seleccionar solo un rango de fechas
        // Nota: calcular resultado fue programado para el caso mas general, discontinuo con MULTIPLE_INTERVAL_SELECTION
        tablaEstadoHabitaciones.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);  
           
        if (paraReservar)
            // Seleccionar multiples habitaciones
            tablaEstadoHabitaciones.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        else
            // Seleccionar solo una habitacion
            tablaEstadoHabitaciones.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION); 
    }

    private void configurarTabla() 
    {
        List<Dupla<String, ArrayList<Integer>>> tiposYHabitaciones = gesAl.getTiposYHabitaciones();
        
        // Encabezados tipos de habitacion
        modeloTablaEstadoHabitaciones.addColumn("Fecha / Habitación");
        // Columnas "H(...)"
        idHabsTabla = new LinkedList<>();
        for (Dupla<String, ArrayList<Integer>> d : tiposYHabitaciones) // (Tristemente) deben colocarse las columnas por anticipado 
        {
            for (Integer habNro : d.segundo) 
            {
                idHabsTabla.add(habNro);
                modeloTablaEstadoHabitaciones.addColumn("H" + habNro);
            }
        }
        
        TableColumnModel colMod = tablaEstadoHabitaciones.getColumnModel();
        GroupableTableHeader header = (GroupableTableHeader) tablaEstadoHabitaciones.getTableHeader();
        ColumnGroup colGr;
        int j = 1;  
        for (Dupla<String, ArrayList<Integer>> d : tiposYHabitaciones)
        {
            colGr = new ColumnGroup(d.primero);
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
        
        // Centrar fechas de las filas
        // https://stackoverflow.com/questions/7433602/how-to-center-in-jtable-cell-a-value
        DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
        rend.setHorizontalAlignment(SwingConstants.CENTER);
        tablaEstadoHabitaciones.getColumnModel().getColumn(0).setCellRenderer(rend);
        
        // Pintar encabezado "Fechas / Habitaciones"
        tablaEstadoHabitaciones.getColumnModel().getColumn(0).setHeaderRenderer(new FechaHabitacionesHeaderRenderer());
        
        // Ampliar tamanio "Fechas / Habitaciones"
        tablaEstadoHabitaciones.getColumnModel().getColumn(0).setPreferredWidth(110);
        
        // Fijar fechas a la izquierda
        tablaFechaYEstadoHabitaciones = new FixedColumnTable(1, scrollPaneEstadoHabitaciones);
        tablaFechaYEstadoHabitaciones.getFixedTable().setCellSelectionEnabled(false); // Hacer a las fechas no seleccionables
        tablaFechaYEstadoHabitaciones.getFixedTable().setRowHeight(25); // Misma altura de las filas de tablaEstadoHabitaciones       
        tablaFechaYEstadoHabitaciones.getFixedTable().getTableHeader().setReorderingAllowed(false); // Evitar que la col. de las fechas pueda moverse con el mouse
    
        // Fijar tamanios columnas
        tablaFechaYEstadoHabitaciones.getFixedTable().getTableHeader().setResizingAllowed(false);
        tablaEstadoHabitaciones.getTableHeader().setResizingAllowed(false);
    }
    
    private Object[] getFila(Map<LocalDate, HashMap<Integer, TipoEstado>> estadosHabitaciones, LocalDate fecha)
    {
        Object[] fila = new Object[idHabsTabla.size() + 1];
        
        // https://www.baeldung.com/java-datetimeformatter
        fila[0] = FORMATTER.format(fecha);   
        for (int j = 0; j < idHabsTabla.size(); j++)
            fila[j + 1] = TablaColoreada.getColorGrilla(estadosHabitaciones.get(fecha).get(idHabsTabla.get(j)));
        
        return fila;
    }
    
    private void pintarTabla(LocalDate fechaIni, LocalDate fechaFin)
    {
        fechasTabla = new LinkedList<>();
        
        // Completar con datos de la DB
        estadosHabitaciones = gesAl.getEstadosHabitaciones(fechaIni, fechaFin);
        int cantDias = (int) fechaIni.until(fechaFin, ChronoUnit.DAYS) + 1;
        modeloTablaEstadoHabitaciones.setRowCount(0); // Eliminar datos anteriores
        for (int i = 0; i < cantDias; i++)
        {
            modeloTablaEstadoHabitaciones.addRow(this.getFila(estadosHabitaciones, fechaIni.plusDays(i)));
            fechasTabla.add(fechaIni.plusDays(i));                      
        }
        
        modeloTablaEstadoHabitaciones.fireTableDataChanged();
        
        // Completar con los datos adicionales
        this.repintarTabla(reservasUOcupacionesAdicionales);
    }
    
    private void repintarTabla(List<Tripleta<Integer, LocalDate, LocalDate>> l)
    {
        int iIni, iFin, i;
        for (Tripleta<Integer, LocalDate, LocalDate> t : l)
        {
            iIni = fechasTabla.indexOf(t.segundo);
            iFin = fechasTabla.indexOf(t.tercero);
            for (i = iIni; i <= iFin; i++)
            {
                modeloTablaEstadoHabitaciones.setValueAt(
                    paraReservar? TablaColoreada.COLOR_RESERVADA : TablaColoreada.COLOR_OCUPADA,
                    i,                                                                                      
                    idHabsTabla.indexOf(t.primero) + 1               
                );
                
                estadosHabitaciones.get(t.segundo.plusDays(i - iIni))
                    .put(t.primero, paraReservar? TipoEstado.RESERVADA : TipoEstado.OCUPADA);
            }
        }
        
        modeloTablaEstadoHabitaciones.fireTableDataChanged();
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
    // Metodo general para discontinuidad en fechas y multiples habitaciones
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

    // ---------------------------------------------------------------------------------------------------------------------------------------------
    // Auxiliares:
    
    // No hay otra forma sencilla. Arrays.asList() no funciona bien.
    private List<Integer> arrToList (int[] arr)
    {
        List<Integer> newArr = new ArrayList<>();
        for (int i = 0; i < arr.length; i++)
            newArr.add(arr[i]);
        return newArr;
    }
    
    private LocalDate dateToLocalDate(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    private void pasarAOcuparHabitacion()
    {
        EstadiaDTO estadiaActualDTO = new EstadiaDTO();
        Tripleta<Integer, LocalDate, LocalDate> estadiaActualTrip = resultado.get(0);
        
        estadiaActualDTO.setIdHabitacion(estadiaActualTrip.primero);
        estadiaActualDTO.setFechaInicio(estadiaActualTrip.segundo.toString());
        estadiaActualDTO.setFechaFin(estadiaActualTrip.tercero.toString());
        reservasUOcupacionesAdicionales.add(estadiaActualTrip);
        
        frame.setContentPane(new PanelOcuparHabitacion(frame, estadiaActualDTO));
        frame.setTitle("Ocupar habitacion");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setVisible(false);
        frame.getContentPane().setVisible(true);
    }
    
    public static void limpiarHabitaciones(){
        reservasUOcupacionesAdicionales.clear();
    }
    
    /*
        Clase auxiliar al encabezado "Fecha / Habitaciones". 
        Idea recuperada de: https://www.codejava.net/java-se/swing/jtable-column-header-custom-renderer-examples
        No funcionan bien:
            tablaEstadoHabitaciones.getTableHeader().setBackground(new Color(187,187,187));     (pinta solo los otros encabezados)
            tablaFechaYEstadoHabitaciones.getFixedTable().getTableHeader().setBackground(new Color(187,187,187));   (pinta solo el borde)
    */
    public class FechaHabitacionesHeaderRenderer extends JLabel implements TableCellRenderer 
    {
        public FechaHabitacionesHeaderRenderer() 
        {
            super();
            this.setBackground(new Color(187,187,187)); // Gris "suave"
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
        {
            this.setText(value.toString());
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return this;
        }
    }
    
    // ---------------------------------------------------------------------------------------------------------------------------------------------
    // Eventos y codigo autogenerado:
    
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
        scrollPaneEstadoHabitaciones = new javax.swing.JScrollPane();
        tablaEstadoHabitaciones = new TablaColoreada()
        ;

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        panelRangoFechas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
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

        cuadOcupada.setBackground(TablaColoreada.COLOR_OCUPADA);
        cuadOcupada.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuadOcupada.setForeground(new java.awt.Color(255, 0, 0));
        cuadOcupada.setText("■");

        cuadReservada.setBackground(TablaColoreada.COLOR_RESERVADA);
        cuadReservada.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuadReservada.setForeground(new java.awt.Color(255, 255, 0));
        cuadReservada.setText("■");

        cuadDisponible.setBackground(TablaColoreada.COLOR_DISPONIBLE);
        cuadDisponible.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuadDisponible.setForeground(new java.awt.Color(99, 219, 24));
        cuadDisponible.setText("■");

        cuadFueraDeServicio.setBackground(TablaColoreada.COLOR_FUERA_DE_SERVICIO);
        cuadFueraDeServicio.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cuadFueraDeServicio.setForeground(new java.awt.Color(0, 0, 255));
        cuadFueraDeServicio.setText("■");

        siguiente.setText("Siguiente");
        siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siguienteActionPerformed(evt);
            }
        });

        scrollPaneEstadoHabitaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        modeloTablaEstadoHabitaciones = new DefaultTableModel() { // https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEstadoHabitaciones.setModel(modeloTablaEstadoHabitaciones);
        tablaEstadoHabitaciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaEstadoHabitaciones.setCellSelectionEnabled(true);
        tablaEstadoHabitaciones.setRowHeight(25);
        tablaEstadoHabitaciones.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tablaEstadoHabitaciones.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tablaEstadoHabitaciones.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaEstadoHabitaciones.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tablaEstadoHabitaciones.setShowGrid(true);
        scrollPaneEstadoHabitaciones.setViewportView(tablaEstadoHabitaciones);
        tablaEstadoHabitaciones.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelRangoFechas, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addComponent(scrollPaneEstadoHabitaciones)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(siguiente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelar)))
                .addContainerGap())
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
                .addComponent(scrollPaneEstadoHabitaciones, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(siguiente)
                        .addComponent(cancelar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cuadOcupada)
                                .addComponent(lblOcupada))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblReservada)
                                .addComponent(cuadReservada))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cuadDisponible)
                                .addComponent(lblDisponible))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cuadFueraDeServicio)
                                .addComponent(lblFueraDeServicio))))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarActionPerformed
        
        LocalDate ldFechaDesde = null, ldFechaHasta = null;
        
        if (dcFechaDesde.getDate() != null)
            ldFechaDesde = this.dateToLocalDate(dcFechaDesde.getDate());
        if (dcFechaHasta.getDate() != null)
            ldFechaHasta = this.dateToLocalDate(dcFechaHasta.getDate());
        
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
            
            this.pintarTabla(ldFechaDesde, ldFechaHasta);
        }
    }//GEN-LAST:event_buscarActionPerformed

    private void siguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siguienteActionPerformed

        if (paraReservar)
            this.siguienteReservar();
        else
            this.siguienteOcupar();
    }//GEN-LAST:event_siguienteActionPerformed

    private void siguienteReservar()
    {
        Dupla<Boolean, Boolean> disponibilidad = this.verificarDisponibilidad();
        
        if (!disponibilidad.primero) // i.e. rango de fechas y habitaciones invalido
        {
            JOptionPane.showMessageDialog(
                null, 
                "Selección inválida. No todas las habitaciones están disponibles para todo el rango de fechas escogido.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        else
        {
            this.calcularResultado();
            this.repintarTabla(resultado);

            // No parece poder hacerse el "presione cualquier tecla para continuar"
            // https://stackoverflow.com/questions/19870467/how-do-i-get-press-any-key-to-continue-to-work-in-my-java-code/25095049
            JOptionPane.showInternalMessageDialog(
                null, 
                "Habitación seleccionada exitosamente.",
                "",
                JOptionPane.INFORMATION_MESSAGE
            );       
        }
    }
    
    private void siguienteOcupar()
    {
        Dupla<Boolean, Boolean> disponibilidad = this.verificarDisponibilidad();
        
        if (!disponibilidad.primero) // i.e. rango de fechas y habitaciones invalido
        {    
            JOptionPane.showMessageDialog(
                null, 
                "Selección inválida. La habitación no está disponible o reservada para todo el rango de fechas escogido.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        else
        {
            this.calcularResultado();
            
            if (!resultado.get(0).segundo.equals(LocalDate.now()))  // i.e. la estadia no empieza en el dia de hoy
            {
                JOptionPane.showMessageDialog(
                    null, 
                    "La ocupación de la habitación no comienza el día de hoy.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
                resultado = null;
            }
            else
            {
                if (disponibilidad.segundo) // i.e. se tapa una reserva
                {
                    // Obtener las reservas que entran en conflicto con la ocupacion
                    ReservaDAO reservaDAO = new ReservaDAOImpl();
                    List<Tripleta<String, LocalDate, LocalDate>> resHab = 
                        reservaDAO.getReservasHabitacion(
                            resultado.get(0).primero, 
                            LocalDate.now(), 
                            this.dateToLocalDate(dcFechaHasta.getDate())
                        );
                    reservaDAO.close();
                    
                    // Armar str de reservas para el cartel
                    String resHabStr = "";
                    for (Tripleta<String, LocalDate, LocalDate> r : resHab)
                    {
                        resHabStr += 
                            r.primero + 
                            "; del " + FORMATTER.format(r.segundo) + 
                            " al "   + FORMATTER.format(r.tercero) + "\n";
                    }
                    
                    
                    Object[] opciones = {"Ocupar igual", "Volver"};
                    int indOpcionElegida = JOptionPane.showOptionDialog(
                        null, 
                        "Se engloban una o más reservas para la habitación y rango de fecha escogidos:\n" + 
                        resHabStr + 
                        "¿Desea ocupar igualmente?",
                        "Cuidado",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        opciones,
                        opciones[1]
                    );

                    if (indOpcionElegida == 0) // i.e. "Ocupar igual"; cerrar el panel actual, volver
                    {
                        this.repintarTabla(resultado);

                        JOptionPane.showInternalMessageDialog(
                            null, 
                            "Habitación seleccionada exitosamente.", 
                            "", 
                            JOptionPane.INFORMATION_MESSAGE
                        );

                        this.pasarAOcuparHabitacion();
                    }
                }
                else  // i.e. cerrar el panel actual, volver
                {    
                    this.repintarTabla(resultado);

                    JOptionPane.showInternalMessageDialog(
                        null, 
                        "Habitación seleccionada exitosamente.", 
                        "", 
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    this.pasarAOcuparHabitacion();
                }
            }
        }
    }
    
    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        //Al cancelar se debe volver al menu principal
        frame.cambiarPanel(VentanaPrincipal.PANE_MENU_PRINCIPAL);
        //Se limpian las habitaciones con estados pre cargados
        PanelMostrarEstadoHabitacion.limpiarHabitaciones();
        PanelOcuparHabitacion.limpiarEstadias();       
    }//GEN-LAST:event_cancelarActionPerformed

    private void dcFechaDesdePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcFechaDesdePropertyChange
        if (dcFechaHasta.getDate() == null)// || dcFechaHasta.getDate().before(dcFechaDesde.getDate()))
            dcFechaHasta.setDate(new Date(dcFechaDesde.getDate().getTime() + ADELANTO_DIAS * 24 * 60 * 60 * 1000));
    }//GEN-LAST:event_dcFechaDesdePropertyChange

    private void dcFechaHastaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcFechaHastaPropertyChange
        /*
        if (dcFechaHasta.getDate() != null)
            if (dcFechaHasta.getDate().before(dcFechaDesde.getDate()))
                dcFechaHasta.setDate(new Date(dcFechaDesde.getDate().getTime() + ADELANTO_DIAS * 24 * 60 * 60 * 1000));
        */
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
    private javax.swing.JPanel panelRangoFechas;
    private javax.swing.JScrollPane scrollPaneEstadoHabitaciones;
    private javax.swing.JButton siguiente;
    private javax.swing.JTable tablaEstadoHabitaciones;
    private javax.swing.table.DefaultTableModel modeloTablaEstadoHabitaciones;
    // End of variables declaration//GEN-END:variables
}