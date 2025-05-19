import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import database.Database;

public class UpdateStudent extends JPanel {
    private JTextField rollField, nameField, phoneField, emailField, deptField, addressField;
    private JButton searchBtn, updateBtn;

    public UpdateStudent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Update Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(20));

        JPanel searchPanel = new JPanel();
        searchPanel.setMaximumSize(new Dimension(500, 50));
        searchPanel.setLayout(new FlowLayout());

        rollField = new JTextField(10);
        rollField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchBtn.setBackground(new Color(63, 81, 181));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);

        searchPanel.add(new JLabel("Enter Roll Number:"));
        searchPanel.add(rollField);
        searchPanel.add(searchBtn);
        add(searchPanel);
        add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setMaximumSize(new Dimension(500, 300));

        nameField = createTextField();
        deptField = createTextField();
        phoneField = createTextField();
        emailField = createTextField();
        addressField = createTextField();

        formPanel.add(createLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(createLabel("Department:"));
        formPanel.add(deptField);
        formPanel.add(createLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(createLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(createLabel("Address:"));
        formPanel.add(addressField);

        add(formPanel);
        add(Box.createVerticalStrut(20));

        updateBtn = new JButton("Update Student");
        updateBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        updateBtn.setBackground(new Color(63, 81, 181));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(updateBtn);

        // Disable fields until search
        toggleFields(false);

        // Listeners
        searchBtn.addActionListener(e -> fetchStudent());
        updateBtn.addActionListener(e -> updateStudent());
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

    private void toggleFields(boolean enable) {
        nameField.setEnabled(enable);
        deptField.setEnabled(enable);
        phoneField.setEnabled(enable);
        emailField.setEnabled(enable);
        addressField.setEnabled(enable);
        updateBtn.setEnabled(enable);
    }

    private void fetchStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Roll Number.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE roll = ?");
            ps.setString(1, roll);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                deptField.setText(rs.getString("dept"));
                phoneField.setText(rs.getString("phone"));
                emailField.setText(rs.getString("email"));
                addressField.setText(rs.getString("address"));

                toggleFields(true);
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.");
                toggleFields(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateStudent() {
        String roll = rollField.getText().trim();
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || dept.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE students SET name=?, dept=?, phone=?, email=?, address=? WHERE roll=?"
            );
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, address);
            ps.setString(6, roll);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "✅ Student updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update student.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Update Student");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new UpdateStudent());
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
