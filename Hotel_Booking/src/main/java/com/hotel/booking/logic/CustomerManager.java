package com.hotel.booking.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerManager {
    private Connection connection;

    public CustomerManager(Connection connection) {
        this.connection = connection;
    }

    public boolean addCustomer(String name, String email, String phone) {
        String query = "INSERT INTO customers ( name, email,phone) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3,phone );
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

