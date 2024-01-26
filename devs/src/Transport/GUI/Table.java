package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.OrderStatus;
import SuperLee.Transport.BusinessLayer.TransportStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Table extends JTable {

    public Table() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                TableDesign header = new TableDesign(o + "");
                if (i1 == 4) {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                if (i1 != 3) {
                    Component com = super.getTableCellRendererComponent(jtable, o, selected, bln1, i, i1);
                    com.setBackground(Color.WHITE);
                    setBorder(noFocusBorder);
                    if (selected) {
                        com.setForeground(new Color(15, 89, 140));
                    } else {
                        com.setForeground(new Color(102, 102, 102));
                    }
                    return com;
                } else {
                    TransportStatus type = (TransportStatus) o;
                    CellStatus cell = new CellStatus(type);
                    return cell;
                }
//                    if (o instanceof TableStatus) {
//                        TransportStatus type = (TransportStatus) o;
//                        CellStatus cell = new CellStatus(type);
//                        return cell;
//                    } else {
//                        return super.getTableCellRendererComponent(jtable, o, selected, bln1, i, i1);
//                    }
//                }
            }

        });
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }




    // Table Cell Color by status Class
    private class TableStatus extends JLabel {

        public TransportStatus getType() {
            return type;
        }

        public TableStatus() {
            setForeground(Color.WHITE);
        }

        private TransportStatus type;

        public void setType(TransportStatus type) {
            this.type = type;
            setText(type.toString());
            repaint();
        }


        @Override
        protected void paintComponent(Graphics grphcs) {
            if (type != null) {
                Graphics2D g2 = (Graphics2D) grphcs;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint g;
                OrderStatus StatusType;
                if (type == TransportStatus.IN_TRANSIT) {
                    g = new GradientPaint(0, 0, new Color(186, 123, 247), 0, getHeight(), new Color(167, 94, 236));
                } else if (type == TransportStatus.DONE) {
                    g = new GradientPaint(0, 0, new Color(142, 142, 250), 0, getHeight(), new Color(123, 123, 245));
                } else {
                    g = new GradientPaint(0, 0, new Color(241, 208, 62), 0, getHeight(), new Color(211, 184, 61));
                }
                g2.setPaint(g);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
            }
            super.paintComponent(grphcs);
        }
    }


    public class CellStatus extends JPanel {
        TableStatus status;


        public CellStatus(TransportStatus type) {
            initComponents();
            status.setType(type);
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

            status = new TableStatus();

            setBackground(new java.awt.Color(255, 255, 255));

            status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            status.setText("tableStatus1");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap(10, Short.MAX_VALUE)
                                    .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(10, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(status, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                    .addGap(8, 8, 8))
            );
        }// </editor-fold>//GEN-END:initComponents


    }
}