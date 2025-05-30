package com.idarma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardPage extends JFrame {

    private final User currentUser;

    private JTable leaderboardTable;

    public LeaderboardPage(User currentUser) {
        this.currentUser = currentUser;

        setTitle("Leaderboard");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        String[] columnNames = {"No", "Username", "Total Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<DatabaseHelper.LeaderboardEntry> leaderboard = DatabaseHelper.getLeaderboard();

        for (DatabaseHelper.LeaderboardEntry entry : leaderboard) {
            Object[] rowData = {
                    entry.getRank(),
                    entry.getUsername(),
                    entry.getTotalScore()
            };
            model.addRow(rowData);
        }

        leaderboardTable = new JTable(model);
        leaderboardTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);

        JButton backButton = new JButton("Kembali ke Home");
        backButton.addActionListener(e -> {
            dispose();
            new HomePage(currentUser);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
