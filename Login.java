import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.sql.*;

public class Login {

    private int usr = 0;

    public void loginView() {
        JFrame frame = new JFrame();
        Font text = new Font("Times New Roman", Font.PLAIN, 20);
        Home hm = new Home();
        TeacherView tview = new TeacherView();
        StudentView sview = new StudentView();

        // ------------------------- LOGO --------------------------
        JLabel attendance = new JLabel("ATTENDANCE");
        attendance.setForeground(Color.decode("#37474F"));
        attendance.setBounds(100, 275, 400, 50);
        attendance.setFont(new Font("Verdana", Font.BOLD, 50));
        frame.add(attendance);

        JLabel management = new JLabel("MANAGEMENT SYSTEM");
        management.setForeground(Color.decode("#37474F"));
        management.setBounds(280, 310, 400, 50);
        management.setFont(new Font("Verdana", Font.BOLD, 15));
        frame.add(management);

        // ------------------ Panel ------------------
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 500, 600);
        panel.setBackground(Color.decode("#DEE4E7"));
        frame.add(panel);

        // ---------------- Close Button ----------------
        JLabel x = new JLabel("X");
        x.setForeground(Color.decode("#DEE4E7"));
        x.setBounds(965, 20, 100, 20);
        x.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame.add(x);
        x.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // ---------------- Minimize Button ----------------
        JLabel min = new JLabel("_");
        min.setForeground(Color.decode("#DEE4E7"));
        min.setBounds(935, 10, 100, 20);
        min.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame.add(min);
        min.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setState(JFrame.ICONIFIED);
            }
        });

        // ---------------- Login Text ----------------
        JLabel lgn = new JLabel("LOGIN");
        lgn.setForeground(Color.decode("#DEE4E7"));
        lgn.setBounds(625, 100, 350, 75);
        lgn.setFont(new Font("Times New Roman", Font.BOLD, 75));
        frame.add(lgn);

        // ---------------- Username Field ----------------
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.decode("#DEE4E7"));
        userLabel.setBounds(570, 250, 100, 20);
        userLabel.setFont(text);
        frame.add(userLabel);

        JTextField username = new JTextField();
        username.setBounds(570, 285, 360, 35);
        username.setBackground(Color.decode("#DEE4E7"));
        username.setForeground(Color.decode("#37474F"));
        username.setFont(new Font("Times New Roman", Font.BOLD, 15));
        frame.add(username);

        // ---------------- Password Field -----------
