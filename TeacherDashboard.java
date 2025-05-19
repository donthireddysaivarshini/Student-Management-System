import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class TeacherDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private String teacherUsername;

    public TeacherDashboard(String username) {
        this.teacherUsername = username;

        setTitle("Teacher Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ====== HEADER ======
        JLabel title = new JLabel("Teacher Dashboard - Class Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // ====== SIDEBAR PANEL ======
        JPanel sidebar = new JPanel(new GridLayout(10, 1, 10, 10));
        sidebar.setBackground(new Color(45, 52, 54)); // dark gray
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        String[] buttonLabels = {
            "View Students",
            "Mark Attendance",
            "Grade Management",
            "View Grades",
            "Messaging System",
            "Logout"
        };

        JButton[] buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Segoe UI", Font.PLAIN, 18));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(new Color(99, 110, 114));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            sidebar.add(buttons[i]);
        }

        // ====== MAIN CONTENT (with CardLayout) ======
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // ====== Add Panels ======
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + teacherUsername + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        contentPanel.add(welcomePanel, "welcome");

        // Initialize panels (add them only when clicked to avoid unnecessary load)
        buttons[0].addActionListener(e -> {
            contentPanel.add(new ViewStudents(), "viewStudents");
            cardLayout.show(contentPanel, "viewStudents");
        });

        buttons[1].addActionListener(e -> {
            contentPanel.add(new MarkAttendanceForm(teacherUsername), "markAttendance");
            cardLayout.show(contentPanel, "markAttendance");
        });

        buttons[2].addActionListener(e -> {
            contentPanel.add(new GradeManagementForm(teacherUsername), "gradeManagement");
            cardLayout.show(contentPanel, "gradeManagement");
        });

        buttons[3].addActionListener(e -> {
            contentPanel.add(new ViewGradesForm(teacherUsername), "viewGrades");
            cardLayout.show(contentPanel, "viewGrades");
        });
        buttons[4].addActionListener(e -> {
    if (!isCardAlreadyAdded("messagingSystem")) {
        contentPanel.add(new MessagingSystemForm(teacherUsername), "messagingSystem");
    }
    cardLayout.show(contentPanel, "messagingSystem");
});


       
        buttons[5].addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Show welcome by default
        cardLayout.show(contentPanel, "welcome");
    }
    private boolean isCardAlreadyAdded(String name) {
    for (Component comp : contentPanel.getComponents()) {
        if (name.equals(contentPanel.getLayout().toString())) {
            return true;
        }
    }
    return false;
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TeacherDashboard("Mr. John").setVisible(true));
    }
}
