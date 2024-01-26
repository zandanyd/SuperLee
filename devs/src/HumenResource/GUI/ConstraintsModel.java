package SuperLee.HumenResource.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class ConstraintsModel extends DefaultTableModel {

    public ConstraintsModel() {
            super(constraintsTableContent.data,constraintsTableContent.columnNames);
            this.setValueAt("Morning 8:00 - 15:00", 0, 0);
            this.setValueAt("Evening 15:00 - 22:00", 1, 0);
    }


    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0){
            return String.class;
        }
        else{
            return Boolean.class;
        }
    }

    // Custom cell renderer for column headers
    private static class HeaderCellRenderer extends DefaultTableCellRenderer {

        public HeaderCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setForeground(Color.WHITE);
            setBackground(Color.BLUE);
            setFont(getFont().deriveFont(Font.BOLD, 20f));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
    }

    // Custom cell renderer for row headers
    private static class RowHeaderRenderer extends DefaultTableCellRenderer {
        public RowHeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setForeground(Color.WHITE);
            setBackground(Color.BLUE);
            setFont(getFont().deriveFont(Font.BOLD, 18f));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
    }






}

