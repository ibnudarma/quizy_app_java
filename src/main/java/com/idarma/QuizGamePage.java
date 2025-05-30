package com.idarma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class QuizGamePage extends JFrame {
    private final User user;
    private final String tema;
    private final List<GeminiApi.QuizQuestion> quizList;

    private int currentIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton nextButton;

    public QuizGamePage(User user, String tema, List<GeminiApi.QuizQuestion> quizList) {
        this.user = user;
        this.tema = tema;
        this.quizList = quizList;

        setTitle("Kuis: " + tema);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadQuestion();

        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        questionLabel = new JLabel();
        questionLabel.setBounds(20, 20, 550, 40);
        panel.add(questionLabel);

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setBounds(30, 70 + i * 30, 500, 25);
            buttonGroup.add(optionButtons[i]);
            panel.add(optionButtons[i]);
        }

        nextButton = new JButton("Lanjut");
        nextButton.setBounds(400, 200, 100, 30);
        panel.add(nextButton);

        add(panel);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswerAndProceed();
            }
        });
    }

    private void loadQuestion() {
        if (currentIndex >= quizList.size()) {
            showFinalScore();
            return;
        }

        GeminiApi.QuizQuestion question = quizList.get(currentIndex);
        questionLabel.setText((currentIndex + 1) + ". " + question.question);

        for (int i = 0; i < optionButtons.length; i++) {
            if (i < question.options.size()) {
                optionButtons[i].setText(question.options.get(i));
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }

        buttonGroup.clearSelection();
    }

    private void checkAnswerAndProceed() {
        String selectedAnswer = null;
        for (JRadioButton btn : optionButtons) {
            if (btn.isSelected()) {
                selectedAnswer = btn.getText();
                break;
            }
        }

        if (selectedAnswer == null) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu jawaban.");
            return;
        }

        GeminiApi.QuizQuestion currentQuestion = quizList.get(currentIndex);
        if (selectedAnswer.equalsIgnoreCase(currentQuestion.answer)) {
            score++;
        }

        currentIndex++;
        loadQuestion();
    }

    private void showFinalScore() {
        JOptionPane.showMessageDialog(this, "Kuis selesai!\nSkor kamu: " + score + " dari " + quizList.size());
        saveHistoryToDatabase();
        dispose();
        new HomePage(user);
    }

    private void saveHistoryToDatabase() {
        try (Connection conn = ConnectionUtil.getConnection()) {
            if (conn != null) {
                String sql = "INSERT INTO history (user_id, quiz_name, score) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, user.getId());
                    stmt.setString(2, tema);
                    stmt.setInt(3, score);
                    stmt.executeUpdate();
                    System.out.println("Skor berhasil disimpan ke tabel history.");
                }
            } else {
                System.err.println("Koneksi ke database gagal. Skor tidak disimpan.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan skor ke database: " + e.getMessage());
        }
    }
}
