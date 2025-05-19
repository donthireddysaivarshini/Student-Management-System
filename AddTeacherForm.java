import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.*;

public class AddTeacherForm extends JPanel {
    private JTextField nameField, usernameField, emailField, phoneField, deptField;
    private JPasswordField passwordField;

    public AddTeacherForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Add New Teacher");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setMaximumSize(new Dimension(500, 300));

        nameField = createTextField();
        usernameField = createTextField();
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField = createTextField();
        phoneField = createTextField();
        deptField = createTextField();

        formPanel.add(createLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(createLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(createLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(createLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(createLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(createLabel("Department:"));
        formPanel.add(deptField);

        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(formPanel);
        add(Box.createVerticalStrut(20));

        JButton addBtn = new JButton("Add Teacher");
        addBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addBtn.setBackground(new Color(63, 81, 181));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.addActionListener(e -> addTeacher());
        add(addBtn);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return field;
    }

    private void addTeacher() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String dept = deptField.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name, Username, and Password are required.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Phone number must be 10 digits.");
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email format.");
            return;
        }

        try (Connection conn = database.Database.getConnection()) {
            conn.setAutoCommit(false);

            // Insert into teachers table
            PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO teachers (name, username, password, email, phone, dept) VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps1.setString(1, name);
            ps1.setString(2, username);
            ps1.setString(3, password);
            ps1.setString(4, email);
            ps1.setString(5, phone);
            ps1.setString(6, dept);
            ps1.executeUpdate();

            // Optional insert into users table
            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, 'Teacher')"
            );
            ps2.setString(1, username);
            ps2.setString(2, password);
            ps2.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(null, "✅ Teacher added successfully.");
            clearForm();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Error: " + ex.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.\\w+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private void clearForm() {
        nameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        phoneField.setText("");
        deptField.setText("");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Add Teacher Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AddTeacherForm());
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
