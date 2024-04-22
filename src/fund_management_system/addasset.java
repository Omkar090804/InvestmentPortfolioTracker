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

public class addasset extends JFrame {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField searchField; // Change access to public
    public JList<String> suggestionList;
    private DefaultListModel<String> listModel;

    public addasset() {
        getContentPane().setBackground(new Color(240, 240, 240)); 

      
        priceField = new JTextField(5);
        quantityField = new JTextField(5);
        searchField = new JTextField(5);

        setTitle("Add Asset");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

      
        JLabel headerLabel = new JLabel("<html><div style='text-align:center; font-family:Arial; font-weight:bold; font-size:20px;'>Asset Management System</div></html>");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(headerLabel, BorderLayout.NORTH);

        
        JLabel footerLabel = new JLabel("<html><div style='text-align:center; font-family:Arial; font-size:12px;'>© 2024 Asset Management Inc.</div></html>");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(footerLabel, BorderLayout.SOUTH);

        
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

        
        priceField = new JTextField(10);
        quantityField = new JTextField(10);

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
            frame.getContentPane().setBackground(new Color(240, 240, 240));
            frame.setVisible(true);
        });
    }
}
