package SuperLee.HumenResource.GUI;

import javax.swing.*;
import java.awt.*;

public class AddBranch extends JPanel {
    private JTextField addressTextField;
    private DefaultListModel<String> branchListModel;
    private AddBranchController controller;

    public AddBranch() {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel backgroundLabel = new JLabel(new ImageIcon("background.png"));
        backgroundLabel.setLayout(new FlowLayout());
        mainPanel.add(backgroundLabel, BorderLayout.CENTER);

        // Create branch address panel
        JPanel addressPanel = new JPanel(new FlowLayout());
        JLabel addressLabel = new JLabel("Branch Address:");
        addressTextField = new JTextField(20);
        addressPanel.add(addressLabel);
        addressPanel.add(addressTextField);

        // Create branch list panel
        JPanel branchListPanel = new JPanel(new BorderLayout());
        branchListModel = new DefaultListModel<>();
        controller = new AddBranchController(addressTextField);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Branch");
        addButton.addActionListener(e -> addBranchToList());
        addButton.addActionListener(controller);
        branchListModel.addAll(controller.getAllBranches());
        JList<String> branchList = new JList<>(branchListModel);
        JScrollPane branchScrollPane = new JScrollPane(branchList);
        branchListPanel.add(branchScrollPane, BorderLayout.CENTER);

        // Add button to the button panel
        buttonPanel.add(addButton);

        // Add panels to the main panel
        mainPanel.add(addressPanel, BorderLayout.NORTH);
        mainPanel.add(branchListPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to this JPanel
        add(mainPanel);
    }

    private void addBranchToList() {
        if (branchListModel.contains(addressTextField.getText())) {
            return;
        }
        String address = addressTextField.getText();
        if (!address.isEmpty()) {
            branchListModel.addElement(address);
            addressTextField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Add Branch");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.getContentPane().add(new AddBranch());
            frame.setVisible(true);
        });
    }
}
