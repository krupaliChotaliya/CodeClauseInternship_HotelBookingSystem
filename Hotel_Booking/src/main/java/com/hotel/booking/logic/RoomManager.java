package com.hotel.booking.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RoomManager {
    private Connection connection;

    public RoomManager(Connection connection) {
        this.connection = connection;
    }

    public boolean addRoom(String room_number, String roomType, double price,String status) {
        String query = "INSERT INTO rooms (room_number, type, price,status) VALUES (?, ?, ?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
         
            stmt.setString(1, room_number);
              stmt.setString(2, roomType);
            stmt.setDouble(3, price);
             stmt.setString(4, status);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

