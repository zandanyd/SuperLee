package SuperLee.HumenResource.PrasrntationLayer;

import SuperLee.HumenResource.BusinessLayer.*;
import SuperLee.TransportWorkersController;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class  WorkerInterface {
    static void Start(String workerID) {
        BranchController branchController;
        WorkerController workerController;
        DriverController driverController;
        BranchShiftController shiftController;
        String branchID;
        boolean isDriver = true;
        branchController = BranchController.getInstance();
        workerController = WorkerController.getInstance();
        driverController = DriverController.getInstance();
        shiftController = BranchShiftController.getInstance();
        try {

            if(workerController.isExist(workerID)) {
                branchID = branchController.getBranchIdByWorker(workerID);
                isDriver = false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }
        Scanner IntScanner = new Scanner(System.in);
        Scanner StringScanner = new Scanner(System.in);
        Scanner DoubleScanner = new Scanner(System.in);
        int ch = 1;
        try {
            if(!isDriver)
                System.out.println("Hello, " + workerController.getWorkerFirstName(workerID) + " " + workerController.getWorkerLastName(workerID) + " Welcome back!");
            else {
                System.out.println("Hello, " + driverController.getDriverFirstName(workerID) + " " + driverController.getDriverLastName(workerID) + " Welcome back!");
            }
            while (ch != 0) {
                System.out.println("choose one of the following options:");
                System.out.println("1. Change password");
                System.out.println("2. Watch next week shift schedule ");
                System.out.println("3. Add constraint");
                System.out.println("4. Shift manager options");
                System.out.println("5. Show upcoming Transports");
                System.out.println("6. Exit");
                int choice = IntScanner.nextInt();
                int MoreConstraints;
                boolean MoreShifts = true;
                switch (choice) {
                    case 1:
                        System.out.println("Enter your new password");
                        if(isDriver){
                            driverController.setDriverPassword(workerID, StringScanner.nextLine());
                        }
                        else {
                            workerController.setWorkerPassword(workerID, StringScanner.nextLine());
                        }
                        break;
                    case 2:
                        branchID = branchController.getBranchIdByWorker(workerID);
                        System.out.println("Your next week schedule is:");
                        branchController.ShowSchedule(branchID);
                        break;
                    case 3:
                        while (MoreShifts) {
                            // receives details from the worker and creates a constraint\s
                            System.out.println("Please enter a date in the following format DD/MM");
                            String date = StringScanner.nextLine();
                            String[] arrOfStr = date.split("/", 2);
                            if (!UserInterface.InputValidationCheck(arrOfStr[0]) || !UserInterface.InputValidationCheck(arrOfStr[1])) {
                                System.out.println("Please enter a valid date");
                                break;
                            }
                            int day = Integer.parseInt(arrOfStr[0]);
                            int month = Integer.parseInt(arrOfStr[1]);
                            if(day < LocalDate.now().getDayOfMonth() && month < LocalDate.now().getMonthValue()){
                                System.out.println("Please enter a correct date");
                                break;
                            }
                            int year = LocalDate.now().getYear();
                            LocalDate AvbDate = LocalDate.of(year, month, day);
                            System.out.println("Please select which shift would you prefer Morning/Evening");
                            String type = StringScanner.nextLine();
                            ShiftType time = null;
                            if (Objects.equals(type, "Morning")) {
                                time = ShiftType.Morning;
                            } else if (Objects.equals(type, "Evening")) {
                                time = ShiftType.Evening;
                            } else {
                                System.out.println("Please enter a valid shift time");
                                break;
                            }
                            workerController.AddConstraint(workerID, AvbDate, time);
                            System.out.println("Do you have more constraints?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            try {
                                MoreConstraints = IntScanner.nextInt();
                                switch (MoreConstraints) {
                                    case 1:
                                        break;
                                    case 2:
                                        MoreShifts = false;
                                        break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a number");
                                break;
                            }
                        }
                        break;
                    case 4:
                        //checks if the current user is a shift manager
                        branchID = branchController.getBranchIdByWorker(workerID);
                        boolean found_4 = false;
                        if ((!workerController.isShiftManager(workerID))) {
                            System.out.println("Access denied! accessible for Shift managers only!!");
                            break;
                        }
                        int choice1 = 0 ;
                        System.out.println("**************");
                        boolean exit = false;
                        while (!exit) {
                            if(workerController.isShiftManager(workerID)) {
                                System.out.println("Choose one option");
                                System.out.println("1. Add item to the cancel list");
                                System.out.println("2. End shift");
                                System.out.println("3. Back to the previous menu");
                                try {
                                    choice1 = IntScanner.nextInt();
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a number");
                                    break;
                                }
                            }
                            if(choice1 == 3){
                                exit = true;
                                break;
                            }
                            ShiftType time;
                            LocalDate AvbDate;
                            // receives from the shift manager details for a specific shift and update the shift cancellation list\ end the shift
                            System.out.println("Please enter the shift date in the following format DD/MM");
                            String date = StringScanner.nextLine();
                            String[] arrOfStr = date.split("/", 2);
                            if (!UserInterface.InputValidationCheck(arrOfStr[0]) || !UserInterface.InputValidationCheck(arrOfStr[1])) {
                                System.out.println("Please enter a valid date");
                                break;
                            }
                            int day = Integer.parseInt(arrOfStr[0]);
                            int month = Integer.parseInt(arrOfStr[1]);
                            int year = LocalDate.now().getYear();
                            AvbDate = LocalDate.of(year, month, day);
                            System.out.println("Please select the shift type Morning/Evening");
                            String type = StringScanner.nextLine();
                            if (Objects.equals(type, "Morning")) {
                                time = ShiftType.Morning;
                                found_4 = true;
                            } else if (Objects.equals(type, "Evening") && !found_4) {
                                time = ShiftType.Evening;
                            } else {
                                System.out.println("Please enter a valid shift time");
                                break;
                            }

                            if (!shiftController.isExist(branchID, AvbDate, time)) {
                                System.out.println("This shift does not exist in the system");
                                break;
                            }

                            if (!shiftController.isWorkerInShift(branchID, workerID, AvbDate, time)) {
                                System.out.println("Sorry, but you aren't part in this particular shift");
                                break;
                            }

                            switch (choice1) {
                                case 1:
                                    System.out.println("Enter the name of the item you want to cancel");
                                    shiftController.addCancelItem(branchID, AvbDate, time, StringScanner.nextLine());
                                    System.out.println("Item has been cancelled");
                                    break;
                                case 2:
                                    shiftController.EndShift(branchID, AvbDate, time);
                                    System.out.println("Shift ends");
                                    shiftController.printShift(branchID, AvbDate, time);
                                    break;
                                case 3:
                                    exit = true;
                                    break;
                                default:
                                    System.out.println("This option does not exist");
                                    break;
                            }
                        }
                        break;
                    case 5:
                        branchID = branchController.getBranchIdByWorker(workerID);
                        System.out.println("Please enter the shift date you want see the upcoming transports in the following format DD/MM");
                        String date = StringScanner.nextLine();
                        String[] arrOfStr = date.split("/", 2);
                        if (!UserInterface.InputValidationCheck(arrOfStr[0]) || !UserInterface.InputValidationCheck(arrOfStr[1])) {
                            System.out.println("Please enter a valid date");
                            break;
                        }
                        int day = Integer.parseInt(arrOfStr[0]);
                        int month = Integer.parseInt(arrOfStr[1]);
                        int year = LocalDate.now().getYear();
                        LocalDate AvbDate = LocalDate.of(year, month, day);
                        System.out.println("Please select the shift type Morning/Evening");
                        String type = StringScanner.nextLine();
                        ShiftType time;
                        if (Objects.equals(type, "Morning")) {
                            time = ShiftType.Morning;
                        } else if (Objects.equals(type, "Evening") ) {
                            time = ShiftType.Evening;
                        } else {
                            System.out.println("Please enter a valid shift time");
                            break;
                        }
                        System.out.println("These are the upcoming transports for your branch");
                        TransportWorkersController.getInstance().displayAllTransportsBySiteAndDate(branchID,AvbDate.toString());
                        if(workerController.isStockKeeper(workerID))
                            choice1 = 4;
                        break;
                    case 6:
                        System.out.println("Good bye");
                        ch = 0;
                        break;
                    default:
                        System.out.println("There is no such an option");
                        break;
                }
            }
            System.out.println();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}