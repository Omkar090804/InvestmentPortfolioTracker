package fund_management_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class transactionalhistory extends JFrame  {
    private Connection connection; // Corrected variable name
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/javagui";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Arnav@112";
    private JTable table;
    private JLabel totalCostLabel;

    public transactionalhistory() {
        setTitle("Transaction History");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Asset");
        model.addColumn("Quantity");
        model.addColumn("Date");
        model.addColumn("Price");
        model.addColumn("time");

        // Populate table
        // Populate table
try {
    String query = "SELECT * FROM assets";
    PreparedStatement statement = connection.prepareStatement(query);
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
        Vector row = new Vector();
        row.add(resultSet.getString("asset_name"));
        row.add(resultSet.getInt("quantity"));
        row.add(resultSet.getDate("purchase_date"));
        row.add(resultSet.getDouble("purchase_price"));
        // Get the purchase_time as java.sql.Time
        Time purchaseTime = resultSet.getTime("time");
        row.add(purchaseTime != null ? purchaseTime.toString() : ""); // Convert to string for display
        model.addRow(row);
    }
} catch (SQLException e) {
    e.printStackTrace();
}

        // Create JTable with the model
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Customize table appearance
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.lightGray);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.setGridColor(Color.gray);

        // Set custom cell renderer for alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        renderer.setBackground(new Color(240, 240, 240)); // Light gray for even rows
                    } else {
                        renderer.setBackground(Color.WHITE); // White for odd rows
                    }
                }
                return renderer;
            }
        });

        // Add total cost label
        totalCostLabel = new JLabel("Total Cost: " + calculateTotalCost(model));
        totalCostLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalCostLabel.setFont(new Font("Arial", Font.BOLD, 14));
        getContentPane().add(totalCostLabel, BorderLayout.SOUTH);
    }

    private double calculateTotalCost(DefaultTableModel model) {
        double total = 0;
        for (int row = 0; row < model.getRowCount(); row++) {
            double price = (double) model.getValueAt(row, 3);
            total += price;
        }
        return total;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            transactionalhistory gui = new transactionalhistory();
            gui.setVisible(true);
        });
    }
}
