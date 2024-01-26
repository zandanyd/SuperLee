package SuperLee.Transport.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MenuGUI extends JFrame {

    private JFrame frame;
    private JTextField textField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuGUI window = new MenuGUI();
            window.frame.setVisible(true);
        });
    }

    public MenuGUI() {
        initialize();

        // Load the image
        ImageIcon imageIcon = loadImageIcon("Images/logosuperLee.png");

        // Create a JLabel to hold the image
        JLabel imageLabel = new JLabel(imageIcon);

        // Set up the layout
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        getContentPane().add(imageLabel, gbc);

        pack();
        setVisible(true);
    }

    private ImageIcon loadImageIcon(String imagePath) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                Image image = Toolkit.getDefaultToolkit().getImage(imageUrl);
                // You can resize the image if needed
                Image resizedImage = image.getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                return new ImageIcon(resizedImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 400); // Set the frame size to 500x400
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblUsername.setBounds(55, 119, 64, 23);
        frame.getContentPane().add(lblUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblPassword.setBounds(55, 159, 64, 23);
        frame.getContentPane().add(lblPassword);

        textField = new JTextField();
        textField.setBounds(130, 121, 86, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 161, 86, 20);
        frame.getContentPane().add(passwordField);
        passwordField.setColumns(10);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = textField.getText();
                String pass = new String(passwordField.getPassword());
                if (user.equals("transport") && pass.equals("TN2023")) {
                    JOptionPane.showMessageDialog(frame.getComponent(0), "Login Successfully");
                    frame.setVisible(false);

                    // Perform the desired action after successful login
                    // For example, create and show the main menu
                    TransportMenu transportMenu = new TransportMenu();
                    transportMenu.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame.getComponent(0), "Login Failed");
                }
            }
        });
        btnLogin.setBounds(260, 138, 89, 23);
        frame.getContentPane().add(btnLogin);
    }
}