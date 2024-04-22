package fund_management_system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import org.json.JSONObject;
import org.json.JSONException;

public class portfolio extends JFrame {
    private JLabel profitLossLabel;
    private JTable stocksTable;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/javagui";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Arnav@112";

    private static final String API_KEY = "Z8OXSGD09D5ZAMG4"; 
    private static final String API_URL_BASE = "https://www.alphavantage.co/querys";

    public portfolio() {
        setTitle("Portfolio Manager");
        setResizable(true);

        // Initialize components
        profitLossLabel = new JLabel("Profit/Loss: $0.00");
        profitLossLabel.setFont(new Font("Arial", Font.BOLD, 18));
        profitLossLabel.setForeground(Color.BLUE);
        profitLossLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        stocksTable = new JTable();
        stocksTable.setFont(new Font("Arial", Font.PLAIN, 14));
        stocksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        stocksTable.getTableHeader().setForeground(Color.WHITE);
        stocksTable.getTableHeader().setBackground(Color.BLUE);
        stocksTable.setRowHeight(25);

       
        stocksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 3) {
                    Double profitLoss = (Double) value;
                    if (profitLoss < 0) {
                        cell.setForeground(Color.RED);
                    } else {
                        cell.setForeground(Color.GREEN.darker());
                    }
                }
                return cell;
            }
        });

        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(profitLossLabel);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(new JScrollPane(stocksTable), BorderLayout.CENTER);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(Color.WHITE);
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(tablePanel, BorderLayout.CENTER);
        setContentPane(contentPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        JButton refreshButton = new JButton("Refresh"); // Initialize refresh button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePortfolio(); // Call updatePortfolio when refresh button clicked
            }
        });

        topPanel.add(profitLossLabel);
        topPanel.add(refreshButton);
        updatePortfolio();
    }

    private void updatePortfolio() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Asset Name");
        model.addColumn("Quantity");
        model.addColumn("Purchase Price");
        model.addColumn("Profit/Loss");

        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM assets";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String assetName = rs.getString("asset_name");
                    int quantity = rs.getInt("quantity");
                    double purchasePrice = rs.getDouble("purchase_price");
                    double currentPrice = fetchCurrentPrice(assetName);
                    double profitLoss = (currentPrice - purchasePrice) * quantity;

                    model.addRow(new Object[]{assetName, quantity, purchasePrice, profitLoss});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving data: " + e.getMessage());
        }

    
        stocksTable.setModel(model);

        
        double totalProfitLoss = calculateTotalProfitLoss(model);
    double totalMoney = calculateTotalMoney(model);
    profitLossLabel.setText("Total Profit/Loss: $" + totalProfitLoss);
    updatePortfolioTable(totalMoney, totalProfitLoss);
    }

    private double fetchCurrentPrice(String assetName) {
        String symbol = getSymbolFromDatabase(assetName);
        if (symbol.isEmpty()) {
            System.out.println("Symbol not found for asset: " + assetName);
            return 0.0;
        }

        String apiUrl = API_URL_BASE + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + API_KEY;
        System.out.println(apiUrl);
        double currentPrice = 0.0;

        try {
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
                currentPrice = parsePriceFromJson(response.toString());
            } else {
                System.out.println("Error: HTTP response code " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentPrice;
    }
private double calculateTotalMoney(DefaultTableModel model) {
    double totalMoney = 0;
    for (int i = 0; i < model.getRowCount(); i++) {
        Object moneyObj = model.getValueAt(i,2);
        if (moneyObj instanceof Double) {
            totalMoney += (Double) moneyObj;
        } else if (moneyObj instanceof String) {
            try {
                double money = Double.parseDouble((String) moneyObj);
                totalMoney += money;
            } catch (NumberFormatException e) {
                // Handle parsing error
                System.err.println("Error parsing money value: " + e.getMessage());
            }
        }
    }
    return totalMoney;
}
   private String getSymbolFromDatabase(String assetName) {
    String symbol = "";
    try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
        String query = "SELECT asset_name FROM assets WHERE asset_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, assetName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                symbol = rs.getString("asset_name"); 
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return symbol;
}

private double parsePriceFromJson(String jsonResponse) {
    try {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject globalQuote = jsonObject.getJSONObject("Global Quote");
        String priceString = globalQuote.getString("05. price");
        return Double.parseDouble(priceString);
    } catch (JSONException e) {
        System.out.println("Error parsing JSON: " + e.getMessage());
        return 0.0; 
    } catch (Exception e) {
        System.out.println("Error parsing JSON: " + e.getMessage());
        return 0.0; 
    }
}




    private double calculateTotalProfitLoss(DefaultTableModel model) {
    double totalProfitLoss = 0;
    for (int i = 0; i < model.getRowCount(); i++) {
        Object profitLossObj = model.getValueAt(i, 3);
        if (profitLossObj instanceof Double) {
            totalProfitLoss += (Double) profitLossObj;
        } else if (profitLossObj instanceof String) {
            try {
                double profitLoss = Double.parseDouble((String) profitLossObj);
                totalProfitLoss += profitLoss;
            } catch (NumberFormatException e) {
                // Handle parsing error
                System.err.println("Error parsing profit/loss value: " + e.getMessage());
            }
        }
    }
    return totalProfitLoss;
}
    
private void updatePortfolioTable(double totalMoney, double totalCost) {
    try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
        String query = "INSERT INTO portfolio (total_money, profit_loss) VALUES (?, ?) " +
                       "ON DUPLICATE KEY UPDATE total_money = VALUES(total_money), profit_loss = VALUES(profit_loss)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, totalMoney);
            stmt.setDouble(2, totalCost);
            stmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error updating portfolio table: " + e.getMessage());
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            portfolio frame = new portfolio();
            frame.setVisible(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        });
    }
}
