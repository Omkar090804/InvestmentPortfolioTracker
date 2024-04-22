package fund_management_system;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Alerts extends JPanel {

    private JTextPane textPane;

    public Alerts() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600)); // Increased size for better readability and space

        // Heading Panel
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center align heading
        headingPanel.setBorder(new EmptyBorder(20, 20, 10, 20)); // Adjusted padding
        JLabel headingLabel = new JLabel("Delisted Companies", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Increased font size
        headingLabel.setForeground(Color.BLUE);
        headingPanel.add(headingLabel);
        add(headingPanel, BorderLayout.NORTH);

        // Text Pane and Scroll Pane
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20)); // Adjusted padding
        add(scrollPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setBorder(new EmptyBorder(10, 20, 20, 20)); // Adjusted padding
        JLabel dateLabel = new JLabel("Date: " + getCurrentDate(), SwingConstants.RIGHT); // Display current date
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        datePanel.add(dateLabel);
        add(datePanel, BorderLayout.SOUTH);

        fetchDelistedCompanies();
    }

    // Method to fetch current date
    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    private void fetchDelistedCompanies() {
        String apiUrl = "https://financialmodelingprep.com/api/v3/delisted-companies?apikey=x0BN5je45IodTK5wGnYhV4LniIrgoFUc";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            StringBuilder companies = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                JSONObject company = jsonArray.getJSONObject(i);
                String companyName = company.getString("companyName");
                String symbol = company.getString("symbol");
                String exchange = company.getString("exchange");
                String delistedDate = company.getString("delistedDate");

                companies.append("Company Name: ").append(companyName).append("\n");
                companies.append("Symbol: ").append(symbol).append("\n");
                companies.append("Exchange: ").append(exchange).append("\n");
                companies.append("Delisted Date: ").append(delistedDate).append("\n\n"); // Added line breaks for better readability
            
            insertIntoAlerts(companyName,symbol,exchange,delistedDate);
            }

            textPane.setText(companies.toString());

            // Set the font and color of the symbol and exchange
            StyledDocument doc = textPane.getStyledDocument();
            SimpleAttributeSet red = new SimpleAttributeSet();
            SimpleAttributeSet blue = new SimpleAttributeSet();
            StyleConstants.setForeground(red, Color.RED);
            StyleConstants.setForeground(blue, Color.BLUE);

            // Colorize the symbols and exchanges
            String text = textPane.getText();
            if (text.contains("[") && text.contains("]")) {
                doc.setCharacterAttributes(text.indexOf("["), 1, blue, false);
                doc.setCharacterAttributes(text.indexOf("]"), 1, blue, false);
            }
            if (text.contains(" - ")) {
                doc.setCharacterAttributes(text.indexOf(" - "), 3, red, false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 public static void insertIntoAlerts(String companyName, String symbol, String exchange, String delistedDate) {
        try {
            // Establish connection to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javagui", "root", "Arnav@112");

            // Prepare the SQL statement with placeholders for parameters
            String query = "INSERT INTO alerts (company_name, symbol, exchange, delisted_date) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the values for the parameters
            statement.setString(1, companyName);
            statement.setString(2, symbol);
            statement.setString(3, exchange);
            statement.setString(4, delistedDate);

            // Execute the SQL statement
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new alert was inserted successfully!");
            } else {
                System.out.println("Failed to insert the alert.");
            }

            // Close the connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Delisted Companies");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new Alerts());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
