/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package interfaces;

import dto.ServicioPrestadoDTO;
import entidades.Estadia;
import gestores.GestorDeFacturas;
import gestores.GestorDeAlojamientos;
import entidades.PersonaFisica;
import gestores.GestorDePersonas;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fede
 */
public class PanelSeleccionarResponsable extends javax.swing.JPanel {
    private final VentanaPrincipal frame;
    //private final GestorDeFacturas gestorFacturas = GestorDeFacturas.getInstance();
    private final GestorDeAlojamientos gestorAlojamientos = GestorDeAlojamientos.getInstance();
    //private final GestorDePersonas gestorPersonas = GestorDePersonas.getInstance();
    private List<PersonaFisica> pasajeros;
    private int row_selected;
    private int tamPasajeros;

    private PersonaFisica responsable;
    private Estadia estadia;
    
    private boolean flagCarga;//para limpiar la tabla cada vez que se presiona buscar
    private boolean pasarDatos;//para saber si tenemos que realizar la busqueda de los pasajeros
    
    private List<ServicioPrestadoDTO> servPendientes;

    DefaultTableModel dm;
    Integer nroHabitacion;
    LocalTime hora;
    

    /**
     * Creates new form PanelSeleccionarResponsable
     * @param frame
     */
    
    public PanelSeleccionarResponsable(VentanaPrincipal frame) {
        this.frame = frame;
        initComponents();
        
        pasarDatos = false;
        flagCarga = false;
        row_selected = -1;
        
        //CARGAR LA HORA ACTUAL COMO DEFAULT
        cargarHoraActual();
        
    }
    
    public PanelSeleccionarResponsable(VentanaPrincipal frame, Estadia e, List<PersonaFisica> p, LocalTime h, List<ServicioPrestadoDTO> servNoFacturados) {
        this.frame = frame;
        this.estadia = e;
        this.pasajeros = p;
        this.hora = h;
        this.servPendientes = servNoFacturados;
        initComponents();
        
        row_selected = -1;
        flagCarga = false;
        pasarDatos = true;
        //Poner los datos de la estadia y la hora, no se pueden modificar ni buscar
        cargarDatosEstadia();
        
        jButtonBuscar.setEnabled(false);
    }
    
    private void cargarHoraActual(){
        LocalTime time = LocalTime.now();
        int horas = time.getHour();
        int minutos = time.getMinute();
        
        //Horas y minutos, horas, minutos
        if(minutos<10 && horas<10){
            jTextHora.setText("0" + horas + ":0" + minutos);
        }else if(horas<10){
            jTextHora.setText("0" + horas + ":" + minutos);
        }else if(minutos<10){
            jTextHora.setText(horas + ":0" + minutos);
        }else{
            jTextHora.setText(horas + ":" + minutos);
        }
    }
    private void popularTabla(){
        tamPasajeros = pasajeros.size();
        PersonaFisica ocupante;
        dm = (DefaultTableModel) jTable1.getModel();
        
        //for para mostrar los datos de cada ocupante
        for(int j = 0; j<tamPasajeros; j++){
            ocupante = pasajeros.get(j);
            String[] datosFila = {ocupante.getNombres(), ocupante.getApellido(), ocupante.getTipoDocumento().name(), ocupante.getNroDocumento().toString()};
            dm.addRow(datosFila);
        }
    }
    
    private void limpiarTabla(){
        tamPasajeros = pasajeros.size();
        
        for(int j = tamPasajeros; j>0; j--){
            dm.removeRow(j-1);
        }
    }
        
    private void cargarDatosBusqueda(){
        try{
            nroHabitacion = Integer.valueOf(jTextHabitacion.getText());
        }catch (NumberFormatException ex){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"NÚMERO de habitación erróneo o inexistente.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.ERROR_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );
        }
        
        try{
            hora = LocalTime.parse(jTextHora.getText());
        }catch (Exception ex){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"HORA errónea, formato hh:mm.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.ERROR_MESSAGE,
		null, 
		opciones,
		opciones[0]
            );
        }
        
        estadia = gestorAlojamientos.buscarEstadia(nroHabitacion);
        if(estadia == null){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"No existe una estadía para la habitación y la hora seleccionadas.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.ERROR_MESSAGE,
		null, 
		opciones,
		opciones[0]
            );
        }else{
            pasajeros = gestorAlojamientos.buscarOcupantes(estadia);
            flagCarga = true;
            
            //Poner los datos de los pasajeros
            popularTabla();
        }
        
        
    }
    
    private void datosIncorrectos(){
        //habitacion que sea un numero y hora con el formato necesario
    }
    
    private void cargarDatosEstadia() {
        nroHabitacion = estadia.getHabitacion().getNumero();
        jTextHabitacion.setText(nroHabitacion.toString());
        jTextHabitacion.setEditable(false);
        
        jTextHora.setText(hora.toString());
        jTextHora.setEditable(false);
        
        //Poner los datos de los pasajeros
        popularTabla();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextHabitacion = new javax.swing.JTextField();
        jTextHora = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonSiguiente = new javax.swing.JButton();
        jButtonFacturarTercero = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Datos de la Habitación");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Ocupantes de la Habitación");

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Número de Habitación (*)");

        jLabel4.setText("Hora de Salida (*)");

        jButtonBuscar.setText("Buscar");
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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextHora, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonBuscar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jTextHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jButtonBuscar)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre(s)", "Apellido(s)", "Tipo de Documento", "Número de Documento"
            }
        ));
        jTable1.setRowHeight(25);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButtonSiguiente.setText("Siguiente");
        jButtonSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguienteActionPerformed(evt);
            }
        });

        jButtonFacturarTercero.setText("Facturar a Tercero");
        jButtonFacturarTercero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFacturarTerceroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonFacturarTercero)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente)
                    .addComponent(jButtonFacturarTercero))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancelar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
        
        //Si no nos pasaron los datos, tenemos que buscar normalmente
        if(!pasarDatos){
            //datosIncorrectos();//Tambien hacerlo en el gestor.
            if(flagCarga){
                limpiarTabla();
            }
            
            cargarDatosBusqueda();
            
        }else{//Si la lista no es null, significa que venimos de facturar, por lo que no hay que hacer nada(Boton practicamente no funciona)
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"Debe terminar con la facturación de la estadía anterior.", 
		"Atención", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.INFORMATION_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );
        }
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jButtonSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguienteActionPerformed
        // TODO add your handling code here:
        
        if(row_selected==-1){ //Si no selecciono una persona se muestra error
           
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"Seleccione un pasajero como responsable de pago.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.INFORMATION_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );
        }else{//Si se selecciono una persona se pasa a la interfaz Facturar
            
            //Si la persona es menor de edad se debe mostar error
            if(LocalDate.now().compareTo(responsable.getFechaNacimiento())<18){
                Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                    null, 
                    "Seleccione un pasajero mayor de edad.", 
                    "Error", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );
            }else{//Si es mayor de edad seguimos
                
                if(pasarDatos){//Si nos pasaron los datos es que venimos de facturar, entonces hay que devolver la lista de servicios pendientes
                    
                    estadia.calcularCostoFinal(hora);
                    gestorAlojamientos.updateEstadia(estadia);
                    
                    frame.setContentPane(new PanelFacturar(frame,responsable, estadia, pasajeros, hora, servPendientes));
                    frame.setTitle("Facturar");
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.getContentPane().setVisible(false);
                    frame.getContentPane().setVisible(true);
                }else{
                    frame.setContentPane(new PanelFacturar(frame,responsable, estadia, pasajeros, hora));
                    frame.setTitle("Facturar");
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.getContentPane().setVisible(false);
                    frame.getContentPane().setVisible(true);
                }
            }   
        }
    }//GEN-LAST:event_jButtonSiguienteActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // TODO add your handling code here:
        //No puedo cancelar si ya empecé->Encontrar la manera
        frame.cambiarPanel(VentanaPrincipal.PANE_MENU_PRINCIPAL);
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        //Guardo los datos de la fila seleccionada
        row_selected = jTable1.getSelectedRow();
        System.out.println("Fila seleccionada: " + row_selected);
        
        responsable = pasajeros.get(row_selected);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButtonFacturarTerceroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFacturarTerceroActionPerformed
        //Se debe buscar una nroHabitacion
        if(pasarDatos || flagCarga){//Si nos pasaron los datos de facturar o si presionamos buscar
            
            //Hacer el calculo de la estadía
            estadia.calcularCostoFinal(hora);
            gestorAlojamientos.updateEstadia(estadia);
            
            if(pasarDatos){//Si nos pasaron los datos es que venimos de facturar, entonces hay que devolver la lista de servicios pendientes
                
                frame.setContentPane(new PanelFacturarTercero(frame, estadia, hora, pasajeros, servPendientes));
                frame.setTitle("Facturar");
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.getContentPane().setVisible(false);
                frame.getContentPane().setVisible(true);
            }else{
                
                frame.setContentPane(new PanelFacturarTercero(frame, estadia, hora, pasajeros));
                frame.setTitle("Facturar");
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.getContentPane().setVisible(false);
                frame.getContentPane().setVisible(true);
            }
            
        }else{
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"Debe buscar los datos de una habitación.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.INFORMATION_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );
        }
        
        
    }//GEN-LAST:event_jButtonFacturarTerceroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonFacturarTercero;
    private javax.swing.JButton jButtonSiguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextHabitacion;
    private javax.swing.JTextField jTextHora;
    // End of variables declaration//GEN-END:variables

    /*
    public PanelSeleccionarResponsable(VentanaPrincipal frame, Estadia e, List<PersonaFisica> p, LocalTime h, Boolean pE, List<ServicioPrestadoDTO> servNoFacturados) {
        this.frame = frame;
        initComponents();
        row_selected = -1;
        flagCarga = false;
        estadia = e;
        pasajeros = p;
        hora = h;
        pasarEstadia = pE;
        servPendientes = servNoFacturados;
        //Poner los datos de la estadia y la hora, no se pueden modificar ni buscar
        cargarDatosEstadia();
        //Poner los datos de los pasajeros
        popularTabla();
    }
    */
}