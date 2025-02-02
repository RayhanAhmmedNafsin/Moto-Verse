 login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class OrderManagementPanel extends JPanel {
    private JTable orderTable;
    private DefaultTableModel orderTableModel;

    public OrderManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(135, 82, 55));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

         Title
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Cambria", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

         Orders Table
        String[] columns = {"Order ID", "Username", "Bike", "Price", "Order Date", "Status"};
        orderTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        add(orderScrollPane, BorderLayout.CENTER);

         Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(135, 82, 55));

        JButton completeOrderBtn = new JButton("Mark as Completed");
        JButton cancelOrderBtn = new JButton("Cancel Order");
        JButton refreshOrdersBtn = new JButton("Refresh Orders");

        completeOrderBtn.addActionListener(e -> completeOrder());
        cancelOrderBtn.addActionListener(e -> cancelOrder());
        refreshOrdersBtn.addActionListener(e -> loadOrders());

        buttonPanel.add(completeOrderBtn);
        buttonPanel.add(cancelOrderBtn);
        buttonPanel.add(refreshOrdersBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadOrders();
    }

    public void loadOrders() {
        orderTableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("orders_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                orderTableModel.addRow(new Object[]{
                        orderData[0],  Order ID
                        orderData[1],  Username
                        orderData[3],  Bike Name
                        orderData[4],  Price
                        orderData[5],  Order Date
                        orderData[6]   Status
                });
            }
        } catch (IOException ex) {
            if (!ex.getMessage().contains("No such file")) {
                JOptionPane.showMessageDialog(this,
                        "Error loading orders: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void completeOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to complete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderId = orderTable.getValueAt(selectedRow, 0).toString();
        String currentStatus = orderTable.getValueAt(selectedRow, 5).toString();

        if (currentStatus.equals("Completed")) {
            JOptionPane.showMessageDialog(this, "Order is already completed", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            File inputFile = new File("orders_data.txt");
            File tempFile = new File("temp_orders_data.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData[0].equals(orderId)) {
                     Update status to Completed
                    orderData[6] = "Completed";
                    line = String.join(",", orderData);
                }
                writer.write(line);
                writer.newLine();
            }

            writer.close();
            reader.close();

            if (!inputFile.delete()) {
                throw new IOException("Could not delete the original file");
            }
            if (!tempFile.renameTo(inputFile)) {
                throw new IOException("Could not rename temp file");
            }

            JOptionPane.showMessageDialog(this, "Order marked as completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadOrders();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error updating order: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to cancel", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderId = orderTable.getValueAt(selectedRow, 0).toString();
        String currentStatus = orderTable.getValueAt(selectedRow, 5).toString();

        if (currentStatus.equals("Completed") || currentStatus.equals("Cancelled")) {
            JOptionPane.showMessageDialog(this,
                    "Cannot cancel order. Status is already " + currentStatus,
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this order?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                 First, restore the bike stock
                restoreOrderStock(orderId);

                 Then update the order status
                File inputFile = new File("orders_data.txt");
                File tempFile = new File("temp_orders_data.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] orderData = line.split(",");
                    if (orderData[0].equals(orderId)) {
                         Update status to Cancelled
                        orderData[6] = "Cancelled";
                        line = String.join(",", orderData);
                    }
                    writer.write(line);
                    writer.newLine();
                }

                writer.close();
                reader.close();

                if (!inputFile.delete()) {
                    throw new IOException("Could not delete the original file");
                }
                if (!tempFile.renameTo(inputFile)) {
                    throw new IOException("Could not rename temp file");
                }

                JOptionPane.showMessageDialog(this, "Order cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadOrders();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error cancelling order: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void restoreOrderStock(String orderId) throws IOException {
         First get the order details
        String bikeId = "";
        int quantity = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("orders_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData[0].equals(orderId)) {
                    bikeId = orderData[2];
                    quantity = (int) (Double.parseDouble(orderData[4]) / getBikePrice(bikeId));
                    break;
                }
            }
        }

         Now update the bike stock
        File inputFile = new File("bikes_data.txt");
        File tempFile = new File("temp_bikes_data.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] bikeData = line.split(",");
                if (bikeData[0].equals(bikeId)) {
                    int currentStock = Integer.parseInt(bikeData[6]);
                    bikeData[6] = String.valueOf(currentStock + quantity);
                    line = String.join(",", bikeData);
                }
                writer.write(line);
                writer.newLine();
            }
        }

        if (!inputFile.delete()) {
            throw new IOException("Could not delete the original file");
        }
        if (!tempFile.renameTo(inputFile)) {
            throw new IOException("Could not rename temp file");
        }
    }

    private double getBikePrice(String bikeId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("bikes_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bikeData = line.split(",");
                if (bikeData[0].equals(bikeId)) {
                    return Double.parseDouble(bikeData[4]);
                }
            }
        }
        return 0.0;
    }
}
