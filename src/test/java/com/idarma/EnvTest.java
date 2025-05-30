package com.idarma;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

public class EnvTest {
    @Test
    void testEnv() {
        Dotenv dotenv = Dotenv.load(); // baca file .env di root project

        String env = dotenv.get("APP_NAME");
        System.out.println("Hasil dari env : " + env);
    }
}

