/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.gui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.rm.model.MySQL;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Thilina
 */
public class OrderManage extends javax.swing.JFrame {

    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
    DecimalFormat df = new DecimalFormat("00.00");

    /**
     * Creates new form OrderManage1
     */
    public OrderManage() {
        initComponents();
        setIcon();
        loadOrderTable();
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources/restaurant.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadOrderTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            Date date = new Date();
            ResultSet rs = MySQL.search("SELECT `order`.id,customer.id,customer.name,`order`.date_time,SUM( meal_item_has_size.price) AS `price` FROM `order` INNER JOIN order_has_meal_item ON order_has_meal_item.order_id=`order`.id INNER JOIN meal_item ON order_has_meal_item.meal_item_id=meal_item.id INNER JOIN meal_item_size ON order_has_meal_item.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_size ON meal_item_size.id=meal_item_has_size.meal_item_size_id AND meal_item.id=meal_item_has_size.meal_item_id INNER JOIN customer ON `order`.customer_id=customer.id GROUP BY `order`.id order BY `order`.date_time ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("order.id"));
                v.add(rs.getString("customer.id"));
                v.add(rs.getString("customer.name"));
                v.add(rs.getString("order.date_time"));
                v.add(rs.getString("price"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchOrderTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            Date date1 = null;
            Date date2 = null;
            if (jDateChooser1.getDate() != null) {
                date1 = jDateChooser1.getDate();
//                System.out.println(sdf1.format(date1));
            }
            if (jDateChooser2.getDate() != null) {
                date2 = jDateChooser2.getDate();
//                System.out.println(sdf2.format(date2));
            }
            String orderId = "";
            String customerName = "";
            if (!jTextField1.getText().isEmpty()) {
                orderId = jTextField1.getText();
            }
            if (!jTextField2.getText().isEmpty()) {
                customerName = jTextField2.getText();
            }

            String dtquery = "";

            if (date1 != null && date2 == null) {
                dtquery = "AND date_time > '" + sdf1.format(date1) + "'";
            } else if (date2 != null && date1 == null) {
                dtquery = "AND date_time < '" + sdf2.format(date2) + "'";
            } else if (date1 != null && date2 != null) {
//                System.out.println(sdf1.format(date1).equals(sdf2.format(date2)));
                if (sdf1.format(date1).equals(sdf2.format(date2))) {
                    dtquery = "AND date_time > '" + sdf1.format(date1) + "'";
                } else {
                    dtquery = "AND date_time BETWEEN '" + sdf1.format(date1) + "' AND '" + sdf2.format(date2) + "'";
                }
            }

//            System.out.println("SELECT `order`.id,customer.id,customer.name,`order`.date_time,SUM( meal_item_has_size.price) AS `price` FROM `order` INNER JOIN order_has_meal_item ON order_has_meal_item.order_id=`order`.id INNER JOIN meal_item ON order_has_meal_item.meal_item_id=meal_item.id INNER JOIN meal_item_size ON order_has_meal_item.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_size ON meal_item_size.id=meal_item_has_size.meal_item_size_id AND meal_item.id=meal_item_has_size.meal_item_id INNER JOIN customer ON `order`.customer_id=customer.id WHERE `order`.id LIKE '%" + orderId + "%' AND `customer`.name LIKE '%" + customerName + "%' " + dtquery + " GROUP BY `order`.id order BY `order`.date_time ASC");
            ResultSet rs = MySQL.search("SELECT `order`.id,customer.id,customer.name,`order`.date_time,SUM( meal_item_has_size.price) AS `price` FROM `order` INNER JOIN order_has_meal_item ON order_has_meal_item.order_id=`order`.id INNER JOIN meal_item ON order_has_meal_item.meal_item_id=meal_item.id INNER JOIN meal_item_size ON order_has_meal_item.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_size ON meal_item_size.id=meal_item_has_size.meal_item_size_id AND meal_item.id=meal_item_has_size.meal_item_id INNER JOIN customer ON `order`.customer_id=customer.id WHERE `order`.id LIKE '%" + orderId + "%' AND `customer`.name LIKE '%" + customerName + "%' " + dtquery + " GROUP BY `order`.id order BY `order`.date_time ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("order.id"));
                v.add(rs.getString("customer.id"));
                v.add(rs.getString("customer.name"));
                v.add(rs.getString("order.date_time"));
                v.add(rs.getString("price"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("E-Restro 1.0");
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(0, 102, 255));
        jLabel1.setFont(new java.awt.Font("Miriam Libre", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  Manage Customer Orders");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel2.setText("Search Order Options");

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel3.setText("Order Id :");

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel9.setText(" Customer Name :");

        jTextField2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel10.setText("From :");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel11.setText("Date :");

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel12.setText("To :");

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(jLabel11))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jTable1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ORDER ID", "CUSTOMER NO", "CUSTOMER NAME", "DATE TIME", "TOT. PRICE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTable2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "QTY", "PORTION", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel7.setText("Total :");

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel8.setText("00.00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, "card2");

        jMenu3.setText("File");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setText("Refresh");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setText("Exit");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(1316, 708));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int sr = jTable1.getSelectedRow();
        if (evt.getClickCount() == 1) {
            try {
                DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                dtm.setRowCount(0);
                Date date = new Date();
                double grand_tot = 0;

                ResultSet rs = MySQL.search("SELECT meal_item.id,meal_item.name,order_has_meal_item.quantity,meal_item_size.name,meal_item_has_size.price FROM `order` INNER JOIN order_has_meal_item ON `order`.id=order_has_meal_item.order_id INNER JOIN meal_item ON order_has_meal_item.meal_item_id=meal_item.id INNER JOIN meal_item_size ON order_has_meal_item.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_size ON order_has_meal_item.meal_item_size_id=meal_item_has_size.meal_item_size_id AND order_has_meal_item.meal_item_id=meal_item_has_size.meal_item_id WHERE `order`.id='" + jTable1.getValueAt(sr, 0) + "'");
                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("meal_item.id"));
                    v.add(rs.getString("meal_item.name"));
                    v.add(rs.getString("order_has_meal_item.quantity"));
                    v.add(rs.getString("meal_item_size.name"));
                    double tot = Double.parseDouble(rs.getString("meal_item_has_size.price")) * Double.parseDouble(rs.getString("order_has_meal_item.quantity"));
                    grand_tot += tot;
                    v.add(tot);

                    dtm.addRow(v);
                }
                jLabel8.setText(df.format(grand_tot));
                jTable2.setModel(dtm);
            } catch (Exception ex) {
                Logger.getLogger(OrderManage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadSearchOrderTable();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        loadSearchOrderTable();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        loadSearchOrderTable();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
        loadSearchOrderTable();
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm=(DefaultTableModel) jTable2.getModel();
        dtm.setRowCount(0);
        jTextField1.setText("");
        jTextField2.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
        /* Set the Nimbus look and feel */
        UIManager.setLookAndFeel(new FlatIntelliJLaf());
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderManage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
