/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.gui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.rm.model.MySQL;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Thilina
 */
public class RegisterMealItem extends javax.swing.JFrame {

    DecimalFormat df = new DecimalFormat("00.00");
    Double c_total = 0.0;
    MenuManage mm;

    /**
     * Creates new form RegisterBeverageItem
     */
    public RegisterMealItem() {
        initComponents();
        setIcon();
        loadMeasurment();
        loadPortion();
        loadFoodItemTable();
        loadMealType();
    }

    public RegisterMealItem(MenuManage mm) {
        initComponents();
        setIcon();
        loadMeasurment();
        loadPortion();
        loadFoodItemTable();
        loadMealType();
        this.mm = mm;
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources/restaurant.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMealType() {
        try {
            java.sql.ResultSet rs = com.rm.model.MySQL.search("SELECT * FROM `meal_item_type` ORDER BY `name` ASC");
            java.util.Vector v = new java.util.Vector();
            v.add("Select Meal Type");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadMeasurment() {
        try {
            java.sql.ResultSet rs = com.rm.model.MySQL.search("SELECT * FROM `measurment_type` ORDER BY `name` ASC");
            java.util.Vector v = new java.util.Vector();
            v.add("Select Measurement");
            while (rs.next()) {
                if (rs.getString("measurment_type_category_id").equals("1")) {
                    v.add(rs.getString("name") + "- " + rs.getString("containing_amount") + " g");
                } else if (rs.getString("measurment_type_category_id").equals("2")) {
                    v.add(rs.getString("name") + "- " + rs.getString("containing_amount") + " ml");
                } else {
                    v.add(rs.getString("name") + "- " + rs.getString("containing_amount") + " count");
                }
            }
            jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadPortion() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `meal_item_size` ORDER BY `name` ASC");
            Vector v = new Vector();
            v.add("Select Portion");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox2.setModel(new DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadFoodItemTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT meal_item.id,meal_item.name,GROUP_CONCAT(DISTINCT \" \",meal_item_size.name ,\"->\",meal_item_has_size.price,\" \") AS `pap`,GROUP_CONCAT(DISTINCT inventory_item.name) AS `in`,`status`.`name` AS `sn` FROM meal_item_has_size INNER JOIN meal_item ON meal_item_has_size.meal_item_id=meal_item.id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_inventory_item ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN inventory_item ON meal_item_has_inventory_item.inventory_item_id=inventory_item.id INNER JOIN `status` ON `meal_item`.`status_id`=`status`.`id` GROUP BY meal_item.id order BY meal_item.name ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("meal_item.id"));
                v.add(rs.getString("meal_item.name"));
                v.add(rs.getString("pap"));
                v.add(rs.getString("sn"));
                v.add(rs.getString("in"));
                dtm.addRow(v);
            }
            jTable2.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchFoodItemTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT meal_item.id,meal_item.name,GROUP_CONCAT(DISTINCT \" \",meal_item_size.name ,\"->\",meal_item_has_size.price,\" \") AS `pap`,GROUP_CONCAT(DISTINCT inventory_item.name) AS `in`,`status`.`name` AS `sn` FROM meal_item_has_size INNER JOIN meal_item ON meal_item_has_size.meal_item_id=meal_item.id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_inventory_item ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN inventory_item ON meal_item_has_inventory_item.inventory_item_id=inventory_item.id INNER JOIN `status` ON `meal_item`.`status_id`=`status`.`id` WHERE `meal_item`.name LIKE '%" + jTextField5.getText() + "%' GROUP BY meal_item.id order BY meal_item.name ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("meal_item.id"));
                v.add(rs.getString("meal_item.name"));
                v.add(rs.getString("pap"));
                v.add(rs.getString("sn"));
                v.add(rs.getString("in"));
                dtm.addRow(v);
            }
            jTable2.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetMain() {
        jButton3.setText("Register Meal Item");
        jTextField1.setText("");
        jTextField2.setText("00.00");
        jTextField3.setText("");
        jComboBox3.setSelectedItem(0);
        jComboBox4.setSelectedItem(0);
        loadMealType();
        jLabel10.setText("00.00");
        jLabel23.setText("00.00");
        jLabel25.setText("00.00");
        jLabel7.setText("None");
        jLabel17.setText("None");
        jLabel19.setText("None");
        jLabel21.setText("None");
        jLabel29.setText("");
        jLabel30.setText("");
        c_total = 00.00;
        DefaultTableModel dtm1 = (DefaultTableModel) jTable1.getModel();
        dtm1.setRowCount(0);
        DefaultTableModel dtm2 = (DefaultTableModel) jTable3.getModel();
        dtm2.setRowCount(0);
        loadFoodItemTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator4 = new javax.swing.JSeparator();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel28 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("E-Restro 1.0");
        getContentPane().setLayout(new java.awt.CardLayout());

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(0, 102, 255));
        jLabel1.setFont(new java.awt.Font("Miriam Libre", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  Meal Item Register Details");
        jLabel1.setOpaque(true);

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel4.setText("Inventory Items Include in Meal Item Details");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel6.setText("Id :");

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel7.setText("None");

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel8.setText("Quantity :");

        jTextField3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 255));
        jButton1.setText("Select Inventory Item");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 255));
        jButton2.setText("Add");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "Quantity", "MEAS.", "AMOUNT"
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

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jComboBox3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 102, 255));
        jButton5.setText("+");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel16.setText("Name :");

        jLabel17.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel17.setText("None");

        jLabel18.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel18.setText("Category :");

        jLabel19.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel19.setText("None");

        jLabel20.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel20.setText("Brand :");

        jLabel21.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel21.setText("None");

        jLabel22.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel22.setText("Amount :");

        jLabel23.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel23.setText("00.00");

        jLabel24.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel24.setText("Weighted Cost Price :");

        jLabel25.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel25.setText("00.00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel20))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 232, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButton5))
                                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(6, 6, 6))))
                        .addGap(39, 39, 39)))
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel23))
                                .addGap(18, 18, 18))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator4)))
                .addContainerGap())
        );

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel14.setText("Meal Item Name :");

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel11.setText("Profit Want From This Item:");

        jTextField4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel15.setText("Portion & Profit Details");

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel2.setText("Meal Item Portion :");

        jComboBox2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        jTable3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PORTION", "TOTAL PORTION PRICE", "SIZE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jButton4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 102, 255));
        jButton4.setText("Add");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel3.setText("Other Expnses For This Item:");

        jTextField2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField2.setText("00.00");

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel10.setText("00.00");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel5.setText("Total Of Culinary :");

        jButton6.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 102, 255));
        jButton6.setText("+");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel9.setText("Is This The Regular Size Portion :");

        jLabel26.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel26.setText("Or  How Much Times Of the Regular :");

        jTextField6.setEditable(false);
        jTextField6.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        buttonGroup1.add(jCheckBox1);
        jCheckBox1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jCheckBox1.setText("No");
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jCheckBox2);
        jCheckBox2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jCheckBox2.setText("Yes");
        jCheckBox2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox2StateChanged(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel27.setText("X");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jCheckBox2)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox1))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField6)))))
                .addGap(0, 60, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jCheckBox2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton3.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 102, 255));
        jButton3.setText("Register Meal Item");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel28.setText("Meal Item Type :");

        jComboBox4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1272, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1274, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(19, 19, 19)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel29)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel30)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField1)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel28)
                                    .addGap(18, 18, 18)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 1246, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1007, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel28)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel13.setText("Name :");

        jTextField5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTable2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "PORTION & PRICE", "STATUS", "INCLUDES"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(1);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(350);
        }

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel12.setBackground(new java.awt.Color(0, 102, 255));
        jLabel12.setFont(new java.awt.Font("Miriam Libre", 0, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("  Search Meal Item");
        jLabel12.setOpaque(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1042, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        jScrollPane3.setViewportView(jPanel1);

        getContentPane().add(jScrollPane3, "card2");

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        WeightedAvgStockItems weightedAvgStockItems = new WeightedAvgStockItems(this, true);
        weightedAvgStockItems.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        if (jTextField3.getText().isEmpty()) {
            jLabel23.setText("00.00");
        } else if (jComboBox3.getSelectedIndex() == 0) {
            jLabel23.setText("00.00");
            jTextField3.setText("");
            JOptionPane.showMessageDialog(this, "Please Select Measurement Type First", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String[] mt = jComboBox3.getSelectedItem().toString().split(" ");
            for (int i = 0; i < mt.length; i++) {
                System.out.println(mt[i]);
            }
//            System.out.println(mt.length);
            jLabel23.setText(df.format(Double.parseDouble(jTextField3.getText()) * Double.parseDouble(jLabel25.getText()) * Double.parseDouble(mt[mt.length - 2])));
        }
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        MeasurementTypeRegistration measurementTypeRegistration = new MeasurementTypeRegistration(this, true);
        measurementTypeRegistration.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (jLabel17.getText().equals("None")) {
            JOptionPane.showMessageDialog(this, "Please Select An Item First", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Quantity Adding When Making Food", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox3.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select a Measurement Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            Vector v = new Vector();
            v.add(jLabel7.getText());
            v.add(jLabel17.getText());
            v.add(jTextField3.getText());
            String[] m_cnt = jComboBox3.getSelectedItem().toString().split("-");
            v.add(m_cnt[0]);
            v.add(jLabel23.getText());
            dtm.addRow(v);
            jTable1.setModel(dtm);
            jLabel10.setText(df.format(c_total = c_total + Double.parseDouble(jLabel23.getText())));
            jLabel7.setText("None");
            jLabel17.setText("None");
            jLabel19.setText("None");
            jLabel21.setText("None");
            jLabel25.setText("00.00");
            jLabel23.setText("00.00");
            jTextField3.setText("");
            jComboBox3.setSelectedIndex(0);
            jButton1.setText("Select Inventory Item");
            jButton1.setEnabled(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (jLabel10.getText().equals("00.00")) {
            JOptionPane.showMessageDialog(this, "Please Select Inventory Items First", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Profit You Want From This Item", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox2.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Meal Item Portion", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField6.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Select Portion is Regular or No and Set Size", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if (jTable3.getRowCount() == 0) {
                if (!jCheckBox2.isSelected()) {
                    JOptionPane.showMessageDialog(this, "Please Enter the Regular Portion First", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
                    Vector v = new Vector();
                    v.add(jComboBox2.getSelectedItem());
                    double other_exp;
                    if (jTextField2.getText().isEmpty()) {
                        other_exp = 00.00;
                    } else {
                        other_exp = Double.parseDouble(jTextField2.getText());
                    }
                    Double total_profit = Double.parseDouble(jLabel10.getText()) + other_exp + Double.parseDouble(jTextField4.getText());
                    v.add(total_profit);
                    v.add(jLabel27.getText() + " " + jTextField6.getText());
                    dtm.addRow(v);

                    jTextField2.setText("00.00");
                    jTextField4.setText("");
                    jComboBox2.setSelectedIndex(0);
                    jCheckBox1.setSelected(false);
                    jCheckBox2.setSelected(false);
                    jTextField6.setText("");
                    jTextField6.setEditable(false);
                }
            } else {
                if (jCheckBox2.isSelected()) {
                    JOptionPane.showMessageDialog(this, "A Regular Item is Already Existed", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (jTextField6.getText().equals("1")) {
                    JOptionPane.showMessageDialog(this, "Please Enter Correct Portion Size", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
                    Vector v = new Vector();
                    v.add(jComboBox2.getSelectedItem());
                    double other_exp;
                    if (jTextField2.getText().isEmpty()) {
                        other_exp = 00.00;
                    } else {
                        other_exp = Double.parseDouble(jTextField2.getText());
                    }
                    Double total_price = Double.parseDouble(jLabel10.getText()) + other_exp + Double.parseDouble(jTextField4.getText());
                    v.add(df.format(total_price));
                    v.add(jLabel27.getText() + " " + jTextField6.getText());
                    dtm.addRow(v);

                    jTextField2.setText("00.00");
                    jTextField4.setText("");
                    jComboBox2.setSelectedIndex(0);
                    jCheckBox1.setSelected(false);
                    jCheckBox2.setSelected(false);
                    jTextField6.setText("");
                    jTextField6.setEditable(false);
                }
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        MealItemPortionRegistration foodItemPortionRegistration = new MealItemPortionRegistration(this, true);
        foodItemPortionRegistration.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Meal Item Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox4.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Meal Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTable3.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please Add Portion & Price List", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (jButton3.getText() == "Register Meal Item") {

                    ResultSet rcheck = MySQL.search("SELECT * FROM `meal_item` WHERE `name`='" + jTextField1.getText() + "' AND `meal_item_type_id` IN(SELECT `id` FROM `meal_item_type` WHERE `name`='" + jComboBox4.getSelectedItem() + "')");
                    if (rcheck.next()) {
                        JOptionPane.showMessageDialog(this, "This Meal Item Has Already Been Registered", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        ResultSet rmt = MySQL.search("Select * FROM `meal_item_type` WHERE `name`='" + jComboBox4.getSelectedItem() + "'");
                        rmt.next();
                        MySQL.iud("INSERT INTO `meal_item`(`name`,`meal_item_type_id`) VALUES('" + jTextField1.getText() + "','" + rmt.getString("id") + "')");
                        ResultSet rf = MySQL.search("SELECT * FROM `meal_item` WHERE`name`='" + jTextField1.getText() + "' AND `meal_item_type_id`='" + rmt.getString("id") + "'");
                        rf.next();
                        for (int j = 0; j < jTable1.getRowCount(); j++) {
                            ResultSet rinv_id = MySQL.search("Select * FROM `inventory_item` WHERE `id`='" + jTable1.getValueAt(j, 0) + "'");
                            rinv_id.next();
                            ResultSet rm = MySQL.search("SELECT * FROM `measurment_type` WHERE `name`='" + jTable1.getValueAt(j, 3) + "'");
                            rm.next();
                            Double qty = Double.parseDouble(jTable1.getValueAt(j, 2).toString()) * Double.parseDouble(rm.getString("containing_amount"));
                            MySQL.iud("INSERT INTO `meal_item_has_inventory_item`(`meal_item_id`,`inventory_item_id`,`quantity`,`measurment_type_id`) VALUES('" + rf.getString("id") + "','" + rinv_id.getString("id") + "','" + qty + "','" + rm.getString("id") + "')");
                        }
                        for (int i = 0; i < jTable3.getRowCount(); i++) {
                            ResultSet rp = MySQL.search("SELECT * FROM `meal_item_size` WHERE `name`='" + jTable3.getValueAt(i, 0) + "'");
                            rp.next();
                            String[] s_cnt = jTable3.getValueAt(i, 2).toString().split(" ");
                            MySQL.iud("INSERT INTO `meal_item_has_size`(`meal_item_id`,`meal_item_size_id`,`price`,`size_of_normal`) VALUES('" + rf.getString("id") + "','" + rp.getString("id") + "','" + jTable3.getValueAt(i, 1) + "','" + s_cnt[1] + "')");
                        }

//                        Report
                        InputStream stream = RegisterMealItem.class.getResourceAsStream("/com/rm/reports/meal_item_reg.jasper");
                        HashMap parameters = new HashMap();
                        parameters.put("Parameter1", jTextField1.getText());
                        parameters.put("Parameter2", jComboBox4.getSelectedItem());
                        parameters.put("Parameter3", "C:\\Users\\Asi\\Documents\\E-Restro\\reports\\");
                        parameters.put("Parameter4", "C:\\Users\\Asi\\Documents\\E-Restro\\reports\\");
                        Connection dataSource = MySQL.getConnection();
                        JasperPrint jp = JasperFillManager.fillReport(stream, parameters, dataSource);
                        JasperViewer.viewReport(jp, false);
//                        Report

                        resetMain();
                        JOptionPane.showMessageDialog(this, "Meal Item Registered Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                    }
                } else {
                    ResultSet rcheck = MySQL.search("SELECT * FROM `meal_item` WHERE `id`<>'" + jLabel30.getText() + "' AND `name`='" + jTextField1.getText() + "' AND `meal_item_type_id` IN(SELECT `id` FROM `meal_item_type` WHERE `name`='" + jComboBox4.getSelectedItem() + "')");
                    if (rcheck.next()) {
                        JOptionPane.showMessageDialog(this, "This Meal Item Has Already Been Registered", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        ResultSet rmt = MySQL.search("Select * FROM `meal_item_type` WHERE `name`='" + jComboBox4.getSelectedItem() + "'");
                        rmt.next();
                        MySQL.iud("UPDATE `meal_item` SET `name`='" + jTextField1.getText() + "',`meal_item_type_id`='" + rmt.getString("id") + "' WHERE id='" + jLabel30.getText() + "'");
                        MySQL.iud("DELETE FROM `meal_item_has_inventory_item` WHERE `meal_item_id`='" + jLabel30.getText() + "'");
                        for (int j = 0; j < jTable1.getRowCount(); j++) {
                            ResultSet rinv_id = MySQL.search("Select * FROM `inventory_item` WHERE `id`='" + jTable1.getValueAt(j, 0) + "'");
                            rinv_id.next();
                            ResultSet rm = MySQL.search("SELECT * FROM `measurment_type` WHERE `name`='" + jTable1.getValueAt(j, 3) + "'");
                            rm.next();
                            Double qty = Double.parseDouble(jTable1.getValueAt(j, 2).toString()) * Double.parseDouble(rm.getString("containing_amount"));
                            MySQL.iud("INSERT INTO `meal_item_has_inventory_item`(`meal_item_id`,`inventory_item_id`,`quantity`,`measurment_type_id`) VALUES('" + jLabel30.getText() + "','" + rinv_id.getString("id") + "','" + qty + "','" + rm.getString("id") + "')");
                        }
                        MySQL.iud("DELETE FROM `meal_item_has_size` WHERE `meal_item_id`='" + jLabel30.getText() + "'");
                        for (int i = 0; i < jTable3.getRowCount(); i++) {
                            ResultSet rp = MySQL.search("SELECT * FROM `meal_item_size` WHERE `name`='" + jTable3.getValueAt(i, 0) + "'");
                            rp.next();
                            String[] s_cnt = jTable3.getValueAt(i, 2).toString().split(" ");
                            MySQL.iud("INSERT INTO `meal_item_has_size`(`meal_item_id`,`meal_item_size_id`,`price`,`size_of_normal`) VALUES('" + jLabel30.getText() + "','" + rp.getString("id") + "','" + jTable3.getValueAt(i, 1) + "','" + s_cnt[1] + "')");
                        }

//                        Report
                        InputStream stream = RegisterMealItem.class.getResourceAsStream("/com/rm/reports/meal_item_reg.jasper");
                        HashMap parameters = new HashMap();
//                        System.out.println(jTextField1.getText());
//                        System.out.println(jComboBox4.getSelectedItem());
                        parameters.put("Parameter1", jTextField1.getText());
                        parameters.put("Parameter2", jComboBox4.getSelectedItem());
                        parameters.put("Parameter3", "C:\\Users\\Asi\\Documents\\E-Restro\\reports\\");
                        Connection dataSource = MySQL.getConnection();
                        JasperPrint jp = JasperFillManager.fillReport(stream, parameters, dataSource);
                        JasperViewer.viewReport(jp, false);
//                        Report

                        resetMain();
                        JOptionPane.showMessageDialog(this, "Meal Item Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()) {
            jTextField6.setEditable(true);
            jTextField6.grabFocus();
        } else {
            jTextField6.setEditable(false);
        }
    }//GEN-LAST:event_jCheckBox1StateChanged

    private void jCheckBox2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox2StateChanged
        // TODO add your handling code here:
        if (jCheckBox2.isSelected()) {
            jTextField6.setText("1");
        }
    }//GEN-LAST:event_jCheckBox2StateChanged

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        int sr = jTable2.getSelectedRow();
        if (evt.getClickCount() == 3) {
            if (SignIn.user_type_id == 1 || SignIn.user_type_id == 2 || SignIn.user_type_id == 3) {
                if (jTable2.getValueAt(sr, 3).equals("Inactive")) {
                    MySQL.iud("UPDATE `meal_item` SET `status_id`='1' WHERE `id`='" + jTable2.getValueAt(sr, 0) + "'");
                } else {
                    MySQL.iud("UPDATE `meal_item` SET `status_id`='2' WHERE `id`='" + jTable2.getValueAt(sr, 0) + "'");
                }
                loadFoodItemTable();
            } else {
                JOptionPane.showMessageDialog(this, "Only Admin or A General Manager or A Kitchen Manager Can Activate or Deactivate a Meal Item", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (evt.getClickCount() == 2) {
            if (mm != null) {
                try {
                    mm.jLabel6.setText(jTable2.getValueAt(sr, 0).toString());
                    mm.jLabel10.setText(jTable2.getValueAt(sr, 1).toString());
                    String[] p_cnt = jTable2.getValueAt(sr, 2).toString().split(" ,");
                    DefaultTableModel dtm = (DefaultTableModel) mm.jTable1.getModel();
                    dtm.setRowCount(0);
                    for (int i = 0; i < p_cnt.length; i++) {
                        Vector v = new Vector();
                        String[] cnt = p_cnt[i].split("->");
                        v.add(cnt[0]);
                        v.add(cnt[1]);
                        dtm.addRow(v);
                    }
                    mm.jTable1.setModel(dtm);
                    this.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (evt.getClickCount() == 1) {
            try {
                jLabel29.setText("Meal Item Id :");
                jLabel30.setText(jTable2.getValueAt(sr, 0).toString());
                jTextField1.setText(jTable2.getValueAt(sr, 1).toString());
                jLabel30.setText(jTable2.getValueAt(sr, 0).toString());

                ResultSet rmt = MySQL.search("SELECT * FROM `meal_item` WHERE `meal_item`.`name`='" + jTable2.getValueAt(sr, 1) + "'");
                rmt.next();
                if (rmt.getString("meal_item_type_id").equals("1")) {
                    jComboBox4.setSelectedItem("Food Item");
                } else {
                    jComboBox4.setSelectedItem("Beverage Item");
                }
                DefaultTableModel dtm3 = (DefaultTableModel) jTable3.getModel();
                dtm3.setRowCount(0);
//                System.out.println("SELECT meal_item_size.name AS `por`,meal_item_has_size.size_of_normal AS `size`,meal_item_has_size.price AS `price` FROM meal_item_has_size INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id WHERE meal_item_has_size.meal_item_id='"+jTable2.getValueAt(sr, 1)+"'");
                ResultSet rms = MySQL.search("SELECT meal_item_size.name AS `por`,meal_item_has_size.size_of_normal AS `size`,meal_item_has_size.price AS `price` FROM meal_item_has_size INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id WHERE meal_item_has_size.meal_item_id='"+jTable2.getValueAt(sr, 0)+"'");
                while(rms.next()){
                    Vector v = new Vector();
                    v.add(rms.getString("por"));
                    v.add(rms.getString("price"));
                    v.add("X "+rms.getString("size"));
                    dtm3.addRow(v);
                }
                
                jTable3.setModel(dtm3);
                Double tot_amnt = 00.00;
//                System.out.println(tot_amnt);

                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);
                ResultSet rs = MySQL.search("SELECT inventory_item.id,inventory_item.name AS `in`,meal_item_has_inventory_item.quantity,measurment_type.name AS `mn`,ROUND(ROUND((SUM(grn_item.quantity * grn_item.buying_price)/stock.quantity),2)*meal_item_has_inventory_item.quantity,2) AS `amnt` FROM meal_item_has_inventory_item INNER JOIN inventory_item ON meal_item_has_inventory_item.inventory_item_id=inventory_item.id INNER JOIN measurment_type ON meal_item_has_inventory_item.measurment_type_id=measurment_type.id INNER JOIN stock ON stock.inventory_item_id=meal_item_has_inventory_item.inventory_item_id INNER JOIN grn_item ON grn_item.stock_id=stock.id WHERE meal_item_has_inventory_item.meal_item_id ='" + jTable2.getValueAt(sr, 0) + "' GROUP BY inventory_item.id");
                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("id"));
                    v.add(rs.getString("in"));
                    v.add(rs.getString("quantity"));
                    v.add(rs.getString("mn"));
                    tot_amnt += Double.parseDouble(rs.getString("amnt").toString());
                    v.add(rs.getString("amnt"));
                    dtm.addRow(v);
                }
                jTable1.setModel(dtm);
                jLabel10.setText(df.format(tot_amnt));
                c_total += tot_amnt;

                jButton3.setText("Update Meal Item");

            } catch (Exception ex) {
                Logger.getLogger(RegisterMealItem.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
        String qty = jTextField3.getText();
        System.out.println(qty);
        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]|0[.][1-9][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][1-9]|[1-9][0-9]*[.][0-9][1-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        loadSearchFoodItemTable();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (evt.getClickCount() == 2) {
            int option = JOptionPane.showConfirmDialog(this, "Do You Want to Remove this Item From the Meal Inventory Table?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                jLabel10.setText(df.format(c_total = c_total - Double.parseDouble(jTable1.getValueAt(selectedRow, 4).toString())));
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.removeRow(selectedRow);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable3.getSelectedRow();
        if (evt.getClickCount() == 2) {
            int option = JOptionPane.showConfirmDialog(this, "Do You Want to Remove this Portion From the Meal Portion Table?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
                dtm.removeRow(selectedRow);
            }
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        resetMain();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jLabel7.setText("None");
            jLabel17.setText("None");
            jLabel19.setText("None");
            jLabel21.setText("None");
            jLabel25.setText("00.00");
            jLabel23.setText("00.00");
            jTextField3.setText("");
            jComboBox3.setSelectedIndex(0);
            jButton1.setText("Select Inventory Item");
            jButton1.setEnabled(true);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
        /* Set the Nimbus look and feel */
        UIManager.setLookAndFeel(new FlatIntelliJLaf());
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterMealItem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    public javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    public javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    public javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    public javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    public javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
