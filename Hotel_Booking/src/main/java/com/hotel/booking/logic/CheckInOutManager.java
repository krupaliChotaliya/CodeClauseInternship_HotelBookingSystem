package com.hotel.booking.logic;

import com.hotel.booking.Model.DatabaseConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CheckInOutManager {
    private Connection connection;

    public CheckInOutManager(Connection connection) {
        this.connection = connection;
    }

    public boolean checkIn(int reservationId) {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "Checked-In");
            pstmt.setInt(2, reservationId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkOut(int reservationId) {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "Checked-Out");
            pstmt.setInt(2, reservationId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public static void openCheckInOutWindow() {
         JFrame checkInOutFrame = new JFrame("Check-In/Out");
         checkInOutFrame.setSize(400, 300);
         checkInOutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

         JPanel panel = new JPanel();
         checkInOutFrame.add(panel);
         placeCheckInOutComponents(panel);

         checkInOutFrame.setVisible(true);
     }

private static void placeCheckInOutComponents(JPanel panel) {
    panel.setLayout(null);

    JLabel reservationIdLabel = new JLabel("Reservation ID:");
    reservationIdLabel.setBounds(10, 20, 100, 25);
    panel.add(reservationIdLabel);

    JTextField reservationIdText = new JTextField(20);
    reservationIdText.setBounds(120, 20, 165, 25);
    panel.add(reservationIdText);

    JButton checkInButton = new JButton("Check-In");
    checkInButton.setBounds(10, 50, 100, 25);
    panel.add(checkInButton);
    checkInButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int reservationId = Integer.parseInt(reservationIdText.getText());

            Connection connection = DatabaseConnection.getConnection();
            CheckInOutManager checkInOutManager = new CheckInOutManager(connection);
            boolean success = checkInOutManager.checkIn(reservationId);

            if (success) {
                JOptionPane.showMessageDialog(null, "Checked in successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to check in.");
            }
        }
    });

    JButton checkOutButton = new JButton("Check-Out");
    checkOutButton.setBounds(120, 50, 100, 25);
    panel.add(checkOutButton);
    checkOutButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int reservationId = Integer.parseInt(reservationIdText.getText());

            Connection connection = DatabaseConnection.getConnection();
            CheckInOutManager checkInOutManager = new CheckInOutManager(connection);
            boolean success = checkInOutManager.checkOut(reservationId);

            if (success) {
                JOptionPane.showMessageDialog(null, "Checked out successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to check out.");
            }
        }
    });
}

}
