package com.idarma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterPage extends JFrame {
    public RegisterPage() {
        setTitle("Register");
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

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);

        JLabel nameLabel = new JLabel("Nama Lengkap:");
        nameLabel.setBounds(30, 20, 100, 25);
        registerPanel.add(nameLabel);

        JTextField nameText = new JTextField();
        nameText.setBounds(140, 20, 150, 25);
        registerPanel.add(nameText);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 60, 100, 25);
        registerPanel.add(userLabel);

        JTextField userText = new JTextField();
        userText.setBounds(140, 60, 150, 25);
        registerPanel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 100, 100, 25);
        registerPanel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField();
        passwordText.setBounds(140, 100, 150, 25);
        registerPanel.add(passwordText);

        JButton registerButton = new JButton("Buat Akun");
        registerButton.setBounds(140, 140, 100, 25);
        registerPanel.add(registerButton);

        JButton backButton = new JButton("Kembali ke Login");
        backButton.setBounds(140, 180, 150, 25);
        registerPanel.add(backButton);

        backButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        registerButton.addActionListener((ActionEvent e) -> {
            String fullName = nameText.getText().trim();
            String username = userText.getText().trim();
            String password = new String(passwordText.getPassword());

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
                return;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            Connection conn = ConnectionUtil.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Koneksi ke database gagal. Silakan cek koneksi Anda.");
                return;
            }

            try {
                String sql = "INSERT INTO users (full_name, username, password_hash) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, fullName);
                stmt.setString(2, username);
                stmt.setString(3, hashedPassword);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Registrasi berhasil!");
                dispose();
                new LoginPage();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Registrasi gagal: " + ex.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException exception) {
                    System.err.println("Gagal menutup koneksi database: " + exception.getMessage());
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logoPanel, registerPanel);
        splitPane.setDividerLocation(250);
        splitPane.setEnabled(false); // nonaktifkan drag
        add(splitPane);

        setVisible(true);
    }
}
