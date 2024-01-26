package SuperLee.HumenResource.PrasrntationLayer;

import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.HumenResource.BusinessLayer.GenericWorker;
import SuperLee.HumenResource.BusinessLayer.WorkerController;
import SuperLee.Transport.PresentationLayer.SystemUI;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;


public class UserInterface {


    public static boolean InputValidationCheck(String str){
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean InputIntValidationCheck(String str){
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void Start(Boolean HR, Boolean TM, Boolean SM) {
        boolean loaded = false;

        WorkerController workerController = null;
        DriverController driverController = null;
        workerController = WorkerController.getInstance();
        driverController = DriverController.getInstance();


        try {
            System.out.println("****** WELCOME TO SUPERLEE ******");
            Scanner IntScanner = new Scanner(System.in);
            Scanner StringScanner = new Scanner(System.in);
            int choice = 0;
            int ch = 1;
            boolean isDriver = false;
            while (ch != 0) {
                if(HR)
                {
                    System.out.println("choose one of the following options");
                    System.out.println("1. Enter as HR Manager");
                    System.out.println("2. Exit ");
                }
                else if(TM)
                {
                    System.out.println("choose one of the following options");
                    System.out.println("1. Enter as Transport Manager");
                    System.out.println("2. Exit ");
                }
                else if(SM)
                {
                    System.out.println("choose one of the following options");
                    System.out.println("1. Enter as SuperLee worker");
                    System.out.println("2. Enter as HR Manager");
                    System.out.println("3. Enter as Transport Manager");
                    System.out.println("4. Exit ");
                }
                else{
                    System.out.println("choose one of the following options");
                    System.out.println("1. Enter as SuperLee worker");
                    System.out.println("2. Enter as HR Manager");
                    System.out.println("3. Enter as Transport Manager");
                    System.out.println("4. Exit ");
                }
                choice = IntScanner.nextInt();
                if(HR)
                {
                    if(choice == 1)
                    {
                        HrManagerInterface.Start();
                    }
                    if(choice == 2)
                    {
                        choice = 4;
                    }
                }
                if(TM)
                {
                    if(choice == 1)
                    {
                        SystemUI.start();
                    }
                    if(choice == 2)
                    {
                        choice = 4;
                    }
                }
                switch (choice) {
                    case 1:
                        System.out.println("**************************");
                        GenericWorker worker;
                        boolean again = true;
                        while (again) {
                            // the system receives a worker name and checks if there is such a worker in the company
                            System.out.println(" Please enter your user name");
                            String UserName = StringScanner.nextLine();
                            if (!InputValidationCheck(UserName)) {
                                System.out.println("Please enter a valid ID");
                                break;
                            }
                            if (!workerController.isExist(UserName) && !driverController.isExist(UserName)) {
                                System.out.println("This user name does not exist in the system");
                                break;
                            } else {
                                if(driverController.isExist(UserName)){
                                    isDriver = true;
                                }
                                // the system checks if password that the user entered is identical to his password
                                System.out.println(" Please enter your password");
                                String password = StringScanner.nextLine();
                                if(isDriver){
                                    if(!driverController.VerifyId(UserName, password)){
                                        System.out.println("Wrong password");
                                    }
                                    else{
                                        WorkerInterface.Start(UserName);
                                        again = false;
                                        break;
                                    }
                                }
                                if (!workerController.VerifyId(UserName, password)) {
                                    System.out.println("Wrong password");
                                } else {
                                    WorkerInterface.Start(UserName);
                                    again = false;
                                    break;
                                }
                            }
                        }
                        break;
                    case 2:
                        int i = 0;
                        if (SM)
                        {
                            HrManagerInterface.Start();
                            break;
                        }
                        while (i < 3) {
                            // the system checks if the password that was entered belongs to the HR manager
                            System.out.println(" Please enter the HR manager password");
                            String password = StringScanner.nextLine();
                            if (!Objects.equals(password, "DI2023")) {
                                i++;
                                System.out.println("Wrong Password, Please try again you have " + (3 - i) + " more chances");
                            } else {
                                break;
                            }
                        }
                        if (i >= 3) {
                            break;
                        } else {
                            HrManagerInterface.Start();
                        }
                        break;
                    case 3:
                        if (SM)
                        {
                            SystemUI.start();
                            break;
                        }
                        int k = 0;
                        while (k < 3) {
                            // the system checks if the password that was entered belongs to the HR manager
                            System.out.println(" Please enter the Transport manager password");
                            String password = StringScanner.nextLine();
                            if (!Objects.equals(password, "TN2023")) {
                                k++;
                                System.out.println("Wrong Password, Please try again you have " + (3 - k) + " more chances");
                            } else {
                                break;
                            }
                        }
                        if (k >= 3) {
                            break;
                        } else {
                            SystemUI.start();
                        }
                        break;
                    case 4:
                        System.out.println("Good bye, Have a nice day");
                        ch = 0;
                        break;
                    default:
                        System.out.println("Sorry, But this option does not exist");
                        break;

                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
