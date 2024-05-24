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

public class BillingManager {
    private Connection connection;

    public BillingManager(Connection connection) {
        this.connection = connection;
    }

    public boolean generateBill(int reservationId, double amount) {
        String sql = "INSERT INTO billing (reservation_id, amount, date, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setDouble(2, amount);
            pstmt.setDate(3, new Date(System.currentTimeMillis()));
            pstmt.setString(4, "paid");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markBillAsPaid(int billingId) {
        String sql = "UPDATE billing SET status = ? WHERE billing_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "Paid");
            pstmt.setInt(2, billingId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void openBillingWindow() {
    JFrame billingFrame = new JFrame("Billing");
    billingFrame.setSize(400, 300);
    billingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    billingFrame.add(panel);
    placeBillingComponents(panel);

    billingFrame.setVisible(true);
}

private static void placeBillingComponents(JPanel panel) {
    panel.setLayout(null);

    JLabel reservationIdLabel = new JLabel("Reservation ID:");
    reservationIdLabel.setBounds(10, 20, 100, 25);
    panel.add(reservationIdLabel);

    JTextField reservationIdText = new JTextField(20);
    reservationIdText.setBounds(120, 20, 165, 25);
    panel.add(reservationIdText);

    JLabel amountLabel = new JLabel("Amount:");
    amountLabel.setBounds(10, 50, 100, 25);
    panel.add(amountLabel);

    JTextField amountText = new JTextField(20);
    amountText.setBounds(120, 50, 165, 25);
    panel.add(amountText);

    JButton generateBillButton = new JButton("Generate Bill");
    generateBillButton.setBounds(10, 80, 150, 25);
    panel.add(generateBillButton);
    generateBillButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int reservationId = Integer.parseInt(reservationIdText.getText());
            double amount = Double.parseDouble(amountText.getText());

            Connection connection = DatabaseConnection.getConnection();
            BillingManager billingManager = new BillingManager(connection);
            boolean success = billingManager.generateBill(reservationId, amount);

            if (success) {
                JOptionPane.showMessageDialog(null, "Bill generated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to generate bill.");
            }
        }
    });
}

}
