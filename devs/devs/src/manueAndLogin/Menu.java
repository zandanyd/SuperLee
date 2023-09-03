package manueAndLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class Menu extends JPanel {
    private ListMenu<String> listMenu1;
    private EventMenuButton eventMenuButton;


    public Menu() {
        initComponents();
        setOpaque(false);
        listMenu1.setOpaque(false);
        init();
    }

    public void setEventMenu(EventMenuButton eventMenuButton){
        this.eventMenuButton = eventMenuButton;
        listMenu1.setEventMenu(eventMenuButton);
    }


    private void init() {
        if (CurrentUserController.getInstance().getUserJob().equals("HR")) {
            listMenu1.addItem(new Model_Menu("1", "Make New Shift Arrangement", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("2", "Assign New Worker ", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("3", "Watch Shifts", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("4", "End Week or Month", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("5", "Add Branch", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("5", "Workers Data Window", Model_Menu.MenuType.TITLE));
            listMenu1.addItem(new Model_Menu("7", "Watch Upcoming Transports", Model_Menu.MenuType.MENU));
//        listMenu1.addItem(new Model_Menu("7", "Extra", Model_Menu.MenuType.MENU));
//        listMenu1.addItem(new Model_Menu("8", "Extra", Model_Menu.MenuType.MENU));
//        listMenu1.addItem(new Model_Menu("9", "Extra", Model_Menu.MenuType.MENU));
//        listMenu1.addItem(new Model_Menu("10", "Logout", Model_Menu.MenuType.MENU));
//        listMenu1.addItem(new Model_Menu("", "", Model_Menu.MenuType.EMPTY));
        } else if (CurrentUserController.getInstance().getUserJob().equals("TM")) {
            listMenu1.addItem(new Model_Menu("1", "Home", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("2", "Create a new Transport Document", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("3", "Transport Operation", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("4", "Add a new Order", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("5", "Add a new Truck", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("6", "Documents Information", Model_Menu.MenuType.MENU));


        } else if (CurrentUserController.getInstance().getUserJob().equals("Shift Manager")) {
            listMenu1.addItem(new Model_Menu("1", "Add Constraints to the next week", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("2", "Watch Upcoming Transports And Cancel Items", Model_Menu.MenuType.MENU));
        }
        else if(CurrentUserController.getInstance().getUserJob().equals("Stock Keeper")) {

            listMenu1.addItem(new Model_Menu("1", "Add Constraints to the next week", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("2", "Watch Upcoming Transports", Model_Menu.MenuType.MENU));
        }else if(CurrentUserController.getInstance().getUserJob().equals("SM")){
            listMenu1.addItem(new Model_Menu("1", "Make New Shift Arrangement", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("2", "Assign New Worker ", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("3", "Watch Shifts", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("4", "End Week or Month", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("5", "Add Branch", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("5", "Workers Data Window", Model_Menu.MenuType.TITLE));
            listMenu1.addItem(new Model_Menu("7", "Watch Upcoming Transports", Model_Menu.MenuType.MENU));


            //noam add here
            listMenu1.addItem(new Model_Menu("1", "Home", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("2", "Create a new Transport Document", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("3", "Transport Operation", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("4", "Add a new Order", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("5", "Add a new Truck", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("6", "Documents Information", Model_Menu.MenuType.MENU));
        } else {
            listMenu1.addItem(new Model_Menu("1", "Add Constraints to the next week", Model_Menu.MenuType.MENU));
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMoving = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        listMenu1 = new ListMenu<>();

        panelMoving.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon("src/Icons/logo.png")); // NOI18N
        jLabel1.setText("SUPER LEE");

        javax.swing.GroupLayout panelMovingLayout = new javax.swing.GroupLayout(panelMoving);
        panelMoving.setLayout(panelMovingLayout);
        panelMovingLayout.setHorizontalGroup(
                panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMovingLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                .addContainerGap())
        );
        panelMovingLayout.setVerticalGroup(
                panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMovingLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel1)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelMoving, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    @Override
    protected void paintChildren(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#1CB5E0"), 0, getHeight(), Color.decode("#000046"));
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
        super.paintChildren(grphcs);
    }

    private int x;
    private int y;

    public void initMoving(JFrame frame) {
        panelMoving.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                x = me.getX();
                y = me.getY();
            }
        });
        panelMoving.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                frame.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
            }
        });
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(206, 450);
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel panelMoving;
    // End of variables declaration
}
