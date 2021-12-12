/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package interfaces;

import daoImpl.EstadiaDAOImpl;
import dto.FacturaDTO;
import dto.ServicioAFacturar;
import entidades.Estadia;
import entidades.PersonaFisica;
import entidades.ServicioPrestado;
import entidades.TipoFactura;
import gestores.GestorDeAlojamientos;
import gestores.GestorDePersonas;
import java.time.LocalDate;

import dto.ServicioPrestadoDTO;
import entidades.Habitacion;
import entidades.PersonaJuridica;
import entidades.ServicioFacturado;
import entidades.TipoEstado;
import gestores.GestorDeFacturas;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 *
 * @author Fede
 */
public class PanelFacturar extends javax.swing.JPanel {
    private final VentanaPrincipal frame;
    private final GestorDeAlojamientos gestorAlojamientos = GestorDeAlojamientos.getInstance();
    private final GestorDePersonas gestorPersonas = GestorDePersonas.getInstance();
    private final GestorDeFacturas gestorFacturas = GestorDeFacturas.getInstance();
    
    
    private List<ServicioPrestado> serviciosPrestados;
    private List<ServicioAFacturar> serviciosAFacturar;
    public List<ServicioPrestadoDTO> servNoFacturados;
    
    private int tamServicios;
    private BigDecimal costoFEstadia;
    private BigDecimal costoFactura;
    
    
    private Estadia estadia;
    private PersonaFisica responsable;
    private PersonaJuridica rJuridico;
    public Boolean pasarEstadia = false;
    public Boolean pasarServicios = false;
    public List<PersonaFisica> pasajeros;
    public LocalTime hora;
    
    
    JSpinner sp;
    DefaultTableModel dtm;
    boolean flagConsumoSumado = false;
    //DefaultTableModel dm;
    
    

    
    /**
     * Creates new form PanelFacturar
     * @param frame
     */
    public PanelFacturar(VentanaPrincipal frame) {
        initComponents();
        this.frame = frame;
    }
    
    //Viene del menú principal
    public PanelFacturar(VentanaPrincipal frame, PersonaFisica r, Estadia e, List<PersonaFisica> p, LocalTime h) {
        initComponents();
        this.frame = frame;
        estadia = e;
        responsable = r;
        rJuridico=null;
        pasajeros = p;
        hora = h;
        costoFactura = new BigDecimal(0);
        serviciosAFacturar = new ArrayList<>();
        
        agregarSpinnerYcargarModelo();
        actualizarTabla();
        cargarDatos();
    }
    
    //Viene de facturar
    public PanelFacturar(VentanaPrincipal frame, PersonaFisica r, Estadia e, List<PersonaFisica> p, LocalTime h, List<ServicioPrestadoDTO> servP) {
        initComponents();
        this.frame = frame;
        estadia = e;
        responsable = r;
        rJuridico=null;
        pasajeros = p;
        hora = h;
        costoFactura = new BigDecimal(0);
        serviciosAFacturar = new ArrayList<>();
        
        if(!(estadia.getHabitacion().getEstado().name().equals("OCUPADA"))){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
                "La estadía ya fue facturada", 
                "Aviso", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                opciones,
                opciones[0]
            );
            jCheckBox1.setEnabled(false);
        }
        
        agregarSpinnerYcargarModelo();
        cargarServiciosPendientes(servP);
        //actualizarTabla();
        cargarDatos();
    }

    //Viene de Seleccionar responsable
    public PanelFacturar(VentanaPrincipal frame, PersonaJuridica r, Estadia e, LocalTime h, List<PersonaFisica> p) {
        initComponents();
        this.frame = frame;
        estadia = e;
        rJuridico = r;
        pasajeros = p;
        hora = h;
        costoFactura = new BigDecimal(0);
        serviciosAFacturar = new ArrayList<>();
        
        agregarSpinnerYcargarModelo();
        actualizarTabla();
        cargarDatosJuridico();
    }
    
    //Viene de facturar
    public PanelFacturar(VentanaPrincipal frame, PersonaJuridica r, Estadia e, LocalTime h, List<PersonaFisica> p, List<ServicioPrestadoDTO> servP) {
        initComponents();
        this.frame = frame;
        estadia = e;
        rJuridico = r;
        pasajeros = p;
        hora = h;
        costoFactura = new BigDecimal(0);
        serviciosAFacturar = new ArrayList<>();
        jCheckBox1.setEnabled(false);
        
        agregarSpinnerYcargarModelo();
        cargarServiciosPendientes(servP);
        cargarDatosJuridico();
    }
    
    private void cargarServiciosPendientes(List<ServicioPrestadoDTO> servicios) {
        if(servicios != null){
            //Los mando a la lista de servicios pendientes
            //Agrego los datos al arreglo
            Object[] o = new Object[7];
            for(ServicioPrestadoDTO s : servicios){ 
                o[0] = Boolean.FALSE;
                o[1] = s.getNombreConsumo();
                o[2] = s.getPrecioUnitario();            
                o[3] = s.getUnidadesAPagar();            
                o[4] = s.getUnidadesTotales();
                o[5] = s.getCostoTotal();
                o[6] = s.getDescripcion();    
                dtm.addRow(o);
            }
            //Actualizo la tabla
            dtm.fireTableDataChanged();
        }
        
    }
    
    private void actualizarTabla(){
        //Cargo los servicios relacionados con la estadia desde la base de datos
        List<ServicioPrestadoDTO> servicios = new ArrayList<>();
        ServicioPrestado sP;
        serviciosPrestados = estadia.getServiciosPrestados();
        tamServicios = serviciosPrestados.size();
        
        for(int i=0; i<tamServicios; i++){
            sP = serviciosPrestados.get(i);
            
            ServicioPrestadoDTO servicio = new ServicioPrestadoDTO();
            
            servicio.setNombreConsumo(sP.getNombre());//servicio.setNombreConsumo(sP.getTipo().name());
            servicio.setPrecioUnitario(sP.getPrecio());
            servicio.setUnidadesAPagar(0);
            servicio.setUnidadesTotales(sP.getCantidad());
            servicio.setCostoTotal(BigDecimal.valueOf(0));
            servicio.setDescripcion(sP.getNombre());
            
            servicios.add(servicio);
        }
        
        //Agrego los datos al arreglo
        Object[] o = new Object[7];
        for(ServicioPrestadoDTO s : servicios){ 
            o[0] = Boolean.FALSE;
            o[1] = s.getNombreConsumo();
            o[2] = s.getPrecioUnitario();            
            o[3] = s.getUnidadesAPagar();            
            o[4] = s.getUnidadesTotales();
            o[5] = s.getCostoTotal();
            o[6] = s.getDescripcion();    
            dtm.addRow(o);
        }
        //Actualizo la tabla
        dtm.fireTableDataChanged();
    }
    
    private void cargarDatos() {//Se cargan los datos de la estadia y el responsable
        String apyNombre = responsable.getApellido() + " " + responsable.getNombres();
        
        jTextField1.setText(apyNombre);
        jTextField2.setText(responsable.getTipoPosicionFrenteIVA().getTipoFactura().name());
        
        Integer cantNoches = gestorAlojamientos.getCantidadNoches(estadia);
        costoFEstadia = gestorAlojamientos.getCostoFinal(estadia);
        BigDecimal costoNoche = gestorAlojamientos.getCostoNoche(estadia);
        
        jTextField4.setText(costoFEstadia.toString());
        jTextField5.setText(cantNoches.toString() + " Noches x " + costoNoche.toString() + " ARS");
        
    }
    
    private void cargarDatosJuridico() {
        String razonSocial = rJuridico.getRazonSocial();
        
        jTextField1.setText(razonSocial);
        jTextField2.setText(rJuridico.getTipoPosicionFrenteIVA().getTipoFactura().name());
        
        Integer cantNoches = gestorAlojamientos.getCantidadNoches(estadia);
        costoFEstadia = gestorAlojamientos.getCostoFinal(estadia);
        BigDecimal costoNoche = gestorAlojamientos.getCostoNoche(estadia);
        
        jTextField4.setText(costoFEstadia.toString());
        jTextField5.setText(cantNoches.toString() + " Noches x " + costoNoche.toString() + " ARS"); 
    }
    
    private BigDecimal costoTotalConsumo(int row_selected, int cantidad) {
        BigDecimal costoTotal, costoU, cant;
        
        cant = new BigDecimal(cantidad);
        costoU = (BigDecimal) dtm.getValueAt(row_selected, 2);
        costoTotal = costoU.multiply(cant);
        
        dtm.setValueAt(costoTotal, row_selected, 5);
        return costoTotal;
    }
    
    private void controlServiciosFacturados() {
        //checkear si todos los servicios tienen servicioFacturado
        ServicioPrestado servNoFact;
        servNoFacturados = new ArrayList<>();
        
        for(int i=0; i<tamServicios; i++){
            List<ServicioFacturado> servFacturados = serviciosPrestados.get(i).getServiciosFacturados();//Obtengo la lista de servicios facturados
            Integer cantP = serviciosPrestados.get(i).getCantidad();//Obtengo la cantidad de productos del servicio prestado
            
            if(servFacturados.isEmpty()){//No tiene servicios Facturados->Hay que pasar el servicio prestado entero
                
                servNoFact = serviciosPrestados.get(i);
                
                //Crear Servicio Prestado dto
                ServicioPrestadoDTO servPendiente = new ServicioPrestadoDTO();
                
                servPendiente.setNombreConsumo(servNoFact.getNombre());//corregir a: servNoFact.getTipo()
                servPendiente.setPrecioUnitario(servNoFact.getPrecio());
                servPendiente.setUnidadesTotales(servNoFact.getCantidad());
                servPendiente.setDescripcion(servNoFact.getNombre());
                
                //Agregarlo a una lista de servicios a facturar
                
                servNoFacturados.add(servPendiente);
                
                //Pasarlo a la interfaz Seleccionar Responsable
                pasarServicios = true;
                
            }else{//controlar las cantidades
                
                Integer cantF=0;
                
                int tamFacturados = servFacturados.size();
                for(int j=0; j<tamFacturados; j++){
                    //Obtengo la cantidad de productos de cada servicio facturado y se la sumo a la variable
                    cantF = cantF + servFacturados.get(j).getCantidad();
                }
                
                if(Objects.equals(cantP, cantF)){//Si la cantidad de los facturados es igual a la cantidad de productos del servicio prestado, vuelvo al menu principal
                    System.out.println("Todos los servicios fueron facturados");
                    
                }else{//Si los servicios no fueron todos facturados, guardo un dto con los servicios a facturar
                    cantP = cantP-cantF;
                    servNoFact = serviciosPrestados.get(i);
                    servNoFact.setCantidad(cantP);
                    
                    ServicioPrestadoDTO servPendiente = new ServicioPrestadoDTO(servNoFact.getNombre(),//Corregir
                                                                            servNoFact.getPrecio(),
                                                                            servNoFact.getCantidad(),
                                                                            servNoFact.getNombre());
                
                    //Agregarlo a una lista de servicios a facturar
                    servNoFacturados.add(servPendiente);
                    
                    //Pasarlo a la interfaz Seleccionar Responsable
                    pasarServicios = true;
                }
                
            }
        }
    }
    
    private void cargarAMontoTotal() {
        jTextField3.setText(costoFactura.toString());
    }
    
    private void agregarSpinnerYcargarModelo(){
        //Creo el modelo de la tabla
        dtm = (DefaultTableModel) jTableConsumos.getModel();
        
        //Definicion de myspinner
        class MySpinnerEditor extends DefaultCellEditor{
            //JSpinner sP;
            DefaultEditor defaultEditor;
            JTextField text;
            // Initialize the spinner
            public MySpinnerEditor() {
                super(new JTextField());
                sp = new JSpinner();
                defaultEditor = ((DefaultEditor)sp.getEditor());
                text = defaultEditor.getTextField();
            }
            
            // Prepare the spinner component and return it
            @Override
            public Component getTableCellEditorComponent(JTable table, Object 
            value, boolean isSelected, int row, int column) 
            {
                sp.setValue(value);
                return sp;
            }
            
            // Returns the current value of the spinners
            @Override
            public Object getCellEditorValue() {
                return sp.getValue();
            }
        }
        //get the column model from JTable
        TableColumnModel model = jTableConsumos.getColumnModel();
        //get the 2nd column
        TableColumn col = model.getColumn(3);
        //set the editor
        col.setCellEditor(new MySpinnerEditor());  
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButtonAceptar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableConsumos = new javax.swing.JTable();
        jButtonCancelar = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Datos");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Saldos");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Nombre / Razón Social");

        jLabel4.setText("Tipo de Factura");

        jTextField1.setEditable(false);

        jTextField2.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Estadía");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Consumos");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("MONTO TOTAL");

        jTextField3.setEditable(false);

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setText("Costo");

        jLabel9.setText("Descripción");

        jTextField4.setEditable(false);

        jTextField5.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });

        jTableConsumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {

            },
            new String [] {
                "", "Consumos", "Precio unitario", "Unidades a pagar", "Unidades totales", "Costo total", "Descripción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        jTableConsumos.setRowHeight(30);
        jTableConsumos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableConsumosMouseClicked(evt);
            }
        }
    );
    jScrollPane2.setViewportView(jTableConsumos);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 284, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonAceptar, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jCheckBox1))
                        .addComponent(jLabel6))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(jScrollPane2))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jCheckBox1)
                .addComponent(jLabel5))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel6)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel7)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(25, 25, 25)
            .addComponent(jButtonAceptar)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jButtonCancelar.setText("Cancelar");
    jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonCancelarActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jButtonCancelar)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButtonCancelar)
            .addContainerGap())
    );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // TODO add your handling code here:
        //Mostrar Mensaje-> deseas salir?
        Object[] options = { "No", "Si"};
            int opcion = JOptionPane.showOptionDialog(null, "¿Desea cancelar la facturación?", "Cancelar",
                         JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if(opcion == 1) {
                System.out.println("Volver al menú principal");
                frame.cambiarPanel(0);
            } else{
                System.out.println("Seguir facturando");
                frame.cambiarPanel(VentanaPrincipal.PANE_MENU_PRINCIPAL);
            }
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        
        int cantFilas = dtm.getRowCount();
        BigDecimal costoConsumo = null; 
        
        //filasNoSeleccionadas->Ninguna fila seleccionada
        boolean filasNoS = true; 
        
        //Reviso cada fila, si el checkbox está activado y el spinner es !=0, agrego el servicio a la lista
        for(int i=0; i<cantFilas; i++){
            
            if(dtm.getValueAt(i, 0).toString().equals("true") && !dtm.getValueAt(i, 3).equals(0)){
                //Crear el servicio a facturar
                ServicioAFacturar servicio = new ServicioAFacturar();
                costoConsumo = costoTotalConsumo(i, (int) dtm.getValueAt(i, 3));
                
                //Le asigno los datos
                servicio.setIdServicioPrestado(servicioPrestadoDTO);//conseguir el numero de servicio prestado
                servicio.setCantidad((int) dtm.getValueAt(i, 3));
                servicio.setPrecioTotal(costoConsumo);
                serviciosAFacturar.add(servicio);
                
                filasNoS = false;
            }
        }
        
        //Si no hay nada seleccionado salta un error
        if(!jCheckBox1.isSelected() && filasNoS){
            
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"Seleccione un elemento a incluir en la factura.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.INFORMATION_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );
            
        }else{
            //Obtengo los datos de la factura
            BigDecimal factorIVA = new BigDecimal(1.21);
            String fechaEmision = LocalDate.now().toString();
            BigDecimal importeTotal = costoFactura.multiply(factorIVA);//Redondear a 2 decimales
            FacturaDTO f;
            
            //Asignar el responsable que corresponda
            if(rJuridico == null){
                Integer idResponsable = responsable.getIdPersonaFisica();
                TipoFactura tipoF = responsable.getTipoPosicionFrenteIVA().getTipoFactura();
                
                //Crear la facturaDTO
                f = new FacturaDTO(fechaEmision, costoFactura, importeTotal, idResponsable, tipoF);
                
            }else{
                BigInteger idResponsableJ = rJuridico.getCUIT();
                TipoFactura tipoF = rJuridico.getTipoPosicionFrenteIVA().getTipoFactura();
                
                //Crear la facturaDTO
                f= new FacturaDTO(fechaEmision, costoFactura, importeTotal, idResponsableJ, tipoF);
            }
            
            //Asignar idEstadia si corresponde: si el check box está seleccionado y si la estadía no fue facturada
            if(jCheckBox1.isSelected()){
                f.setIdEstadia(estadia.getIdEstadia());
                //System.out.println("Le pasé el id estadia nro: " + estadia.getIdEstadia() + "\n");
            }

            //Asignar lista de Consumos/Servicios
            f.setServiciosAFacturar(serviciosAFacturar);

            //Facturar
            gestorFacturas.Facturar(f);

            //checkea si la estadía fue facturada
            
            
            Integer habitacion = estadia.getHabitacion().getNumero();
            
            estadia = gestorAlojamientos.buscarEstadia(habitacion);
            
            System.out.println(estadia.getHabitacion().getEstado().name() + "\n");
            
            if(estadia.getHabitacion().getEstado().name().equals("OCUPADA")){
                //La habitación está ocupada->no facturé la estadía->la paso a la interfaz
                pasarEstadia = true;
            }

            //checkear si los servicios fueron facturados
            controlServiciosFacturados();

            if((pasarEstadia && pasarServicios) || pasarServicios || pasarEstadia){//Paso los servicios y la estadía
                //Mostrar mensaje, no terminaste de pagar
                Object opciones[] = {"Aceptar"};
                    JOptionPane.showOptionDialog(
                    null, 
                    "Los saldos de la estadía no fueron facturados completamente, seleccione otro responsable.", 
                    "Aviso", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );

                frame.setContentPane(new PanelSeleccionarResponsable(frame, estadia, pasajeros, hora, servNoFacturados));
                frame.setTitle("Facturar");
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.getContentPane().setVisible(false);
                frame.getContentPane().setVisible(true);

            }else {
                //Mostrar mensaje y mandar al menú principal
                Object opciones[] = {"Aceptar"};
                    JOptionPane.showOptionDialog(
                    null, 
                    "Los saldos de la estadía fueron facturados completamente, volviendo al menú principal...", 
                    "Aviso", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );
                frame.cambiarPanel(VentanaPrincipal.PANE_MENU_PRINCIPAL);
            }
        }    
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        
        if(!jCheckBox1.isSelected()){//Si no está seleccionado debo restar la estadía
            costoFactura = costoFactura.subtract(costoFEstadia);
            
        }else{
            costoFactura = costoFactura.add(costoFEstadia);
        }
        cargarAMontoTotal();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTableConsumosMouseClicked(java.awt.event.MouseEvent evt) {                                            
       
        int filasSeleccionadas = jTableConsumos.getSelectedRowCount();
        if(filasSeleccionadas == 1){
            int row_selected = jTableConsumos.getSelectedRow();
            int limite = (int) dtm.getValueAt(row_selected,4);
            int spActual = (int) dtm.getValueAt(row_selected,3);
            
            
            SpinnerModel model = new SpinnerNumberModel(
                spActual,  //initial value
                0,  //minimum value
                limite, //maximum value
                1   //step
            ); 
            sp.setModel(model);
            
            
            BigDecimal costoConsumo = costoTotalConsumo(row_selected, (int) sp.getModel().getValue());
            
            //Check true y flag true: Tengo que sumarlo pero ya sumé, no sumo otra vez -> no hago nada
            //Check true y flag false: Tengo que sumarlo y no lo sumé -> lo sumo y modifico el flag
            //Check false y flag true: No tengo que sumarlo y lo sumé -> lo resto y modifico el flag
            //Check false y flag false: No tengo que sumarlo y no lo sumé -> no hago nada
            
            if(dtm.getValueAt(row_selected, 0).toString().equals("true") && !flagConsumoSumado){
                
                costoFactura = costoFactura.add(costoConsumo);
                flagConsumoSumado = true;
                
            }else{
                if(dtm.getValueAt(row_selected, 0).toString().equals("false") && flagConsumoSumado){
                    costoFactura = costoFactura.subtract(costoConsumo);
                    flagConsumoSumado = false;
                    
                }
            }
            
            cargarAMontoTotal();
            
        }
    }    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableConsumos;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables

    
    /*
    //NO VA
    public PanelFacturar(VentanaPrincipal frame, PersonaJuridica r, Estadia e) {
        initComponents();
        estadia = e;
        rJuridico = r;
        this.frame = frame;
        costoFactura = new BigDecimal(0);
        serviciosAFacturar = new ArrayList<>();
        
        agregarSpinnerYcargarModelo();
        actualizarTabla();
        cargarDatosJuridico();
    }
    */
    
    /*
    private void cargarConsumos() {
        //gestorAlojamientos.getServiciosPrestados(estadiaG);
        serviciosPrestados = estadia.getServiciosPrestados();
        
        System.out.println(serviciosPrestados);
        dtm = (DefaultTableModel) jTableConsumos.getModel();
        
        tamServicios = serviciosPrestados.size();
        ServicioPrestado servicioP;
        
        //for para mostrar los datos de cada servicio
        for(int j = 0; j<tamServicios; j++){
            servicioP = serviciosPrestados.get(j);
            String[] datosFila = {"", servicioP.getPrecio().toString(), "", servicioP.getCantidad().toString(), "", servicioP.getNombre()};
            //String[] datosFila = {servicioP.getTipo().name(), servicioP.getPrecio().toString(), "", servicioP.getCantidad().toString(), "", servicioP.getNombre()};
            dtm.addRow(datosFila);
        }
        
    }
    */
}
