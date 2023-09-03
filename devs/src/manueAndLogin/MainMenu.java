package manueAndLogin;

import SuperLee.HumenResource.GUI.*;
import SuperLee.HumenResource.PrasrntationLayer.HrManagerInterface;
import SuperLee.Transport.GUI.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainMenu extends JFrame {

    private PanelBorder panelBorder1;
    private Menu menu1;
    private JPanel mainPanel;
    private Forn1Test forn1Test;

    public MainMenu() {
        setTitle("Super Lee");
        initComponents();
//        setBackground(new Color(0, 0, 0, 0));
        menu1.initMoving(MainMenu.this);
        forn1Test = new Forn1Test();
//        setUndecorated(true);

        menu1.setEventMenu(new EventMenuButton() {
            @Override
            public void press(int idx) {
                if(CurrentUserController.getInstance().getUserJob().equals("HR")){

                    if(idx == 0) {
                        setForm(new NewShiftArrangement());
                    }
                    if(idx == 1){
                        setForm(new NewWorker());
                    }
                    if(idx == 2){
                        setForm(new WatchShifts());
                    }
                    if(idx == 3){
                        setForm(new EndWeekMonth());
                    }
                    if(idx == 4){
                        setForm(new AddBranch());
                    }
                    if(idx == 5){
                        setForm(new WorkersDataWindow());
                    }
                    if ((idx == 6)){
                        setForm(new upcomingTransport());
                    }
                }
                else if(CurrentUserController.getInstance().getUserJob().equals("Shift Manager") || CurrentUserController.getInstance().getUserJob().equals("Stock Keeper")){
                    if(idx == 0) {
                        setForm(new constraintsAssignments());
                    }
                    if(idx == 1){
                        setForm(new ShiftManagerAndStockKeeper());
                    }
                }
                else if(CurrentUserController.getInstance().getUserJob().equals("TM")){
                    if(idx == 4){
                        setForm(new Forn1Test());
                    }
                    if(idx == 3){
                        setForm(new orderGUI());
                    }
                    if(idx == 1){
                        setForm(new CreateDocumentGui());
                    }
                    if(idx == 2){
                        setForm(new transportGUI());
                    }
                    if(idx == 5){
                        setForm(new TransportsInfo());
                    }
                    if(idx ==6){
                        setForm(new OrdersInfo());
                    }
                    if(idx == 0){
                        setForm(new HomePanel());
                    }
                }
                else if(CurrentUserController.getInstance().getUserJob().equals("SM")){
                    if(idx == 0) {
                        setForm(new NewShiftArrangement());
                    }
                    if(idx == 1){
                        setForm(new NewWorker());
                    }
                    if(idx == 2){
                        setForm(new WatchShifts());
                    }
                    if(idx == 3){
                        setForm(new EndWeekMonth());
                    }
                    if(idx == 4){
                        setForm(new AddBranch());
                    }
                    if(idx == 5){
                        setForm(new WorkersDataWindow());
                    }
                    if ((idx == 6)){
                        setForm(new upcomingTransport());
                    }
                    if(idx == 11){
                        setForm(new Forn1Test());
                    }
                    if(idx == 10){
                        setForm(new orderGUI());
                    }
                    if(idx == 8){
                        setForm(new CreateDocumentGui());
                    }
                    if(idx == 9){
                        setForm(new transportGUI());
                    }
                    if(idx == 12){
                        setForm(new TransportsInfo());
                    }
                    if(idx ==13){
                        setForm(new OrdersInfo());
                    }
                    if(idx == 7){
                        setForm(new HomePanel());
                    }
                }
                else {
                    if(idx == 0) {
                        setForm(new constraintsAssignments());
                    }
                }




            }
        });
        Forn1Test forn1Test = new Forn1Test();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelBorder1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void setForm(JComponent com) {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    private void initComponents() {
        panelBorder1 = new PanelBorder();
        menu1 = new Menu();
        mainPanel = new JPanel();


        panelBorder1.setBackground(new Color(242, 242, 242));

        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
                panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelBorder1Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap())))
        );
        panelBorder1Layout.setVerticalGroup(
                panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
                        .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    //    public static void main(String args[]) {
//        try {
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//            ex.printStackTrace();
//        }
//
//        java.awt.EventQueue.invokeLater(() -> {
//            new MainMenu().setVisible(true);
//
//        });
//    }
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setSize(1186, 621);
                mainMenu.setLocationRelativeTo(null);
                mainMenu.setVisible(true);
            }
        });
    }
}
