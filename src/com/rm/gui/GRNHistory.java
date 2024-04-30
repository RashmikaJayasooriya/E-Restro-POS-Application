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
public class GRNHistory extends javax.swing.JFrame {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("00.00");

    /**
     * Creates new form OrderManage1
     */
    public GRNHistory() {
        initComponents();
        setIcon();
        loadGrnHisTable();
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources/restaurant.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGrnHisTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            Date date = new Date();
            ResultSet rs = MySQL.search("SELECT grn.unique_id AS `grn_id`,purchase_order.unique_id AS `po_id`,purchase_order.date,grn.recieved_date,supplier.name AS `sup_name`,DATEDIFF(grn.recieved_date,purchase_order.date) AS `lead_time`,user.name AS `us_name` FROM grn INNER JOIN purchase_order ON grn.purchase_order_id=purchase_order.id INNER JOIN supplier ON grn.supplier_id=supplier.id INNER JOIN user ON grn.user_id=user.id order BY grn.recieved_date ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("grn_id"));
                v.add(rs.getString("po_id"));
                v.add(rs.getString("date"));
                v.add(rs.getString("recieved_date"));
                v.add(rs.getString("sup_name"));
                v.add(rs.getString("lead_time"));
                v.add(rs.getString("us_name"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchGrnHisTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            Date date1 = null;
            Date date2 = null;
            Date date3 = null;
            Date date4 = null;
            if (jDateChooser1.getDate() != null) {
                date1 = jDateChooser1.getDate();
//                System.out.println(sdf.format(date1));
            }
            if (jDateChooser2.getDate() != null) {
                date2 = jDateChooser2.getDate();
//                System.out.println(sdf.format(date2));
            }
            if (jDateChooser3.getDate() != null) {
                date2 = jDateChooser3.getDate();
//                System.out.println(sdf.format(date2));
            }
            if (jDateChooser4.getDate() != null) {
                date2 = jDateChooser4.getDate();
//                System.out.println(sdf.format(date2));
            }

            String guid = "";
            String pouid = "";
            String supn = "";
            String un = "";
            if (!jTextField1.getText().isEmpty()) {
                guid = jTextField1.getText();
            }
            if (!jTextField2.getText().isEmpty()) {
                supn = jTextField2.getText();
            }
            if (!jTextField3.getText().isEmpty()) {
                pouid = jTextField3.getText();
            }
            if (!jTextField4.getText().isEmpty()) {
                un = jTextField4.getText();
            }

            String gdtquery = "";

            if (date1 != null && date2 == null) {
                gdtquery = "AND `grn`.`recieved_date` > '" + sdf.format(date1) + "'";
            } else if (date2 != null && date1 == null) {
                gdtquery = "AND `grn`.`recieved_date` < '" + sdf.format(date2) + "'";
            } else if (date1 != null && date2 != null) {
//                System.out.println(sdf1.format(date1).equals(sdf2.format(date2)));
                if (sdf.format(date1).equals(sdf.format(date2))) {
                    gdtquery = "AND `grn`.`recieved_date` > '" + sdf.format(date1) + "'";
                } else {
                    gdtquery = "AND `grn`.`recieved_date` BETWEEN '" + sdf.format(date1) + "' AND '" + sdf.format(date2) + "'";
                }
            }

            String pdtquery = "";

            if (date3 != null && date4 == null) {
                pdtquery = "AND `purchase_order`.`date` > '" + sdf.format(date3) + "'";
            } else if (date4 != null && date3 == null) {
                pdtquery = "AND `purchase_order`.`date` < '" + sdf.format(date4) + "'";
            } else if (date3 != null && date4 != null) {
//                System.out.println(sdf1.format(date1).equals(sdf2.format(date2)));
                if (sdf.format(date3).equals(sdf.format(date4))) {
                    pdtquery = "AND `purchase_order`.`date` > '" + sdf.format(date3) + "'";
                } else {
                    pdtquery = "AND `purchase_order`.`date` BETWEEN '" + sdf.format(date3) + "' AND '" + sdf.format(date4) + "'";
                }
            }

//            ResultSet rs = MySQL.search("SELECT `order`.id,customer.id,customer.name,`order`.date_time,SUM( meal_item_has_size.price) AS `price` FROM `order` INNER JOIN order_has_meal_item ON order_has_meal_item.order_id=`order`.id INNER JOIN meal_item ON order_has_meal_item.meal_item_id=meal_item.id INNER JOIN meal_item_size ON order_has_meal_item.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_size ON meal_item_size.id=meal_item_has_size.meal_item_size_id AND meal_item.id=meal_item_has_size.meal_item_id INNER JOIN customer ON `order`.customer_id=customer.id WHERE `order`.id LIKE '%" + orderId + "%' AND `customer`.name LIKE '%" + customerName + "%' " + dtquery + " GROUP BY `order`.id order BY `order`.date_time ASC");
            ResultSet rs = MySQL.search("SELECT grn.unique_id AS `grn_id`,purchase_order.unique_id AS `po_id`,purchase_order.date,grn.recieved_date,supplier.name AS `sup_name`,DATEDIFF(grn.recieved_date,purchase_order.date) AS `lead_time`,user.name AS `us_name` FROM grn INNER JOIN purchase_order ON grn.purchase_order_id=purchase_order.id INNER JOIN supplier ON grn.supplier_id=supplier.id INNER JOIN user ON grn.user_id=user.id WHERE `grn`.`unique_id` LIKE '%" + guid + "%' AND `supplier`.`name` LIKE '%" + supn + "%' AND `purchase_order`.`unique_id` LIKE '%" + pouid + "%' AND `user`.`name` LIKE '%" + un + "%' " + gdtquery + " " + pdtquery + " order BY grn.recieved_date ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("grn_id"));
                v.add(rs.getString("po_id"));
                v.add(rs.getString("date"));
                v.add(rs.getString("recieved_date"));
                v.add(rs.getString("sup_name"));
                v.add(rs.getString("lead_time"));
                v.add(rs.getString("us_name"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jDateChooser3.setDate(null);
        jDateChooser4.setDate(null);
        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        dtm.setRowCount(0);
        jLabel8.setText("00.00");
        jLabel19.setText("00.00");
        jLabel21.setText("None");
        jLabel23.setText("00.00");
        loadGrnHisTable();
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
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("E-Restro 1.0");
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(0, 102, 255));
        jLabel1.setFont(new java.awt.Font("Miriam Libre", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  GRN HISTORY");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel2.setText("Search GRN History Options");

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel3.setText("GRN ID:");

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel9.setText("Supplier Name :");

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

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel4.setText("Purchase Order Id :");

        jTextField3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel13.setText("GRN Created User Name :");

        jTextField4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel14.setText("Date :");

        jLabel15.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel15.setText("From :");

        jDateChooser3.setDateFormatString("yyyy-MM-dd");
        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel16.setText("To :");

        jDateChooser4.setDateFormatString("yyyy-MM-dd");
        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });

        jButton1.setForeground(new java.awt.Color(0, 51, 255));
        jButton1.setText("Refresh");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel11)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
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
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jTable1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "GRN UNIQUE NO", "RELATED PURCHASE ORDER NO", "PURCHASE ORDER CREATED DATE", "RECIEVED DATE", "SUPPLIER NAME", "LEAD TIME", "USER"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
                "GRN ITEM ID", "INV. NAME", "QTY.", "MEASUREMENT", "BYUING PRICE", "STOCK ID", "MFD", "EXD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel7.setFont(new java.awt.Font("Microsoft Tai Le", 1, 12)); // NOI18N
        jLabel7.setText(" Total Price :");

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("00.00");

        jLabel18.setFont(new java.awt.Font("Microsoft Tai Le", 1, 12)); // NOI18N
        jLabel18.setText("Total Payment Made:");

        jLabel19.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel19.setText("00.00");

        jLabel20.setFont(new java.awt.Font("Microsoft Tai Le", 1, 12)); // NOI18N
        jLabel20.setText("Payment Type :");

        jLabel21.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel21.setText("None");

        jLabel22.setFont(new java.awt.Font("Microsoft Tai Le", 1, 12)); // NOI18N
        jLabel22.setText("Balance :");

        jLabel23.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel23.setText("00.00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

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

        setSize(new java.awt.Dimension(1376, 708));
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

                ResultSet rs = MySQL.search("SELECT grn_item.id,inventory_item.name AS `inv_name`,grn_item.quantity,measurment_type.name AS `meas_name`,grn_item.buying_price,stock.id,stock.mfd,stock.exd FROM grn_item INNER JOIN stock ON grn_item.stock_id=stock.id INNER JOIN inventory_item ON stock.inventory_item_id=inventory_item.id INNER JOIN measurment_type ON grn_item.measurment_type=measurment_type.id WHERE grn_item.grn_id IN (SELECT grn.id FROM grn WHERE grn.unique_id='" + jTable1.getValueAt(sr, 0) + "')");
                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("id"));
                    v.add(rs.getString("inv_name"));
                    v.add(rs.getString("quantity"));
                    v.add(rs.getString("meas_name"));
                    v.add(rs.getString("buying_price"));
                    v.add(rs.getString("id"));
                    v.add(rs.getString("mfd"));
                    v.add(rs.getString("exd"));
                    double tot = Double.parseDouble(rs.getString("quantity")) * Double.parseDouble(rs.getString("buying_price"));
                    grand_tot += tot;
                    dtm.addRow(v);
                }
                jLabel8.setText(df.format(grand_tot));
                jTable2.setModel(dtm);
                ResultSet rp = MySQL.search("SELECT * FROM grn_payment INNER JOIN payment_type ON grn_payment.payment_type_id=payment_type.id WHERE grn_payment.grn_id IN (SELECT grn.id FROM grn WHERE grn.unique_id='" + jTable1.getValueAt(sr, 0) + "')");
                rp.next();
                jLabel19.setText(rp.getString("payment"));
                jLabel21.setText(rp.getString("name"));
                jLabel23.setText(rp.getString("balance"));
            } catch (Exception ex) {
                Logger.getLogger(GRNHistory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadSearchGrnHisTable();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        loadSearchGrnHisTable();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        if (jDateChooser1.getDate() != null) {
            loadSearchGrnHisTable();
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
        if (jDateChooser2.getDate() != null) {
            loadSearchGrnHisTable();
        }
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        loadSearchGrnHisTable();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        // TODO add your handling code here:
        if (jDateChooser3.getDate() != null) {
            loadSearchGrnHisTable();
        }
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        // TODO add your handling code here:
        if (jDateChooser4.getDate() != null) {
            loadSearchGrnHisTable();
        }
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        loadSearchGrnHisTable();
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        refresh();
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GRNHistory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
