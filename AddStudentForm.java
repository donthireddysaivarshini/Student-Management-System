import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

public class AddStudentForm extends JPanel {
    private JTextField nameField, rollField, phoneField, emailField, addressField;
    private JComboBox<String> genderCombo, deptCombo;
    private JDateChooser dobChooser, joinDateChooser;

    public AddStudentForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Add New Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setMaximumSize(new Dimension(500, 500));

        // Create fields
        nameField = createTextField();
        rollField = createTextField();
        dobChooser = new JDateChooser();
        dobChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dobChooser.setDateFormatString("yyyy-MM-dd");

        deptCombo = new JComboBox<>(new String[]{"CSE", "IT", "IoT","ECE", "EEE", "MECH", "CIVIL"});
        deptCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        phoneField = createTextField();
        emailField = createTextField();
        addressField = createTextField();

        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        joinDateChooser = new JDateChooser();
        joinDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        joinDateChooser.setDateFormatString("yyyy-MM-dd");

        // Add to form panel
        formPanel.add(createLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(createLabel("Roll:"));
        formPanel.add(rollField);
        formPanel.add(createLabel("DOB:"));
        formPanel.add(dobChooser);
        formPanel.add(createLabel("Department:"));
        formPanel.add(deptCombo);
        formPanel.add(createLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(createLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(createLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(createLabel("Gender:"));
        formPanel.add(genderCombo);
        formPanel.add(createLabel("Join Date:"));
        formPanel.add(joinDateChooser);

        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(formPanel);
        add(Box.createVerticalStrut(10));

        JButton addBtn = new JButton("Add Student");
        addBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addBtn.setBackground(new Color(63, 81, 181));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.addActionListener(e -> addStudent());
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

    private void addStudent() {
        try (Connection conn = database.Database.getConnection()) {
            String name = nameField.getText().trim();
            String roll = rollField.getText().trim();
            java.util.Date dob = dobChooser.getDate();
            String dept = (String) deptCombo.getSelectedItem();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            java.util.Date joinDate = joinDateChooser.getDate();

            if (name.isEmpty() || roll.isEmpty() || dob == null || joinDate == null ||
                phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled.");
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

            // Check for duplicate roll
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE roll = ?");
            checkStmt.setString(1, roll);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Oops! This student already exists.");
                return;
            }

            String sql = "INSERT INTO students (name, roll, dob, dept, phone, email, address, gender, join_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, roll);
            stmt.setDate(3, new java.sql.Date(dob.getTime()));
            stmt.setString(4, dept);
            stmt.setString(5, phone);
            stmt.setString(6, email);
            stmt.setString(7, address);
            stmt.setString(8, gender);
            stmt.setDate(9, new java.sql.Date(joinDate.getTime()));

            int result = stmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "✅ Student added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "❌ Failed to add student.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.\\w+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Add Student Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AddStudentForm());
        frame.setSize(600, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
