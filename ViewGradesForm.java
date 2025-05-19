import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import database.Database;

public class ViewGradesForm extends JPanel {
    private String teacherUsername;

    public ViewGradesForm(String teacherUsername) {
        this.teacherUsername = teacherUsername;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Student Grades Report", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        String[] columns = {
            "ID", "Student Roll", "Subject1", "Subject2", "Subject3",
            "Subject4", "Subject5", "Subject6", "Average", "GPA", "Grade"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable
            }
        };

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM grades WHERE teacher_username = ?"
            );
            ps.setString(1, teacherUsername);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("student_roll"),
                    rs.getInt("subject1"),
                    rs.getInt("subject2"),
                    rs.getInt("subject3"),
                    rs.getInt("subject4"),
                    rs.getInt("subject5"),
                    rs.getInt("subject6"),
                    rs.getFloat("average"),
                    rs.getFloat("gpa"),
                    rs.getString("grade")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading grade data.");
        }

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setGridColor(new Color(220, 220, 220));
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(144, 202, 249));
        table.setSelectionForeground(Color.BLACK);

        // Zebra striping
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return c;
            }
        });

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
    }
}
