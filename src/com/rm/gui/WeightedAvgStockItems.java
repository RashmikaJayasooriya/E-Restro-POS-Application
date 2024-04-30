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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Thilina
 */
public class WeightedAvgStockItems extends javax.swing.JDialog {

    DecimalFormat df = new DecimalFormat("00.00");
    RegisterMealItem rf;

    /**
     * Creates new form WeightedAvgStockItems
     */
    public WeightedAvgStockItems(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setIcon();
        loadTable();
        loadCategory();
        loadBrand();
    }

    public WeightedAvgStockItems(RegisterMealItem parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setIcon();
        loadTable();
        loadCategory();
        loadBrand();
        this.rf = parent;
    }

    public void setIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/rm/resources//restaurant.png")));
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

    public void loadTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs = MySQL.search("SELECT inventory_item.id,inventory_item.name,inventory_category.name AS `ct`,inventory_brand.name AS `br`,stock.quantity,measurment_type.name AS `mt`,measurment_type.`containing_amount`,measurment_type_category_id,ROUND(SUM(grn_item.quantity * grn_item.buying_price)/stock.quantity,2) AS `weighted_cost` FROM `inventory_item`  INNER JOIN stock ON stock.inventory_item_id=inventory_item.id INNER JOIN grn_item ON grn_item.stock_id=stock.id INNER JOIN inventory_category ON inventory_item.category_id=inventory_category.id INNER JOIN inventory_brand ON inventory_item.brand_id=inventory_brand.id INNER JOIN measurment_type ON grn_item.measurment_type=measurment_type.id GROUP BY inventory_item.id");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("ct"));
                v.add(rs.getString("br"));
                v.add(rs.getString("quantity"));
                if (rs.getString("measurment_type_category_id").equals("1")) {
                    v.add(rs.getString("mt") + "-" + rs.getString("containing_amount") + "g");
                } else if (rs.getString("measurment_type_category_id").equals("2")) {
                    v.add(rs.getString("mt") + "-" + rs.getString("containing_amount") + "ml");
                } else {
                    v.add(rs.getString("mt") + "-" + rs.getString("containing_amount") + "counts");
                }
//                System.out.println(Double.valueOf(rs.getString("tot_cost_per_item")));
//                System.out.println(Double.valueOf(rs.getString("quantity")));
//                System.out.println("________________________________________________");
//                double weighted_cost_price = Double.valueOf(rs.getString("tot_cost_per_item")) / Double.valueOf(rs.getString("quantity"));
                v.add(rs.getString("weighted_cost"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
        } catch (Exception ex) {
            Logger.getLogger(InventoryRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSearchTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            String bname = null;
            String cname = null;

            if (jComboBox1.getSelectedIndex() != 0) {
                cname = "'" + jComboBox1.getSelectedItem().toString() + "'";
            }
            if (jComboBox2.getSelectedIndex() != 0) {
                bname = "'" + jComboBox2.getSelectedItem().toString() + "'";
            }
            ResultSet rs = MySQL.search("SELECT inventory_item.id,inventory_item.name,inventory_category.name AS `ct`,inventory_brand.name AS `br`,stock.quantity,measurment_type.name AS `mt`,measurment_type.`containing_amount`,measurment_type_category_id,ROUND(SUM(grn_item.quantity * grn_item.buying_price)/stock.quantity,2) AS `weighted_cost` FROM `inventory_item`  INNER JOIN stock ON stock.inventory_item_id=inventory_item.id INNER JOIN grn_item ON grn_item.stock_id=stock.id INNER JOIN inventory_category ON inventory_item.category_id=inventory_category.id INNER JOIN inventory_brand ON inventory_item.brand_id=inventory_brand.id INNER JOIN measurment_type ON grn_item.measurment_type=measurment_type.id WHERE `inventory_item`.name LIKE '%" + jTextField1.getText() + "%' AND `inventory_category`.name= IFNULL(" + cname + ",`inventory_category`.name) AND `inventory_brand`.name= IFNULL(" + bname + ",`inventory_brand`.name) GROUP BY inventory_item.id");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("ct"));
                v.add(rs.getString("br"));
                v.add(rs.getString("quantity"));
                if (rs.getString("measurment_type_category_id").equals("1")) {
                    v.add(rs.getString("mt") + "-" + rs.getString("containing_amount") + "g");
                } else if (rs.getString("measurment_type_category_id").equals("2")) {
                    v.add(rs.getString("mt") + "-" + rs.getString("containing_amount") + "ml");
                } else {
                    v.add(rs.getString("mt") + "-" + rs.getString("containing_amount") + "counts");
                }

//                double weighted_cost_price = Double.valueOf(rs.getString("tot_cost_per_item")) / Double.valueOf(rs.getString("quantity"));
                v.add(rs.getString("weighted_cost"));
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
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
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
        jLabel1.setText("  Stock Inventoy Items");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel2.setText("Category :");

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jLabel3.setText("Name :");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 382, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.CardLayout());

        jTable1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "CATEGORY", "BRAND", "QUANTITY REMAINING", "MEASUREMENT", "WEIGHTED COST PRICE"
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

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int sr = jTable1.getSelectedRow();
        if (evt.getClickCount() == 2) {
            if (rf != null) {
                rf.jLabel7.setText(jTable1.getValueAt(sr, 0).toString());
                rf.jLabel17.setText(jTable1.getValueAt(sr, 1).toString());
                rf.jLabel19.setText(jTable1.getValueAt(sr, 2).toString());
                rf.jLabel21.setText(jTable1.getValueAt(sr, 3).toString());
                rf.jLabel25.setText(jTable1.getValueAt(sr, 6).toString());
                rf.jButton1.setText("Update Culinary Item");
                rf.jButton1.setEnabled(false);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadSearchTable();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        loadSearchTable();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        loadSearchTable();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                WeightedAvgStockItems dialog = new WeightedAvgStockItems(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    // End of variables declaration//GEN-END:variables
}
