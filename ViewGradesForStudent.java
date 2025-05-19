

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import database.Database;

public class ViewGradesForStudent extends JPanel {
    private DefaultTableModel model;
    private String roll;

    public ViewGradesForStudent(String roll) {
        this.roll = roll;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Your Grades", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Teacher", "S1", "S2", "S3", "S4", "S5", "S6", "Avg", "Grade", "GPA"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);

        // Center align cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Refresh Button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        JButton refreshBtn = new JButton("🔄 Refresh Grades");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshBtn.setBackground(new Color(39, 174, 96));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(180, 40));
        refreshBtn.addActionListener(e -> loadGrades());
        bottomPanel.add(refreshBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        loadGrades();
    }

    public void loadGrades() {
        model.setRowCount(0); // Clear existing data
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM grades WHERE student_roll = ?"
            );
            ps.setString(1, roll);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("teacher_username"),
                    rs.getInt("subject1"),
                    rs.getInt("subject2"),
                    rs.getInt("subject3"),
                    rs.getInt("subject4"),
                    rs.getInt("subject5"),
                    rs.getInt("subject6"),
                    rs.getFloat("average"),
                    rs.getString("grade"),
                    rs.getFloat("gpa")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
