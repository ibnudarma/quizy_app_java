package com.idarma;
import javax.swing.*;

public class HomePage extends JFrame {
    private User user;
    public HomePage(User user) {
        this.user = user;
        setTitle("Home - " + user.getUsername());
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JButton startButton = new JButton("Start Game");
        startButton.setBounds(80, 20, 130, 25);
        panel.add(startButton);

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setBounds(80, 60, 130, 25);
        panel.add(leaderboardButton);

        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(80, 100, 130, 25);
        panel.add(quitButton);

        startButton.addActionListener(e -> {
            dispose();
            new QuizInputPage(user);
        });

        leaderboardButton.addActionListener(e -> {
            dispose();
            new LeaderboardPage(user);
        });

        quitButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
    }
}
