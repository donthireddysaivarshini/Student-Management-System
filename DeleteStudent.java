import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import database.Database;

public class DeleteStudent extends JPanel {
    private JTextField rollField;
    private JButton deleteBtn;

    public DeleteStudent() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Delete Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridwidth = 1;

        JLabel rollLabel = new JLabel("Enter Roll Number:");
        rollLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(rollLabel, gbc);

        rollField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(rollField, gbc);

        deleteBtn = new JButton("Delete Student");
        deleteBtn.setBackground(new Color(214, 48, 49));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(deleteBtn, gbc);

        deleteBtn.addActionListener(e -> deleteStudent());
    }

    private void deleteStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Roll Number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE roll = ?");
            ps.setString(1, roll);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                rollField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No student found with that Roll Number.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while deleting the student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
