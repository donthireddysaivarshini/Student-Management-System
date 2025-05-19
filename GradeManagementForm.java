import database.Database;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class GradeManagementForm extends JPanel {
    private JTextField studentRollField;
    private JTextField[] subjectFields;
    private JLabel avgLabel, gradeLabel;
    private JButton calculateBtn, submitBtn;
    private String teacherUsername;

    public GradeManagementForm(String username) {
        this.teacherUsername = username;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setBackground(Color.WHITE);

        // 🔹 Title
        JLabel titleLabel = new JLabel("Grade Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // 🔹 Form Panel (Center Area)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 15);

        // 🔹 Student Roll No - near top
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Student Roll No:"), gbc);
        studentRollField = new JTextField(15);
        studentRollField.setFont(inputFont);
        gbc.gridx = 1;
        formPanel.add(studentRollField, gbc);

        // 🔹 Subject Marks
        subjectFields = new JTextField[6];
        for (int i = 0; i < 6; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            formPanel.add(new JLabel("Subject " + (i + 1) + " Marks:"), gbc);
            subjectFields[i] = new JTextField(10);
            subjectFields[i].setFont(inputFont);
            gbc.gridx = 1;
            formPanel.add(subjectFields[i], gbc);
        }

        // 🔹 Calculate Grade Button - Centered and Enlarged
        calculateBtn = new JButton("Calculate Grade");
        styleButton(calculateBtn, new Color(52, 152, 219));
        calculateBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(Box.createVerticalStrut(10), gbc); // small space
        gbc.gridy = 8;
        formPanel.add(calculateBtn, gbc);

        // 🔹 Average and Grade Labels
        avgLabel = new JLabel("Average: -");
        gradeLabel = new JLabel("Grade: -");
        avgLabel.setFont(labelFont);
        gradeLabel.setFont(labelFont);
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(avgLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(gradeLabel, gbc);

        // 🔹 Submit Button
        submitBtn = new JButton("Submit Grade");
        styleButton(submitBtn, new Color(39, 174, 96));
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(submitBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 🔹 Event Listeners
        calculateBtn.addActionListener(e -> calculateGrade());
        submitBtn.addActionListener(e -> saveGrade());
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private void calculateGrade() {
        try {
            int total = 0;
            for (JTextField field : subjectFields) {
                total += Integer.parseInt(field.getText());
            }
            float avg = total / 6f;
            float gpa = (total / 600f) * 10;

            char grade;
            if (gpa >= 9) grade = 'A';
            else if (gpa >= 8) grade = 'B';
            else if (gpa >= 6.5) grade = 'C';
            else if (gpa >= 5) grade = 'D';
            else grade = 'F';

            avgLabel.setText("Average: " + avg);
            gradeLabel.setText("Grade: " + grade + " (GPA: " + String.format("%.2f", gpa) + ")");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter valid marks for all subjects.");
        }
    }

    private void saveGrade() {
        try (Connection conn = Database.getConnection()) {
            String roll = studentRollField.getText().trim();
            if (roll.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the student roll number.");
                return;
            }

            int[] marks = new int[6];
            int total = 0;
            for (int i = 0; i < 6; i++) {
                marks[i] = Integer.parseInt(subjectFields[i].getText());
                total += marks[i];
            }

            float avg = total / 6f;
            float gpa = (total / 600f) * 10;
            char grade;
            if (gpa >= 9) grade = 'A';
            else if (gpa >= 8) grade = 'B';
            else if (gpa >= 6.5) grade = 'C';
            else if (gpa >= 5) grade = 'D';
            else grade = 'F';

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO grades (student_roll, teacher_username, subject1, subject2, subject3, subject4, subject5, subject6, average, grade, gpa) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE subject1=?, subject2=?, subject3=?, subject4=?, subject5=?, subject6=?, average=?, grade=?, gpa=?"
            );

            ps.setString(1, roll);
            ps.setString(2, teacherUsername);
            for (int i = 0; i < 6; i++) ps.setInt(i + 3, marks[i]);
            ps.setFloat(9, avg);
            ps.setString(10, String.valueOf(grade));
            ps.setFloat(11, gpa);

            for (int i = 0; i < 6; i++) ps.setInt(i + 12, marks[i]);
            ps.setFloat(18, avg);
            ps.setString(19, String.valueOf(grade));
            ps.setFloat(20, gpa);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Grade saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving grade.");
        }
    }
}
