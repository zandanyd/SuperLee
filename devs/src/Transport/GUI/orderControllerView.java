package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.Item;
import SuperLee.Transport.BusinessLayer.OrdersController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class orderControllerView implements ActionListener{
    private JTextField orderNumber;
    private JTextField supplierName;
    private JTextField supplierNumber;
    private JTextField address;
    private JTextField phoneNumber;
    private JComboBox<String> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> dayComboBox;
    private static ArrayList<Item> items = new ArrayList<Item>();

    private OrdersController ordersController = OrdersController.getInstance();

    public orderControllerView(JTextField orderNumber, JTextField supplierName, JTextField supplierNumber, JTextField address, JTextField phoneNumber, JComboBox<String> yearComboBox, JComboBox<String> monthComboBox, JComboBox<String> dayComboBox) {
        super();
        this.orderNumber = orderNumber;
        this.supplierName = supplierName;
        this.supplierNumber = supplierNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.yearComboBox = yearComboBox;
        this.monthComboBox = monthComboBox;
        this.dayComboBox = dayComboBox;
//        this.ordersController = ordersController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String year = (String) yearComboBox.getSelectedItem();
        String month = (String) monthComboBox.getSelectedItem();
        String day = (String) dayComboBox.getSelectedItem();
        String date = day + "-" + month + "-" + year;
        ordersController.createTransportOrder(Integer.parseInt(orderNumber.getText()),supplierName.getText(),Integer.parseInt(supplierNumber.getText()),address.getText(),date, phoneNumber.getText(), items);
    }

    public static void addItemTolist(Item item){
        items.add(item);
    }

}
