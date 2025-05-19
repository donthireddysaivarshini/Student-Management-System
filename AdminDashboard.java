

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class AdminDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ====== HEADER ======
        JLabel title = new JLabel("Admin Dashboard - Student Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // ====== SIDEBAR PANEL ======
        JPanel sidebar = new JPanel(new GridLayout(10, 1, 10, 10));
        sidebar.setBackground(new Color(45, 52, 54)); // dark gray
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Create buttons
        String[] btnTexts = {
            "Add Student",
            "View All Students",
            "Update Student",
            "Delete Student",
            "Search Student",
            "Add Teacher Login",
            "View All Teachers",
            "Logout"
        };

        JButton[] buttons = new JButton[btnTexts.length];
        for (int i = 0; i < btnTexts.length; i++) {
            buttons[i] = new JButton(btnTexts[i]);
            buttons[i].setFont(new Font("Segoe UI", Font.PLAIN, 18));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(new Color(99, 110, 114));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            sidebar.add(buttons[i]);
        }

        // Button actions
        buttons[0].addActionListener(e -> cardLayout.show(contentPanel, "addStudent"));
        buttons[1].addActionListener(e -> {
        contentPanel.add(new ViewStudents(), "viewStudents");
        cardLayout.show(contentPanel, "viewStudents");
        });


        buttons[2].addActionListener(e -> {
        contentPanel.add(new UpdateStudent(), "updateStudent");
        cardLayout.show(contentPanel, "updateStudent");
    });

        buttons[3].addActionListener(e -> {
        contentPanel.add(new DeleteStudent(), "deleteStudent");
        cardLayout.show(contentPanel, "deleteStudent");
        });

        buttons[4].addActionListener(e -> {
        contentPanel.add(new SearchStudent(), "searchStudent");
        cardLayout.show(contentPanel, "searchStudent");
        });

        buttons[5].addActionListener(e -> {
    contentPanel.add(new AddTeacherForm(), "addTeacher");
    cardLayout.show(contentPanel, "addTeacher");
});

        buttons[6].addActionListener(e -> {
    contentPanel.add(new ViewTeachers(), "viewTeachers");
    cardLayout.show(contentPanel, "viewTeachers");
});

        buttons[7].addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(sidebar, BorderLayout.WEST);

        // ====== MAIN CONTENT (with CardLayout) ======
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Welcome Panel (initial screen)
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, Admin!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 22));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        contentPanel.add(welcomePanel, "welcome");

        // AddStudentForm Panel
        contentPanel.add(new AddStudentForm(), "addStudent");

        add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
