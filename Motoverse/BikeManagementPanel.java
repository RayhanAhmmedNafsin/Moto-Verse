package login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import java.awt.*;
import java.io.*;
import java.util.Calendar;

public class BikeManagementPanel extends JPanel {
    private JTable bikeTable;
    private DefaultTableModel bikeTableModel;
    private JTextField brandField, modelField, yearField, priceField, stockField;
    private JTextArea descriptionArea;
    private JComboBox<String> conditionCombo;

    public BikeManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(135, 82, 55));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

   
        JLabel titleLabel = new JLabel("Bike Management");
        titleLabel.setFont(new Font("Cambria", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        
        String[] columns = {"ID", "Brand", "Model", "Year", "Price", "Condition", "Stock", "Description"};
        bikeTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bikeTable = new JTable(bikeTableModel);
        JScrollPane bikeScrollPane = new JScrollPane(bikeTable);
        add(bikeScrollPane, BorderLayout.CENTER);

        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(135, 82, 55));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        
        brandField = new JTextField(15);
        modelField = new JTextField(15);
        yearField = new JTextField(15);
        priceField = new JTextField(15);
        stockField = new JTextField(15);
        descriptionArea = new JTextArea(3, 15);
        conditionCombo = new JComboBox<>(new String[]{"New", "Used", "Refurbished"});

        
        addFormComponent(formPanel, "Brand:", brandField, gbc, 0);
        addFormComponent(formPanel, "Model:", modelField, gbc, 1);
        addFormComponent(formPanel, "Year:", yearField, gbc, 2);
        addFormComponent(formPanel, "Price:", priceField, gbc, 3);
        addFormComponent(formPanel, "Stock:", stockField, gbc, 4);
        addFormComponent(formPanel, "Condition:", conditionCombo, gbc, 5);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(135, 82, 55));

        JButton addButton = new JButton("Add Bike");
        JButton updateButton = new JButton("Update Bike");
        JButton deleteButton = new JButton("Delete Bike");
        JButton clearButton = new JButton("Clear Fields");
        JButton refreshButton = new JButton("Refresh List");

        addButton.addActionListener(e -> addBike());
        updateButton.addActionListener(e -> updateBike());
        deleteButton.addActionListener(e -> deleteBike());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadBikes());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

       
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(135, 82, 55));
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

       
        bikeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bikeTable.getSelectedRow() != -1) {
                int row = bikeTable.getSelectedRow();
                brandField.setText(bikeTable.getValueAt(row, 1).toString());
                modelField.setText(bikeTable.getValueAt(row, 2).toString());
                yearField.setText(bikeTable.getValueAt(row, 3).toString());
                priceField.setText(bikeTable.getValueAt(row, 4).toString());
                conditionCombo.setSelectedItem(bikeTable.getValueAt(row, 5).toString());
                stockField.setText(bikeTable.getValueAt(row, 6).toString());
                descriptionArea.setText(bikeTable.getValueAt(row, 7).toString());
            }
        });

        loadBikes();
    }

    private void addFormComponent(JPanel panel, String label, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void addBike() {
        if (!validateBikeFields()) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bikes_data.txt", true))) {
            String bikeData = String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                    System.currentTimeMillis(), 
                    brandField.getText(),
                    modelField.getText(),
                    yearField.getText(),
                    priceField.getText(),
                    conditionCombo.getSelectedItem(),
                    stockField.getText(),
                    descriptionArea.getText().replace(",", ";"));

            writer.write(bikeData);
            writer.newLine();

            JOptionPane.showMessageDialog(this, "Bike added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadBikes();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error adding bike: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBike() {
        int selectedRow = bikeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bike to update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateBikeFields()) return;

        String bikeId = bikeTable.getValueAt(selectedRow, 0).toString();
        try {
            File inputFile = new File("bikes_data.txt");
            File tempFile = new File("temp_bikes_data.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] bikeData = line.split(",");
                if (bikeData[0].equals(bikeId)) {
                    
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                            bikeId,
                            brandField.getText(),
                            modelField.getText(),
                            yearField.getText(),
                            priceField.getText(),
                            conditionCombo.getSelectedItem(),
                            stockField.getText(),
                            descriptionArea.getText().replace(",", ";")));
                } else {
                    writer.write(line);
                }
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

            JOptionPane.showMessageDialog(this, "Bike updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBikes();
            clearFields();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating bike: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBike() {
        int selectedRow = bikeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bike to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this bike?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String bikeId = bikeTable.getValueAt(selectedRow, 0).toString();
            try {
                File inputFile = new File("bikes_data.txt");
                File tempFile = new File("temp_bikes_data.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] bikeData = line.split(",");
                    if (!bikeData[0].equals(bikeId)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }

                writer.close();
                reader.close();

                if (!inputFile.delete()) {
                    throw new IOException("Could not delete the original file");
                }
                if (!tempFile.renameTo(inputFile)) {
                    throw new IOException("Could not rename temp file");
                }

                JOptionPane.showMessageDialog(this, "Bike deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBikes();
                clearFields();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting bike: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadBikes() {
        bikeTableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("bikes_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bikeData = line.split(",");
                bikeTableModel.addRow(bikeData);
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

    private boolean validateBikeFields() {
        if (brandField.getText().isEmpty() || modelField.getText().isEmpty() ||
                yearField.getText().isEmpty() || priceField.getText().isEmpty() ||
                stockField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int year = Integer.parseInt(yearField.getText());
            if (year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR) + 1) {
                JOptionPane.showMessageDialog(this, "Invalid year", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            double price = Double.parseDouble(priceField.getText());
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int stock = Integer.parseInt(stockField.getText());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "Stock cannot be negative", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearFields() {
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        priceField.setText("");
        stockField.setText("");
        descriptionArea.setText("");
        conditionCombo.setSelectedIndex(0);
        bikeTable.clearSelection();
    }
}