/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package interfaces;

import dto.ServicioPrestadoDTO;
import entidades.Estadia;
import gestores.GestorDeAlojamientos;
import entidades.PersonaFisica;
import entidades.ServicioFacturado;
import entidades.ServicioPrestado;
import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

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
        limitarCampos();
        agregarRadioButton();
        
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
        jTextHabitacion.setEditable(false);
        jTextHora.setEditable(false);
        
        agregarRadioButton();
    }
    
    private void agregarRadioButton(){
        //Agrego el radiobutton a la tabla
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
            String[] datosFila = {ocupante.getNombres(), ocupante.getApellido(), ocupante.getTipoDocumento().name(), ocupante.getNumeroDocumento().toString()};
            dm.addRow(datosFila);
        }
        //Para evitar que se apreten los botones sin datos cargados
        jButtonFacturarTercero.setEnabled(true);
        jButtonSiguiente.setEnabled(true);
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
            hora = LocalTime.parse(jTextHora.getText());
        }catch (Exception ex){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"Complete los campos NÚMERO de habitación y hora (hh:mm) con el formato correcto.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.ERROR_MESSAGE, 
		null, 
		opciones,
		opciones[0]
            );
            
            jLabel3.setForeground(Color.red);
            jLabel4.setForeground(Color.red);
            
            return;
        }
        try{
            estadia = gestorAlojamientos.buscarEstadia(nroHabitacion);
        }catch(Exception ex){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"No existe la habitación seleccionada.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.ERROR_MESSAGE,
		null, 
		opciones,
		opciones[0]
            );
            return;
        }
            
        if(estadia ==null){
            Object opciones[] = {"Aceptar"};
            JOptionPane.showOptionDialog(
                null, 
		"No existe una estadía actual para la habitación seleccionada.", 
		"Error", 
		JOptionPane.DEFAULT_OPTION, 
		JOptionPane.ERROR_MESSAGE,
		null, 
		opciones,
		opciones[0]
            );
            return;
        }
        pasajeros = gestorAlojamientos.buscarOcupantes(estadia);
        flagCarga = true;
            
        //Poner los datos de los pasajeros
        popularTabla();
    }
    
    private void datosIncorrectos(){
        //habitacion que sea un numero y hora con el formato necesario
    }
    
    private void limitarCampos(){
        //Limita la longitud de los campos
        jTextHabitacion.setDocument(new JTextFieldLimit(4));
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

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

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
                        .addComponent(jTextHabitacion, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addGap(228, 228, 228)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextHora, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
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

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre(s)", "Apellido(s)", "Tipo de Documento", "Número de Documento"
            }
        ));
        jTable1.setRowHeight(30);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jTable1.setShowVerticalLines(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButtonSiguiente.setText("Siguiente");
        jButtonSiguiente.setEnabled(false);
        jButtonSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguienteActionPerformed(evt);
            }
        });

        jButtonFacturarTercero.setText("Facturar a Tercero");
        jButtonFacturarTercero.setEnabled(false);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCancelar)
                .addContainerGap())
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
        //Si la estadía ya está facturada y los servicios tambien
        //La lista de servicios prestados tiene que estar completamente facturada
        List<ServicioPrestado> serviciosPrestados = estadia.getServiciosPrestados();
        
        //System.out.println(serviciosPrestados);
        boolean todosFacturados=false;
        
        if(estadia.getFactura()!=null){
            //EstadiaFacturada
            
            if(!serviciosPrestados.isEmpty()){
                //Servicios Existentes->Controlar que estén pagados
                
                int tamanioP = serviciosPrestados.size();
                int cantP;
                
                for(int i=0; i<tamanioP; i++){
                    
                    int cantF = 0;
                    ServicioPrestado servicioP = serviciosPrestados.get(i);
                    List<ServicioFacturado> serviciosFacturados = servicioP.getServiciosFacturados();
                    cantP = servicioP.getCantidad();
                    
                    int tamanioF = serviciosFacturados.size();

                    for(int j=0; j<tamanioF; j++){
                        ServicioFacturado servicioF = serviciosFacturados.get(j);

                        cantF=cantF + servicioF.getCantidad();
                    }

                    if(cantF==cantP){
                        todosFacturados = true;//Están todos facturados
                    }else{
                        todosFacturados = false;
                    }
                }
                
                if(todosFacturados){
                    //MENSAJE TODO PAGADO
                    Object opciones[] = {"Aceptar"};
                    JOptionPane.showOptionDialog(
                        null, 
                        "La estadía y los servicios ya fueron facturados, seleccione otra habitación", 
                        "Aviso", 
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        opciones,
                        opciones[0]
                    );
                }else{
                    //Faltan mandar a facturar, pasar un dto
                    pasarTodo();
                }
            }else{
                //MENSAJE TODO PAGADO
                Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                    null, 
                    "La estadía y los servicios ya fueron facturados, seleccione otra habitación", 
                    "Aviso", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );
            }
        }else{
            //Estadia no Facturada->Pasar Estadia
            
            if(!serviciosPrestados.isEmpty()){
                //Servicios Existentes->Controlar que estén pagados
                
                int tamanioP = serviciosPrestados.size();
                int cantP;
                
                for(int i=0; i<tamanioP; i++){
                    
                    int cantF = 0;
                    ServicioPrestado servicioP = serviciosPrestados.get(i);
                    List<ServicioFacturado> serviciosFacturados = servicioP.getServiciosFacturados();
                    cantP = servicioP.getCantidad();
                    
                    int tamanioF = serviciosFacturados.size();

                    for(int j=0; j<tamanioF; j++){
                        ServicioFacturado servicioF = serviciosFacturados.get(j);

                        cantF=cantF + servicioF.getCantidad();
                    }

                    if(cantF==cantP){
                        todosFacturados = true;//Están todos facturados
                    }else{
                        todosFacturados = false;
                    }
                }
                
                if(todosFacturados){
                    //Pasar solo estadía
                    pasarSoloEstadia();
                }else{
                    //Faltan mandar a facturar, pasar todo(tal vez solo un dto)
                    pasarTodo();
                }
            }else{
                //Pasar solo estadía
                pasarSoloEstadia();
            }
        }
        
        
        
        /*
        boolean todosFacturados=true;
        
        if(serviciosPrestados.isEmpty() && !(estadia.getHabitacion().getEstado().name().equals("OCUPADA"))){
            
            
                
        }else{//Hay servicios prestados o la estadía no está ocupada o ambas
            
            if(!serviciosPrestados.isEmpty()){
                
                int tamanioP = serviciosPrestados.size();
                int cantP;
                int cantF = 0;
                for(int i=0; i<tamanioP; i++){

                    ServicioPrestado servicioP = serviciosPrestados.get(i);
                    List<ServicioFacturado> serviciosFacturados = servicioP.getServiciosFacturados();
                    int tamanioF = serviciosFacturados.size();

                    for(int j=0; j<tamanioF; j++){
                        ServicioFacturado servicioF = serviciosFacturados.get(j);

                        cantF=cantF + servicioF.getCantidad();
                    }

                    cantP = servicioP.getCantidad();

                    if(cantF==cantP){
                        todosFacturados = true;//Están todos facturados
                    }else{
                        todosFacturados = false;
                    }
                }
            }else{//No hay servicios prestados, entonces la estadía está ocupada
                if(!(estadia.getHabitacion().getEstado().name().equals("OCUPADA"))){
                    
                }
            }
            
            if(!(estadia.getHabitacion().getEstado().name().equals("OCUPADA")) && todosFacturados){//Si la estadia está desocupada y todos los servicios facturados
            
                Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                    null, 
                    "La estadía y los servicios ya fueron facturados, seleccione otra habitación", 
                    "Aviso", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );
            }else{
                

                        if(pasarDatos){//Si nos pasaron los datos es que venimos de facturar, entonces hay que devolver la lista de servicios pendientes

                            
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
            }
        }
        */
        
        
        
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
        
        //La lista de servicios prestados tiene que estar completamente facturada
        List<ServicioPrestado> serviciosPrestados = estadia.getServiciosPrestados();
        
        boolean todosFacturados=true;
        
        if(serviciosPrestados.isEmpty() && !(estadia.getHabitacion().getEstado().name().equals("OCUPADA"))){
            Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                    null, 
                    "La estadía y los servicios ya fueron facturados, seleccione otra habitación", 
                    "Aviso", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );
        }else{//Hay servicios prestados
            
            
            if(!serviciosPrestados.isEmpty()){
                
                int tamanioP = serviciosPrestados.size();
                int cantP;
                int cantF = 0;
                for(int i=0; i<tamanioP; i++){

                    ServicioPrestado servicioP = serviciosPrestados.get(i);
                    List<ServicioFacturado> serviciosFacturados = servicioP.getServiciosFacturados();
                    int tamanioF = serviciosFacturados.size();

                    for(int j=0; j<tamanioF; j++){
                        ServicioFacturado servicioF = serviciosFacturados.get(j);

                        cantF=cantF + servicioF.getCantidad();
                    }

                    cantP = servicioP.getCantidad();

                    if(cantF==cantP){
                        todosFacturados = true;//Están todos facturados->
                    }else{
                        todosFacturados = false;
                    }
                }
            }
            
            
            if(!(estadia.getHabitacion().getEstado().name().equals("OCUPADA")) && todosFacturados){//Si la estadia está desocupada y todos los servicios facturados
            
                Object opciones[] = {"Aceptar"};
                JOptionPane.showOptionDialog(
                    null, 
                    "La estadía y los servicios ya fueron facturados, seleccione otra habitación", 
                    "Aviso", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones,
                    opciones[0]
                );
            }else{
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
            }
        }
    }//GEN-LAST:event_jButtonFacturarTerceroActionPerformed

    private void pasarSoloEstadia() {
        
        if(controlSeleccionadoYMayor()){
            frame.setContentPane(new PanelFacturar(frame,responsable, estadia, pasajeros, hora));
            frame.setTitle("Facturar");
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setVisible(false);
            frame.getContentPane().setVisible(true);
        }
    }
    
    private void pasarTodo() {
        if(controlSeleccionadoYMayor()){
            frame.setContentPane(new PanelFacturar(frame,responsable, estadia, pasajeros, hora, servPendientes));
            frame.setTitle("Facturar");
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setVisible(false);
            frame.getContentPane().setVisible(true);
        }
    }
    
    private boolean controlSeleccionadoYMayor(){
        
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
            return false;
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
                return false;
            }else{//Si es mayor de edad seguimos

                estadia.calcularCostoFinal(hora);
                gestorAlojamientos.updateEstadia(estadia);
                return true;
            }
        }
    }

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
    
    /*
    private void datosIncorrectos(){
        //habitacion que sea un numero y hora con el formato necesario
    }
    */

    
    
}