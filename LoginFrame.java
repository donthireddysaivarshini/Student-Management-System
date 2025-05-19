import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Student Management System - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Theme colors
        Color primaryColor = new Color(46, 134, 193);   // #2E86C1
        Color backgroundColor = new Color(244, 246, 247); // #F4F6F7
        Color textColor = new Color(28, 40, 51);         // #1C2833

        // Fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);

        // Main container panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Login box panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createLineBorder(primaryColor, 2));
        loginPanel.setPreferredSize(new Dimension(400, 350));
        loginPanel.setLayout(new GridBagLayout());

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(textColor);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(fieldFont);
        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(textColor);
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(fieldFont);
        gbc.gridy = 3;
        loginPanel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(buttonFont);
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.addActionListener(e -> handleLogin());

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        mainPanel.add(loginPanel);

        setContentPane(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        String role = getUserRole(username, password);
        if (role == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials.");
        } else if (role.equalsIgnoreCase("Admin")) {
            new AdminDashboard().setVisible(true);
            dispose();
        } else if (role.equalsIgnoreCase("Teacher")) {
            new TeacherDashboard(username).setVisible(true);
            dispose();
        } else if (role.equalsIgnoreCase("Student")) {
            new StudentDashboard(username, password).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Access Denied. Unknown role.");
        }
    }

    private String getUserRole(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "Yob95765.")) {
            // Admin
            String sqlAdmin = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmtAdmin = conn.prepareStatement(sqlAdmin);
            stmtAdmin.setString(1, username);
            stmtAdmin.setString(2, password);
            ResultSet rsAdmin = stmtAdmin.executeQuery();
            if (rsAdmin.next()) {
                return rsAdmin.getString("role");
            }

            // Teacher
            String sqlTeacher = "SELECT * FROM teachers WHERE username = ? AND password = ?";
            PreparedStatement stmtTeacher = conn.prepareStatement(sqlTeacher);
            stmtTeacher.setString(1, username);
            stmtTeacher.setString(2, password);
            ResultSet rsTeacher = stmtTeacher.executeQuery();
            if (rsTeacher.next()) {
                return "Teacher";
            }

            // Student
            String sqlStudent = "SELECT * FROM students WHERE name = ? AND roll = ?";
            PreparedStatement stmtStudent = conn.prepareStatement(sqlStudent);
            stmtStudent.setString(1, username);
            stmtStudent.setString(2, password);
            ResultSet rsStudent = stmtStudent.executeQuery();
            if (rsStudent.next()) {
                return "Student";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
