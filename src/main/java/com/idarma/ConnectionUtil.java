package com.idarma;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionUtil {
    private static final Logger logger = Logger.getLogger(ConnectionUtil.class.getName());
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Berhasil terkoneksi dengan database.");
            return conn;
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Driver MySQL tidak ditemukan.", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Koneksi ke database gagal.", e);
        }
        return null;
    }
}
