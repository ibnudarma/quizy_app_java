package com.idarma;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GeminiApiTest {

    @Test
    void testGenerateContent() throws Exception {
            List<GeminiApi.QuizQuestion> result = GeminiApi.generateQuiz("Python", 5);
            result.forEach(quizQuestion -> {
                System.out.println(quizQuestion.answer);
            });

    }
}
