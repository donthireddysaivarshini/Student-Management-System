import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import database.Database;

public class MarkAttendanceForm extends JPanel {
    private String teacherUsername;
    private JComboBox<String> classDropdown;
    private JPanel studentPanel;
    private JButton submitBtn;
    private JTextField dateField;

    public MarkAttendanceForm(String username) {
        this.teacherUsername = username;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);

        // 🔹 Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        classDropdown = new JComboBox<>();
        classDropdown.setPreferredSize(new Dimension(150, 30));
        loadDepartments();

        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateField = new JTextField(10);
        dateField.setText(LocalDate.now().toString());
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton loadBtn = new JButton("🔄 Load Students");
        styleButton(loadBtn, new Color(52, 152, 219));

        topPanel.add(deptLabel);
        topPanel.add(classDropdown);
        topPanel.add(dateLabel);
        topPanel.add(dateField);
        topPanel.add(loadBtn);

        add(topPanel, BorderLayout.NORTH);

        // 🔹 Center Panel - Student Checkboxes
        studentPanel = new JPanel();
        studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
        studentPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(studentPanel);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Bottom Panel - Submit Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        submitBtn = new JButton("✅ Submit Attendance");
        styleButton(submitBtn, new Color(39, 174, 96));
        bottomPanel.add(submitBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // 🔹 Actions
        loadBtn.addActionListener(e -> loadStudents());
        submitBtn.addActionListener(e -> submitAttendance());
    }

    private void loadDepartments() {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT dept FROM students");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                classDropdown.addItem(rs.getString("dept"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading departments.");
        }
    }

    private void loadStudents() {
        studentPanel.removeAll();
        String dept = (String) classDropdown.getSelectedItem();

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT roll FROM students WHERE dept = ?");
            ps.setString(1, dept);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JCheckBox cb = new JCheckBox(rs.getString("roll"));
                cb.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                cb.setBackground(Color.WHITE);
                studentPanel.add(cb);
            }
            studentPanel.revalidate();
            studentPanel.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students.");
        }
    }

    private void submitAttendance() {
        Component[] components = studentPanel.getComponents();
        String dept = (String) classDropdown.getSelectedItem();
        String dateText = dateField.getText().trim();

        try {
            LocalDate selectedDate = LocalDate.parse(dateText); // Validate date format

            try (Connection conn = Database.getConnection()) {
                for (Component comp : components) {
                    if (comp instanceof JCheckBox) {
                        JCheckBox cb = (JCheckBox) comp;
                        String roll = cb.getText();
                        String status = cb.isSelected() ? "Present" : "Absent";

                        PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO attendance (student_roll, teacher_username, date, status) VALUES (?, ?, ?, ?)"
                        );
                        ps.setString(1, roll);
                        ps.setString(2, teacherUsername);
                        ps.setDate(3, java.sql.Date.valueOf(selectedDate));
                        ps.setString(4, status);
                        ps.executeUpdate();
                    }
                }

                JOptionPane.showMessageDialog(this, "✅ Attendance recorded for " + selectedDate);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Invalid date format or database error.");
        }
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}
