package Attendance;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentView {
    private JFrame frame;
    private DefaultTableModel model;

    public void stView(int id) {
        frame = new JFrame("Student Dashboard");
        frame.setLayout(null);

        addControlButtons();
        addWelcomeLabel(id);
        addAttendanceTable(id);
        addAttendanceStats(id);
        setupFrame();
    }

    private void addControlButtons() {
        JLabel closeBtn = createLabel("X", 965, 10, 20, e -> frame.dispose());
        JLabel minimizeBtn = createLabel("_", 935, 0, 20, e -> frame.setState(JFrame.ICONIFIED));

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1000, 35);
        panel.setBackground(Color.decode("#DEE4E7"));
        panel.setLayout(null);
        panel.add(closeBtn);
        panel.add(minimizeBtn);

        frame.add(panel);
    }

    private void addWelcomeLabel(int id) {
        JLabel welcomeLabel = createLabel("Welcome " + getUser(id) + ",", 10, 50, 20, null);
        frame.add(welcomeLabel);
    }

    private void addAttendanceTable(int id) {
        model = new DefaultTableModel(new String[]{"DATE", "STATUS"}, 0);

        JTable table = new JTable(model);
        table.setFont(new Font("Times New Roman", Font.BOLD, 20));
        table.setRowHeight(50);

        JScrollPane scPane = new JScrollPane(table);
        scPane.setBounds(500, 50, 480, 525);
        frame.add(scPane);

        updateTable(id);
    }

    private void addAttendanceStats(int id) {
        String[] labels = {"TOTAL CLASSES:", "CLASSES ATTENDED:", "CLASSES MISSED:", "ATTENDANCE PERCENTAGE:"};
        int[] stats = getAttendanceStats(id);

        for (int i = 0; i < labels.length; i++) {
            frame.add(createLabel(labels[i], 25, 180 + (i * 100), 20, null));
            frame.add(createLabel(i == 3 ? stats[i] + "%" : String.valueOf(stats[i]), 200, 180 + (i * 100), 20, null));
        }
    }

    private void setupFrame() {
        frame.setSize(1000, 600);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.getContentPane().setBackground(Color.decode("#37474F"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JLabel createLabel(String text, int x, int y, int fontSize, MouseAdapter action) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 300, 20);
        label.setForeground(Color.decode("#DEE4E7"));
        label.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
        if (action != null) label.addMouseListener(action);
        return label;
    }

    private String getUser(int id) {
        String query = "SELECT name FROM user WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rst = pst.executeQuery()) {
                if (rst.next()) return rst.getString("name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "User";
    }

    private void updateTable(int id) {
        String query = "SELECT dt, status FROM attend WHERE stid = ? ORDER BY dt DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    model.addRow(new Object[]{res.getString("dt"), res.getString("status")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int[] getAttendanceStats(int id) {
        int[] stats = new int[4];
        String query = "SELECT " +
                       "COUNT(*) AS total, " +
                       "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) AS attended, " +
                       "SUM(CASE WHEN status = 'Absent' THEN 1 ELSE 0 END) AS missed " +
                       "FROM attend WHERE stid = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rst = pst.executeQuery()) {
                if (rst.next()) {
                    stats[0] = rst.getInt("total");
                    stats[1] = rst.getInt("attended");
                    stats[2] = rst.getInt("missed");
                    stats[3] = (stats[0] > 0) ? (stats[1] * 100) / stats[0] : 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}
