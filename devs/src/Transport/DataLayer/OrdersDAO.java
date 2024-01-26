package SuperLee.Transport.DataLayer;
import SuperLee.Transport.BusinessLayer.CustomKey;
import SuperLee.Transport.BusinessLayer.Item;
import SuperLee.Transport.BusinessLayer.OrderStatus;
import SuperLee.Transport.BusinessLayer.TrainingType;
import SuperLee.Transport.BusinessLayer.TransportOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OrdersDAO { // Singleton

    private Connection conn;
    private static OrdersDAO single_instance = null;
    private HashMap<CustomKey, TransportOrder> cache;


    private OrdersDAO() {
        cache = new HashMap<>();
    }

    public static synchronized OrdersDAO getInstance() {
        if(single_instance == null){
            single_instance = new OrdersDAO();
        }
        return single_instance;
    }

    // TODO check done!
    public void addTransportOrder(TransportOrder transportOrder) {
        try {
            // Check cache for existing object with same order number and training type
            CustomKey cacheKey = new CustomKey(transportOrder.getOrderNumber(), transportOrder.getTrainingRequired());
            if (cache.containsKey(cacheKey)) {
                throw new IllegalArgumentException("Transport order with same order number and training type already exists");
            }
            // Start transaction
            conn = DataSource.openConnection();

            // Prepare statements for inserting into TransportOrders and TransportOrder_Items tables
            PreparedStatement insertTransportOrderStatement = conn.prepareStatement("INSERT INTO TransportOrders (orderNumber, supplierName, supplierNumber, address, date, contact_phone, status, trainingType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement insertTransportOrderItemsStatement = conn.prepareStatement("INSERT INTO TransportOrder_Items (orderNumber, trainingType, itemName, itemAmount, product_type) VALUES (?, ?, ?, ?, ?)");


            try {
                // Insert transport order into TransportOrders table
                insertTransportOrderStatement.setInt(1, transportOrder.getOrderNumber());
                insertTransportOrderStatement.setString(2, transportOrder.getSupplierName());
                insertTransportOrderStatement.setInt(3, transportOrder.getSupplierNumber());
                insertTransportOrderStatement.setString(4, transportOrder.getAddress());
                insertTransportOrderStatement.setString(5, transportOrder.getDate());
                insertTransportOrderStatement.setString(6, transportOrder.getContactPhone());
                insertTransportOrderStatement.setString(7, transportOrder.getStatus().toString());
                insertTransportOrderStatement.setString(8, transportOrder.getTrainingRequired().toString());
                insertTransportOrderStatement.executeUpdate();

                // Insert items into TransportOrder_Items table
                ArrayList<Item> items = transportOrder.getItems();
                for (Item item : items) {
                    insertTransportOrderItemsStatement.setInt(1, transportOrder.getOrderNumber());
                    insertTransportOrderItemsStatement.setString(2, transportOrder.getTrainingRequired().toString());
                    insertTransportOrderItemsStatement.setString(3, item.getName());
                    insertTransportOrderItemsStatement.setInt(4, item.getAmount());
                    insertTransportOrderItemsStatement.setString(5, item.getStorageType().toString());
                    insertTransportOrderItemsStatement.executeUpdate();
                }

                DataSource.closeConnection();
                // Add to cache
                cache.put(cacheKey, transportOrder);

            } catch (SQLException e) {
                // Rollback transaction and re-throw exception
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO check done!
    public void updateOrder(TransportOrder order) {
        try {
            conn = DataSource.openConnection();

            // Update TransportOrders table
            PreparedStatement updateOrderStmt = conn.prepareStatement(
                    "UPDATE TransportOrders SET supplierName = ?, supplierNumber = ?, address = ?, date = ?, contact_phone = ?, status = ? WHERE orderNumber = ? AND trainingType = ?");
            updateOrderStmt.setString(1, order.getSupplierName());
            updateOrderStmt.setInt(2, order.getSupplierNumber());
            updateOrderStmt.setString(3, order.getAddress());
            updateOrderStmt.setString(4, order.getDate());
            updateOrderStmt.setString(5, order.getContactPhone());
            updateOrderStmt.setString(6, order.getStatus().toString());
            updateOrderStmt.setInt(7, order.getOrderNumber());
            updateOrderStmt.setString(8, order.getTrainingRequired().toString());
            updateOrderStmt.executeUpdate();
            updateOrderStmt.close();

            // Update TransportOrder_Items table
            PreparedStatement deleteItemsStmt = conn.prepareStatement(
                    "DELETE FROM TransportOrder_Items WHERE orderNumber = ? AND trainingType = ?");
            deleteItemsStmt.setInt(1, order.getOrderNumber());
            deleteItemsStmt.setString(2, order.getTrainingRequired().toString());
            deleteItemsStmt.executeUpdate();
            deleteItemsStmt.close();

            PreparedStatement insertItemsStmt = conn.prepareStatement(
                    "INSERT INTO TransportOrder_Items (orderNumber, trainingType, itemName, itemAmount, product_type) VALUES (?, ?, ?, ?, ?)");
            for (Item item : order.getItems()) {
                insertItemsStmt.setInt(1, order.getOrderNumber());
                insertItemsStmt.setString(2, order.getTrainingRequired().toString());
                insertItemsStmt.setString(3, item.getName());
                insertItemsStmt.setInt(4, item.getAmount());
                insertItemsStmt.setString(5, item.getStorageType());
                insertItemsStmt.executeUpdate();
            }
            insertItemsStmt.close();

           DataSource.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    //TODO check done
    public ArrayList<TransportOrder> getOrdersByTrainingType(TrainingType type1) {
        String type = type1.toString();
        ArrayList<TransportOrder> orders = new ArrayList<>();

        try {
            conn = DataSource.openConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TransportOrders WHERE trainingType = ?");
            stmt.setString(1, type);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int orderNumber = resultSet.getInt("orderNumber");
                CustomKey key = new CustomKey(orderNumber, type);
                if (cache.containsKey(key)) {
                    orders.add(cache.get(key));
                } else {
                    TransportOrder order = new TransportOrder(orderNumber);
                    order.setOrderNumber(orderNumber);
                    order.setSupplierName(resultSet.getString("supplierName"));
                    order.setSupplierNumber(resultSet.getInt("supplierNumber"));
                    order.setAddress(resultSet.getString("address"));
                    order.setDate(resultSet.getString("date"));
                    order.setContact_phone(resultSet.getString("contact_phone"));
                    order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
                    order.setTrainingType(TrainingType.valueOf(resultSet.getString("trainingType")));
                    order.setItems(getItemsByOrderID(orderNumber, type));
                    orders.add(order);

                    cache.put(key, order);
                }
            }
            DataSource.closeConnection();
            return orders;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return null;
    }

    // helper function
    public ArrayList<Item> getItemsByOrderID(int orderNumber, String trainingType) {
        ArrayList<Item> items = new ArrayList<>();

        try {
            conn = DataSource.openConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TransportOrder_Items WHERE orderNumber = ? AND trainingType = ?");
            stmt.setInt(1, orderNumber);
            stmt.setString(2, trainingType);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String storageType = resultSet.getString("product_type");
                String itemName = resultSet.getString("itemName");
                Item item = new Item(itemName, storageType);
                item.setAmount(resultSet.getInt("itemAmount"));
                items.add(item);
            }
            DataSource.closeConnection();
            return items;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return null;
    }


    // TODO check done!
    public TransportOrder getTransportOrderByIDAndTrainingType(int orderNumber, TrainingType trainingType1) {
        String trainingType = trainingType1.toString();
        // search in the cache
        CustomKey key = new CustomKey(orderNumber, trainingType1);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        TransportOrder transportOrder = null;
        String sql = "SELECT * FROM TransportOrders WHERE orderNumber = ? AND trainingType = ?";
        conn = DataSource.openConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderNumber);
            pstmt.setString(2, trainingType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String supplierName = rs.getString("supplierName");
                int supplierNumber = rs.getInt("supplierNumber");
                String address = rs.getString("address");
                String date = rs.getString("date");
                String contactPhone = rs.getString("contact_phone");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status"));

                // Get the items list
                ArrayList<Item> items = getItemsByOrderID(orderNumber, trainingType);

                transportOrder = new TransportOrder(orderNumber, supplierName, supplierNumber, address, date, contactPhone, items);
                transportOrder.setStatus(status);
                transportOrder.setTrainingType(trainingType1);
                cache.put(key, transportOrder);
                DataSource.closeConnection();
                return transportOrder;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return transportOrder;
    }

}





