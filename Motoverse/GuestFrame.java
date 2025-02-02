package login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GuestFrame extends JFrame {
    private JTable bikesTable;
    private DefaultTableModel bikesTableModel;
    private JPanel bikesPanel;
    private static final int CARDS_PER_PAGE = 4;
    private static final int CARDS_PER_ROW = 4;
    private int currentPage = 0;

    public GuestFrame() {
        setTitle("Guest View - MotoVerse System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(135, 82, 55));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        createBikesPanel();

        
        String[] columns = {"Brand", "Model", "Year", "Price", "Condition", "Description"};
        bikesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bikesTable = new JTable(bikesTableModel);
        bikesTable.getTableHeader().setReorderingAllowed(false);
        bikesTable.getTableHeader().setResizingAllowed(false);

        
        bikesTable.getColumnModel().getColumn(0).setPreferredWidth(100); 
        bikesTable.getColumnModel().getColumn(1).setPreferredWidth(100); 
        bikesTable.getColumnModel().getColumn(2).setPreferredWidth(60);  
        bikesTable.getColumnModel().getColumn(3).setPreferredWidth(80);  
        bikesTable.getColumnModel().getColumn(4).setPreferredWidth(80);  
        bikesTable.getColumnModel().getColumn(5).setPreferredWidth(200); 

        JScrollPane scrollPane = new JScrollPane(bikesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

       =
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(135, 82, 55));

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        buttonPanel.add(backButton);
        mainPanel.add(bikesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadBikes();
    }

    private void updateCardDisplay(ArrayList<JPanel> cards, JPanel cardDisplayPanel, int page) {
        cardDisplayPanel.removeAll(); 
        int start = page * CARDS_PER_PAGE;
        int end = Math.min(start + CARDS_PER_PAGE, cards.size());
        for (int i = start; i < end; i++) {
            cardDisplayPanel.add(cards.get(i));
        }
        cardDisplayPanel.revalidate();
        cardDisplayPanel.repaint();
    }

    private JPanel createItemCard(String title, String description, String imagePath, int screenWidth, int screenHeight) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setSize(new Dimension((int) (screenWidth * .8), (int) (screenHeight * .8)));

        
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(imagePath); 
        Image scaledImage = icon.getImage().getScaledInstance(cardPanel.getWidth() / 2, cardPanel.getHeight() / 2, Image.SCALE_SMOOTH);
        imageLabel.setSize(cardPanel.getWidth(), cardPanel.getHeight());
        imageLabel.setIcon(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

       
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        JLabel descriptionLabel = new JLabel("<html><div style='width:100%;padding-left:20px'><h3 style='text-align: left; '>" + description + "</h3></div></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        cardPanel.add(imageLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5))); 
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));  Spacer
        cardPanel.add(descriptionLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));  Spacer

         Add MouseListener to the card
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showLoginOrRegisterDialog();
            }
        });

        return cardPanel;
    }

    private void showLoginOrRegisterDialog() {
        String[] options = {"Login", "Register"};
        int choice = JOptionPane.showOptionDialog(this,
                "You need to login or register to proceed.",
                "Login or Register",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
             Open Login Frame
            new Login().setVisible(true);
            dispose();
        } else if (choice == 1) {
             Open Register Frame
            new Register().setVisible(true);
            dispose();
        }
    }

    private void createBikesPanel() {
        bikesPanel = new JPanel(new BorderLayout(10, 10));
        bikesPanel.setBackground(new Color(135, 82, 55));
        bikesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

         Title
        JLabel titleLabel = new JLabel("Available Bikes");
        titleLabel.setFont(new Font("Cambria", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

         Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(135, 82, 55));

        JLabel filterLabel = new JLabel("Show:");
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(135, 82, 55));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(filterPanel, BorderLayout.EAST);

        bikesPanel.add(topPanel, BorderLayout.NORTH);

         Bikes Table
        String[] columns = {"ID", "Brand", "Model", "Year", "Price", "Condition", "Availability", "Description"};
        bikesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bikesTable = new JTable(bikesTableModel);
        JScrollPane scrollPane = new JScrollPane(bikesTable);

        EXTRA CODE TO EDIT STARTS HERE
        JPanel theMainPanel = new JPanel();
        theMainPanel.setLayout(new BorderLayout());
        JPanel cardDisplayPanel = new JPanel();
        cardDisplayPanel.setLayout(new GridLayout(0, CARDS_PER_ROW, 10, 10));
        cardDisplayPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

         Navigation panel
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");

         Add components to navigation panel
        navigationPanel.add(previousButton);
        navigationPanel.add(nextButton);

        theMainPanel.add(cardDisplayPanel, BorderLayout.CENTER);
        theMainPanel.add(navigationPanel, BorderLayout.SOUTH);

        Data from file
        ArrayList<JPanel> cards = new ArrayList<>();

        bikesTableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("bikes_data.txt"))) {
            String line;
            BufferedReader reader2 = new BufferedReader(new FileReader("img_data.txt"));
            String line2 = reader2.readLine();
            String[] imgData = line2.split(",");
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                String[] bikeData = line.split(",");

                int stock = Integer.parseInt(bikeData[6]);
                String availability = stock > 0 ? "Available" : "Stock Out";
                String imagePath = imgData[counter];
                String item = bikeData[1];
                String Model = "Model: " + bikeData[2];
                String Year = "Year: " + bikeData[3];
                String Price = "Price: " + bikeData[4] + " BDT";
                String Condition = "Condition: " + bikeData[5];
                String Status = "Status: " + availability;
                String Desc = "Color: " + bikeData[7];
                String description = "<html><br>" + Model + "<br>" + Year + "<br>" + Price + "<br>" + Condition + "<br>" + Status + "<br>" + Desc + "</html>";
                cards.add(createItemCard(item, description, imagePath, 400, 400));

                counter++;

            }
        } catch (IOException ex) {
            if (!ex.getMessage().contains("No such file")) {
                JOptionPane.showMessageDialog(this,
                        "Error loading bikes: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        updateCardDisplay(cards, cardDisplayPanel, currentPage);

         Button actions
        previousButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateCardDisplay(cards, cardDisplayPanel, currentPage);
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * CARDS_PER_PAGE < cards.size()) {
                currentPage++;
                updateCardDisplay(cards, cardDisplayPanel, currentPage);
            }
        });
        ENDSS
        bikesPanel.add(theMainPanel, BorderLayout.CENTER);
    }

    private void loadBikes() {
        bikesTableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("bikes_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bikeData = line.split(",");
                if (Integer.parseInt(bikeData[6]) > 0) {  Only show bikes with stock > 0
                    bikesTableModel.addRow(new Object[]{
                            bikeData[1],  Brand
                            bikeData[2],  Model
                            bikeData[3],  Year
                            "$" + bikeData[4],  Price
                            bikeData[5],  Condition
                            bikeData[7]   Description
                    });
                }
            }
        } catch (IOException ex) {
            if (!ex.getMessage().contains("No such file")) {
                JOptionPane.showMessageDialog(this,
                        "Error loading bikes: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GuestFrame().setVisible(true);
        });
    }
}