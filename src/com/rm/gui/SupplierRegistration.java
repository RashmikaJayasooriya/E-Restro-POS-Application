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
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Thilina
 */
public class SupplierRegistration extends javax.swing.JFrame {

    PurchaseOrder po;
    GRN grn;

    /**
     * Creates new form SupplierRegistration
     */
    public SupplierRegistration() {
        initComponents();
        setIcon();
        loadSupplier();
        loadSupplierTable();
    }

    public SupplierRegistration(PurchaseOrder po) {
        initComponents();
        setIcon();
        loadSupplier();
        loadSupplierTable();
        this.po = po;
    }

    public SupplierRegistration(GRN grn) {
        initComponents();
        setIcon();
        loadSupplier();
        loadSupplierTable();
        this.grn = grn;
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources/restaurant.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSupplier() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `city` ORDER BY `name` ASC");
            Vector v = new Vector();
            v.add("Select City");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox1.setModel(new DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSupplierTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT * FROM `supplier` ORDER BY `name` ASC");
            while (rs.next()) {
                Vector v = new Vector();
                ResultSet ra = MySQL.search("SELECT * FROM `address` WHERE `id`='" + rs.getString("address_id") + "'");
                ra.next();
                ResultSet rc = MySQL.search("SELECT * FROM `city` WHERE `id`='" + ra.getString("city_id") + "'");
                rc.next();
                ResultSet rst = MySQL.search("SELECT * FROM `status` WHERE `id`='" + rs.getString("status_id") + "'");
                rst.next();
                String address = ra.getString("address_no") + ", " + ra.getString("street_name") + ", " + rc.getString("name");
                v.add(rs.getString("id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("contact_no"));
                v.add(rs.getString("email"));
                v.add(address);
                v.add(rst.getString("name"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchSupplierTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT * FROM `supplier`WHERE `name` LIKE '%" + jTextField1.getText() + "%' AND `contact_no` LIKE '%" + jTextField2.getText() + "%' AND `email` LIKE '%" + jTextField3.getText() + "%' ORDER BY `name` ASC");
            while (rs.next()) {
                Vector v = new Vector();
                ResultSet ra = MySQL.search("SELECT * FROM `address` WHERE `id`='" + rs.getString("address_id") + "'");
                ra.next();
                ResultSet rc = MySQL.search("SELECT * FROM `city` WHERE `id`='" + ra.getString("city_id") + "'");
                rc.next();
                ResultSet rst = MySQL.search("SELECT * FROM `status` WHERE `id`='" + rs.getString("status_id") + "'");
                rst.next();
                String address = ra.getString("address_no") + ", " + ra.getString("street_name") + ", " + rc.getString("name");
                v.add(rs.getString("id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("contact_no"));
                v.add(rs.getString("email"));
                v.add(address);
                v.add(rst.getString("name"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jComboBox1.setSelectedIndex(0);
        jButton1.setText("Register Supplier");
        jLabel8.setText("");
        jLabel9.setText("");
        loadSupplierTable();
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
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("E-Restro 1.0");
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(0, 102, 255));
        jLabel1.setFont(new java.awt.Font("Miriam Libre", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  Supplier Details");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel2.setText("Name :");

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel3.setText("Contact Number :");

        jTextField2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel4.setText("Email :");

        jTextField3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel5.setText("No :");

        jTextField4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        jTextField5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel6.setText("Street Name :");

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel7.setText("City :");

        jComboBox1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 255));
        jButton1.setText("Register Supplier");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.CardLayout());

        jTable1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "CONTACT NO", "EMAIL", "ADDRESS", "STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Supplier Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Supplier Contact No", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[0245678][0-9]{7}").matcher(jTextField2.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid Contact Number", "warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Supplier Email", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("^([a-z]+|[A-Z]+|[0-9]+|\\.?|\\_?)+@([a-z]+)\\.([a-z]+)$").matcher(jTextField3.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid Email", "warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Supplier No", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField5.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Supplier Street Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Supplier City", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if (jButton1.getText().equals("Register Supplier")) {
                try {
                    ResultSet check = MySQL.search("SELECT * FROM `supplier` WHERE `name`='" + jTextField1.getText() + "' AND `contact_no`='" + jTextField2.getText() + "' AND `email`='" + jTextField3.getText() + "'");
                    if (check.next()) {
                        JOptionPane.showMessageDialog(this, "Supplier Already Registered", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        ResultSet rc = MySQL.search("SELECT * FROM `city` WHERE `name`='" + jComboBox1.getSelectedItem().toString() + "'");
                        rc.next();
                        ResultSet raddresscheck = MySQL.search("SELECT * FROM `address` WHERE `address_no`='" + jTextField4.getText() + "' AND `street_name`='" + jTextField5.getText() + "' AND `city_id`='" + rc.getString("id") + "'");
                        String id = null;
                        if (raddresscheck.next()) {
                            id = raddresscheck.getString("id");
                        } else {
                            MySQL.iud("INSERT INTO `address`(`address_no`,`street_name`,`city_id`) VALUES('" + jTextField4.getText() + "','" + jTextField5.getText() + "','" + rc.getString("id") + "')");
                            ResultSet rs = MySQL.search("SELECT LAST_INSERT_ID()");
                            rs.next();
                            id = rs.getString(1);
                        }
                        MySQL.iud("INSERT INTO `supplier`(`name`,`address_id`,`contact_no`,`email`,`status_id`) VALUES('" + jTextField1.getText() + "','" + id + "','" + jTextField2.getText() + "','" + jTextField3.getText() + "','1')");
                        resetFields();
                        JOptionPane.showMessageDialog(this, "Supplier Registered Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SupplierRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    ResultSet check = MySQL.search("SELECT * FROM `supplier` WHERE `name`='" + jTextField1.getText() + "' AND `contact_no`='" + jTextField2.getText() + "' AND `email`='" + jTextField3.getText() + "' AND `id`<>'" + jLabel9.getText() + "'");
                    if (check.next()) {
                        JOptionPane.showMessageDialog(this, "Supplier Already Registered", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        ResultSet rc = MySQL.search("SELECT * FROM `city` WHERE `name`='" + jComboBox1.getSelectedItem().toString() + "'");
                        rc.next();
                        ResultSet raddresscheck = MySQL.search("SELECT * FROM `address` WHERE `address_no`='" + jTextField4.getText() + "' AND `street_name`='" + jTextField5.getText() + "' AND `city_id`='" + rc.getString("id") + "'");
                        String id = null;
                        if (raddresscheck.next()) {
                            id = raddresscheck.getString("id");
                        } else {
                            MySQL.iud("INSERT INTO `address`(`address_no`,`street_name`,`city_id`) VALUES('" + jTextField4.getText() + "','" + jTextField5.getText() + "','" + rc.getString("id") + "')");
                            ResultSet rs = MySQL.search("SELECT LAST_INSERT_ID()");
                            rs.next();
                            id = rs.getString(1);
                        }
                        MySQL.iud("UPDATE `supplier` SET `name`='" + jTextField1.getText() + "',`address_id`='" + id + "',`contact_no`='" + jTextField2.getText() + "',`email`='" + jTextField3.getText() + "' WHERE `id`='" + jLabel9.getText() + "'");
                        resetFields();
                        JOptionPane.showMessageDialog(this, "Supplier Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SupplierRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int sr = jTable1.getSelectedRow();

        if (evt.getClickCount() == 3) {
            if (SignIn.user_type_id == 1 || SignIn.user_type_id == 2 || SignIn.user_type_id == 3) {
                try {
                    if (jTable1.getValueAt(sr, 5).equals("Active")) {
                        MySQL.iud("UPDATE `supplier` SET `status_id`='2' WHERE `id`='" + jTable1.getValueAt(sr, 0) + "'");
                        JOptionPane.showMessageDialog(this, "Supplier Blocked Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        MySQL.iud("UPDATE `supplier` SET `status_id`='1' WHERE `id`='" + jTable1.getValueAt(sr, 0) + "'");
                        JOptionPane.showMessageDialog(this, "Supplier Unblocked Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    resetFields();

                } catch (Exception ex) {
                    Logger.getLogger(SupplierRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Only Admin or A General Manager or A Assistant Manager Can Disabled a Supplier", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (evt.getClickCount() == 2) {
            if (po != null) {
                if (jTable1.getValueAt(sr, 5).toString().equals("Inactive")) {
                    JOptionPane.showMessageDialog(this, "You Cannot Select a Blocked Supplier", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    po.jLabel5.setText(jTable1.getValueAt(sr, 0).toString());
                    po.jLabel7.setText(jTable1.getValueAt(sr, 1).toString());
                    po.jLabel9.setText(jTable1.getValueAt(sr, 2).toString());
                    po.jLabel23.setText(jTable1.getValueAt(sr, 3).toString());
                    po.jLabel11.setText(jTable1.getValueAt(sr, 4).toString());
                    po.jButton1.setText("Update Supplier");
                    po.jButton1.setEnabled(false);
                    this.dispose();
                }
            }
            if (grn != null) {
                if (jTable1.getValueAt(sr, 5).toString().equals("Inactive")) {
                    JOptionPane.showMessageDialog(this, "You Cannot Select a Blocked Supplier", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    po.jLabel5.setText(jTable1.getValueAt(sr, 0).toString());
                    po.jLabel7.setText(jTable1.getValueAt(sr, 1).toString());
                    po.jLabel9.setText(jTable1.getValueAt(sr, 2).toString());
                    po.jLabel23.setText(jTable1.getValueAt(sr, 3).toString());
                    po.jLabel11.setText(jTable1.getValueAt(sr, 4).toString());
                    this.dispose();
                }
            }
        } else if (evt.getClickCount() == 1) {
            jLabel8.setText("Id :");
            jLabel9.setText(jTable1.getValueAt(sr, 0).toString());
            jTextField1.setText(jTable1.getValueAt(sr, 1).toString());
            jTextField2.setText(jTable1.getValueAt(sr, 2).toString());
            jTextField3.setText(jTable1.getValueAt(sr, 3).toString());
            String address_ct = jTable1.getValueAt(sr, 4).toString();
            String[] address = address_ct.split(", ");
            jTextField4.setText(address[0]);
            jTextField5.setText(address[1]);
            jComboBox1.setSelectedItem(address[2]);
            jButton1.setText("Update Supplier");
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        loadSearchSupplierTable();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
        String mobile = jTextField2.getText();
        String text = mobile + evt.getKeyChar();

        if (mobile.length() == 10) {
            evt.consume();
        } else {
            if (!Pattern.compile("[0-9]+").matcher(text).matches()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadSearchSupplierTable();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        loadSearchSupplierTable();
    }//GEN-LAST:event_jTextField2KeyReleased

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
                new SupplierRegistration().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
