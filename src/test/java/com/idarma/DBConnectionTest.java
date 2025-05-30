package com.idarma;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {
    private static final Logger logger = Logger.getLogger(ConnectionUtil.class.getName());
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    @Test
    void testConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            assertNotNull(conn, "Koneksi ke database gagal: connection null");
            conn.close(); // penting: tutup koneksi
        } catch (ClassNotFoundException e) {
            fail("Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            fail("Koneksi ke database gagal: " + e.getMessage());
        }
    }
}
