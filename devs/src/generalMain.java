import SuperLee.HumenResource.PrasrntationLayer.UserInterface;
import manueAndLogin.CurrentUserController;
import manueAndLogin.MainMenu;
import manueAndLogin.MyNewMain;

import java.sql.SQLException;

public class generalMain {

    public static void main(String[] args) throws SQLException {
        if(args.length==0){

                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new MyNewMain().setVisible(true);
                    }
                });
            return;
        }
        if(args[0].equals("CLI"))
        {
            if(args.length==1){
                UserInterface.Start(false,false,false);
                return;
            }
            if(args[1].equals("logisticalManager"))
            {
                UserInterface.Start(false,true,false);
            }
            if(args[1].equals("HRManager"))
            {
                UserInterface.Start(true,false,false);
            }
            if(args[1].equals("StoreManager"))
            {
                UserInterface.Start(false,false,true);

            }
        }
        if(args[0].equals("GUI"))
        {
            if(args.length==1){
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new MyNewMain().setVisible(true);
                    }
                });
                return;
            }
            if(args[1].equals("logisticalManager"))
            {
                CurrentUserController.getInstance().setUserJob("TM");
            }
            if(args[1].equals("HRManager"))
            {
                CurrentUserController.getInstance().setUserJob("HR");
            }
            if(args[1].equals("StoreManager"))
            {
                CurrentUserController.getInstance().setUserJob("SM");
            }
            MainMenu mainMenu = new MainMenu();
            mainMenu.setSize(1186, 621);
            mainMenu.setLocationRelativeTo(null);
            mainMenu.setVisible(true);
        }
    }
}

