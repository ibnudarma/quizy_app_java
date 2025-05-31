package com.idarma;

import javax.swing.*;
import java.util.List;

public class QuizInputPage extends JFrame {
    private final User user;

    public QuizInputPage(User user) {
        this.user = user;

        setTitle("Input Tema dan Jumlah Quiz");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel labelTema = new JLabel("Masukkan tema quiz (5 - 30 karakter):");
        labelTema.setBounds(10, 20, 350, 25);
        panel.add(labelTema);

        JTextField themeInput = new JTextField();
        themeInput.setBounds(10, 50, 360, 25);
        panel.add(themeInput);

        JLabel labelJumlah = new JLabel("Masukkan jumlah soal (1 - 10):");
        labelJumlah.setBounds(10, 90, 350, 25);
        panel.add(labelJumlah);

        JTextField jumlahInput = new JTextField();
        jumlahInput.setBounds(10, 120, 360, 25);
        panel.add(jumlahInput);

        JButton submitBtn = new JButton("Generate Quiz");
        submitBtn.setBounds(10, 160, 150, 30);
        panel.add(submitBtn);

        add(panel);
        setVisible(true);

        submitBtn.addActionListener(e -> {
            String tema = themeInput.getText().trim();
            String jumlahStr = jumlahInput.getText().trim();

            if (tema.isEmpty() || tema.length() < 5 || tema.length() > 30) {
                JOptionPane.showMessageDialog(this, "Tema harus diisi, minimal 5 dan maksimal 20 karakter.");
                return;
            }

            int jumlahSoal;
            try {
                jumlahSoal = Integer.parseInt(jumlahStr);
                if (jumlahSoal < 1 || jumlahSoal > 10) {
                    JOptionPane.showMessageDialog(this, "Jumlah soal harus antara 1 sampai 10.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah soal harus berupa angka yang valid.");
                return;
            }

            submitBtn.setEnabled(false);
            submitInBackground(tema, jumlahSoal, submitBtn);
        });

    }

    private void submitInBackground(String tema, int jumlahSoal, JButton buttonToEnable) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private List<GeminiApi.QuizQuestion> quizList;

            @Override
            protected Void doInBackground() {
                try {
                    quizList = GeminiApi.generateQuiz(tema, jumlahSoal);
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
                            "Gagal membuat kuis. Coba tema lain atau jumlah soal lain.",
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        worker.execute();
    }
}
