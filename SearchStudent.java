import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import database.Database;

public class SearchStudent extends JPanel {
    private JTextField rollField;
    private JTextArea resultArea;
    private JButton searchBtn;

    public SearchStudent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // === Title ===
        JLabel title = new JLabel("Search Student by Roll Number");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title);

        // === Center Form ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(500, 150));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Roll Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel rollLabel = new JLabel("Enter Roll Number:");
        rollLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(rollLabel, gbc);

        // Roll Field
        gbc.gridx = 1;
        rollField = new JTextField(15);
        rollField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(rollField, gbc);

        // Search Button
        gbc.gridx = 1;
        gbc.gridy = 1;
        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchBtn.setBackground(new Color(63, 81, 181)); // Same as UpdateStudent
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(searchBtn, gbc);

        add(formPanel);

        // === Result Area ===
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        resultArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Details"));
        scrollPane.setMaximumSize(new Dimension(600, 250));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(scrollPane);

        // === Action ===
        searchBtn.addActionListener(e -> searchStudent());
    }

    void searchStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Roll Number.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE roll = ?");
            ps.setString(1, roll);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("ID       : ").append(rs.getInt("id")).append("\n");
                sb.append("Name     : ").append(rs.getString("name")).append("\n");
                sb.append("Roll     : ").append(rs.getString("roll")).append("\n");
                sb.append("DOB      : ").append(rs.getDate("dob")).append("\n");
                sb.append("Dept     : ").append(rs.getString("dept")).append("\n");
                sb.append("Phone    : ").append(rs.getString("phone")).append("\n");
                sb.append("Email    : ").append(rs.getString("email")).append("\n");
                sb.append("Address  : ").append(rs.getString("address")).append("\n");
                sb.append("Gender   : ").append(rs.getString("gender")).append("\n");
                sb.append("Join Date: ").append(rs.getDate("join_date")).append("\n");
                resultArea.setText(sb.toString());
            } else {
                resultArea.setText("❌ No student found with Roll Number: " + roll);
            }

        } catch (Exception e) {
            resultArea.setText("⚠️ Error while fetching student.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Search Student");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new SearchStudent());
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
