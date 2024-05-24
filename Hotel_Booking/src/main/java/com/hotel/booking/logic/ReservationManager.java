package com.hotel.booking.logic;

import java.sql.*;

public class ReservationManager {
    private Connection connection;

    public ReservationManager(Connection connection) {
        this.connection = connection;
    }

    public boolean createReservation(int roomId, int customerId, Date checkInDate, Date checkOutDate) {
        String sql = "INSERT INTO reservations (room_id, customer_id, check_in_date, check_out_date, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, customerId);
            pstmt.setDate(3, checkInDate);
            pstmt.setDate(4, checkOutDate);
            pstmt.setString(5, "Booked");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
