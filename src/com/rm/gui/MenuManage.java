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
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Thilina
 */
public class MenuManage extends javax.swing.JFrame {

    /**
     * Creates new form MenuManage
     */
    public MenuManage() {
        initComponents();
        setIcon();
        loadMenu();
        loadMenuCategory();
        loadMenuTable();
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources/restaurant.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMenu() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `menu` ORDER BY `name` ASC");
            Vector v = new Vector();
            v.add("Select Menu");
            while (rs.next()) {
                v.add(rs.getString("name") + " " + rs.getString("time_from") + "-" + rs.getString("time_until"));
            }
            jComboBox1.setModel(new DefaultComboBoxModel(v));
            jComboBox3.setModel(new DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadMenuCategory() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `menu_category` ORDER BY `name` ASC");
            Vector v = new Vector();
            v.add("Select Menu Category");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox2.setModel(new DefaultComboBoxModel(v));
            jComboBox4.setModel(new DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadMenuTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT * FROM menu INNER JOIN menu_has_meal_item ON menu_has_meal_item.menu_id=menu.id INNER JOIN menu_category ON menu_has_meal_item.menu_category_id=menu_category.id INNER JOIN meal_item ON menu_has_meal_item.meal_item_id=meal_item.id ORDER BY menu.name ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("menu.time_from") + "-" + rs.getString("menu.time_until"));
                v.add(rs.getString("menu.name"));
                v.add(rs.getString("menu_category.name"));
                v.add(rs.getString("meal_item.name"));
                dtm.addRow(v);
            }
            jTable2.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(MenuRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchMenuTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);
            String tf = null;
            String tu = null;
            String mc = null;
            if (jComboBox3.getSelectedIndex() != 0) {
                String[] ttcnt = jComboBox3.getSelectedItem().toString().split(" ");
                String[] tcnt = ttcnt[1].split("-");
                tf = "'" + tcnt[0] + "'";
                tu = "'" + tcnt[1] + "'";
            }
            if (jComboBox4.getSelectedIndex() != 0) {
                mc = "'" + jComboBox4.getSelectedItem().toString() + "'";
            }
//            System.out.println("SELECT * FROM menu INNER JOIN menu_has_meal_item ON menu_has_meal_item.menu_id=menu.id INNER JOIN menu_category ON menu_has_meal_item.menu_category_id=menu_category.id INNER JOIN meal_item ON menu_has_meal_item.meal_item_id=meal_item.id WHERE `menu`.time_from=IFNULL(" + tf + ",`menu`.time_from) AND `menu`.time_until=IFNULL(" + tu + ",`menu`.time_until) AND `menu_category`.name=IFNULL(" + mc + ",`menu_category`.name) ORDER BY menu.name ASC");
            ResultSet rs = MySQL.search("SELECT * FROM menu INNER JOIN menu_has_meal_item ON menu_has_meal_item.menu_id=menu.id INNER JOIN menu_category ON menu_has_meal_item.menu_category_id=menu_category.id INNER JOIN meal_item ON menu_has_meal_item.meal_item_id=meal_item.id WHERE `menu`.time_from=IFNULL(" + tf + ",`menu`.time_from) AND `menu`.time_until=IFNULL(" + tu + ",`menu`.time_until) AND `menu_category`.name=IFNULL(" + mc + ",`menu_category`.name) ORDER BY menu.name ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("menu.time_from") + "-" + rs.getString("menu.time_until"));
                v.add(rs.getString("menu.name"));
                v.add(rs.getString("menu_category.name"));
                v.add(rs.getString("meal_item.name"));
                dtm.addRow(v);
            }
            jTable2.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(MenuRegistration.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("E-Restro 1.0");
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jButton4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 102, 255));
        jButton4.setText("Add Menu");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jButton3.setText("Select Item");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel5.setText("Id :");

        jLabel1.setBackground(new java.awt.Color(0, 102, 255));
        jLabel1.setFont(new java.awt.Font("Miriam Libre", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  Menu Item Details");
        jLabel1.setOpaque(true);

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel6.setText("None");

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel2.setText("Menu Time : ");

        jComboBox1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 255));
        jButton1.setText("+");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel3.setText("Menu Category : ");

        jComboBox2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

        jButton2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 255));
        jButton2.setText("+");
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
                "PORTION", "PRICE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel4.setText("Item Details :");

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel9.setText("Name :");

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel10.setText("None");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(28, 28, 28)
                                    .addComponent(jLabel6))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jComboBox2, 0, 140, Short.MAX_VALUE)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addGap(7, 7, 7)
                                            .addComponent(jButton1))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButton2))))
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(48, 48, 48)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jLabel4))
                .addGap(13, 13, 13)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel5, java.awt.BorderLayout.LINE_START);

        jLabel11.setBackground(new java.awt.Color(0, 102, 255));
        jLabel11.setFont(new java.awt.Font("Miriam Libre", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("  Search Menus");
        jLabel11.setOpaque(true);

        jComboBox3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel12.setText("Menu Time : ");

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel13.setText("Menu Category :");

        jComboBox4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jPanel7.setLayout(new java.awt.CardLayout());

        jTable2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TIME", "MENU NAME", "MENU CATEGORY", "ITEM NAME"
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

        jPanel7.add(jScrollPane2, "card2");

        jButton5.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 102, 255));
        jButton5.setText("Print Menu");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel11)
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel4, "card2");

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

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MenuRegistration menuRegistration = new MenuRegistration(this, true);
        menuRegistration.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
        loadSearchMenuTable();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        MenuCategoryRegistration menuCategoryRegistration = new MenuCategoryRegistration(this, true);
        menuCategoryRegistration.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        RegisterMealItem registerFoodItem = new RegisterMealItem(this);
        registerFoodItem.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Meal Time", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Meal Category", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jLabel6.getText().equals("None")) {
            JOptionPane.showMessageDialog(this, "Please Select a Meal Item", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                String[] m = jComboBox1.getSelectedItem().toString().split(" ");
                String[] t = m[1].split("-");
                ResultSet rm = MySQL.search("SELECT * FROM `menu` WHERE `name`='" + m[0] + "' AND `time_from`='" + t[0] + "' AND `time_until`='" + t[1] + "'");
                rm.next();
                ResultSet rmc = MySQL.search("SELECT * FROM `menu_category` WHERE `name`='" + jComboBox2.getSelectedItem() + "'");
                rmc.next();
                ResultSet rcheck = MySQL.search("SELECT * FROM `menu_has_meal_item` WHERE `menu_id`='" + rm.getString("id") + "' AND `meal_item_id`='" + jLabel6.getText() + "' AND `menu_category_id`='" + rmc.getString("id") + "'");
                if (rcheck.next()) {
                    JOptionPane.showMessageDialog(this, "Meal Item Already Added to the Same Menu With Same Category", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    MySQL.iud("INSERT INTO `menu_has_meal_item`(`menu_id`,`meal_item_id`,`menu_category_id`) VALUES('" + rm.getString("id") + "','" + jLabel6.getText() + "','" + rmc.getString("id") + "')");
                    jComboBox1.setSelectedIndex(0);
                    jComboBox2.setSelectedIndex(0);
                    jLabel6.setText("None");
                    jLabel10.setText("None");
                    DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                    dtm.setRowCount(0);
                    jComboBox3.setSelectedIndex(0);
                    jComboBox4.setSelectedIndex(0);
                    JOptionPane.showMessageDialog(this, "Meal Item Added to Menu Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                Logger.getLogger(MenuManage.class.getName()).log(Level.SEVERE, null, ex);
            }

            loadMenuTable();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
        loadSearchMenuTable();
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            // TODO add your handling code here:
//                Reports
            InputStream stream1 = MenuManage.class.getResourceAsStream("/com/rm/reports/menu.jasper");
//            InputStream stream22 = MenuManage.class.getResourceAsStream("../reports/menu_category_sub.jasper");
//            InputStream stream3 = MenuManage.class.getResourceAsStream("../reports/menu_category_meal_items.jasper");

            HashMap parameters = new HashMap();
            parameters.put("Parameter1", "C:\\Users\\Asi\\Documents\\E-Restro\\reports\\");
            parameters.put("Parameter2", "C:\\Users\\Asi\\Documents\\E-Restro\\reports\\");

            Connection dataSource = MySQL.getConnection();

            JasperPrint jp1 = JasperFillManager.fillReport(stream1, parameters, dataSource);
            JasperViewer.viewReport(jp1, false);
//                Reports
        } catch (JRException ex) {
            Logger.getLogger(MenuManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        int sr = jTable2.getSelectedRow();

        if (evt.getClickCount() == 2) {
            if (SignIn.user_type_id == 1 || SignIn.user_type_id == 2 || SignIn.user_type_id == 3 || SignIn.user_type_id == 4) {
                MySQL.iud("DELETE FROM `menu_has_meal_item` WHERE `menu_id` IN(SELECT `id` FROM `menu` WHERE `name`='" + jTable2.getValueAt(sr, 1) + "') AND `menu_category_id` IN(SELECT `id` FROM `menu_category` WHERE `name`='" + jTable2.getValueAt(sr, 2) + "') AND `meal_item_id` IN(SELECT `id` FROM `meal_item` WHERE `name`='" + jTable2.getValueAt(sr, 3) + "') ");
                JOptionPane.showMessageDialog(this, "Meal Item Remove From Menu Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                jComboBox3.setSelectedIndex(0);
                jComboBox4.setSelectedIndex(0);
                loadMenuTable();
            } else {
                JOptionPane.showMessageDialog(this, "Only Admin or A General Manager or Assistant Manager or Kitchen Manager Can Delete a Menu Meal Item", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jLabel6.setText("None");
        jLabel10.setText("None");
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);
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
                new MenuManage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
