/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author Nahuel Riquelme
 */
public class PanelMenuPrincipal extends javax.swing.JPanel {
    private final VentanaPrincipal frame;

    /**
     * Creates new form MenuPrincipal
     * @param frame
     */
    public PanelMenuPrincipal(VentanaPrincipal frame) {
        initComponents();
        this.frame = frame;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButtonGestionarPasajeros = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButtonFacturar = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButtonOcuparHabitacion = new javax.swing.JButton();
        jButtonIngresarPago = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Hotel Premier");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jButtonGestionarPasajeros.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonGestionarPasajeros.setText("Gestionar pasajeros");
        jButtonGestionarPasajeros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGestionarPasajerosActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setText("Cancelar reserva");

        jButton9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton9.setText("Gestionar responsable de pago");

        jButtonFacturar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonFacturar.setText("Facturar");
        jButtonFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFacturarActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton13.setText("Gestionar listado");

        jButton14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton14.setText("Reservar habitación");

        jButtonOcuparHabitacion.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonOcuparHabitacion.setText("Ocupar habitación");
        jButtonOcuparHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOcuparHabitacionActionPerformed(evt);
            }
        });

        jButtonIngresarPago.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonIngresarPago.setText("Ingresar pago");
        jButtonIngresarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIngresarPagoActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hp.jpeg"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonGestionarPasajeros, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonOcuparHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9)
                            .addComponent(jButtonIngresarPago, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 435, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButtonGestionarPasajeros, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonIngresarPago, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonOcuparHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGestionarPasajerosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGestionarPasajerosActionPerformed
        // TODO add your handling code here:
        frame.cambiarPanel(VentanaPrincipal.PANE_GESTIONAR_PASAJEROS);
    }//GEN-LAST:event_jButtonGestionarPasajerosActionPerformed

    private void jButtonFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFacturarActionPerformed
        // TODO add your handling code here:
        frame.cambiarPanel(VentanaPrincipal.PANE_SELECCIONAR_RESPONSABLE);
    }//GEN-LAST:event_jButtonFacturarActionPerformed

    private void jButtonIngresarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIngresarPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonIngresarPagoActionPerformed

    private void jButtonOcuparHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOcuparHabitacionActionPerformed
        // TODO add your handling code here:
        frame.cambiarPanel(VentanaPrincipal.PANE_OCUPAR_HABITACION);
    }//GEN-LAST:event_jButtonOcuparHabitacionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonFacturar;
    private javax.swing.JButton jButtonGestionarPasajeros;
    private javax.swing.JButton jButtonIngresarPago;
    private javax.swing.JButton jButtonOcuparHabitacion;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
