package SuperLee.Transport.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import SuperLee.Transport.BusinessLayer.*;


public class transportControllerView implements ActionListener{
    TransportsController transportsController = TransportsController.getInstance();
    TransportDocumentController transportDocumentController = TransportDocumentController.getInstance();
    TransportManagment transportManagment = TransportManagment.getInstance();
    JComboBox<String> transportComboBox;
    JComboBox<String> startTransportComboBox;
    JComboBox<String > manageTransportComboBox;
    JButton endTransportButton;
    JButton startTransportButton;

    public transportControllerView(JComboBox<String> transportComboBox, JComboBox<String> startTransportComboBox, JComboBox<String > manageTransportComboBox) {
        super();
        this.transportComboBox = transportComboBox;
        this.startTransportComboBox = startTransportComboBox;
        this.manageTransportComboBox = manageTransportComboBox;

        showTransportToFinishComboBox(transportComboBox);
        showTransportToStartComboBox(startTransportComboBox);
        showTransportToManageComboBox(manageTransportComboBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == endTransportButton) {
            // Action for the endTransportButton
            Object selectedTransport = transportComboBox.getSelectedItem();
            if (selectedTransport != null) {
                try {
                    String transportString = selectedTransport.toString();
                    int transportId = Integer.parseInt(transportString.replace("transport ", ""));
                    transportsController.finishTransport(transportId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(transportComboBox, "Invalid transport ID: " + selectedTransport);
                }
            } else {
                JOptionPane.showMessageDialog(transportComboBox, "Please select a transport to finish.");
            }
        } else if (source == startTransportButton) {
            // Action for the startTransportButton
            Object selectedTransport2 = startTransportComboBox.getSelectedItem();
            if (selectedTransport2 != null) {
                try {
                    String transportString2 = selectedTransport2.toString();
                    int docId = Integer.parseInt(transportString2.replace("transportDoc ", ""));
                    transportManagment.startTransport(docId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(startTransportComboBox, "Invalid transport document ID: " + selectedTransport2);
                }
            } else {
                JOptionPane.showMessageDialog(startTransportComboBox, "Please select a transport to start.");
            }
        }
    }
    // ------
    public void setEndTransportButton(JButton endTransportButton) {
        this.endTransportButton = endTransportButton;
    }
    public void setStartTransportButton(JButton startTransportButton) {
        this.startTransportButton = startTransportButton;
    }

    // -------
    public void showTransportToFinishComboBox(JComboBox<String> transportComboBox){
        ArrayList<Transport> TransportList = transportsController.getAllInTransitTransports();
        for (Transport transport : TransportList){
            transportComboBox.addItem("transport " + transport.getId());
        }
    }
    public void showTransportToStartComboBox(JComboBox<String> startTransportComboBox){
        ArrayList<TransportDocument> TransportDocList = transportDocumentController.getAllPendingDocuments();
        for (TransportDocument transportDocument : TransportDocList){
            startTransportComboBox.addItem("transportDoc " + transportDocument.getId());
        }
    }
    public void showTransportToManageComboBox(JComboBox<String> manageTransportComboBox){
        ArrayList<Transport> TransportInProccess = transportsController.getAllInTransitTransports();
        for (Transport transport : TransportInProccess){
            manageTransportComboBox.addItem("transport " + transport.getId());
        }
    }
    public ArrayList<Transport> transportsInfo(){
        ArrayList<Transport> allTransports = transportsController.getAllTransports();
        return allTransports;
    }
}
