import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class StudentDashboard extends JFrame {
    private String studentName;
    private String studentRoll;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public StudentDashboard(String studentName, String studentRoll) {
        this.studentName = studentName;
        this.studentRoll = studentRoll;

        setTitle("Student Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ====== HEADER ======
        JLabel title = new JLabel("Student Dashboard - Welcome " + studentName + " (" + studentRoll + ")", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // ====== SIDEBAR ======
        JPanel sidebar = new JPanel(new GridLayout(10, 1, 10, 10));
        sidebar.setBackground(new Color(45, 52, 54)); // dark gray
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        String[] btnTexts = {
            "View Attendance",
            "View Grades",
            "View Messages",
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

        // ====== Button Actions ======
        buttons[0].addActionListener(e -> {
            contentPanel.add(new ViewAttendanceForStudent(studentRoll), "viewAttendance");
            cardLayout.show(contentPanel, "viewAttendance");
        });

        buttons[1].addActionListener(e -> {
            contentPanel.add(new ViewGradesForStudent(studentRoll), "viewGrades");
            cardLayout.show(contentPanel, "viewGrades");
        });

        buttons[2].addActionListener(e -> {
            contentPanel.add(new StudentMessagingForm(studentRoll), "viewMessages");
            cardLayout.show(contentPanel, "viewMessages");
        });

        buttons[3].addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(sidebar, BorderLayout.WEST);

        // ====== MAIN CONTENT AREA ======
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Your Dashboard, " + studentName + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 22));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        contentPanel.add(welcomePanel, "welcome");

        add(contentPanel, BorderLayout.CENTER);
    }

    // ====== MAIN METHOD FOR TESTING ======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard("John Doe", "12345").setVisible(true));
    }
}
