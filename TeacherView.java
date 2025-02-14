package Attendance;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;

public class TeacherView {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/attendance";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "password";

    public void tcView(int id) {
        JFrame frame = new JFrame("Teacher Dashboard");
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
        createButton(frame, "ADD ATTENDANCE", 150, 200, 650, 60, btnFont, e -> openAddAttendanceView());
        createButton(frame, "EDIT ATTENDANCE", 150, 350, 650, 60, btnFont, e -> openEditAttendanceView());

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
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
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

    private void openAddAttendanceView() {
        try {
            new AddAttendance().addView();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openEditAttendanceView() {
        try {
            new EditAttendance().editView();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
