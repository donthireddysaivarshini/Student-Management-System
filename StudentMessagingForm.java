import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import database.Database;

public class StudentMessagingForm extends JPanel {
    private JComboBox<String> teacherDropdown;
    private JPanel chatPanel;
    private JScrollPane chatScroll;
    private JTextArea messageInput;
    private JButton sendBtn;
    private String studentRoll;

    public StudentMessagingForm(String studentRoll) {
        this.studentRoll = studentRoll;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === TOP PANEL (Teacher Selection) ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Teacher:"));
        teacherDropdown = new JComboBox<>();
        loadTeachers();
        topPanel.add(teacherDropdown);
        add(topPanel, BorderLayout.NORTH);

        teacherDropdown.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadMessages();
            }
        });

        // === CHAT PANEL ===
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        chatScroll = new JScrollPane(chatPanel);
        chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(chatScroll, BorderLayout.CENTER);

        // === MESSAGE INPUT PANEL ===
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageInput = new JTextArea(3, 30);
        messageInput.setLineWrap(true);
        messageInput.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(messageInput), BorderLayout.CENTER);

        sendBtn = new JButton("Send");
        sendBtn.setBackground(new Color(40, 167, 69)); // green
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFocusPainted(false);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(sendBtn, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        sendBtn.addActionListener(e -> sendMessage());

        loadMessages(); // default load
    }

    private void loadTeachers() {
        try (Connection conn = Database.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT username FROM teachers");
            while (rs.next()) {
                teacherDropdown.addItem(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSelectedTeacherUsername() {
        return (String) teacherDropdown.getSelectedItem();
    }

    private void loadMessages() {
        String teacherUsername = getSelectedTeacherUsername();
        if (teacherUsername == null) return;

        chatPanel.removeAll();

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT sender_username, message, sent_at FROM messages " +
                "WHERE (sender_username=? AND receiver_username=?) OR (sender_username=? AND receiver_username=?) " +
                "ORDER BY sent_at"
            );
            ps.setString(1, studentRoll);
            ps.setString(2, teacherUsername);
            ps.setString(3, teacherUsername);
            ps.setString(4, studentRoll);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("sender_username");
                String message = rs.getString("message");
                Timestamp timestamp = rs.getTimestamp("sent_at");

                JLabel msgLabel = new JLabel("<html><body style='width: 200px;'>" + message + "<br><small><i>" + timestamp + "</i></small></body></html>");
                msgLabel.setOpaque(true);
                msgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                JPanel msgWrapper = new JPanel(new FlowLayout(sender.equals(studentRoll) ? FlowLayout.RIGHT : FlowLayout.LEFT));
                msgWrapper.setBackground(Color.WHITE);

                if (sender.equals(studentRoll)) {
                    msgLabel.setBackground(new Color(204, 229, 255)); // Light Blue
                } else {
                    msgLabel.setBackground(new Color(220, 248, 198)); // Light Green
                }

                msgWrapper.add(msgLabel);
                chatPanel.add(msgWrapper);
            }

            chatPanel.revalidate();
            chatPanel.repaint();
            JScrollBar vertical = chatScroll.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();
        String teacherUsername = getSelectedTeacherUsername();
        if (message.isEmpty() || teacherUsername == null) return;

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)"
            );
            ps.setString(1, studentRoll);
            ps.setString(2, teacherUsername);
            ps.setString(3, message);
            ps.executeUpdate();
            messageInput.setText("");
            loadMessages();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
