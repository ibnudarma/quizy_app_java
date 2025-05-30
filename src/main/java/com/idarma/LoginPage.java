package com.idarma;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class LoginPage extends JFrame {
    public LoginPage() {
        setTitle("Login");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setBackground(Color.WHITE);

        try {
            ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));
            Image scaledImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoPanel.add(logoLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            logoPanel.add(new JLabel("Logo tidak ditemukan"), BorderLayout.CENTER);
        }

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        loginPanel.add(userLabel);

        JTextField userText = new JTextField();
        userText.setBounds(120, 30, 150, 25);
        loginPanel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        loginPanel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField();
        passwordText.setBounds(120, 70, 150, 25);
        loginPanel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(120, 110, 80, 25);
        loginPanel.add(loginButton);

        JButton registerButton = new JButton("Daftar");
        registerButton.setBounds(210, 110, 80, 25);
        loginPanel.add(registerButton);

        loginButton.addActionListener(e -> {
            String username = userText.getText().trim();
            String password = new String(passwordText.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username dan Password tidak boleh kosong.");
                return;
            }

            Connection conn = ConnectionUtil.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Koneksi ke database gagal.");
                return;
            }

            try {
                String sql = "SELECT id, full_name, username, password_hash FROM users WHERE username = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if (BCrypt.checkpw(password, storedHash)) {
                        int id = rs.getInt("id");
                        String fullName = rs.getString("full_name");
                        String user = rs.getString("username");

                        User loggedInUser = new User(id, fullName, user);
                        JOptionPane.showMessageDialog(null, "Login Berhasil");
                        dispose();
                        new HomePage(loggedInUser);  // Kirim objek User ke HomePage
                    } else {
                        JOptionPane.showMessageDialog(null, "Password salah!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username tidak ditemukan!");
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat login: " + ex.getMessage());
            }
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterPage();
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logoPanel, loginPanel);
        splitPane.setDividerLocation(250);
        splitPane.setEnabled(false); // nonaktifkan drag
        add(splitPane);

        setVisible(true);
    }
}
