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
import java.awt.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
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
    
    
    
    
    private List<ServicioPrestado> servPrestados;
    private int tamServicios;
    private BigDecimal costoFEstadia;
    private BigDecimal costoFactura;
    private Integer contChBox;
    private List<ServicioAFacturar> listaServiciosAFacturar;
    private Estadia estadia;
    private PersonaFisica responsable;
    
    JSpinner sp;
    DefaultTableModel dtm;
    //DefaultTableModel dm;
    

    
    /**
     * Creates new form PanelFacturar
     * @param frame
     */
    public PanelFacturar(VentanaPrincipal frame) {
        initComponents();
        this.frame = frame;
    }

    public PanelFacturar(VentanaPrincipal frame, PersonaFisica r, Estadia e) {
        initComponents();
        estadia = e;
        responsable = r;
        this.frame = frame;
        costoFactura = new BigDecimal(0);
        contChBox = 0;
        agregarSpinnerYcargarModelo();
        actualizarTabla();
        //cargarDatos();
        
    }
    
        private void agregarSpinnerYcargarModelo(){
        //Creo el modelo de la tabla
        dtm = (DefaultTableModel) jTableConsumos.getModel();
        
        //Definicion de myspinner
        class MySpinnerEditor extends DefaultCellEditor{
            //JSpinner sp;
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
    
    private void actualizarTabla(){
        //Cargo los servicios relacionados con la estadia desde la base de datos
        ServicioPrestadoDTO servicio = new ServicioPrestadoDTO();
        servicio.setNombreConsumo("MINIBAR");
        servicio.setPrecioUnitario(BigDecimal.valueOf(100.00));
        servicio.setUnidadesAPagar(0);
        servicio.setUnidadesTotales(4);
        servicio.setCostoTotal(BigDecimal.valueOf(0));
        servicio.setDescripcion("CERVEZA MILLER");
        ServicioPrestadoDTO servicio2 = new ServicioPrestadoDTO();
        servicio2.setNombreConsumo("MINIBAR");
        servicio2.setPrecioUnitario(BigDecimal.valueOf(100.00));
        servicio2.setUnidadesAPagar(0);
        servicio2.setUnidadesTotales(6);
        servicio2.setCostoTotal(BigDecimal.valueOf(0));
        servicio2.setDescripcion("CERVEZA MILLER");
        List<ServicioPrestadoDTO> servicios = new ArrayList<>();
        servicios.add(servicio);
        servicios.add(servicio2);
        servicios.add(servicio);
        servicios.add(servicio);
        //Agrego los datos al arreglo
        Object[] o = new Object[7];
        for(ServicioPrestadoDTO s : servicios){ 
            o[0] = false;
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
    
    
    private void cargarDatos() {
        String apyNombre = responsable.getApellido() + " " + responsable.getNombres();
        
        jTextField1.setText(apyNombre);
        jTextField2.setText(responsable.getTipoPosicionFrenteIVA().getTipoFactura().name());
        
        Integer cantNoches = gestorAlojamientos.getCantidadNoches(estadia);
        costoFEstadia = gestorAlojamientos.getCostoFinal(estadia);
        BigDecimal costoNoche = gestorAlojamientos.getCostoNoche(estadia);
        
        jTextField4.setText(costoFEstadia.toString());
        jTextField5.setText(cantNoches.toString() + " Noches x " + costoNoche.toString() + " ARS");
        
        cargarConsumos();
    }
    
    private void cargarConsumos() {
        //gestorAlojamientos.getServiciosPrestados(estadiaG);
        servPrestados = estadia.getServiciosPrestados();
        
        System.out.println(servPrestados);
        dtm = (DefaultTableModel) jTableConsumos.getModel();
        
        tamServicios = servPrestados.size();
        ServicioPrestado servicioP;
        
        //for para mostrar los datos de cada servicio
        for(int j = 0; j<tamServicios; j++){
            servicioP = servPrestados.get(j);
            String[] datosFila = {"", servicioP.getPrecio().toString(), "", servicioP.getCantidad().toString(), "", servicioP.getNombre()};
            //String[] datosFila = {servicioP.getTipo().name(), servicioP.getPrecio().toString(), "", servicioP.getCantidad().toString(), "", servicioP.getNombre()};
            dtm.addRow(datosFila);
        }
        
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
            new Object [][] {

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
        frame.cambiarPanel(VentanaPrincipal.PANE_MENU_PRINCIPAL);
        //Mostrar Mensaje-> deseas salir?
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        // TODO add your handling code here:
        BigDecimal factorIVA = new BigDecimal(0.21);
        Integer idEstadia = null;
        String fechaEmision = LocalDate.now().toString();
        Integer idResponsable = responsable.getIdPersonaFisica();
        TipoFactura tipoF = responsable.getTipoPosicionFrenteIVA().getTipoFactura();
        BigDecimal importeTotal = costoFactura.multiply(factorIVA);
        
        //Crear la facturaDTO
        FacturaDTO f= new FacturaDTO(fechaEmision, costoFactura, importeTotal, idResponsable, tipoF);
        
        //Asignar idEstadia si corresponde
        if(!(contChBox == 0 || contChBox%2 == 0)){
            idEstadia = estadia.getIdEstadia();
            f.setIdEstadia(idEstadia);
        }
        
        //Asignar lista de Consumos/Servicios
        f.setServiciosAFacturar(listaServiciosAFacturar);
        
        
        
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        contChBox++;
        if(contChBox == 0 || contChBox%2 == 0){
            costoFactura = costoFactura.subtract(costoFEstadia);//Si lo vuelvo a presionar se le resta el valor de la estadia
            
        }else {
            costoFactura = costoFactura.add(costoFEstadia);
        }

        //System.out.println(costoFactura + " " + costoFinal);
        jTextField3.setText(costoFactura.toString());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTableConsumosMouseClicked(java.awt.event.MouseEvent evt) {                                            
        //Lo uso para limitar los valore del spinner
        int filasSeleccionadas = jTableConsumos.getSelectedRowCount();
        if(filasSeleccionadas == 1){
            int row_selected = jTableConsumos.getSelectedRow();
            int limite = (int) dtm.getValueAt(row_selected,4);
            SpinnerModel model = new SpinnerNumberModel(
                0,  //initial value
                0,  //minimum value
                limite, //maximum value
                1   //step
            ); 
            sp.setModel(model);
            System.out.println("El limite es:" + limite);
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
}
