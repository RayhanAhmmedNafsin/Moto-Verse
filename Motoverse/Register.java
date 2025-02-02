package login;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Register extends JFrame {
    private JTextField nameField, emailField, phoneField, usernameField;
    private JPasswordField passwordField;

    public Register() {
        setTitle("Register To Motoverse!!");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
         Main Panel for form fields
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(135, 82, 55));

         Add form components with labels
        formPanel.add(createLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(createLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(createLabel("Phone Number:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(createLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(createLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

         Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(135, 82, 55));
        JLabel titleLabel = new JLabel("Register to MotoVerse");
        titleLabel.setFont(new Font("Cambria", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        titlePanel.add(titleLabel);

         Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(135, 82, 55));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");
        
         Style the buttons
        registerButton.setPreferredSize(new Dimension(120, 40));
        backButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Invalid email format. Email must end with @gmail.com.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (phone.length() != 11) {
                JOptionPane.showMessageDialog(this, "Invalid phone number. Phone number must be 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (password.length() < 4) {
                JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (userExists(username, password)) {
                JOptionPane.showMessageDialog(this, "User already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                saveToFile(name, email, phone, username, password);
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new Login().setVisible(true);
                dispose();
            }
        });

        backButton.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

         Add panels to frame
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private boolean userExists(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("registration_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(username) && userData[4].equals(password)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void saveToFile(String name, String email, String phone, String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("registration_data.txt", true))) {
            writer.write(username + "," + name + "," + email + "," + phone + "," + password);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 