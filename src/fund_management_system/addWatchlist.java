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

public class addWatchlist extends JFrame {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField searchField; // Change access to public
    private JList<String> suggestionList;
    private DefaultListModel<String> listModel;

    public addWatchlist() { // Constructor, no return type specified
        setTitle("Add Watchlist");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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
            public void mouseClicked(MouseEvent evt) {
                JList<String> list = (JList<String>) evt.getSource();
                if (evt.getClickCount() == 1) {
                    int index = list.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        nameField.setText(list.getModel().getElementAt(index));
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

        setSize(300, 300);
        setLocationRelativeTo(null);
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
            String query = "INSERT INTO watchlists (asset_name, quantity, purchase_price) VALUES (?, ?, ?)";
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
            new addasset().setVisible(true);
        });
    }
}
