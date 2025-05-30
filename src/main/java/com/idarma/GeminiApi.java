package com.idarma;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.util.List;

public class GeminiApi {

    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<QuizQuestion> generateQuiz(String tema) throws Exception {
        try (Client client = new Client()) {
            GenerateContentResponse response = client.models.generateContent(
                    MODEL_NAME,
                    "Buatkan 5 soal kuis pilihan ganda tentang tema: " + tema +
                            ". Formatkan output sebagai JSON array saja, dengan tiap objek berisi 'question', 'options' (array), dan 'answer'.",
                    null
            );

            String text = response.text();
            System.out.println("Raw API response:\n" + text);

            return parseQuizQuestionsFromJson(text);
        }
    }

    private static List<QuizQuestion> parseQuizQuestionsFromJson(String json) throws JsonProcessingException {
        json = json.trim();

        if (json.startsWith("```")) {
            json = json.replaceAll("^```(?:json)?\\s*", ""); // hapus ```json atau ```
            json = json.replaceAll("\\s*```$", "");          // hapus penutup ```
        }

        if (json.startsWith("\"")) {
            json = mapper.readValue(json, String.class); // decode string-nya
        }

        return mapper.readValue(json, new TypeReference<List<QuizQuestion>>() {});
    }

    public static class QuizQuestion {
        public String question;
        public List<String> options;
        public String answer;

        @Override
        public String toString() {
            return "QuizQuestion{" +
                    "question='" + question + '\'' +
                    ", options=" + options +
                    ", answer='" + answer + '\'' +
                    '}';
        }
    }
}
