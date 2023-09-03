package manueAndLogin;

import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.HumenResource.BusinessLayer.SimpleWorkerType;
import SuperLee.HumenResource.BusinessLayer.WorkerController;
import SuperLee.HumenResource.PrasrntationLayer.HrManagerInterface;
import com.raven.swing.MyPasswordField;
import com.raven.swing.MyTextField;

import javax.swing.*;
import java.util.ArrayList;

public class CurrentUserController {
    private static CurrentUserController instance = null;
    private String userJob;
    private String id;

    private CurrentUserController(){
    }
    public static CurrentUserController getInstance(){
        if(instance == null){
            instance = new CurrentUserController();
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public String getUserJob() {
        return userJob;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }
    public boolean setAll(MyTextField userName, MyPasswordField password){
        if(userName.getText().equals("HR") && password.getText().equals("DI2023")){
            this.userJob = "HR";
            this.id = null;
            ArrayList<String> messages = HrManagerInterface.UrgentMessagesForTheHR();
            if(messages.size()>0){
                JOptionPane.showMessageDialog(null, String.join("",messages), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return true;
        }
        if(userName.getText().equals("SM") && password.getText().equals("SM2023")){
            this.userJob = "SM";
            this.id = null;
            return true;

        }
        if(userName.getText().equals("TM") && password.getText().equals("TN2023")){
            this.userJob = "TM";
            this.id = null;
            return true;
        }
        if(userName.getText().equals("TM") && password.getText().equals("TN2023")){
            this.userJob = "TM";
            this.id = null;
            return true;
        }
        try {
            if(WorkerController.getInstance().isExist(userName.getText())){
                if(!WorkerController.getInstance().VerifyId(userName.getText(),password.getText())){
                    JOptionPane.showMessageDialog(null, "wrong user name or password try again", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(WorkerController.getInstance().isShiftManager(userName.getText())){
                    this.userJob = "Shift Manager";
                    this.id = userName.getText();
                    return true;
                }
                if(WorkerController.getInstance().isTrainingExist(SimpleWorkerType.StockKeeper,userName.getText())){
                    this.userJob = "Stock Keeper";
                    this.id = userName.getText();
                    return true;
                }
                this.userJob = "Simple Worker";
                this.id = userName.getText();
                return true;
            }
            if(DriverController.getInstance().isExist(userName.getText())){
                if(!DriverController.getInstance().VerifyId(userName.getText(),password.getText())) {
                    JOptionPane.showMessageDialog(null, "wrong user name or password try again", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                this.userJob = "Driver";
                this.id = userName.getText();
                return true;
            }
            JOptionPane.showMessageDialog(null, "wrong user name or password try again", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "try again", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }
}
