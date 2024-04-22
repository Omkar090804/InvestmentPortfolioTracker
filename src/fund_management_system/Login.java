package fund_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JTextField emailTextField;
    JPasswordField passwordField;

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/javagui";
    static final String USERNAME = "root";
    static final String PASSWORD = "Arnav@112";

    Login() {
        setTitle("Investment Portfolio Tracker");

        // Logo
        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("images/logo.jpg"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledLogo);
        JLabel logoLabel = new JLabel(scaledLogoIcon);
        logoLabel.setBounds(50, 20, 100, 100);

        // Title
        JLabel titleLabel = new JLabel("Investment Portfolio Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(170, 40, 300, 30);

        // Email Label and TextField
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setBounds(100, 160, 100, 20);
        emailTextField = new JTextField();
        emailTextField.setBounds(200, 160, 200, 25);

        // Password Label and TextField
        JLabel passwordLabel = new JLabel("PIN:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(100, 200, 100, 20);
        passwordField = new JPasswordField();
        passwordField.setBounds(200, 200, 200, 25);

        // Sign In Button
        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signInButton.setBounds(120, 240, 100, 30);
        signInButton.addActionListener(this);

        // Clear Button
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setBounds(230, 240, 100, 30);
        clearButton.addActionListener(this);

        // Sign Up Button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpButton.setBounds(170, 290, 100, 30);
        signUpButton.addActionListener(this);

        // Add components to the content pane
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        add(logoLabel);
        add(titleLabel);
        add(emailLabel);
        add(emailTextField);
        add(passwordLabel);
        add(passwordField);
        add(signInButton);
        add(clearButton);
        add(signUpButton);

        // Set frame properties
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on screen
        setResizable(false); // Prevent resizing
        setVisible(true);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Clear")) {
            emailTextField.setText("");
            passwordField.setText("");
        } else if (ae.getActionCommand().equals("Sign In")) {
            String email = emailTextField.getText();
            String pin = new String(passwordField.getPassword());
            String hashedpassword = hashPassword(pin);
            try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String query = "SELECT * FROM users WHERE email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, email);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        if (storedPassword.equals(hashedpassword)) {
                            JOptionPane.showMessageDialog(this, "Sign in successful!");
                            new Dashboard().setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid email or Password.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "You have not signed up yet. Please sign up first.");
                    }
                }
            } catch (SQLException e) {
                if (e.getSQLState().equals("08001")) {
                    JOptionPane.showMessageDialog(this, "Unable to establish a connection to the database.");
                } else {
                    JOptionPane.showMessageDialog(this, "SQL error occurred: " + e.getMessage());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error occurred: " + e.getMessage());
            }
        } else if (ae.getActionCommand().equals("Sign Up")) {
            new signup().setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}