package SuperLee.Transport.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransportMenu extends JFrame {

    public TransportMenu() {
        // Set up the JFrame
        setTitle("Transport Menu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the components
        JButton transportButton = new JButton("Transport Operations Menu");
        transportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transportGUI transportGUI = new transportGUI();
                transportGUI.setVisible(true);
                TransportMenu.this.setVisible(false); // Hide the TransportMenu frame
            }
        });

        JButton orderButton = new JButton("Order Operations Menu");
        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orderGUI orderGUI = new orderGUI();
                orderGUI.setVisible(true);
                TransportMenu.this.setVisible(false); // Hide the TransportMenu frame
            }
        });

        JButton truckButton = new JButton("Truck Operations Menu");
        truckButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                truckGUI truckGUI = new truckGUI();
                truckGUI.setVisible(true);
                TransportMenu.this.setVisible(false); // Hide the TransportMenu frame
            }
        });

        JPanel panel = new JPanel();
        panel.add(transportButton);
        panel.add(orderButton);
        panel.add(truckButton);

        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransportMenu transportMenu = new TransportMenu();
        });
    }
}