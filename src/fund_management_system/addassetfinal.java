/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package fund_management_system;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.*;
/**
 *
 * @author Aryan
 */
/*
public class addassetfinal extends javax.swing.JFrame {

    /**
     * Creates new form addassetfinal
     */
/*
    public addassetfinal() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
/*
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pricefield = new javax.swing.JTextField();
        quantityfield = new javax.swing.JTextField();
        symbolfield = new javax.swing.JTextField();
        namefield = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");

        pricefield.setText("jTextField1");

        quantityfield.setText("jTextField2");

        symbolfield.setText("jTextField3");

        namefield.setText("jTextField4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(symbolfield, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quantityfield, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(66, 66, 66)
                                .addComponent(pricefield, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(namefield, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(156, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(namefield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pricefield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(quantityfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(symbolfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
/*
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(addassetfinal.

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addassetfinal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addassetfinal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addassetfinal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addassetfinal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField namefield;
    private javax.swing.JTextField pricefield;
    private javax.swing.JTextField quantityfield;
    private javax.swing.JTextField symbolfield;
    // End of variables declaration//GEN-END:variables
}*/


public class addassetfinal extends JFrame {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField searchField; // Change access to public
    public JList<String> suggestionList;
    private DefaultListModel<String> listModel;

    public addassetfinal() {
        getContentPane().setBackground(new Color(240, 240, 240)); // Adjust the RGB values as needed

        // Constructor, no return type specified
        priceField = new JTextField(10);
        quantityField = new JTextField(10);
        searchField = new JTextField(20);

        setTitle("Add Asset");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("<html><div style='text-align:center; font-family:Arial; font-weight:bold; font-size:20px;'>Asset Management System</div></html>");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(headerLabel, BorderLayout.NORTH);

        // Footer
        JLabel footerLabel = new JLabel("<html><div style='text-align:center; font-family:Arial; font-size:12px;'>© 2024 Asset Management Inc.</div></html>");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(footerLabel, BorderLayout.SOUTH);

        // Create name field
        nameField = new JTextField(20);
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions(nameField.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions(nameField.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions(nameField.getText().trim());
            }
        });

        // Create price and quantity fields
        priceField = new JTextField(10);
        quantityField = new JTextField(10);

        // Create search field
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions(searchField.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions(searchField.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions(searchField.getText().trim());
            }
        });

        // Create suggestion list
        listModel = new DefaultListModel<>();
        suggestionList = new JList<>(listModel);
        suggestionList.setVisibleRowCount(5);
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList<String> list = (JList<String>) evt.getSource();
                if (evt.getClickCount() == 1) {
                    int index = list.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        nameField.setText(list.getModel().getElementAt(index));
                        listModel.clear(); // Clear all other choices
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(suggestionList);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(nameField, BorderLayout.NORTH);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        // Create submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String symbol = searchField.getText().trim();
            String priceStr = priceField.getText().trim();
            String quantityStr = quantityField.getText().trim();

            if (!name.isEmpty() && !symbol.isEmpty() && !priceStr.isEmpty() && !quantityStr.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                insertIntoDatabase(name, quantity, price);
            }
        });

        // Add components to the frame
        add(searchPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Symbol:"));
        inputPanel.add(searchField);

        add(inputPanel, BorderLayout.CENTER);

        add(submitButton, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(null);

        // Initialize frontend
        initFrontend();
    }

    private void initFrontend() {
        // Set up search field
        searchField.setBorder(BorderFactory.createCompoundBorder(
                searchField.getBorder(),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)
        ));

        // Set up suggestion list
        suggestionList.setBorder(BorderFactory.createEmptyBorder());
        suggestionList.setFont(new Font("Arial", Font.PLAIN, 14));
        suggestionList.setBackground(new Color(255, 255, 255));
        suggestionList.setFixedCellHeight(20);

        // Set up name field
        nameField.setBorder(BorderFactory.createCompoundBorder(
                nameField.getBorder(),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)
        ));

        // Set up submit button
 
    }

    private void updateSuggestions(String keywords) {
        listModel.clear();
        if (!keywords.isEmpty()) {
            try {
                String apiUrl = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + keywords + "&apikey=Z8OXSGD09D5ZAMG4";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse JSON response and add suggestions to list
                    JSONArray jsonArray = new JSONObject(response.toString()).getJSONArray("bestMatches");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String symbol = obj.getString("1. symbol");
                        listModel.addElement(symbol);
                    }
                }
                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertIntoDatabase(String name, int quantity, double price) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javagui", "root", "Arnav@112");
            String query = "INSERT INTO assets (asset_name, quantity, purchase_price) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, quantity);
            statement.setDouble(3, price);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Entry added to database successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error adding entry to database.");
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding entry to database: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            addasset frame = new addasset();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.getContentPane().setBackground(new Color(240, 240, 240));
            frame.setVisible(true);
        });
    }
}

