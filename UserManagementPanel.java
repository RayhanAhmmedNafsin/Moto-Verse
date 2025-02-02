package login;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class UserManagementPanel extends JPanel {
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JButton deleteUserButton, refreshButton;

    public UserManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(135, 82, 55));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

         Title Label
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Cambria", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

         User List
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBackground(new Color(245, 245, 245));
        userList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(600, 400));
        add(userScrollPane, BorderLayout.CENTER);

         User Buttons Panel
        JPanel userButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        userButtonPanel.setBackground(new Color(135, 82, 55));

        deleteUserButton = new JButton("Delete User");
        refreshButton = new JButton("Refresh List");

        deleteUserButton.addActionListener(e -> deleteSelectedUser());
        refreshButton.addActionListener(e -> loadUsers());

        userButtonPanel.add(deleteUserButton);
        userButtonPanel.add(refreshButton);
        add(userButtonPanel, BorderLayout.SOUTH);

        loadUsers();
    }

    private void loadUsers() {
        userListModel.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("registration_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                userListModel.addElement(String.format("Username: %s | Name: %s | Email: %s | Phone: %s",
                        userData[0], userData[1], userData[2], userData[3]));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading users: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        int selectedIndex = userList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a user to delete",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedUser = userList.getSelectedValue();
        String username = selectedUser.split("\\|")[0].split(":")[1].trim();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user: " + username + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            deleteUserFromFile(username);
        }
    }

    private void deleteUserFromFile(String username) {
        try {
            File inputFile = new File("registration_data.txt");
            File tempFile = new File("temp_registration_data.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean userDeleted = false;

            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (!userData[0].equals(username)) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    userDeleted = true;
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

            if (userDeleted) {
                JOptionPane.showMessageDialog(this,
                        "User deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting user: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}