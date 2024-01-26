package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.TransportOrder;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ModelOrdersTable extends DefaultTableModel {

    public ModelOrdersTable() {
        super(Constants.DATA, Constants.TABLE_HEADER);
    }


    public class Constants {

        public static final Object[] TABLE_HEADER = {"Order Number", "Supplier Name",
                "Supplier Number", "Address", "Date", "Contact Phone", "Order Status", "Order Type"};

        public static final Object[][] DATA = null;
//                { "BAC", "Bank of America Corporation", 15.98, 0.14, "+0.88%",
//                        32157250 },
//                { "AAPL", "Apple Inc.", 126.57, -1.97, "-1.54%", 31367143 },
//                { "ABBV", "AbbVie Inc.", 57.84, -2.43, "-4.03%", 30620258 },
//                { "ECA", "Encana Corporation", 11.74, -0.53, "-4.33%", 27317436 },
//                { "VALE", "Vale S.A.", 6.55, -0.33, "-4.80%", 19764400 },
//                { "FB", "Facebook, Inc.", 81.53, 0.64, "+0.78%", 16909729 },
//                { "PBR", "Petróleo Brasileiro S.A. - Petrobras", 6.05, -0.12,
//                        "-2.02%", 16181759 },
//                { "NOK", "Nokia Corporation", 8.06, 0.01, "+0.12%", 13611860 },
//                { "PCYC", "Pharmacyclics Inc.", 254.67, 24.19, "+10.50%", 13737834 },
//                { "RAD", "Rite Aid Corporation", 7.87, -0.18, "-2.24%", 13606253 } };
    }

    // Method to populate the table with data from the database
    public void populateTable(ArrayList<TransportOrder> orders) {
        // Clear existing data
        setRowCount(0);

        // Populate the table with new data
        for (TransportOrder order : orders) {
            Object[] rowData = {
                    order.getOrderNumber(),
                    order.getSupplierName(),
                    order.getSupplierNumber(),
                    order.getAddress(),
                    order.getDate(),
                    order.getContactPhone(),
                    order.getStatus(),
                    order.getTrainingRequired()
            };
            addRow(rowData);
        }
    }
}
