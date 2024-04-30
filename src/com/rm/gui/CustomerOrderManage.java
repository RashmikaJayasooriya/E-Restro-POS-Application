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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
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
public class CustomerOrderManage extends javax.swing.JFrame {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:M:s");
    static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:MM:ss");
    String dt;
    Double total = 0.0;
    String m_name = null;

    /**
     * Creates new form OrderManage
     */
    public CustomerOrderManage() {
        initComponents();
        setIcon();
        loadFoodItemTable();
        loadOrderId();
        loadOrderTable();
        loadMealCategory();
        loadMealPortion();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Date date = new Date();
                    dt = sdf2.format(date);
//                    System.out.println(dt);
//                    System.out.println("SELECT * FROM menu WHERE '" + dt + "' BETWEEN menu.time_from AND menu.time_until AND menu.status_id='1'");
                    ResultSet rct = MySQL.search("SELECT * FROM menu WHERE '" + dt + "' BETWEEN menu.time_from AND menu.time_until AND menu.status_id='1'");
                    if (rct.next()) {
                        jLabel3.setText(dt);
                        jLabel2.setText(rct.getString("name"));
                        if (!rct.getString("name").equals(m_name)) {
                            m_name = rct.getString("name");
                            loadFoodItemTable();
                        }
                    } else {
                        jLabel2.setText("No Menu Item Activated To This Time");
                        jLabel3.setText(dt);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CustomerOrderManage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources/restaurant.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFoodItemTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT meal_item.id,meal_item.name,menu_category.name,GROUP_CONCAT(DISTINCT meal_item_size.name ,\" -> \",meal_item_has_size.price,\" \") AS `pap`,GROUP_CONCAT(DISTINCT inventory_item.name) AS `in`,`meal_item_type`.name AS `mtn` FROM meal_item_has_size INNER JOIN meal_item ON meal_item_has_size.meal_item_id=meal_item.id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_inventory_item ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN inventory_item ON meal_item_has_inventory_item.inventory_item_id=inventory_item.id INNER JOIN menu_has_meal_item ON meal_item.id=menu_has_meal_item.meal_item_id INNER JOIN menu_category ON menu_has_meal_item.menu_category_id=menu_category.id INNER JOIN menu ON menu_has_meal_item.menu_id=menu.id INNER JOIN meal_item_type ON meal_item.meal_item_type_id=meal_item_type.id WHERE '" + dt + "' BETWEEN menu.time_from AND menu.time_until AND menu.status_id='1' AND meal_item.status_id='1' GROUP BY meal_item.id order BY menu_category.name AND meal_item_type.id ASC");
            while (rs.next()) {

                String[] pp_cnt = rs.getString("pap").toString().split(" ,");
                String new_pp_cnt = "";
                for (int i = 0; i < pp_cnt.length; i++) {
                    String[] p_cnt = pp_cnt[i].split(" -> ");
                    ResultSet rc1 = MySQL.search("SELECT COUNT(*) AS c1 FROM meal_item_has_inventory_item INNER JOIN meal_item_has_size ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN stock ON meal_item_has_inventory_item.inventory_item_id=stock.inventory_item_id WHERE meal_item_has_inventory_item.meal_item_id='" + rs.getString("id") + "' AND meal_item_size.name='" + p_cnt[0] + "'");
                    rc1.next();
                    ResultSet rc2 = MySQL.search("SELECT COUNT(*) AS c2 FROM meal_item_has_inventory_item INNER JOIN meal_item_has_size ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN stock ON meal_item_has_inventory_item.inventory_item_id=stock.inventory_item_id WHERE meal_item_has_inventory_item.meal_item_id='" + rs.getString("id") + "' AND meal_item_size.name='" + p_cnt[0] + "' AND stock.quantity > (meal_item_has_inventory_item.quantity * meal_item_has_size.size_of_normal)");
                    rc2.next();
                    if (Integer.parseInt(rc1.getString("c1")) == Integer.parseInt(rc2.getString("c2"))) {
                        new_pp_cnt = new_pp_cnt.concat(pp_cnt[i]);
                        if (i < (pp_cnt.length - 1)) {
                            new_pp_cnt = new_pp_cnt.concat(",");
                        }
                    }
                }
                if (new_pp_cnt != null) {
                    Vector v = new Vector();
                    v.add(rs.getString("meal_item.id"));
                    v.add(rs.getString("meal_item.name"));
                    v.add(rs.getString("menu_category.name"));
                    v.add(new_pp_cnt);
                    v.add(rs.getString("in"));
                    v.add(rs.getString("mtn"));
                    dtm.addRow(v);
                }

            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchFoodItemTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            String mid = "";
            String mname = "";
            String mcname = null;
            String mportion = null;

            if (!jTextField2.getText().isEmpty()) {
                mid = jTextField2.getText();
            }
            if (!jTextField1.getText().isEmpty()) {
                mname = jTextField1.getText();
            }
            if (jComboBox4.getSelectedIndex() != 0) {
                mcname = "'" + jComboBox4.getSelectedItem().toString() + "'";
            }
            if (jComboBox5.getSelectedIndex() != 0) {
                mportion = "'" + jComboBox5.getSelectedItem().toString() + "'";
            }

            Vector qv = new Vector();
            if (!jTextField3.getText().isEmpty()) {
                qv.add("`meal_item_has_size`.price >= '" + jTextField3.getText() + "'");
            }
            if (!jTextField4.getText().isEmpty()) {
                qv.add("`meal_item_has_size`.price <= '" + jTextField4.getText() + "'");
            }

            String aq = " ";
            for (int i = 0; i < qv.size(); i++) {
                aq += "AND ";
                aq += qv.get(i);
                aq += " ";
            }

//            System.out.println("SELECT meal_item.id,meal_item.name,menu_category.name,GROUP_CONCAT(DISTINCT meal_item_size.name ,\" -> \",meal_item_has_size.price,\" \") AS `pap`,GROUP_CONCAT(DISTINCT inventory_item.name) AS `in`,`meal_item_type`.name AS `mtn` FROM meal_item_has_size INNER JOIN meal_item ON meal_item_has_size.meal_item_id=meal_item.id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_inventory_item ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN inventory_item ON meal_item_has_inventory_item.inventory_item_id=inventory_item.id INNER JOIN menu_has_meal_item ON meal_item.id=menu_has_meal_item.meal_item_id INNER JOIN menu_category ON menu_has_meal_item.menu_category_id=menu_category.id INNER JOIN menu ON menu_has_meal_item.menu_id=menu.id INNER JOIN meal_item_type ON meal_item.meal_item_type_id=meal_item_type.id WHERE '" + dt + "' BETWEEN menu.time_from AND menu.time_until AND menu.status_id=\"1\" AND `meal_item`.id LIKE '%" + mid + "%' AND `meal_item`.name LIKE '%" + mname + "%' AND `menu_category`.name=IFNULL(" + mcname + ",`menu_category`.name) AND `meal_item_size`.name=IFNULL(" + mportion + ",`meal_item_size`.name) "+aq+" GROUP BY meal_item.id order BY meal_item.name ASC");
            ResultSet rs = MySQL.search("SELECT meal_item.id,meal_item.name,menu_category.name,GROUP_CONCAT(DISTINCT meal_item_size.name ,\" -> \",meal_item_has_size.price,\" \") AS `pap`,GROUP_CONCAT(DISTINCT inventory_item.name) AS `in`,`meal_item_type`.name AS `mtn` FROM meal_item_has_size INNER JOIN meal_item ON meal_item_has_size.meal_item_id=meal_item.id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_inventory_item ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN inventory_item ON meal_item_has_inventory_item.inventory_item_id=inventory_item.id INNER JOIN menu_has_meal_item ON meal_item.id=menu_has_meal_item.meal_item_id INNER JOIN menu_category ON menu_has_meal_item.menu_category_id=menu_category.id INNER JOIN menu ON menu_has_meal_item.menu_id=menu.id INNER JOIN meal_item_type ON meal_item.meal_item_type_id=meal_item_type.id WHERE '" + dt + "' BETWEEN menu.time_from AND menu.time_until AND menu.status_id='1' AND meal_item.status_id='1' AND `meal_item`.id LIKE '%" + mid + "%' AND `meal_item`.name LIKE '%" + mname + "%' AND `meal_item_type`.name=IFNULL(" + mcname + ",`meal_item_type`.name) AND `meal_item_size`.name=IFNULL(" + mportion + ",`meal_item_size`.name) " + aq + " GROUP BY meal_item.id order BY meal_item.name ASC");
            while (rs.next()) {

                String[] pp_cnt = rs.getString("pap").toString().split(" ,");
                String new_pp_cnt = "";
                for (int i = 0; i < pp_cnt.length; i++) {
                    String[] p_cnt = pp_cnt[i].split(" -> ");
                    ResultSet rc1 = MySQL.search("SELECT COUNT(*) AS c1 FROM meal_item_has_inventory_item INNER JOIN meal_item_has_size ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN stock ON meal_item_has_inventory_item.inventory_item_id=stock.inventory_item_id WHERE meal_item_has_inventory_item.meal_item_id='" + rs.getString("id") + "' AND meal_item_size.name='" + p_cnt[0] + "'");
                    rc1.next();
                    ResultSet rc2 = MySQL.search("SELECT COUNT(*) AS c2 FROM meal_item_has_inventory_item INNER JOIN meal_item_has_size ON meal_item_has_inventory_item.meal_item_id=meal_item_has_size.meal_item_id INNER JOIN meal_item_size ON meal_item_has_size.meal_item_size_id=meal_item_size.id INNER JOIN stock ON meal_item_has_inventory_item.inventory_item_id=stock.inventory_item_id WHERE meal_item_has_inventory_item.meal_item_id='" + rs.getString("id") + "' AND meal_item_size.name='" + p_cnt[0] + "' AND stock.quantity > (meal_item_has_inventory_item.quantity * meal_item_has_size.size_of_normal)");
                    rc2.next();
                    if (Integer.parseInt(rc1.getString("c1")) == Integer.parseInt(rc2.getString("c2"))) {
                        new_pp_cnt = new_pp_cnt.concat(pp_cnt[i]);
                        if (i < (pp_cnt.length - 1)) {
                            new_pp_cnt = new_pp_cnt.concat(",");
                        }
                    }
                }
                if (new_pp_cnt != null) {
                    Vector v = new Vector();
                    v.add(rs.getString("meal_item.id"));
                    v.add(rs.getString("meal_item.name"));
                    v.add(rs.getString("menu_category.name"));
                    v.add(new_pp_cnt);
                    v.add(rs.getString("in"));
                    v.add(rs.getString("mtn"));
                    dtm.addRow(v);
                }

            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadOrderId() {
        try {
            Date date = new Date();
            ResultSet rs = MySQL.search("SELECT * FROM `order` WHERE `date_time`='" + sdf.format(date) + "' ORDER BY `id` ASC");
            Vector v = new Vector();
            v.add("Select Order Id");
            while (rs.next()) {
                v.add(rs.getString("id"));
            }
            jComboBox1.setModel(new DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadMealCategory() {
        try {
            Date date = new Date();
            ResultSet rs = MySQL.search("SELECT * FROM `meal_item_type` ORDER BY `id` ASC");
            Vector v = new Vector();
            v.add("Select Meal Item Type");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox4.setModel(new DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadMealPortion() {
        try {
            Date date = new Date();
            ResultSet rs = MySQL.search("SELECT * FROM `meal_item_size` ORDER BY `id` ASC");
            Vector v = new Vector();
            v.add("Select Meal Item Size");
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            jComboBox5.setModel(new DefaultComboBoxModel(v));
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadOrderTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);
            Date date = new Date();
            ResultSet rs = MySQL.search("SELECT `order`.id,customer.name,`order`.date_time,GROUP_CONCAT(meal_item.name,\"-> \",meal_item_size.name,\"(x\",order_has_meal_item.quantity,\")\") AS `npq`,SUM( meal_item_has_size.price) AS `price` FROM `order` INNER JOIN order_has_meal_item ON order_has_meal_item.order_id=`order`.id INNER JOIN meal_item ON order_has_meal_item.meal_item_id=meal_item.id INNER JOIN meal_item_size ON order_has_meal_item.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_size ON meal_item_size.id=meal_item_has_size.meal_item_size_id AND meal_item.id=meal_item_has_size.meal_item_id INNER JOIN customer ON `order`.customer_id=customer.id GROUP BY `order`.id order BY `order`.date_time ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("order.id"));
                v.add(rs.getString("customer.name"));
                v.add(rs.getString("order.date_time"));
                v.add(rs.getString("npq"));
                v.add(rs.getString("price"));
                dtm.addRow(v);
            }
            jTable3.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchOrderTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);
            Date date = new Date();
            String orderId = null;
            String customerName = null;
            if (jComboBox1.getSelectedIndex() == 0) {
                orderId = "'" + jComboBox1.getSelectedItem().toString() + "'";
            }
            if (jTextField5.getText().isEmpty()) {
                customerName = jTextField5.getText();
            }
            ResultSet rs = MySQL.search("SELECT `order`.id,customer.name,`order`.date_time,GROUP_CONCAT(meal_item.name,\"-> \",meal_item_size.name,\"(x\",order_has_meal_item.quantity,\")\") AS `npq`,SUM( meal_item_has_size.price) AS `price` FROM `order` INNER JOIN order_has_meal_item ON order_has_meal_item.order_id=`order`.id INNER JOIN meal_item ON order_has_meal_item.meal_item_id=meal_item.id INNER JOIN meal_item_size ON order_has_meal_item.meal_item_size_id=meal_item_size.id INNER JOIN meal_item_has_size ON meal_item_size.id=meal_item_has_size.meal_item_size_id AND meal_item.id=meal_item_has_size.meal_item_id INNER JOIN customer ON `order`.customer_id=customer.id WHERE `order`.id=IFNULL(" + orderId + ",`order`.`id`) AND `customer`.name LIKE '%" + customerName + "%' GROUP BY `order`.id order BY `order`.date_time ASC");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("order.id"));
                v.add(rs.getString("customer.name"));
                v.add(rs.getString("order.date_time"));
                v.add(rs.getString("npq"));
                v.add(rs.getString("price"));
                dtm.addRow(v);
            }
            jTable3.setModel(dtm);
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

        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("E-Restro 1.0");
        getContentPane().setLayout(new java.awt.CardLayout());

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 102, 255));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Miriam Libre", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Customer Orders");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(1669, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setBackground(new java.awt.Color(0, 102, 255));
        jLabel12.setFont(new java.awt.Font("Miriam Libre", 0, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("  Today Orders");
        jLabel12.setOpaque(true);

        jTable3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ORDER ID", "CUSTOMER NO", "CUSTOMER NAME", "DATE TIME", "MEAL ITEMS", "PRICE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jLabel15.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel15.setText("Order Id :");

        jComboBox1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel16.setText("Customer Name :");

        jTextField5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jButton4.setText("Customer Order History");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1326, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1326, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(472, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(246, 241, 241));

        jPanel8.setLayout(new java.awt.CardLayout());

        jTable2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "QTY", "PORTION", "TOTAL"
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

        jPanel8.add(jScrollPane2, "card2");

        jLabel2.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jLabel2.setText("None");

        jLabel3.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jLabel3.setText("00:00 PM");

        jLabel4.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 14)); // NOI18N
        jLabel4.setText("Total");

        jLabel5.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("00.00");

        jButton1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 255));
        jButton1.setText("Send Order");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 51, 51));
        jButton2.setText("Cancel Order");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Noto Sans Light", 0, 12)); // NOI18N
        jLabel18.setText("Select Customer :");

        jButton3.setFont(new java.awt.Font("Noto Sans Light", 0, 12)); // NOI18N
        jButton3.setText("Select");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Noto Sans Light", 0, 12)); // NOI18N
        jLabel17.setText("Id :");

        jLabel19.setFont(new java.awt.Font("Noto Sans Light", 0, 12)); // NOI18N
        jLabel19.setText("1");

        jLabel20.setFont(new java.awt.Font("Noto Sans Light", 0, 12)); // NOI18N
        jLabel20.setText("Default");

        jLabel21.setFont(new java.awt.Font("Noto Sans Light", 0, 12)); // NOI18N
        jLabel21.setText("Name :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(jLabel21))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(jLabel17))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jButton3)))
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );

        jPanel5.add(jPanel6, java.awt.BorderLayout.LINE_START);

        jPanel7.setBackground(new java.awt.Color(249, 254, 249));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Noto Sans Cond", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Food Menu");
        jLabel6.setOpaque(true);

        jPanel9.setLayout(new java.awt.CardLayout());

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel7.setText("Name :");

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel13.setText("Meal Type : ");

        jComboBox4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel8.setText("ID :");

        jTextField2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel9.setText("From :");

        jTextField3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel10.setText("Price :");

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel11.setText("To :");

        jTextField4.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel14.setText("Portion :");

        jComboBox5.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "CATEGORY", "PORTION AND PRICE", "INCLUDES", "TYPE"
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(30);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(30);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(19);
        }

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox5, 0, 156, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jScrollPane3.setViewportView(jPanel2);

        getContentPane().add(jScrollPane3, "card2");

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

        setSize(new java.awt.Dimension(1366, 708));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int sr = jTable1.getSelectedRow();
        if (evt.getClickCount() == 2) {
            GetCustomerOrderDetails customerOrderDetails = new GetCustomerOrderDetails(this, true);
            customerOrderDetails.jLabel3.setText(jTable1.getValueAt(sr, 0).toString());
            customerOrderDetails.jLabel4.setText(jTable1.getValueAt(sr, 1).toString());
            String[] pc_cnt = jTable1.getValueAt(sr, 3).toString().split(",");
            Vector v = new Vector();
            v.add("Select Portion");
//            System.out.println(pc_cnt.length);
            for (int i = 0; i < pc_cnt.length; i++) {
                v.add(pc_cnt[i]);
            }
            customerOrderDetails.jComboBox1.setModel(new DefaultComboBoxModel(v));
            customerOrderDetails.setVisible(true);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        CustomerRegistration customerRegistration = new CustomerRegistration(this);
        customerRegistration.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jTable2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Items to Order", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                ResultSet rt = MySQL.search("SELECT * FROM `customer` WHERE `id`='" + jLabel19.getText() + "'");
                rt.next();
                Date date = new Date();
                MySQL.iud("INSERT INTO `order`(`date_time`,`customer_id`) Values('" + sdf.format(date) + "','" + rt.getString("id") + "')");
                ResultSet ro = MySQL.search("SELECT * FROM `order` WHERE `date_time`='" + sdf.format(date) + "' AND `customer_id`='" + rt.getString("id") + "'");
                ro.next();
                for (int i = 0; i < jTable2.getRowCount(); i++) {
                    ResultSet rfp = MySQL.search("SELECT * FROM `meal_item_size` WHERE `name`='" + jTable2.getValueAt(i, 3) + "'");
                    rfp.next();
                    MySQL.iud("INSERT INTO `order_has_meal_item`(`order_id`,`meal_item_id`,`meal_item_size_id`,`quantity`) VALUES('" + ro.getString("id") + "','" + jTable2.getValueAt(i, 0) + "','" + rfp.getString("id") + "','" + jTable2.getValueAt(i, 2) + "')");

//                    System.out.println("SELECT COUNT(*) AS `count` FROM `meal_item_has_inventory_item` WHERE `meal_item_id`='" + jTable2.getValueAt(i, 0) + "'");
                    ResultSet rmic = MySQL.search("SELECT COUNT(*) AS `count` FROM `meal_item_has_inventory_item` WHERE `meal_item_id`='" + jTable2.getValueAt(i, 0) + "'");
                    rmic.next();
//                    System.out.println("SELECT * FROM `meal_item_has_inventory_item` INNER JOIN `stock` ON meal_item_has_inventory_item.inventory_item_id=stock.inventory_item_id WHERE `meal_item_id`='" + jTable2.getValueAt(i, 0) + "'");
                    ResultSet rmi = MySQL.search("SELECT * FROM `meal_item_has_inventory_item` INNER JOIN `stock` ON meal_item_has_inventory_item.inventory_item_id=stock.inventory_item_id WHERE `meal_item_id`='" + jTable2.getValueAt(i, 0) + "'");

                    for (int j = 0; j < Integer.parseInt(rmic.getString("count")); j++) {
                        rmi.next();
//                        System.out.println(Double.parseDouble(rmi.getString("stock.quantity")) - Double.parseDouble(rmi.getString("meal_item_has_inventory_item.quantity")));
                        double new_qty = Double.parseDouble(rmi.getString("stock.quantity")) - Double.parseDouble(rmi.getString("meal_item_has_inventory_item.quantity"));
//                        System.out.println("UPDATE `stock` SET `quantity`='" + new_qty + "' WHERE `inventory_item_id`='" + rmi.getString("meal_item_has_inventory_item.inventory_item_id") + "'");
                        MySQL.iud("UPDATE `stock` SET `quantity`='" + new_qty + "' WHERE `inventory_item_id`='" + rmi.getString("meal_item_has_inventory_item.inventory_item_id") + "'");
                    }
                }

                invoice invoice = new invoice(this, true);
                invoice.jLabel3.setText(ro.getString("id"));
                invoice.jLabel5.setText(rt.getString("id"));
                SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd H:mm:s");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:s");

//                System.out.println("SELECT * FROM `menu` WHERE '" + sdf.format(date) + "' BETWEEN `time_from` AND `time_until`");
                ResultSet rtc = MySQL.search("SELECT * FROM `menu` WHERE '" + sdf.format(date) + "' BETWEEN `time_from` AND `time_until`");
                rtc.next();
                invoice.jLabel9.setText(rtc.getString("name"));
                invoice.jLabel13.setText(sdfd.format(date));
                invoice.jLabel11.setText(jLabel5.getText());

                DefaultTableModel dtm = (DefaultTableModel) invoice.jTable1.getModel();
                dtm.setRowCount(0);
                for (int i = 0; i < jTable2.getRowCount(); i++) {

                    Vector v = new Vector();
                    v.add(jTable2.getValueAt(i, 1));
                    v.add(jTable2.getValueAt(i, 3));
                    v.add(jTable2.getValueAt(i, 2));
                    Double price = Double.parseDouble(jTable2.getValueAt(i, 4).toString()) / Double.parseDouble(jTable2.getValueAt(i, 2).toString());
                    v.add(price);
                    dtm.addRow(v);

                }
                invoice.jTable1.setModel(dtm);

                invoice.setVisible(true);

                

//Report
                    InputStream stream = PurchaseOrder.class.getResourceAsStream("/com/rm/reports/kitchen_ticket.jrxml");
                    JasperReport jr = JasperCompileManager.compileReport(stream);
                    HashMap parameters = new HashMap();
                    parameters.put("Parameter1", sdf.format(date));
                    parameters.put("Parameter2", SignIn.user_id);
                    parameters.put("Parameter3", ro.getString("id"));

                    Connection dataSource = MySQL.getConnection();

                    JasperPrint jp = JasperFillManager.fillReport(jr, parameters, dataSource);
                    JasperViewer.viewReport(jp, false);
//Report

                    jLabel5.setText("00.00");
                    total=00.00;
                    DefaultTableModel dtm2 = (DefaultTableModel) jTable2.getModel();
                    dtm2.setRowCount(0);
                    loadOrderTable();
                    JOptionPane.showMessageDialog(this, "Order Send Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                

            } catch (Exception ex) {
                Logger.getLogger(CustomerOrderManage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        dtm.setRowCount(0);
        jLabel19.setText("1");
        jLabel20.setText("Default");
        jLabel5.setText("00.00");
        total=00.00;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        int sr = jTable2.getSelectedRow();

        if (evt.getClickCount() == 2) {
            jLabel5.setText(String.valueOf(total - Double.parseDouble(jTable2.getValueAt(sr, 4).toString())));
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.removeRow(sr);
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        loadSearchOrderTable();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        loadSearchOrderTable();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        loadSearchFoodItemTable();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadSearchFoodItemTable();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
        loadSearchFoodItemTable();
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        loadSearchFoodItemTable();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        loadSearchFoodItemTable();
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
        loadSearchFoodItemTable();
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        OrderManage orderManage = new OrderManage();
        orderManage.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        dtm.setRowCount(0);
        jLabel19.setText("1");
        jLabel20.setText("Default");
        jLabel5.setText("00.00");
        total=00.00;
        loadFoodItemTable();
        loadMealCategory();
        loadMealPortion();
        loadOrderTable();

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2KeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the Nimbus look and feel */
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
//</editor-fold>
//</editor-fold>
//</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new CustomerOrderManage().setVisible(true);
                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(CustomerOrderManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    public javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    public javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
