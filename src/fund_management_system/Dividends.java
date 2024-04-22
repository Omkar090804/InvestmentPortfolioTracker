package fund_management_system;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

// Interface for fetching dividends
interface DividendFetcher {
    List<Dividend> fetchDividends(List<String> symbols);
}

// Class representing dividend data
class Dividend {
    private String symbol;
    private double amount;
    private String date;
private String paymentDate;

    public Dividend(String symbol, double amount, String date, String paymentDate) {
    this.symbol = symbol;
    this.amount = amount;
    this.date = date;
    this.paymentDate = paymentDate; // Set payment date
}


    // Getters and setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
public String getPaymentDate() {
    return paymentDate;
}

public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
}

}

public class Dividends extends JFrame implements DividendFetcher {

    private final JTable table;
    private final JTextField assetTextField;
    private final DefaultTableModel model;
    private List<String> assetTable;

    public Dividends() {
        setTitle("Dividend Table");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Asset Table Input Panel
        JPanel assetInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel assetLabel = new JLabel("Asset Table :");
        assetTextField = new JTextField(30);
        JButton refreshButton = new JButton("search Dividends");
        refreshButton.addActionListener((ActionEvent e) -> {
            fetchAndDisplayDividends();
        });
       JButton Button =new JButton("view your dividends");
       Button.addActionListener((ActionEvent e)->{
        displayDividendsFromDatabase();
    });
        assetInputPanel.add(assetLabel);
        assetInputPanel.add(assetTextField);
        assetInputPanel.add(refreshButton);
        assetInputPanel.add(Button);
        contentPane.add(assetInputPanel, BorderLayout.NORTH);

        // Dividend Table Panel
        model = new DefaultTableModel(new Object[]{"Symbol", "Dividend", "Date","payment Date"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        setContentPane(contentPane);
    }

    // Method to fetch and display dividends

    /**
     *
     * @param symbols
     * @return
     */
    @Override
    public List<Dividend> fetchDividends(List<String> symbols) {
        model.setRowCount(0); // Clear existing rows
        assetTable = new ArrayList<>();

        String assetText = assetTextField.getText().trim();
        if (assetText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter symbols in the asset table.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String[] symbolsArray = assetText.split(",");
        for (String symbol : symbolsArray) {
            assetTable.add(symbol.trim());
        }

        List<Dividend> dividends = new ArrayList<>();
        for (String symbol : assetTable) {
            try {
                String apiUrl = "https://financialmodelingprep.com/api/v3/historical-price-full/stock_dividend/" + symbol + "?apikey=x0BN5je45IodTK5wGnYhV4LniIrgoFUc";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                StringBuilder response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray historicalArray = jsonObject.getJSONArray("historical");
                for (int i = 0; i < 5; i++) {
                    JSONObject historicalObj = historicalArray.getJSONObject(i);
                    String date = historicalObj.getString("date");
                    double dividend = historicalObj.getDouble("dividend");
                    String paymentDate =historicalObj.getString("paymentDate");
                    dividends.add(new Dividend(symbol, dividend, date,paymentDate));
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error fetching data from the server.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (JSONException e) {
                JOptionPane.showMessageDialog(this, "Unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return dividends;
    }

    // Method to display dividends in the table
   private void fetchAndDisplayDividends() {
    // Fetch asset symbols from the database
    List<String> assetSymbols = fetchAssetSymbolsFromDatabase();
    
    // Fetch dividends from the API
    List<Dividend> dividends = fetchDividends(assetSymbols);

    // Display dividends in the table
    if (dividends != null) {
        for (Dividend dividend : dividends) {
            model.addRow(new Object[]{dividend.getSymbol(), dividend.getAmount(), dividend.getDate(), dividend.getPaymentDate()});
        }
    }

    // Insert dividends into the database
    insertDividendIntoDatabase(dividends);
}


 private List<String> fetchAssetSymbolsFromDatabase() {
        List<String> assetSymbols = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javagui", "root", "Arnav@112")) {
            String query = "SELECT asset_name FROM Assets";
            try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    assetSymbols.add(resultSet.getString("asset_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching asset symbols from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return assetSymbols;
    }
private void insertDividendIntoDatabase(List<Dividend> dividends) {
    // Connect to the database and insert the dividends
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javagui", "root", "Arnav@112")) {
        String sql = "INSERT INTO Dividends (Amount, EX_DIVIDEND_DAY, Payable_Date) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Dividend dividend : dividends) {
                stmt.setBigDecimal(1, BigDecimal.valueOf(dividend.getAmount()));
                stmt.setDate(2, java.sql.Date.valueOf(dividend.getDate()));
                stmt.setString(3, dividend.getPaymentDate());
                stmt.executeUpdate();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error inserting dividends into the database.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void displayDividendsFromDatabase() {
        List<Dividend> dividends = fetchDividendsFromDatabase();
        if (dividends != null) {
            // Clear existing table data
            model.setRowCount(0);
            // Populate the table with dividends from the database
            for (Dividend dividend : dividends) {
                model.addRow(new Object[]{dividend.getSymbol(), dividend.getAmount(), dividend.getDate(), dividend.getPaymentDate()});
            }
        }
    }

    // Method to fetch dividends from the database
    private List<Dividend> fetchDividendsFromDatabase() {
        List<Dividend> dividends = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javagui", "root", "Arnav@112")) {
            String query = "select * from dividends;";
            try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String symbol="sample";
                    double amount = resultSet.getDouble("Amount");
                    String date = resultSet.getString("EX_DIVIDEND_DAY");
                    String paymentDate = resultSet.getString("Payable_Date");
                    dividends.add(new Dividend(symbol,amount, date, paymentDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching dividends from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return dividends;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dividends frame = new Dividends();
            frame.setVisible(true);
        });
    }
}
