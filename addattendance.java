package Attendance;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AddAttendance {
    Connection con;
    DefaultTableModel model = new DefaultTableModel();

    public void addView() throws SQLException {
        connect();
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
                System.exit(0);
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
        JTable table = new JTable(new DefaultTableModel(new Object[]{"ID", "NAME", "STATUS"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        model = (DefaultTableModel) table.getModel();
        JScrollPane scPane = new JScrollPane(table);
        scPane.setBounds(500, 50, 480, 525);
        frame.add(scPane);

        // Date Field
        JLabel dt = new JLabel("DATE : ");
        dt.setFont(text);
        dt.setBounds(25, 60, 75, 20);
        dt.setForeground(Color.decode("#DEE4E7"));
        frame.add(dt);

        JTextField dtbox = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dtbox.setBounds(100, 60, 150, 25);
        dtbox.setBackground(Color.decode("#DEE4E7"));
        dtbox.setFont(text);
        dtbox.setForeground(Color.decode("#37474F"));
        frame.add(dtbox);

        // Class Selection
        JLabel classes = new JLabel("CLASS : ");
        classes.setFont(text);
        classes.setBounds(25, 150, 100, 20);
        classes.setForeground(Color.decode("#DEE4E7"));
        frame.add(classes);

        JComboBox<String> clss = new JComboBox<>(classEt());
        clss.setBounds(110, 150, 100, 25);
        frame.add(clss);

        // Attendance Already Marked Message
        JLabel txt = new JLabel("");
        txt.setFont(text);
        txt.setBounds(125, 525, 350, 20);
        txt.setForeground(Color.red);
        frame.add(txt);

        // View Button
        JButton view = new JButton("VIEW");
        view.setBounds(175, 275, 150, 50);
        view.setFont(btn);
        view.setBackground(Color.decode("#DEE4E7"));
        view.setForeground(Color.decode("#37474F"));
        frame.add(view);
        view.addActionListener(e -> {
            try {
                if (check(String.valueOf(clss.getSelectedItem()), dtbox.getText())) {
                    txt.setText("Attendance Already Marked!!!");
                } else {
                    tblupdt(String.valueOf(clss.getSelectedItem()));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Absent Button
        JButton ab = new JButton("ABSENT");
        ab.setBounds(75, 365, 150, 50);
        ab.setFont(btn);
        ab.setBackground(Color.decode("#DEE4E7"));
        ab.setForeground(Color.decode("#37474F"));
        frame.add(ab);
        ab.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) table.setValueAt("Absent", row, 2);
        });

        // Present Button
        JButton pre = new JButton("PRESENT");
        pre.setBounds(275, 365, 150, 50);
        pre.setFont(btn);
        pre.setBackground(Color.decode("#DEE4E7"));
        pre.setForeground(Color.decode("#37474F"));
        frame.add(pre);
        pre.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) table.setValueAt("Present", row, 2);
        });

        // Submit Button
        JButton sbmt = new JButton("SUBMIT");
        sbmt.setBounds(175, 450, 150, 50);
        sbmt.setFont(btn);
        sbmt.setBackground(Color.decode("#DEE4E7"));
        sbmt.setForeground(Color.decode("#37474F"));
        frame.add(sbmt);
        sbmt.addActionListener(e -> {
            for (int i = 0; i < table.getRowCount(); i++) {
                try {
                    addItem(Integer.parseInt(String.valueOf(table.getValueAt(i, 0))),
                            String.valueOf(table.getValueAt(i, 2)),
                            dtbox.getText(),
                            String.valueOf(clss.getSelectedItem()));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            model.setRowCount(0);
        });

        // Frame Settings
        frame.setSize(1000, 600);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.decode("#37474F"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/attendance";
        String user = "root";
        String pass = "password";
        con = DriverManager.getConnection(url, user, pass);
    }

    public String[] classEt() throws SQLException {
        String str1 = "SELECT name FROM class";
        Statement stm = con.createStatement();
        ResultSet rst = stm.executeQuery(str1);
        ArrayList<String> classList = new ArrayList<>();
        while (rst.next()) {
            classList.add(rst.getString("name"));
        }
        return classList.toArray(new String[0]);
    }

    public void tblupdt(String classes) {
        model.setRowCount(0);
        try {
            ResultSet res = dbSearch(classes);
            while (res.next()) {
                model.addRow(new Object[]{res.getInt("id"), res.getString("name"), "Present"});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet dbSearch(String classes) throws SQLException {
        String query = "SELECT * FROM students WHERE class = '" + classes + "'";
        Statement stm = con.createStatement();
        return stm.executeQuery(query);
    }

    public void addItem(int id, String status, String date, String classes) throws SQLException {
        String query = "INSERT INTO attend VALUES(" + id + ", '" + date + "', '" + status + "', '" + classes + "')";
        Statement stm = con.createStatement();
        stm.executeUpdate(query);
    }

    public boolean check(String classes, String dt) throws SQLException {
        String query = "SELECT * FROM attend WHERE class = '" + classes + "' AND dt = '" + dt + "'";
        Statement stm = con.createStatement();
        return stm.executeQuery(query).next();
    }
}
