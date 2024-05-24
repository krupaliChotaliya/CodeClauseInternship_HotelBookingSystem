package com.hotel.booking.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "<DB_URL>"; //write your database URL
    private static final String USER = "<DB_USERNAME>"; //username of your db
    private static final String PASSWORD = "<DB_PASSWORD>"; //password of your db

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            initializeDatabase(connection);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void initializeDatabase(Connection connection) {
        String[] createTableStatements = {
            "CREATE TABLE IF NOT EXISTS customers (" +
            "customer_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(100) NOT NULL, " +
            "email VARCHAR(100) NOT NULL, " +
            "phone VARCHAR(15) NOT NULL" +
            ")",

            "CREATE TABLE IF NOT EXISTS rooms (" +
            "room_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "room_number VARCHAR(10) NOT NULL, " +
            "type VARCHAR(50) NOT NULL, " +
            "price DECIMAL(10, 2) NOT NULL, " +
            "status VARCHAR(20) NOT NULL" +  
            ")",

            "CREATE TABLE IF NOT EXISTS reservations (" +
            "reservation_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "room_id INT NOT NULL, " +
            "customer_id INT NOT NULL, " +
            "check_in_date DATE NOT NULL, " +
            "check_out_date DATE NOT NULL, " +
            "status VARCHAR(20) NOT NULL, " +
            "FOREIGN KEY (room_id) REFERENCES rooms(room_id), " +
            "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)" +
            ")",

            "CREATE TABLE IF NOT EXISTS billing (" +
            "billing_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "reservation_id INT NOT NULL, " +
            "amount DECIMAL(10, 2) NOT NULL, " +
            "date DATE NOT NULL, " +
            "status VARCHAR(20) NOT NULL, " +
            "FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)" +
            ")"
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : createTableStatements) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
