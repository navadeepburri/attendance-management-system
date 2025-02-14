package Attendance;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EditAttendance {
    
    private Connection con;
    private DefaultTableModel model = new DefaultTableModel();
    
    public void editView() {
        try {
            con = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        
        JFrame frame = new JFrame();
        Font text = new Font("Times New Roman", Font.PLAIN, 18);
        Font btn = new Font("Times New Roman", Font.BOLD, 20);
        
        // Close Button
        JLabel x = new JLabel("X");
        x.setForeground(Color.decode("#37474F"));
        x.setBounds(965, 10, 100, 20);
        x.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame.add(x);
        x.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose(); // Close only this window
            }
        });

        // Back Button
        JLabel back = new JLabel("< BACK");
        back.setForeground(Color.decode("#37474F"));
        back.setFont(new Font("Times New Roman", Font.BOLD, 17));
        back.setBounds(18, 10, 100, 20);
        frame.add(back);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
            }
        });

        // Panel
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1000, 35);
        panel.setBackground(Color.decode("#DEE4E7"));
        frame.add(panel);

        // Table
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Allow editing only in status column
            }
        };
        model = (DefaultTableModel) table.getModel();
        model.addColumn("ID");
        model.addColumn("NAME");
        model.addColumn("STATUS");
        JScrollPane scPane = new JScrollPane(table);
        scPane.setBounds(500, 50, 480, 525);
        frame.add(scPane);

        // Date Field
        JLabel dt = new JLabel("DATE : ");
        dt.setFont(text);
        dt.setBounds(25, 60, 75, 20);
        dt.setForeground(Color.decode("#DEE4E7"));
        frame.add(dt);
        JTextField dtbox = new JTextField();
        dtbox.setBounds(100, 60, 150, 25);
        dtbox.setBackground(Color.decode("#DEE4E7"));
        dtbox.setFont(text);
        dtbox.setForeground(Color.decode("#37474F"));
        dtbox.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        frame.add(dtbox);

        // Class Selection
        JLabel classes = new JLabel("CLASS : ");
        classes.setFont(text);
        classes.setBounds(25, 150, 100, 20);
        classes.setForeground(Color.decode("#DEE4E7"));
        frame.add(classes);
        JComboBox<String> clss = new JComboBox<>(getClassList());
        clss.setBounds(110, 150, 50, 25);
        frame.add(clss);

        // View Button
        JButton view = new JButton("VIEW");
        view.setBounds(175, 275, 150, 50);
        view.setFont(btn);
        view.setBackground(Color.decode("#DEE4E7"));
        view.setForeground(Color.decode("#37474F"));
        frame.add(view);
        view.addActionListener(e -> updateTable((String) clss.getSelectedItem(), dtbox.getText()));

        // Absent Button
        JButton ab = new JButton("ABSENT");
        ab.setBounds(75, 365, 150, 50);
        ab.setFont(btn);
        ab.setBackground(Color.decode("#DEE4E7"));
        ab.setForeground(Color.decode("#37474F"));
        frame.add(ab);
        ab.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                table.setValueAt("Absent", selectedRow, 2);
            }
        });

        // Present Button
        JButton pre = new JButton("PRESENT");
        pre.setBounds(275, 365, 150, 50);
        pre.setFont(btn);
        pre.setBackground(Color.decode("#DEE4E7"));
        pre.setForeground(Color.decode("#37474F"));
        frame.add(pre);
        pre.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                table.setValueAt("Present", selectedRow, 2);
            }
        });

        // Submit Button
        JButton sbmt = new JButton("SUBMIT");
        sbmt.setBounds(75, 450, 150, 50);
        sbmt.setFont(btn);
        sbmt.setBackground(Color.decode("#DEE4E7"));
        sbmt.setForeground(Color.decode("#37474F"));
        frame.add(sbmt);
        sbmt.addActionListener(e -> {
            for (int i = 0; i < table.getRowCount(); i++) {
                try {
                    updateAttendance(
                        Integer.parseInt(String.valueOf(table.getValueAt(i, 0))),
                        String.valueOf(table.getValueAt(i, 2)),
                        dtbox.getText()
                    );
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            model.setRowCount(0); // Clear table
        });

        // Frame Settings
        frame.setSize(1000, 600);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.decode("#37474F"));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public String[] getClassList() {
        try (Statement stm = con.createStatement();
             ResultSet rst = stm.executeQuery("SELECT name FROM class")) {
            String[] classes = new String[25];
            int i = 0;
            while (rst.next()) {
                classes[i++] = rst.getString("name");
            }
            return classes;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public void updateTable(String classes, String dt) {
        model.setRowCount(0); // Clear table
        try (PreparedStatement pstmt = con.prepareStatement(
            "SELECT students.id, students.name, attend.status FROM attend JOIN students ON attend.stid = students.id WHERE attend.class = ? AND attend.dt = ?")) {
            pstmt.setString(1, classes);
            pstmt.setString(2, dt);
            ResultSet res = pstmt.executeQuery();
            while (res.next()) {
                model.addRow(new Object[]{res.getInt("id"), res.getString("name"), res.getString("status")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAttendance(int id, String status, String date) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(
            "UPDATE attend SET status = ? WHERE stid = ? AND dt = ?")) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.setString(3, date);
            pstmt.executeUpdate();
        }
    }
}
