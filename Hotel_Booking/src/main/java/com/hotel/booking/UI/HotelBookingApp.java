package com.hotel.booking.UI;


import com.hotel.booking.Model.DatabaseConnection;
import com.hotel.booking.logic.BillingManager;
import com.hotel.booking.logic.CheckInOutManager;
import com.hotel.booking.logic.CustomerManager;
import com.hotel.booking.logic.ReservationManager;
import com.hotel.booking.logic.RoomManager;
import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class HotelBookingApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hotel Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JButton reservationButton = new JButton("Reservations");
        reservationButton.setBounds(10, 20, 150, 25);
        panel.add(reservationButton);
        reservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReservationWindow();
            }
        });

        JButton checkInOutButton = new JButton("Check-In/Out");
        checkInOutButton.setBounds(10, 50, 150, 25);
        panel.add(checkInOutButton);
        checkInOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCheckInOutWindow();
            }
        });

        JButton billingButton = new JButton("Billing");
        billingButton.setBounds(10, 80, 150, 25);
        panel.add(billingButton);
        billingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openBillingWindow();
            }
        });
        
        
          JButton addRoomButton = new JButton("Add Room");
            addRoomButton.setBounds(10, 110, 150, 25);
            panel.add(addRoomButton);
            addRoomButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openAddRoomWindow();
                }
            });

            // Button for adding a customer
            JButton addCustomerButton = new JButton("Add Customer");
            addCustomerButton.setBounds(10, 140, 150, 25);
            panel.add(addCustomerButton);
            addCustomerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openAddCustomerWindow();
                }
            });
    }

    private static void openReservationWindow() {
        JFrame reservationFrame = new JFrame("Reservation");
        reservationFrame.setSize(400, 300);
        reservationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        reservationFrame.add(panel);
        placeReservationComponents(panel);

        reservationFrame.setVisible(true);
    }

  private static void placeReservationComponents(JPanel panel) {
    panel.setLayout(null);

    JLabel roomIdLabel = new JLabel("Room ID:");
    roomIdLabel.setBounds(10, 20, 80, 25);
    panel.add(roomIdLabel);

    JTextField roomIdText = new JTextField(20);
    roomIdText.setBounds(100, 20, 165, 25);
    panel.add(roomIdText);

    JLabel customerIdLabel = new JLabel("Customer ID:");
    customerIdLabel.setBounds(10, 50, 80, 25);
    panel.add(customerIdLabel);

    JTextField customerIdText = new JTextField(20);
    customerIdText.setBounds(100, 50, 165, 25);
    panel.add(customerIdText);

    JLabel checkInDateLabel = new JLabel("Check-In Date (yyyy-MM-dd):");
    checkInDateLabel.setBounds(10, 80, 200, 25);
    panel.add(checkInDateLabel);

    JTextField checkInDateText = new JTextField(20);
    checkInDateText.setBounds(200, 80, 165, 25);
    panel.add(checkInDateText);

    JLabel checkOutDateLabel = new JLabel("Check-Out Date (yyyy-MM-dd):");
    checkOutDateLabel.setBounds(10, 110, 200, 25);
    panel.add(checkOutDateLabel);

    JTextField checkOutDateText = new JTextField(20);
    checkOutDateText.setBounds(200, 110, 165, 25);
    panel.add(checkOutDateText);

    JButton createButton = new JButton("Create Reservation");
    createButton.setBounds(10, 140, 200, 25);
    panel.add(createButton);
    createButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int roomId = Integer.parseInt(roomIdText.getText());
                int customerId = Integer.parseInt(customerIdText.getText());
                Date checkInDate = Date.valueOf(checkInDateText.getText());
                Date checkOutDate = Date.valueOf(checkOutDateText.getText());

                Connection connection = DatabaseConnection.getConnection();
                ReservationManager reservationManager = new ReservationManager(connection);
                
                boolean success = reservationManager.createReservation(roomId, customerId, checkInDate, checkOutDate);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Reservation created successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create reservation.");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    JButton showRoomsButton = new JButton("Show Available Rooms");
    showRoomsButton.setBounds(10, 170, 200, 25);
    panel.add(showRoomsButton);
    showRoomsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAvailableRooms(roomIdText);
        }
    });
}

private static void showAvailableRooms(JTextField roomIdText) {
    JFrame frame = new JFrame("Available Rooms");
    frame.setSize(600, 400);
    frame.setLayout(new BorderLayout());

    String[] columnNames = {"Room ID", "Type", "Price"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    JTable table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String roomId = model.getValueAt(selectedRow, 0).toString();
                roomIdText.setText(roomId);
                frame.dispose();
            }
        }
    });

    Connection connection = DatabaseConnection.getConnection();
    String query = "SELECT room_id,type, price FROM rooms";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int roomId = rs.getInt("room_id");
            String roomType = rs.getString("type");
            double price = rs.getDouble("price");
            model.addRow(new Object[]{roomId, roomType, price});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    JScrollPane scrollPane = new JScrollPane(table);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.setVisible(true);
}



    private static void openCheckInOutWindow() {
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
                displayBill(reservationId);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to generate bill.");
            }
        }
    });
}

private static void displayBill(int reservationId) {
    JFrame billFrame = new JFrame("Bill Details");
    billFrame.setSize(400, 300);
    billFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(null);
    billFrame.add(panel);

    Connection connection = DatabaseConnection.getConnection();
    String query = "SELECT * FROM billing WHERE reservation_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, reservationId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            JLabel billIdLabel = new JLabel("Bill ID:");
            billIdLabel.setBounds(10, 20, 100, 25);
            panel.add(billIdLabel);

            JTextField billIdText = new JTextField(String.valueOf(rs.getInt("billing_id")));
            billIdText.setBounds(120, 20, 165, 25);
            billIdText.setEditable(false);
            panel.add(billIdText);

            JLabel reservationIdLabel = new JLabel("Reservation ID:");
            reservationIdLabel.setBounds(10, 50, 100, 25);
            panel.add(reservationIdLabel);

            JTextField reservationIdText = new JTextField(String.valueOf(rs.getInt("reservation_id")));
            reservationIdText.setBounds(120, 50, 165, 25);
            reservationIdText.setEditable(false);
            panel.add(reservationIdText);

            JLabel amountLabel = new JLabel("Amount:");
            amountLabel.setBounds(10, 80, 100, 25);
            panel.add(amountLabel);

            JTextField amountText = new JTextField(String.valueOf(rs.getDouble("amount")));
            amountText.setBounds(120, 80, 165, 25);
            amountText.setEditable(false);
            panel.add(amountText);

            JLabel dateLabel = new JLabel("Date:");
            dateLabel.setBounds(10, 110, 100, 25);
            panel.add(dateLabel);

            JTextField dateText = new JTextField(rs.getDate("date").toString());
            dateText.setBounds(120, 110, 165, 25);
            dateText.setEditable(false);
            panel.add(dateText);
            
            
            JLabel StatusLabel = new JLabel("Status:");
            StatusLabel.setBounds(10, 140, 100, 25);
            panel.add(StatusLabel);

            JTextField StatusText = new JTextField(rs.getString("status").toString());
            StatusText.setBounds(120, 140, 165, 25);
            StatusText.setEditable(false);
            panel.add(StatusText);
            
        } else {
            JOptionPane.showMessageDialog(null, "No bill found for this reservation ID.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    billFrame.setVisible(true);
}

    
    private static void createAndShowGUI() {
    JFrame frame = new JFrame("Hotel Booking System");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLayout(null);

    JButton reservationButton = new JButton("Reservation");
    reservationButton.setBounds(10, 10, 150, 25);
    frame.add(reservationButton);
    reservationButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openReservationWindow();
        }
    });

    JButton checkInOutButton = new JButton("Check-In/Out");
    checkInOutButton.setBounds(10, 45, 150, 25);
    frame.add(checkInOutButton);
    checkInOutButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openCheckInOutWindow();
        }
    });

    JButton billingButton = new JButton("Billing");
    billingButton.setBounds(10, 80, 150, 25);
    frame.add(billingButton);
    billingButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openBillingWindow();
        }
    });

    JButton addRoomButton = new JButton("Add Room");
    addRoomButton.setBounds(10, 115, 150, 25);
    frame.add(addRoomButton);
    addRoomButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openAddRoomWindow();
        }
    });

    JButton addCustomerButton = new JButton("Add Customer");
    addCustomerButton.setBounds(10, 150, 150, 25);
    frame.add(addCustomerButton);
    addCustomerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openAddCustomerWindow();
        }
    });

    frame.setVisible(true);
}

private static void openAddRoomWindow() {
  
      JFrame frame = new JFrame("Add Room");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Room Number Label and Text Field
        JLabel roomNumberLabel = new JLabel("Room Number:");
        roomNumberLabel.setBounds(10, 20, 100, 25);
        frame.add(roomNumberLabel);

        JTextField roomNumberText = new JTextField(20);
        roomNumberText.setBounds(120, 20, 165, 25);
        frame.add(roomNumberText);

        // Room Type Label and Text Field
        JLabel roomTypeLabel = new JLabel("Room Type:");
        roomTypeLabel.setBounds(10, 60, 100, 25);
        frame.add(roomTypeLabel);

        JTextField roomTypeText = new JTextField(20);
        roomTypeText.setBounds(120, 60, 165, 25);
        frame.add(roomTypeText);

        // Room Status Label and Text Field
        JLabel roomStatusLabel = new JLabel("Room Status:");
        roomStatusLabel.setBounds(10, 100, 100, 25);
        frame.add(roomStatusLabel);

        JTextField roomStatusText = new JTextField(20);
        roomStatusText.setBounds(120, 100, 165, 25);
        frame.add(roomStatusText);

        // Price Label and Text Field
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(10, 140, 100, 25);
        frame.add(priceLabel);

        JTextField priceText = new JTextField(20);
        priceText.setBounds(120, 140, 165, 25);
        frame.add(priceText);

        // Add Room Button
        JButton addButton = new JButton("Add Room");
        addButton.setBounds(10, 180, 150, 25);
        frame.add(addButton);


    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String room_number = roomNumberText.getText();
                String roomType = roomTypeText.getText();
                  String status = roomStatusText.getText();
                double price = Double.parseDouble(priceText.getText());

                Connection connection = DatabaseConnection.getConnection();
                RoomManager roomManager = new RoomManager(connection);
                boolean success = roomManager.addRoom(room_number, roomType, price,status);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Room added successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add room.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    frame.setVisible(true);
}

private static void openAddCustomerWindow() {
    JFrame frame = new JFrame("Add Customer");
    frame.setSize(400, 300);
    frame.setLayout(null);

    JLabel nameLabel = new JLabel("Name: ");
    nameLabel.setBounds(10, 20, 80, 25);
    frame.add(nameLabel);

    JTextField nameText = new JTextField(20);
    nameText.setBounds(100, 20, 165, 25);
    frame.add(nameText);

    JLabel  emailLabel = new JLabel("Email:");
    emailLabel.setBounds(10, 50, 80, 25);
    frame.add(emailLabel);

    JTextField emailText = new JTextField(20);
    emailText.setBounds(100, 50, 165, 25);
    frame.add(emailText);

    JLabel phoneLabel = new JLabel("Phone:");
    phoneLabel.setBounds(10, 80, 80, 25);
    frame.add(phoneLabel);

    JTextField phoneText = new JTextField(20);
    phoneText.setBounds(100, 80, 165, 25);
    frame.add(phoneText);

    JButton addButton = new JButton("Add Customer");
    addButton.setBounds(10, 110, 150, 25);
    frame.add(addButton);
    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                  String name = nameText.getText();
                String email = emailText.getText();
                 String phone = phoneText.getText();

                Connection connection = DatabaseConnection.getConnection();
                CustomerManager customerManager = new CustomerManager(connection);
                boolean success = customerManager.addCustomer( name, email,phone);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Customer added successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add customer.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    frame.setVisible(true);
}

}
