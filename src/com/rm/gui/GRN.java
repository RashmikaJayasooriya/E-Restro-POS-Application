/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.gui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.rm.model.MySQL;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Thilina
 */
public class GRN extends javax.swing.JFrame {

    DecimalFormat df = new DecimalFormat("00.00");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates new form GRN
     */
    public GRN() {
        initComponents();
        setIcon();
        loadPurchaseOrders();
        loadPaymentType();
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources/restaurant.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPurchaseOrders() {
        try {
            java.sql.ResultSet rs = com.rm.model.MySQL.search("SELECT * FROM `purchase_order` WHERE `recieved_status`='0' ORDER BY `date` ASC");
            java.util.Vector v = new java.util.Vector();
            v.add("Select Purchase Order No");
            while (rs.next()) {
                v.add(rs.getString("unique_id"));
            }
            jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadPaymentType() {
        try {
            java.sql.ResultSet rs = com.rm.model.MySQL.search("SELECT * FROM `payment_type` ORDER BY `name` ASC");
            java.util.Vector v = new java.util.Vector();
            v.add("Select Payment Method");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadPoInventoryTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT * FROM `purchase_order_item` WHERE `purchase_order_id` IN(SELECT `id` FROM `purchase_order` WHERE `unique_id`='" + jComboBox3.getSelectedItem() + "')");
            while (rs.next()) {
                Vector v = new Vector();
                ResultSet ri = MySQL.search("SELECT * FROM `inventory_item` WHERE `id`='" + rs.getString("inventory_item_id") + "'");
                ri.next();
                ResultSet rc = MySQL.search("SELECT * FROM `inventory_category` WHERE `id`='" + ri.getString("category_id") + "'");
                rc.next();
                ResultSet rb = MySQL.search("SELECT * FROM `inventory_brand` WHERE `id`='" + ri.getString("brand_id") + "'");
                rb.next();
                v.add(ri.getString("id"));
                v.add(ri.getString("name"));
                v.add(rc.getString("name"));
                v.add(rb.getString("name"));
                dtm.addRow(v);
            }
            jTable2.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateTotal() {
        double total = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            total += Double.parseDouble(jTable1.getValueAt(i, 9).toString());
        }
        jLabel26.setText(df.format(total));
    }

    public void resetSub() {
        jLabel13.setText("None");
        jLabel15.setText("None");
        jLabel16.setText("None");
        jLabel17.setText("00.00");
        jTextField1.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
    }

    public void resetMain() {
        jLabel5.setText("None");
        jLabel7.setText("None");
        jLabel9.setText("None");
        jLabel33.setText("None");
        jLabel11.setText("None");
        jLabel26.setText("00.00");
        jComboBox2.setSelectedIndex(0);
        jTextField3.setText("");
        jLabel30.setText("00.00");
        jLabel30.setForeground(Color.black);
        jComboBox3.setSelectedIndex(0);
        DefaultTableModel dtm1 = (DefaultTableModel) jTable1.getModel();
        dtm1.setRowCount(0);
        DefaultTableModel dtm2 = (DefaultTableModel) jTable2.getModel();
        dtm2.setRowCount(0);
        loadPurchaseOrders();
    }

    public void acceptItemEnter() {
        try {
            DefaultTableModel dtm1 = (DefaultTableModel) jTable1.getModel();
            dtm1.setRowCount(0);
            jLabel26.setText("00.00");
            jComboBox2.setSelectedIndex(0);
            jTextField3.setText("");
            updateTotal();
            jLabel30.setText("00.00");
            jLabel30.setForeground(Color.black);
            // TODO add your handling code here:
            loadPoInventoryTable();
//                System.out.println("SELECT * FROM `supplier` WHERE `id` IN(SELECT `supplier_id` FROM `purchase_order` WHERE `unique_id`='" + jComboBox3.getSelectedItem() + "')");
            ResultSet rs = MySQL.search("SELECT * FROM `supplier` WHERE `id` IN(SELECT `supplier_id` FROM `purchase_order` WHERE `unique_id`='" + jComboBox3.getSelectedItem() + "')");
            rs.next();
//                System.out.println("SELECT * FROM `address` WHERE `id`='" + rs.getString("address_id") + "'");
            ResultSet ra = MySQL.search("SELECT * FROM `address` WHERE `id`='" + rs.getString("address_id") + "'");
            ra.next();
//                System.out.println("SELECT * FROM `city` WHERE `id`='" + ra.getString("city_id") + "'");
            ResultSet rc = MySQL.search("SELECT * FROM `city` WHERE `id`='" + ra.getString("city_id") + "'");
            rc.next();
            String address = ra.getString("address_no") + ", " + ra.getString("street_name") + ", " + rc.getString("name");
            jLabel5.setText(rs.getString("id"));
            jLabel7.setText(rs.getString("name"));
            jLabel9.setText(rs.getString("contact_no"));
            jLabel33.setText(rs.getString("email"));
            jLabel11.setText(address);
        } catch (Exception ex) {
            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
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
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("E-Restro 1.0");
        getContentPane().setLayout(new java.awt.CardLayout());

        jLabel1.setBackground(new java.awt.Color(0, 102, 255));
        jLabel1.setFont(new java.awt.Font("Miriam Libre", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("   GRN Details");
        jLabel1.setOpaque(true);

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel3.setText("Supplier Details");

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel4.setText("Id :");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel5.setText("None");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel6.setText("Name :");

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel7.setText("None");

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel8.setText("Contact No :");

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel9.setText("None");

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel10.setText("Address :");

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel11.setText("None");

        jLabel32.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel32.setText("Email :");

        jLabel33.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel33.setText("None");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel32))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Inventory Details");

        jLabel20.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel20.setText("Quantity  Ordered:");

        jLabel21.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel21.setText("Buying Price :");

        jLabel22.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel22.setText("MFD :");

        jLabel23.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel23.setText("EXD :");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");

        jDateChooser2.setDateFormatString("yyyy-MM-dd");

        jButton2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 255));
        jButton2.setText("Add GRN");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel34.setText("Select Purchase Order No :");

        jComboBox3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Name", "Brand", "Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel35.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel35.setText("Recieved Products Details");

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel12.setText("Id :");

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel13.setText("None");

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel14.setText("Name :");

        jLabel15.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel15.setText("None");

        jLabel16.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel16.setText("None");

        jLabel17.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel17.setText("00.00");

        jLabel36.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel36.setText("Quantity Accepted :");

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel35))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(78, 78, 78)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel21))
                                .addGap(17, 17, 17)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField1)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 26, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(16, 16, 16)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "INVENTORY ID", "NAME", "BRAND", "CATEGORY", "QUANTITY", "MEAS.", "BYING PRICE", "MFD", "EXD", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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

        jLabel24.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel24.setText("Payment Details");

        jLabel25.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel25.setText("Total Payment :");

        jLabel26.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel26.setText("00.00");

        jLabel27.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel27.setText("Payment Method :");

        jComboBox2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel28.setText("Payment :");

        jTextField3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel29.setText("Balance :");

        jLabel30.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel30.setText("00.00");

        jButton3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 102, 255));
        jButton3.setText("Print GRN");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel31.setBackground(new java.awt.Color(0, 102, 255));
        jLabel31.setFont(new java.awt.Font("Miriam Libre", 0, 15)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText(" GRN Table");
        jLabel31.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, "card2");

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setText("Refresh");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(1366, 768));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        if (jComboBox3.getSelectedIndex() != 0) {
            if (jTable1.getRowCount() > 0) {
                int option = JOptionPane.showConfirmDialog(this, "Changing Into New Purchase Order Will Erase The Current Items In The Accepted Table. Do You Want To Continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    acceptItemEnter();
                }
            } else {
                acceptItemEnter();
            }

        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            try {
                int selectedRow = jTable2.getSelectedRow();
                jLabel13.setText(jTable2.getValueAt(selectedRow, 0).toString());
                jLabel15.setText(jTable2.getValueAt(selectedRow, 1).toString());
                ResultSet rs = MySQL.search("SELECT * FROM `purchase_order_item` WHERE `purchase_order_id` IN(SELECT `id` FROM `purchase_order` WHERE `unique_id`='" + jComboBox3.getSelectedItem() + "') AND `inventory_item_id`='" + jTable2.getValueAt(selectedRow, 0) + "' ");
                rs.next();
                ResultSet rm = MySQL.search("SELECT * FROM `measurment_type` WHERE `id`='" + rs.getString("measurment_type_id") + "'");
                rm.next();
                if (rm.getString("measurment_type_category_id").equals("1")) {
                    jLabel16.setText(rs.getString("quantity") + " " + rm.getString("name") + " of " + rm.getString("containing_amount") + " g");
                } else if (rm.getString("measurment_type_category_id").equals("2")) {
                    jLabel16.setText(rs.getString("quantity") + " " + rm.getString("name") + " of " + rm.getString("containing_amount") + " ml");
                } else {
                    jLabel16.setText(rs.getString("quantity") + " " + rm.getString("name") + " of " + rm.getString("containing_amount") + " counts");
                }
                jLabel17.setText(rs.getString("buying_price"));
            } catch (Exception ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        if (jComboBox3.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select a Purchase Order Related to GRN", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jLabel13.getText().equals("None")) {
            JOptionPane.showMessageDialog(this, "Please Select a Inventory Item", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Accepted From Recieved Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please Enter Manufature Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please Enter Expire Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser1.getDate().after(jDateChooser2.getDate())) {
            JOptionPane.showMessageDialog(this, "Please Enter a Correct Manufature Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser1.getDate().after(new Date())) {
            JOptionPane.showMessageDialog(this, "Please Enter a Correct Manufature Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser2.getDate().before(new Date())) {
            JOptionPane.showMessageDialog(this, "Please Enter a Correct Expire Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser2.getDate().equals(new Date())) {
            JOptionPane.showMessageDialog(this, "Please Enter a Correct Expire Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser2.getDate().before(jDateChooser1.getDate())) {
            JOptionPane.showMessageDialog(this, "Please Enter a Correct Expire Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            boolean isFound = false;
            int x = -1;
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                if (jTable1.getValueAt(i, 0).equals(jLabel13.getText())) {
                    isFound = true;
                    x = i;
                    break;
                }
            }
            if (isFound == true) {
                int option = JOptionPane.showConfirmDialog(this, "Inventory Item Already Added. Do You Want to Update the Quantity and MFD and EXD", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    jTable1.setValueAt(jTextField1.getText(), x, 4);
                    jTable1.setValueAt(sdf.format(jDateChooser1.getDate()), x, 7);
                    jTable1.setValueAt(sdf.format(jDateChooser2.getDate()), x, 8);
                    jTable1.setValueAt(Double.parseDouble(jTextField1.getText()) * Double.parseDouble(jLabel17.getText()), x, 9);
                    updateTotal();
                }
            } else {

                Vector v = new Vector();
                v.add(jLabel13.getText());
                v.add(jLabel15.getText());
                v.add(jTable2.getValueAt(jTable2.getSelectedRow(), 2));
                v.add(jTable2.getValueAt(jTable2.getSelectedRow(), 3));
                v.add(jTextField1.getText());
                String[] qty_cnt = jLabel16.getText().split(" ");
                String wrd = "";
                for (int i = 1; i < qty_cnt.length - 3; i++) {
                    wrd = wrd.concat(qty_cnt[i]) + " ";
                }
                v.add(wrd + "-" + qty_cnt[qty_cnt.length - 2] + " " + qty_cnt[qty_cnt.length - 1]);
                v.add(jLabel17.getText());
                v.add(sdf.format(jDateChooser1.getDate()));
                v.add(sdf.format(jDateChooser2.getDate()));
                v.add(Double.parseDouble(jTextField1.getText()) * Double.parseDouble(jLabel17.getText()));
                dtm.addRow(v);
                jTable1.setModel(dtm);
                updateTotal();
            }
            resetSub();

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        if (jTextField3.getText().isEmpty()) {
            jLabel30.setText("00.00");
            jLabel30.setForeground(Color.black);
        } else {
            Double balance = Double.parseDouble(jTextField3.getText()) - Double.parseDouble(jLabel26.getText());
            jLabel30.setText(df.format(balance));
            if (balance < 0) {
                jLabel30.setForeground(Color.red);
            } else if (balance > 0) {
                jLabel30.setForeground(Color.green);
            } else {
                jLabel30.setForeground(Color.black);
            }
        }
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (jTable1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please Add Inventory To Table", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTable2.getRowCount() != jTable1.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Please Select All Items in the Purchase Order and Enter the Accepted Quantity For the Items", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox2.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select a Payement Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter the Payment Amount", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                long mTime = System.currentTimeMillis();
                String uniqueId = mTime + "-" + SignIn.user_id;
                ResultSet rpo = MySQL.search("SELECT * FROM `purchase_order` WHERE `unique_id`='" + jComboBox3.getSelectedItem() + "'");
                rpo.next();
                System.out.println("INSERT INTO `grn`(`supplier_id`,`recieved_date`,`user_id`,`unique_id`,`purchase_order_id`) VALUES('" + jLabel5.getText() + "','" + String.valueOf(sdf.format(new Date())) + "','" + SignIn.user_id + "','" + uniqueId + "','" + rpo.getString("id") + "')");
                MySQL.iud("INSERT INTO `grn`(`supplier_id`,`recieved_date`,`user_id`,`unique_id`,`purchase_order_id`) VALUES('" + jLabel5.getText() + "','" + String.valueOf(sdf.format(new Date())) + "','" + SignIn.user_id + "','" + uniqueId + "','" + rpo.getString("id") + "')");
                ResultSet rg = MySQL.search("SELECT * FROM `grn` WHERE `unique_id`='" + uniqueId + "'");
                rg.next();
                ResultSet rpt = MySQL.search("SELECT * FROM `payment_type` WHERE `name`='" + jComboBox2.getSelectedItem() + "'");
                rpt.next();
                MySQL.iud("INSERT INTO `grn_payment`(`grn_id`,`payment_type_id`,`payment`,`balance`) VALUES('" + rg.getString("id") + "','" + rpt.getString("id") + "','" + jTextField3.getText() + "','" + jLabel30.getText() + "')");

                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    ResultSet rst = MySQL.search("SELECT * FROM `stock` WHERE `inventory_item_id`='" + jTable1.getValueAt(i, 0) + "' AND `mfd`='" + jTable1.getValueAt(i, 7) + "' AND `exd`='" + jTable1.getValueAt(i, 8) + "'");
                    String stock_id;
                    String[] qty_cnt = jTable1.getValueAt(i, 5).toString().split("-");
                    String[] unt_cnt = qty_cnt[1].split(" ");
                    ResultSet rmt = MySQL.search("SELECT * FROM `measurment_type` WHERE `name`='" + qty_cnt[0] + "' AND `containing_amount`='" + unt_cnt[0] + "'");
                    rmt.next();
                    if (rst.next()) {
                        System.out.println("2");
                        stock_id = rst.getString("id");
                        String stock_qty = rst.getString("quantity");
                        double updatedQty = Double.parseDouble(stock_qty) + Double.parseDouble(jTable1.getValueAt(i, 4).toString());
                        MySQL.iud("UPDATE `stock` SET `quantity`='" + updatedQty + "' WHERE `id`='" + stock_id + "'");
                    } else {
                        double qty = Double.parseDouble(jTable1.getValueAt(i, 4).toString()) * Double.parseDouble(rmt.getString("containing_amount"));
                        MySQL.iud("INSERT INTO `stock`(`inventory_item_id`,`quantity`,`measurment_type_id`,`mfd`,`exd`) VALUES('" + jTable1.getValueAt(i, 0) + "','" + qty + "','" + rmt.getString("id") + "','" + jTable1.getValueAt(i, 7) + "','" + jTable1.getValueAt(i, 8) + "')");
                        ResultSet rs = MySQL.search("SELECT * FROM `stock` WHERE `inventory_item_id`='" + jTable1.getValueAt(i, 0) + "' AND `quantity`='" + qty + "' AND `measurment_type_id`='" + rmt.getString("id") + "' AND `mfd`='" + jTable1.getValueAt(i, 7) + "' AND `exd`='" + jTable1.getValueAt(i, 8) + "'");
                        rs.next();
                        stock_id = rs.getString("id");
                    }

                    MySQL.iud("INSERT INTO `grn_item`(`quantity`,`measurment_type`,`buying_price`,`grn_id`,`stock_id`) VALUES('" + jTable1.getValueAt(i, 4) + "','" + rmt.getString("id") + "','" + jTable1.getValueAt(i, 6) + "','" + rg.getString("id") + "','" + stock_id + "')");
                }
                MySQL.iud("UPDATE `purchase_order` SET `recieved_status`='1' WHERE `unique_id`='" + jComboBox3.getSelectedItem() + "'");
//                Reports
                InputStream stream = GRN.class.getResourceAsStream("/com/rm/reports/RESTNTGRNnext.jrxml");
                JasperReport jr = JasperCompileManager.compileReport(stream);
                HashMap parameters = new HashMap();
                String[] ad_cnt = jLabel11.getText().split(",");
                System.out.println(ad_cnt[0] + "," + ad_cnt[1]);
                System.out.println(ad_cnt[2]);
                System.out.println("date");
                parameters.put("Parameter1", uniqueId);
                parameters.put("Parameter2", new SimpleDateFormat().format(new Date()));
                parameters.put("Parameter3", jComboBox3.getSelectedItem());
                parameters.put("Parameter4", jLabel7.getText());
                parameters.put("Parameter5", jLabel9.getText());
                System.out.println(Double.parseDouble(jTextField3.getText().toString()));
                parameters.put("Parameter6", Double.parseDouble(jTextField3.getText().toString()));

                Connection dataSource = MySQL.getConnection();

                JasperPrint jp = JasperFillManager.fillReport(jr, parameters, dataSource);
                JasperViewer.viewReport(jp, false);
//                Reports
                resetMain();
                JOptionPane.showMessageDialog(this, "New GRN Created", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
        String qty = jTextField1.getText();
        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]|0[.][1-9][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][1-9]|[1-9][0-9]*[.][0-9][1-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int sr = jTable1.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        if (evt.getClickCount() == 2) {
            dtm.removeRow(sr);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here
        String pay = jTextField3.getText();
        String text = pay + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]|0[.][1-9][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][1-9]|[1-9][0-9]*[.][0-9][1-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        resetMain();
        resetSub();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
        /* Set the Nimbus look and feel */
        UIManager.setLookAndFeel(new FlatIntelliJLaf());

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GRN().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    public javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    public javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    public javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    public javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
