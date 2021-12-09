/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import daoImpl.exceptions.OcuparHabitacionException;
import dto.EstadiaDTO;
import dto.PersonaFisicaDTO;
import entidades.TipoDocumento;
import gestores.GestorDeAlojamientos;
import gestores.GestorDePersonas;
import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Nahuel Riquelme
 */
public class PanelOcuparHabitacion extends javax.swing.JPanel {
    private final VentanaPrincipal frame;
    private final GestorDePersonas gestorPersonas = GestorDePersonas.getInstance();
    private final GestorDeAlojamientos gestorAlojamientos = GestorDeAlojamientos.getInstance();
    private PasajerosTableModel model1;
    private PasajerosTableModel2 model2;
    private int row_selected1;
    private int row_selected2;
    private List<PersonaFisicaDTO> pasajerosDTO;
    private boolean pasajeroCargado;
    private PersonaFisicaDTO personaSeleccionada1;
    private PersonaFisicaDTO personaSeleccionada2;
    //Manejo de la capacidad de cada habitacion
    private Integer capacidadHabitacion;
    private Integer acompañantesCargados;
    //Para cargar en la lista de estadiasDTO
    public EstadiaDTO estadiaDTOactual; //Se guarda fechaInicio, fechaFin, habitacion
    public static List<EstadiaDTO> estadiasDTO = new ArrayList<>(); //Se van guardando las estadias a crear

    /**
     * Creates new form OcuparHabitacion
     * @param frame
     * @param e
     */
    public PanelOcuparHabitacion(VentanaPrincipal frame, EstadiaDTO e) {
        initComponents();
        limitarCampos();
        this.frame = frame;
        this.estadiaDTOactual = e; //Viene desde la interface mostrar estado de habitacion
        cargarModelo();
    }
    
    private void limitarCampos(){
        //Limita la longitud de los campos
        JTextApellido.setDocument(new JTextFieldLimit(32));
        JTextNombre.setDocument(new JTextFieldLimit(32));
        JTextDocumento.setDocument(new JTextFieldLimit(16));
    }
    
    private void cargarModelo(){
        model1 = new PasajerosTableModel();
        model2 = new PasajerosTableModel2();
        jTable1.setModel(model1);
        jTable2.setModel(model2);
        pasajeroCargado = false;
        //Le agrego el radiobutton a las tablas
        agregarRadioButton();        
    }
    
    private void agregarRadioButton(){
        class MyTableCellRenderer extends JRadioButton implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
           this.setSelected(isSelected);
            setBackground(Color.WHITE);
           return this;
        }
    }
        TableColumnModel cm = jTable1.getColumnModel();
        cm.addColumn(new TableColumn(0, 10, new MyTableCellRenderer(), null));
        cm.moveColumn(cm.getColumnCount() - 1, 0);
        
        TableColumnModel cm2 = jTable2.getColumnModel();
        cm2.addColumn(new TableColumn(0, 10, new MyTableCellRenderer(), null));
        cm2.moveColumn(cm2.getColumnCount() - 1, 0);
    }
    
    private void cargarDatosBusqueda(){
        String nombre = JTextNombre.getText();
        String apellido = JTextApellido.getText();
        String tipoDocumento;
        if(jComboBoxTipoDoc.getSelectedItem() == null){
            tipoDocumento = "";
        }else tipoDocumento = jComboBoxTipoDoc.getSelectedItem().toString();
        String nroDocumento = JTextDocumento.getText();
        pasajerosDTO = gestorPersonas.buscarPasajero(nombre,apellido,tipoDocumento,nroDocumento);  
        actualizarTabla1();
    }
    
    private void actualizarTabla1(){
        //Si la persona ya esta cargada como acompañante, no la muestro en los resultados
        pasajerosDTO.removeIf(p -> model2.getDatos().contains(p));
        model1.setDatos(pasajerosDTO);
        model1.fireTableDataChanged();
    }
   
    public static void limpiarEstadias(){
        estadiasDTO.clear();
    }
    
    private void cargarCapacidadHabitacion(){
        capacidadHabitacion = gestorAlojamientos.getCapacidadHabitacion(estadiaDTOactual.getIdHabitacion());
    }
    
    private void calcularAcompañantesCargados(){
        Long aux = model2.getDatos().stream().filter(p -> p.getCategoria().equals("Acompañante")).count();
        acompañantesCargados = aux.intValue();        
    }
    
    private int calcularEdadPasajero(){
        LocalDate nacimiento = LocalDate.parse(model1.personaSelecionada(row_selected1).getFechaNacimiento());
        LocalDate hoy = LocalDate.now();
        
        Period periodo = Period.between(nacimiento, hoy);

        return periodo.getYears();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JTextNombre = new javax.swing.JTextField();
        jComboBoxTipoDoc = new javax.swing.JComboBox<>();
        DefaultComboBoxModel tipoDocModel = new DefaultComboBoxModel();
        tipoDocModel.addElement(null);
        for(TipoDocumento t: TipoDocumento.values()){
            tipoDocModel.addElement(t);
        }
        jComboBoxTipoDoc.setModel(tipoDocModel);
        jComboBoxTipoDoc.setSelectedItem(TipoDocumento.DNI);
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        JTextApellido = new javax.swing.JTextField();
        JTextDocumento = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTable1.getTableHeader().setReorderingAllowed(false);
        jButtonCargarAcompañante = new javax.swing.JButton();
        jButtonCargarPasajero = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel5 = new javax.swing.JPanel();
        jButtonQuitar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTable2.getTableHeader().setReorderingAllowed(false);
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButtonAceptar = new javax.swing.JButton();
        jButtonSeguir = new javax.swing.JButton();
        jButtonCargarOtra = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jButtonCancelar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1184, 798));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Criterios de busqueda");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel1.setText("Nombres(s)");

        jLabel2.setText("Tipo de documento");

        JTextNombre.setNextFocusableComponent(JTextApellido);

        jComboBoxTipoDoc.setNextFocusableComponent(JTextDocumento);
        jComboBoxTipoDoc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxTipoDocItemStateChanged(evt);
            }
        });

        jLabel4.setText("Apellido(s)");

        jLabel5.setText("Numero de documento");

        JTextApellido.setNextFocusableComponent(jComboBoxTipoDoc);

        JTextDocumento.setNextFocusableComponent(jButtonBuscar);
        JTextDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTextDocumentoKeyTyped(evt);
            }
        });

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.setNextFocusableComponent(jButtonCargarPasajero);
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxTipoDoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JTextNombre))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JTextDocumento)
                    .addComponent(JTextApellido))
                .addGap(103, 103, 103))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonBuscar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(JTextApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(JTextDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonBuscar)
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Resultados de busqueda");

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

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
        jTable1.setRowHeight(30);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButtonCargarAcompañante.setText("Cargar acompañante");
        jButtonCargarAcompañante.setEnabled(false);
        jButtonCargarAcompañante.setNextFocusableComponent(jButtonQuitar);
        jButtonCargarAcompañante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCargarAcompañanteActionPerformed(evt);
            }
        });

        jButtonCargarPasajero.setText("Cargar pasajero");
        jButtonCargarPasajero.setEnabled(false);
        jButtonCargarPasajero.setNextFocusableComponent(jButtonCargarAcompañante);
        jButtonCargarPasajero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCargarPasajeroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(filler2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCargarPasajero, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCargarAcompañante, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonCargarAcompañante)
                                .addComponent(jButtonCargarPasajero))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jButtonQuitar.setText("Quitar");
        jButtonQuitar.setEnabled(false);
        jButtonQuitar.setNextFocusableComponent(jButtonAceptar);
        jButtonQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitarActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable2.setRowHeight(30);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(filler3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonQuitar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filler4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonQuitar)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filler4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Personas cargadas");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jButtonAceptar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.setNextFocusableComponent(jButtonSeguir);
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });

        jButtonSeguir.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonSeguir.setText("Seguir cargando");
        jButtonSeguir.setEnabled(false);
        jButtonSeguir.setNextFocusableComponent(jButtonCargarOtra);
        jButtonSeguir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeguirActionPerformed(evt);
            }
        });

        jButtonCargarOtra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonCargarOtra.setText("Cargar otra habitacion");
        jButtonCargarOtra.setEnabled(false);
        jButtonCargarOtra.setName(""); // NOI18N
        jButtonCargarOtra.setNextFocusableComponent(jButtonSalir);
        jButtonCargarOtra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCargarOtraActionPerformed(evt);
            }
        });

        jButtonSalir.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonSalir.setText("Salir");
        jButtonSalir.setEnabled(false);
        jButtonSalir.setNextFocusableComponent(jButtonCancelar);
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirOcuparHabitacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSeguir, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCargarOtra, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAceptar)
                            .addComponent(jButtonSeguir)
                            .addComponent(jButtonCargarOtra)
                            .addComponent(jButtonSalir))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(filler7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(filler5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(filler6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.setNextFocusableComponent(JTextNombre);
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(415, 415, 415)
                                .addComponent(jLabel8))
                            .addComponent(jLabel3)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(16, 16, 16))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonCancelar)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jButtonCancelar)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxTipoDocItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxTipoDocItemStateChanged
        // TODO add your handling code here:
        //Sino se selecciona tipo de documento no se puede poner numero
        if(jComboBoxTipoDoc.getSelectedItem()!=null){
            JTextDocumento.setEnabled(true);
        }else{
            JTextDocumento.setEnabled(false);
            JTextDocumento.setText(null);
        }
    }//GEN-LAST:event_jComboBoxTipoDocItemStateChanged

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
        //Carga los datos en la tabla
        cargarDatosBusqueda();
        cargarCapacidadHabitacion();
        System.out.println("La capacidad de la habitación es de " + capacidadHabitacion + " persona/s");
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jButtonCargarPasajeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCargarPasajeroActionPerformed
        //La persona seleccionada se marca como pasajero
        //personaSeleccionada1.setCategoria("Pasajero");
        
        //Si la persona es mayor de edad sigo con la carga 
        if(calcularEdadPasajero() >= 18 ){
        //Agrego la persona a la tabla2 de personas cargadas
        PersonaFisicaDTO aux = new PersonaFisicaDTO(model1.personaSelecionada(row_selected1),"Pasajero");
        //model2.agregarPersona(model1.personaSelecionada(row_selected1));
        model2.agregarPersona(aux);
        model2.fireTableDataChanged();
        pasajeroCargado = true;
        //NO Elimino la persona de la tabla1 resultados de busqueda
        //pasajerosDTO.remove(personaSeleccionada1);
        //model1.fireTableDataChanged();
        
        //Limpio la seleccion y desactivo botones
        jTable1.clearSelection();
        jButtonCargarAcompañante.setEnabled(false);
        jButtonCargarPasajero.setEnabled(false);
        }else{ //Si la persona es menor de edad muestro un mensaje
            Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                null, 
                "La persona responsable de la habitación debe ser mayor de edad", 
                "Responsable invalido", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                opciones,
                opciones[0]
                );
        }
                

    }//GEN-LAST:event_jButtonCargarPasajeroActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        //Captura las personas seleccionadas en la tabla1
        int filasSeleccionadas = jTable1.getSelectedRowCount();
        if(filasSeleccionadas == 1){
            row_selected1 = jTable1.getSelectedRow();
            //Mostrar apellido y id del pasajero clikeado
            personaSeleccionada1 = model1.personaSelecionada(row_selected1);
            System.out.println(personaSeleccionada1.getApellido() + " " + personaSeleccionada1.getId());
            //System.out.println("Edad: " + calcularEdadPasajero());
            //Si hay alguien seleccionado y todavia no hay pasajero, se activa el boton
            if(!pasajeroCargado)jButtonCargarPasajero.setEnabled(true);
            //Si hay alguien seleccionado se activa el boton
            //Siempre y cuando no se haya alcanzado la capacidad
            //Integer personasCargadas = model2.getDatos().size();
            calcularAcompañantesCargados();
            if(acompañantesCargados < capacidadHabitacion)jButtonCargarAcompañante.setEnabled(true);     
        }
        if(evt.getClickCount()==2){
            System.out.println("Se ha hecho un doble click");
            if(pasajeroCargado){
                //System.out.println("Se cargo acompañante");
                jButtonCargarAcompañante.doClick();
            }else{
                //System.out.println("Se cargo responsable");
                jButtonCargarPasajero.doClick();
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButtonCargarAcompañanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCargarAcompañanteActionPerformed
        //Primero debo seleccionar al responsable de la habitacion antes de seleccionar los acompañantes
        if(pasajeroCargado){       
        
        //Ver si la persona ya figura como acompañante en alguna estadia en proceso
        Boolean condicion = true;
        for(EstadiaDTO e : estadiasDTO){
            for(Integer i : e.getIdsPasajeroAcompañante()){
                if(pasajerosDTO.get(row_selected1).getId().equals(i)){
                    condicion = false;
                    break; //Si ya se encuentra la persona, se sale del bucle
                }
            }
        }
        //Ver si la persona ya figura como acompañante en alguna estadia actual
        /*
           Se deben ver solo las estadias en proceso, es decir, aquellas cuya fecha de finalizacion se posterior
           a la fecha actual y si en ellas existe la persona seleccionada como acompañante, esta no se puede cargar
        */
        if(condicion){
            //Si no existe en la estadia en proceso, se busca en las ya creadas
            condicion = gestorPersonas.NoExisteAcompañante(pasajerosDTO.get(row_selected1).getId());
        }
        
        //Si no figura como acompañante se carga correctamente
        if(condicion){
            //La persona seleccionada se marca como acompañante
            personaSeleccionada1.setCategoria("Acompañante");
            //Agrego la persona a la tabla2 de personas cargadas
            model2.agregarPersona(personaSeleccionada1);
            model2.fireTableDataChanged();
            //Elimino la persona de la lista de resultados de busqueda
            pasajerosDTO.remove(personaSeleccionada1);
            //Recargo la tabla1 sin la persona eliminada
            model1.fireTableDataChanged();
            
            //Limpio la seleccion y desactivo los botones 
            jTable1.clearSelection();
            jButtonCargarAcompañante.setEnabled(false);
            jButtonCargarPasajero.setEnabled(false);
        }else{
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"La persona seleccionada ya figura como acompañante en otra habitación", 
		"Persona invalida", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.INFORMATION_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );
        }
        
        //Si se alcanza la maxima capacidad
        //Integer personasCargadas = model2.getDatos().size();
        calcularAcompañantesCargados();
        System.out.println("Capacidad = " + capacidadHabitacion);
        System.out.println("Cargados = " + acompañantesCargados);
            if(acompañantesCargados == capacidadHabitacion){
                Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                    null, 
                    "La habitacion ya alcanzo su capacidad maxima de personas", 
                    "Capacidad alcanzada", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );
            }
            
        }else{
            Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                    null, 
                    "Primero seleccione al responsable de la habitación", 
                    "Responsable no seleccionado", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );
        }
    }//GEN-LAST:event_jButtonCargarAcompañanteActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        //Captura las personas seleccionadas en la tabla2
        int filasSeleccionadas = jTable2.getSelectedRowCount();
        if(filasSeleccionadas == 1){
            row_selected2 = jTable2.getSelectedRow();
            personaSeleccionada2 = model2.personaSelecionada(row_selected2);
            System.out.println(personaSeleccionada2.getApellido() + " " + personaSeleccionada2.getId());         
            jButtonQuitar.setEnabled(true);
        }
        if(evt.getClickCount()==2){
            System.out.println("Se ha hecho un doble click");
            //System.out.println("Se ha quitado una persona");
            jButtonQuitar.doClick();
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButtonQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitarActionPerformed
        //Primero se deben quitar los acompañantes antes de eliminar al pasajero
        
        //Si el tamaño es mayor a 1, hay acompañantes cargados
        if(model2.getDatos().size() > 1 ){
            
            //Si la persona seleccionada es acompañante
            if(personaSeleccionada2.getCategoria().equals("Acompañante")){
                //Agrego la persona a la tabla1 de resultados de busqueda y ordeno los datos
                pasajerosDTO.add(personaSeleccionada2);
                pasajerosDTO.sort((p1,p2) -> p1.compareTo(p2));
                //Recargo la tabla1 para agregar a la persona seleccionada
                model1.fireTableDataChanged();
                
                //Elimino la persona de la tabla2 de personas cargadas
                model2.quitarPersona(row_selected2);
                //Recargo la tabla 2 para quitar la persona seleccionada
                model2.fireTableDataChanged();
                //Limpio la seleccion y desactivo el boton
                jTable2.clearSelection();
                jButtonQuitar.setEnabled(false);
            }else{
                //Si se selecciona un pasajero muestro el mensaje
                Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                null, 
                "Primero debe quitar los acompañantes", 
                "Persona inválida", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                opciones,
                opciones[0]
                );
            }   
            
        }else{
            //Se supone que solo deberia quedar el pasajero
            pasajeroCargado=false;
            //Elimino la persona de la tabla2 de personas cargadas
            model2.quitarPersona(row_selected2);
            //Recargo la tabla 2 para quitar la persona seleccionada
            model2.fireTableDataChanged();
            //Limpio la seleccion y desactivo el boton
            jTable2.clearSelection();
            jButtonQuitar.setEnabled(false);
        }
    }//GEN-LAST:event_jButtonQuitarActionPerformed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        //Si no se selecciono un pasajero se muestra el mensaje correspondiente y se pone el foco en resultados de busqueda
        Boolean condicion1 = model2.getDatos().stream().filter(p -> p.getCategoria().equals("Pasajero")).collect(Collectors.toList()).isEmpty();
        Boolean condicion2 = model2.getDatos().stream().filter(p -> p.getCategoria().equals("Acompañante")).collect(Collectors.toList()).isEmpty();

        if(condicion1){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"Por favor, seleccione un pasajero responsable para continuar", 
		"Pasajero no seleccionado", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.INFORMATION_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );
            
        }
        else if(condicion2){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"Por favor, seleccione algún acompañante para continuar", 
		"Acompañante/s no seleccionado/s", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.INFORMATION_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );  
            
        }else{
        //Si se selecciono un pasajero y algún acompañante, se actualiza la información
        //Y se activan los demas botones        
        jButtonSeguir.setEnabled(true);
        jButtonCargarOtra.setEnabled(true);
        jButtonSalir.setEnabled(true);
        jButtonAceptar.setEnabled(false);
        
        jButtonBuscar.setEnabled(false);
        JTextApellido.setEnabled(false);
        JTextNombre.setEnabled(false);
        JTextDocumento.setEnabled(false);
        jComboBoxTipoDoc.setEnabled(false);
        jTable1.setEnabled(false);
        jTable1.clearSelection();
        jButtonCargarPasajero.setEnabled(false);
        jButtonCargarAcompañante.setEnabled(false);
        jTable2.setEnabled(false);
        jTable2.clearSelection();
        jButtonQuitar.setEnabled(false);
        
        jButtonSeguir.setNextFocusableComponent(jButtonCargarOtra);
        }
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    private void jButtonSeguirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeguirActionPerformed
        //Se vuelve el enfoque a la pestaña de criterios de busqueda y se limpian los textbox
        jButtonSeguir.setEnabled(false);
        jButtonCargarOtra.setEnabled(false);
        jButtonSalir.setEnabled(false);
        jButtonAceptar.setEnabled(true);
        
        jButtonBuscar.setEnabled(true);
        JTextApellido.setEnabled(true);
        JTextApellido.setText(null);
        JTextNombre.setEnabled(true);
        JTextNombre.setText(null);
        JTextDocumento.setEnabled(true);
        JTextDocumento.setText(null);
        jComboBoxTipoDoc.setEnabled(true);
        jTable1.setEnabled(true);
        jTable2.setEnabled(true);
        
        jButtonSeguir.setNextFocusableComponent(JTextNombre);
    }//GEN-LAST:event_jButtonSeguirActionPerformed

    private void jButtonCargarOtraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCargarOtraActionPerformed
        //Se capturan los datos de la interfaz actual
        
        //Se captura el id del responsable
        Integer idPasajeroResponsable = null;     
        for (PersonaFisicaDTO p : model2.getDatos()) {
            if(p.getCategoria().equals("Pasajero")){
                idPasajeroResponsable = p.getId();
            }            
        }
        
        //Se capturan los ids de los acompañantes
        List<Integer> idPasajerosAcompañantes = new ArrayList<>();  
        model2.getDatos().stream().filter(p -> (p.getCategoria().equals("Acompañante"))).forEachOrdered(p -> {
            idPasajerosAcompañantes.add(p.getId());
        });
        
        //Se cargan en el DTO
        estadiaDTOactual.setIdPasajeroResponsable(idPasajeroResponsable);
        estadiaDTOactual.setIdsPasajeroAcompañante(idPasajerosAcompañantes);
        estadiasDTO.add(estadiaDTOactual);
        
        //Se pasa a la interfaz "Mostrar estado de Habitaciones"
        frame.cambiarPanel(VentanaPrincipal.PANE_OCUPAR_HABITACION);
        
    }//GEN-LAST:event_jButtonCargarOtraActionPerformed

    private void jButtonSalirOcuparHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirOcuparHabitacionActionPerformed
        //Se actualiza la informacion y el estado de las habitaciones seleccionadas
        
        //Se capturan los datos de la interfaz actual
        Integer idPasajeroResponsable = null;     
        for (PersonaFisicaDTO p : model2.getDatos()) {
            if(p.getCategoria().equals("Pasajero")){
                idPasajeroResponsable = p.getId();
            }            
        }
        List<Integer> idPasajerosAcompañantes = new ArrayList<>();  
        model2.getDatos().stream().filter(p -> (p.getCategoria().equals("Acompañante"))).forEachOrdered(p -> {
            idPasajerosAcompañantes.add(p.getId());
        });
        
        estadiaDTOactual.setIdPasajeroResponsable(idPasajeroResponsable);
        estadiaDTOactual.setIdsPasajeroAcompañante(idPasajerosAcompañantes);
        estadiasDTO.add(estadiaDTOactual);
        
        //Se mandan a crear las estadias
        try {
            gestorAlojamientos.OcuparHabitacion(estadiasDTO);
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
            null, 
            "Las estadías fueron creadas correctamente", 
            "Ocupación existosa", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            opciones,
            opciones[0]
            );
        } catch (OcuparHabitacionException e) { //Si falla se captura la excepcion y se muestra un mensaje
            JOptionPane.showMessageDialog(null,e.getMessage(),"Ocupación fallida",JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(PanelOcuparHabitacion.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception ex) {
            Logger.getLogger(PanelOcuparHabitacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Termina el caso de uso
        frame.cambiarPanel(VentanaPrincipal.PANE_MENU_PRINCIPAL);       
        //Limpias la lista de estadias por crear 
        limpiarEstadias();
        interfaces.mostrarEstadoHabitacion.PanelMostrarEstadoHabitacion.limpiarHabitaciones();
    }//GEN-LAST:event_jButtonSalirOcuparHabitacionActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        Object[] options = { "No", "Si"};
        int opcion = JOptionPane.showOptionDialog(null, "¿Esta seguro de cancelar la ocupación?", "Cancelar ocupación",
                     JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(opcion == 1) {
            //Se retorna a la interfaz anterior
            System.out.println("Volver al menú principal");
            frame.cambiarPanel(VentanaPrincipal.PANE_MENU_PRINCIPAL);
            //Se limpia la lista de estadias por crear 
            limpiarEstadias();
            interfaces.mostrarEstadoHabitacion.PanelMostrarEstadoHabitacion.limpiarHabitaciones();
        } else System.out.println("Seguir con la ocupación");
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void JTextDocumentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTextDocumentoKeyTyped
        //Para que se ingrese solo valores numericos
        char c = evt.getKeyChar();
        if(!Character.isDigit(c)){
            evt.consume();
        }
    }//GEN-LAST:event_JTextDocumentoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField JTextApellido;
    private javax.swing.JTextField JTextDocumento;
    private javax.swing.JTextField JTextNombre;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonCargarAcompañante;
    private javax.swing.JButton jButtonCargarOtra;
    private javax.swing.JButton jButtonCargarPasajero;
    private javax.swing.JButton jButtonQuitar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonSeguir;
    private javax.swing.JComboBox<TipoDocumento> jComboBoxTipoDoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
