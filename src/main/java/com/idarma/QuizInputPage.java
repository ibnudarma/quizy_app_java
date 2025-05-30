package com.idarma;

import javax.swing.*;
import java.util.List;

public class QuizInputPage extends JFrame {
    private final User user;

    public QuizInputPage(User user) {
        this.user = user;

        setTitle("Input Tema Quiz");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("Masukkan tema quiz (max 20 karakter):");
        label.setBounds(10, 20, 350, 25);
        panel.add(label);

        JTextField themeInput = new JTextField();
        themeInput.setBounds(10, 50, 360, 25);
        panel.add(themeInput);

        JButton submitBtn = new JButton("Generate Quiz");
        submitBtn.setBounds(10, 90, 150, 30);
        panel.add(submitBtn);

        add(panel);
        setVisible(true);

        submitBtn.addActionListener(e -> {
            String tema = themeInput.getText().trim();
            if (tema.isEmpty() || tema.length() > 20) {
                JOptionPane.showMessageDialog(this, "Tema harus diisi dan maksimal 20 karakter.");
                return;
            }

            // Disable tombol untuk mencegah klik berulang saat loading
            submitBtn.setEnabled(false);
            submitInBackground(tema, submitBtn);
        });
    }

    private void submitInBackground(String tema, JButton buttonToEnable) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private List<GeminiApi.QuizQuestion> quizList;

            @Override
            protected Void doInBackground() {
                try {
                    quizList = GeminiApi.generateQuiz(tema);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            QuizInputPage.this,
                            "Gagal mengambil soal dari Gemini API:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
                return null;
            }

            @Override
            protected void done() {
                buttonToEnable.setEnabled(true); // Aktifkan kembali tombol
                if (quizList != null && !quizList.isEmpty()) {
                    dispose(); // Tutup halaman input
                    new QuizGamePage(user, tema, quizList); // lanjut ke game
                } else {
                    JOptionPane.showMessageDialog(
                            QuizInputPage.this,
                            "Gagal membuat kuis. Coba tema lain.",
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        worker.execute();
    }
}
