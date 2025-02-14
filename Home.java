import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;

public class Home {
    private final Admin adminInstance = new Admin();

    public void homeView(int id) {
        JFrame frame = new JFrame("Attendance Management System");
        Font btnFont = new Font("Times New Roman", Font.BOLD, 20);

        // Close Button
        JLabel closeBtn = new JLabel("X");
        closeBtn.setForeground(Color.decode("#37474F"));
        closeBtn.setBounds(965, 10, 100, 20);
        closeBtn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame.add(closeBtn);
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
            }
        });

        // Minimize Button
        JLabel minimizeBtn = new JLabel("_");
        minimizeBtn.setForeground(Color.decode("#37474F"));
        minimizeBtn.setBounds(935, 0, 100, 20);
        frame.add(minimizeBtn);
        minimizeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setState(JFrame.ICONIFIED);
            }
        });

        // Top Panel
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1000, 35);
        panel.setBackground(Color.decode("#DEE4E7"));
        frame.add(panel);

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome " + getUser(id) + ",");
        welcomeLabel.setForeground(Color.decode("#DEE4E7"));
        welcomeLabel.setBounds(10, 50, 250, 20);
        welcomeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        frame.add(welcomeLabel);

        // Buttons
        createButton(frame, "STUDENTS", 150, 125, 700, 60, btnFont, e -> openStudentView());
        createButton(frame, "ADD ATTENDANCE", 150, 250, 400, 60, btnFont, e -> openAddAttendanceView());
        createButton(frame, "EDIT ATTENDANCE", 600, 250, 250, 60, btnFont, e -> openEditAttendanceView());
        createButton(frame, "TEACHERS", 150, 375, 700, 60, btnFont, e -> openTeachersView());
        createButton(frame, "ADMIN", 150, 500, 250, 60, btnFont, e -> openAdminView());
        createButton(frame, "CLASS", 450, 500, 400, 60, btnFont, e -> openClassView());

        // Frame Properties
        frame.setSize(1000, 600);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.getContentPane().setBackground(Color.decode("#37474F"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createButton(JFrame frame, String text, int x, int y, int width, int height, Font font, ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(font);
        button.setBackground(Color.decode("#DEE4E7"));
        button.setForeground(Color.decode("#37474F"));
        button.addActionListener(action);
        frame.add(button);
    }

    private String getUser(int id) {
        String query = "SELECT name FROM user WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rst = pst.executeQuery()) {
                if (rst.next()) {
                    return rst.getString("name");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "User";
    }

    private void openStudentView() {
        SwingUtilities.invokeLater(() -> {
            try {
                new Students().studentView();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void openAddAttendanceView() {
        SwingUtilities.invokeLater(() -> {
            try {
                new AddAttendance().addView();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void openEditAttendanceView() {
        SwingUtilities.invokeLater(() -> {
            try {
                new EditAttendance().editView();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void openTeachersView() {
        SwingUtilities.invokeLater(() -> {
            try {
                new Teachers().teachersView();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void openAdminView() {
        SwingUtilities.invokeLater(() -> {
            try {
                adminInstance.adminView();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void openClassView() {
        SwingUtilities.invokeLater(() -> new Class().classView());
    }
}
