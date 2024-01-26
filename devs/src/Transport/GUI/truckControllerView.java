package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.TruckController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class truckControllerView implements ActionListener {
    private JTextField licenseField;
    private JTextField modelField;
    private JTextField netWeightField;
    private JTextField maxWeightField;
    private JComboBox<String> truckTypeComboBox;
    private TruckController truckController = TruckController.getInstance();

    public truckControllerView(JTextField licenseField, JTextField modelField, JTextField netWeightField, JTextField maxWeightField, JComboBox<String> truckTypeComboBox, TruckController truckController){
        super();
        this.licenseField =licenseField;
        this.modelField = modelField;
        this.netWeightField = netWeightField;
        this.maxWeightField = maxWeightField;
        this.truckTypeComboBox = truckTypeComboBox;
        this.truckController = truckController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        truckController.createTruck(Integer.parseInt(licenseField.getText()),modelField.getText(),Integer.parseInt(netWeightField.getText()),Integer.parseInt(maxWeightField.getText()),truckTypeComboBox.getSelectedItem().toString());
    }

    public boolean checkLicenseExist(int licenseNumber){
        return truckController.truckExist(licenseNumber);
    }

}
