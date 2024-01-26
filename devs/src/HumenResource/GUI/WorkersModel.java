package SuperLee.HumenResource.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class WorkersModel extends DefaultTableModel {

    private WorkersTableContent tableContent;

    public WorkersModel() {
        super(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex + 1) {
                case 1, 2, 3, 4, 5, 6, 9, 10, 13:
                    return String.class;
                case 7, 11:
                    return Integer.class;
                case 8, 12:
                    return Double.class;
            }
        return super.getColumnClass(columnIndex);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 12; // Only the "List Of Types" column is editable
    }




}
