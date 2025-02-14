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
import javax.swing.*;

import javax.swing.table.DefaultTableModel;

public class ClassManager {
    DefaultTableModel model = new DefaultTableModel();
    int check;
    JButton edit, delete, add;

    public void classView() {
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

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1000, 35);
        panel.setBackground(Color.decode("#DEE4E7"));
        frame.add(panel);

        JLabel id = new JLabel("ID : ");
        id.setFont(text);
        id.setBounds(25, 150, 40, 20);
        id.setForeground(Color.decode("#DEE4E7"));
        frame.add(id);

        JTextField idbox = new JTextField();
        idbox.setBounds(60, 150, 50, 25);
        idbox.setBackground(Color.decode("#DEE4E7"));
        idbox.setFont(text);
        idbox.setForeground(Color.decode("#37474F"));
        idbox.setEditable(false);
        frame.add(idbox);

        JLabel nm = new JLabel("NAME : ");
        nm.setFont(text);
        nm.setBounds(25, 240, 150, 20);
        nm.setForeground(Color.decode("#DEE4E7"));
        frame.add(nm);

        JTextField name = new JTextField();
        name.setBounds(25, 270, 400, 35);
        name.setBackground(Color.decode("#DEE4E7"));
        name.setFont(text);
        name.setForeground(Color.decode("#37474F"));
        name.setEditable(false);
        frame.add(name);

        JButton save = new JButton("SAVE");
        save.setBounds(25, 500, 125, 50);
        save.setFont(btn);
        save.setBackground(Color.decode("#DEE4E7"));
        save.setForeground(Color.decode("#37474F"));
        save.setEnabled(false);
        frame.add(save);
        save.addActionListener(e -> {
            try {
                if (check == 1) {
                    adder(Integer.parseInt(idbox.getText()), name.getText());
                } else if (check == 2) {
                    editor(Integer.parseInt(idbox.getText()), name.getText());
                }
                idbox.setText(String.valueOf(getid()));
                edit.setEnabled(false);
                delete.setEnabled(false);
                name.setText("");
                model.setRowCount(0);
                tblupdt();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        edit = new JButton("EDIT");
        edit.setBounds(175, 500, 125, 50);
        edit.setFont(btn);
        edit.setEnabled(false);
        edit.setBackground(Color.decode("#DEE4E7"));
        edit.setForeground(Color.decode("#37474F"));
        frame.add(edit);
        edit.addActionListener(e -> {
            edit.setEnabled(false);
            save.setEnabled(true);
            check = 2;
            name.setEditable(true);
        });

        add = new JButton("ADD");
        add.setBounds(325, 500, 125, 50);
        add.setFont(btn);
        add.setBackground(Color.decode("#DEE4E7"));
        add.setForeground(Color.decode("#37474F"));
        frame.add(add);
        add.addActionListener(e -> {
            add.setEnabled(false);
            delete.setEnabled(false);
            save.setEnabled(true);
            name.setEditable(true);
            check = 1;
            try {
                idbox.setText(String.valueOf(getid()));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        delete = new JButton("DELETE");
        delete.setBounds(175, 432, 125, 50);
        delete.setFont(btn);
        delete.setBackground(Color.decode("#DEE4E7"));
        delete.setForeground(Color.decode("#37474F"));
        delete.setEnabled(false);
        frame.add(delete);
        delete.addActionListener(e -> {
            try {
                deleter(Integer.parseInt(idbox.getText()));
                idbox.setText(String.valueOf(getid()));
                name.setText("");
                model.setRowCount(0);
                tblupdt();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JTable table = new JTable(model);
        model.addColumn("ID");
        model.addColumn("NAME");
        tblupdt();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                idbox.setText(String.valueOf(model.getValueAt(row, 0)));
                name.setText(String.valueOf(model.getValueAt(row, 1)));
                edit.setEnabled(true);
                save.setEnabled(false);
                delete.setEnabled(true);
            }
        });

        JScrollPane scPane = new JScrollPane(table);
        scPane.setBounds(500, 50, 480, 525);
        frame.add(scPane);

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

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/attendance";
        String user = "root";
        String pass = "password";
        return DriverManager.getConnection(url, user, pass);
    }

    private void tblupdt() {
        try (Connection con = getConnection(); Statement stm = con.createStatement(); ResultSet res = stm.executeQuery("SELECT * FROM class")) {
            while (res.next()) {
                model.addRow(new Object[]{res.getInt("id"), res.getString("name")});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int getid() throws SQLException {
        try (Connection con = getConnection(); Statement stm = con.createStatement(); ResultSet rst = stm.executeQuery("SELECT MAX(id) FROM class")) {
            return rst.next() ? rst.getInt(1) + 1 : 1;
        }
    }

    private void adder(int id, String name) throws SQLException {
        try (Connection con = getConnection(); Statement stm = con.createStatement()) {
            stm.executeUpdate("INSERT INTO class VALUES (" + id + ", '" + name + "')");
        }
    }

    private void deleter(int id) throws SQLException {
        try (Connection con = getConnection(); Statement stm = con.createStatement()) {
            stm.executeUpdate("DELETE FROM class WHERE id = " + id);
        }
    }

    private void editor(int id, String name) throws SQLException {
        try (Connection con = getConnection(); Statement stm = con.createStatement()) {
            stm.executeUpdate("UPDATE class SET name = '" + name + "' WHERE id = " + id);
        }
    }
}
