package fund_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class signup extends JFrame implements ActionListener {
    JTextField nameField, aadharField, phoneField, dobField, usernameField, emailField;
    JPasswordField passwordField;
    JButton signUpButton;

    // JDBC URL, username, and password for your database
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/javagui";
    static final String USERNAME = "root";
    static final String PASSWORD = "Arnav@112";

    signup() {
        setTitle("Sign Up");

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(150, 20, 100, 30);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setBounds(50, 60, 100, 20);
        nameField = new JTextField();
        nameField.setBounds(160, 60, 150, 25);

        JLabel aadharLabel = new JLabel("Aadhar Number:");
        aadharLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        aadharLabel.setBounds(50, 100, 120, 20);
        aadharField = new JTextField();
        aadharField.setBounds(160, 100, 150, 25);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setBounds(50, 140, 120, 20);
        phoneField = new JTextField();
        phoneField.setBounds(160, 140, 150, 25);

        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        dobLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dobLabel.setBounds(50, 180, 200, 20);
        dobField = new JTextField();
        dobField.setBounds(260, 180, 150, 25);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setBounds(50, 220, 100, 20);
        usernameField = new JTextField();
        usernameField.setBounds(160, 220, 150, 25);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setBounds(50, 260, 100, 20);
        emailField = new JTextField();
        emailField.setBounds(160, 260, 150, 25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(50, 300, 100, 20);
        passwordField = new JPasswordField();
        passwordField.setBounds(160, 300, 150, 25);

        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpButton.setBounds(150, 350, 100, 30);
        signUpButton.addActionListener(this);

        getContentPane().setBackground(Color.cyan);
        setLayout(null);
        add(titleLabel);
        add(nameLabel);
        add(nameField);
        add(aadharLabel);
        add(aadharField);
        add(phoneLabel);
        add(phoneField);
        add(dobLabel);
        add(dobField);
        add(usernameLabel);
        add(usernameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(signUpButton);

        setSize(450, 450);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
    if (ae.getSource() == signUpButton) {
        String name = nameField.getText().trim();
        String aadhar = aadharField.getText().trim();
        String phone = phoneField.getText().trim();
        String dob = dobField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || aadhar.isEmpty() || phone.isEmpty() || dob.isEmpty() ||
                username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

       
        String hashedPassword = hashPassword(password);

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO users (name, aadhar, phone, dob, username, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, aadhar);
                stmt.setString(3, phone);
                stmt.setString(4, dob);
                stmt.setString(5, username);
                stmt.setString(6, email);
                stmt.setString(7, hashedPassword); 
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sign up successful!");
                // You can redirect to the login page or perform any other action here
                new Login().setVisible(true);
                dispose();
            } catch (SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(this, "A user with this username or email already exists.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error occurred: " + e.getMessage());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Connection error occurred: " + e.getMessage());
        }
    }
}


    public static void main(String[] args) {
        new signup();
    }
}
