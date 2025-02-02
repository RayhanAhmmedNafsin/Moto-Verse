package login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Login extends JFrame implements ActionListener {
    private JButton loginButton, registerButton, adminLoginButton, userLoginButton, guestLoginButton;
    private JPanel panel;
    private JLabel welcomeLabel, imageLabel, usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ImageIcon image;

    public Login() {
        super("MotoVerse System");
        setBounds(600, 300, 750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        
      
        
        image = new ImageIcon("img/icon.jpg"); 
        imageLabel = new JLabel(image);
        imageLabel.setBounds(0, 0, 800, 600);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        imageLabel.setAlignmentY(CENTER_ALIGNMENT);
        

        welcomeLabel = new JLabel("Welcome to MotoVerse ");
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBounds(210, 210, 600, 30);
        welcomeLabel.setFont(new Font("Cambria", Font.BOLD, 30));
        panel.add(welcomeLabel);

        image = new ImageIcon("motoverse_logo.png"); 
        imageLabel = new JLabel(image);
        imageLabel.setBounds(162, 100, 425, 250);
        panel.add(imageLabel);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.black);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setBounds(150, 260, 100, 30);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(250, 260, 200, 30);
        panel.add(usernameField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.black);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setBounds(150, 320, 100, 30);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(250, 320, 200, 30);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setBounds(250, 380, 100, 40);
        panel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        registerButton.setBounds(350, 380, 100, 40);
        panel.add(registerButton);

        adminLoginButton = new JButton("Admin");
        adminLoginButton.addActionListener(this);
        adminLoginButton.setBounds(160, 430, 150, 40);
        adminLoginButton.setVisible(false);
        panel.add(adminLoginButton);

        userLoginButton = new JButton("User");
        userLoginButton.addActionListener(this);
        userLoginButton.setBounds(310, 430, 150, 40);
        userLoginButton.setVisible(false);
        panel.add(userLoginButton);

        guestLoginButton = new JButton("Guest");
        guestLoginButton.addActionListener(this);
        guestLoginButton.setBounds(450, 430, 150, 40);
        guestLoginButton.setVisible(false);
        panel.add(guestLoginButton);

        image = new ImageIcon("img/icon.jpg"); 
        imageLabel = new JLabel(image);
        imageLabel.setBounds(0, 0, 800, 600);
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        imageLabel.setAlignmentY(CENTER_ALIGNMENT);

        
        add(panel);
        panel.add(imageLabel);

        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (e.getSource() == loginButton) {
            adminLoginButton.setVisible(true);
            userLoginButton.setVisible(true);
            guestLoginButton.setVisible(true);
        } else if (e.getSource() == adminLoginButton) {
            if (username.equals("admin") && password.equals("admin")) {
                JOptionPane.showMessageDialog(this, "Admin login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new AdminFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == userLoginButton) {
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (validateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "User login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new UserFrame(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == guestLoginButton) {
            JOptionPane.showMessageDialog(this, "Guest login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new GuestFrame().setVisible(true);
            dispose();
        } else if (e.getSource() == registerButton) {
            new Register().setVisible(true);
            dispose();
        }
    }

    private boolean validateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("registration_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(username) && userData[4].equals(password)) {  Corrected index for password
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}