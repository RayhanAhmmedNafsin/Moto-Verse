package login;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    private JPanel mainPanel;
    private JButton backButton;

    public AdminFrame() {
        setTitle("Admin Dashboard - MotoVerse System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        mainPanel = new JPanel(new CardLayout());

       
        UserManagementPanel userPanel = new UserManagementPanel();
        mainPanel.add(userPanel, "Users");

       
        BikeManagementPanel bikePanel = new BikeManagementPanel();
        mainPanel.add(bikePanel, "Bikes");

       
        OrderManagementPanel orderPanel = new OrderManagementPanel();
        mainPanel.add(orderPanel, "Orders");

        
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setBackground(new Color(135, 82, 55));

        JButton userManagementBtn = new JButton("User Management");
        JButton bikeManagementBtn = new JButton("Bike Management");
        JButton orderManagementBtn = new JButton("Order Management");
        backButton = new JButton("Back to Login");

        userManagementBtn.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Users"));
        bikeManagementBtn.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Bikes"));
        orderManagementBtn.addActionListener(e -> {
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Orders");
            orderPanel.loadOrders();
        });
        backButton.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        navPanel.add(userManagementBtn);
        navPanel.add(bikeManagementBtn);
        navPanel.add(orderManagementBtn);
        navPanel.add(backButton);

        
        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
}