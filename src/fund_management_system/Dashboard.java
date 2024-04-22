package fund_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener {
    
    public Dashboard() {
        setTitle("Investment Portfolio Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel welcomeLabel = new JLabel("Welcome to Your Investment Dashboard");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton viewPortfolioButton = createStyledButton("View Portfolio", Color.decode("#0099FF"));
        viewPortfolioButton.addActionListener(this);

        JButton addAssetButton = createStyledButton("Add Asset", Color.decode("#51CD51"));
        addAssetButton.addActionListener(this);

        JButton logoutButton = createStyledButton("Logout", Color.decode("#FF3333"));
        logoutButton.addActionListener(this);

        JLabel infoLabel = new JLabel("Investment Portfolio Tracker v1.0");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setForeground(Color.GRAY);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.add(welcomeLabel);
        buttonPanel.add(viewPortfolioButton);
        buttonPanel.add(addAssetButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(infoLabel);

        add(buttonPanel);
        pack();
        setLocationRelativeTo(null); // Center the window on screen
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false); // Remove the default border
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("View Portfolio")) {
            System.out.println("Viewing Portfolio...");
            new portfolio().setVisible(true);
        } else if (actionCommand.equals("Add Asset")) {
            System.out.println("Adding Asset...");
            new addasset().setVisible(true);
            // Perform action for adding asset
        } else if (actionCommand.equals("Logout")) {
            System.out.println("Logging out...");
            // Perform action for logging out
        }
        dispose(); // Close the dashboard window after performing an action
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}
