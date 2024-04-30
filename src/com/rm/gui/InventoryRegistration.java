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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Thilina
 */
public class InventoryRegistration extends javax.swing.JFrame {

    PurchaseOrder po;

    /**
     * Creates new form InventoryRegistration
     */
    public InventoryRegistration() {
        initComponents();
        setIcon();
        loadCategory();
        loadBrand();
        loadInventoryTable();
    }

    public InventoryRegistration(PurchaseOrder po) {
        initComponents();
        setIcon();
        loadCategory();
        loadBrand();
        loadInventoryTable();
        this.po = po;
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources/restaurant.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCategory() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `inventory_category` ORDER BY `name` ASC");
            Vector v = new Vector();
            v.add("Select Category");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox1.setModel(new DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadBrand() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `inventory_brand` ORDER BY `name` ASC");
            Vector v = new Vector();
            v.add("Select Brand");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox2.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInventoryTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT * FROM `inventory_item` INNER JOIN inventory_category ON inventory_item.category_id=inventory_category.id INNER JOIN inventory_brand ON inventory_item.brand_id=inventory_brand.id order BY inventory_category.name ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("inventory_item.name"));
                v.add(rs.getString("inventory_brand.name"));
                v.add(rs.getString("inventory_category.name"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchInventoryTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            String name = "";
            String bname = null;
            String cname = null;
            if (!jTextField1.getText().isEmpty()) {
                name = jTextField1.getText();
            }
            if (jComboBox1.getSelectedIndex() != 0) {
                cname = "'" + jComboBox1.getSelectedItem().toString() + "'";
            }
            if (jComboBox2.getSelectedIndex() != 0) {
                bname = "'" + jComboBox2.getSelectedItem().toString() + "'";
            }
            ResultSet rs = MySQL.search("SELECT * FROM `inventory_item` INNER JOIN inventory_category ON inventory_item.category_id=inventory_category.id INNER JOIN inventory_brand ON inventory_item.brand_id=inventory_brand.id WHERE `inventory_item`.name LIKE '%" + name + "%' AND `inventory_category`.name= IFNULL(" + cname + ",`inventory_category`.name) AND `inventory_brand`.name= IFNULL(" + bname + ",`inventory_brand`.name) order BY inventory_category.name ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("inventory_item.name"));
                v.add(rs.getString("inventory_brand.name"));
                v.add(rs.getString("inventory_category.name"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetFields() {
        jTextField1.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jLabel5.setText("");
        jLabel6.setText("");
        jButton1.setText("Save");
        jTextField1.grabFocus();
        loadInventoryTable();
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
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("E-Restro 1.0");
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(0, 102, 255));
        jLabel1.setFont(new java.awt.Font("Miriam Libre", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" Inventory Details");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel2.setText("Inventory Name :");

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel3.setText("Category :");

        jComboBox1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel4.setText("Brand :");

        jComboBox2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 255));
        jButton1.setText("Save");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 255));
        jButton2.setText("+");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 102, 255));
        jButton3.setText("+");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.CardLayout());

        jTable1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "BRAND", "CATEGORY"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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

        jPanel3.add(jScrollPane1, "card2");

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, "card2");

        jMenu2.setText("File");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setText("Refresh");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setText("Exit");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(1316, 708));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        loadSearchInventoryTable();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Inventory Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Inventory Category", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox2.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Inventory Brand", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if (jButton1.getText().equals("Save")) {
                try {
                    ResultSet rc = MySQL.search("SELECT * FROM `inventory_category` WHERE `name`='" + jComboBox1.getSelectedItem().toString() + "'");
                    rc.next();
                    ResultSet rb = MySQL.search("SELECT * FROM `inventory_brand` WHERE `name`='" + jComboBox2.getSelectedItem().toString() + "'");
                    rb.next();
                    ResultSet check = MySQL.search("SELECT * FROM `inventory_item` WHERE `name`='" + jTextField1.getText() + "' AND `category_id`='" + rc.getString("id") + "' AND `brand_id`='" + rb.getString("id") + "'");
                    if (check.next()) {
                        JOptionPane.showMessageDialog(this, "Inventory Item Already Registered", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        MySQL.iud("INSERT INTO `inventory_item`(`name`,`category_id`,`brand_id`) VALUES('" + jTextField1.getText() + "','" + rc.getString("id") + "','" + rb.getString("id") + "')");
                        resetFields();
                        JOptionPane.showMessageDialog(this, "Inventory Item Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    ResultSet rc = MySQL.search("SELECT * FROM `inventory_category` WHERE `name`='" + jComboBox1.getSelectedItem().toString() + "'");
                    rc.next();
                    ResultSet rb = MySQL.search("SELECT * FROM `inventory_brand` WHERE `name`='" + jComboBox2.getSelectedItem().toString() + "'");
                    rb.next();
                    ResultSet check = MySQL.search("SELECT * FROM `inventory_item` WHERE `name`='" + jTextField1.getText() + "' AND `category_id`='" + rc.getString("id") + "' AND `brand_id`='" + rb.getString("id") + "' AND `id`<>'" + jLabel6.getText() + "'");
                    if (check.next()) {
                        JOptionPane.showMessageDialog(this, "Inventory Item Already Registered", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        MySQL.iud("UPDATE `inventory_item` SET `name`='" + jTextField1.getText() + "',`category_id`='" + rc.getString("id") + "',`brand_id`='" + rb.getString("id") + "' WHERE `id`='" + jLabel6.getText() + "'");
                        resetFields();
                        JOptionPane.showMessageDialog(this, "Inventory Item Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int sr = jTable1.getSelectedRow();

        if (evt.getClickCount() == 3) {
            if (SignIn.user_type_id == 1 || SignIn.user_type_id == 2) {
                try {
                    ResultSet check = MySQL.search("SELECT * FROM `meal_item_has_inventory_item` WHERE `inventory_item_id`='" + jTable1.getValueAt(sr, 0) + "' ");
                    ResultSet pocheck = MySQL.search("SELECT * FROM `purchase_order_item` INNER JOIN `purchase_order` ON  purchase_order_item.purchase_order_id=purchase_order.id WHERE `inventory_item_id`='" + jTable1.getValueAt(sr, 0) + "' AND purchase_order.recieved_status='0' ");
                    ResultSet stcheck = MySQL.search("SELECT * FROM `stock` WHERE `inventory_item_id`='" + jTable1.getValueAt(sr, 0) + "' ");
                    if (check.next()) {
                        JOptionPane.showMessageDialog(this, "This Inventory Item Registered as Using For Meal Item", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (pocheck.next()) {
                        JOptionPane.showMessageDialog(this, "This Inventory Item Is Ordered", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (stcheck.next()) {
                        JOptionPane.showMessageDialog(this, "This Inventory Item is Already In Stock", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        MySQL.iud("DELETE FROM `inventory_item` WHERE `id`='" + jTable1.getValueAt(sr, 0) + "'");
                        resetFields();
                        JOptionPane.showMessageDialog(this, "Inventory Item Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        resetFields();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Only Admin or A General Manager Can Delete an Inventory Item", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (evt.getClickCount() == 2) {
            if (po != null) {
                po.jLabel13.setText(jTable1.getValueAt(sr, 0).toString());
                po.jLabel15.setText(jTable1.getValueAt(sr, 1).toString());
                po.jLabel17.setText(jTable1.getValueAt(sr, 3).toString());
                po.jLabel19.setText(jTable1.getValueAt(sr, 2).toString());
                this.dispose();
            }
        } else if (evt.getClickCount() == 1) {
            jTextField1.setText(jTable1.getValueAt(sr, 1).toString());
            jLabel6.setText(jTable1.getValueAt(sr, 0).toString());
            jLabel5.setText("Id :");
            jButton1.setText("Update");
            String brand = jTable1.getValueAt(sr, 2).toString();
            String category = jTable1.getValueAt(sr, 3).toString();
            jComboBox1.setSelectedItem(category);
            jComboBox2.setSelectedItem(brand);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        InventoryCategoryRegistration invcr = new InventoryCategoryRegistration(this, true);
        invcr.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        InventoryBrandRegistration invbr = new InventoryBrandRegistration(this, true);
        invbr.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadSearchInventoryTable();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        loadSearchInventoryTable();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
       resetFields();
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InventoryRegistration().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
