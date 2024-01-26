package SuperLee.HumenResource.PrasrntationLayer;
import SuperLee.HumenResource.BusinessLayer.*;
import SuperLee.Transport.BusinessLayer.LicenseType;
import SuperLee.Transport.BusinessLayer.TrainingType;
import SuperLee.TransportWorkersController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;



public class HrManagerInterface {


    public static ArrayList<String> UrgentMessagesForTheHR() {
        BranchController b = BranchController.getInstance();
        BranchShiftController s = BranchShiftController.getInstance();
        ArrayList<String> UrgentMessages = new ArrayList<>();
        try {
            LocalDate Today = LocalDate.now();
            LocalDate Tomorrow = null;
            LocalTime TimeToCheck = null;
            int tomorrow = 0;
            if (Today.lengthOfMonth() < Today.getDayOfMonth() + 1) {
                tomorrow = 1;
                Tomorrow = LocalDate.of(LocalDate.now().getYear(), Today.getMonthValue() + 1, tomorrow);
            } else {
                tomorrow = LocalDate.now().getDayOfMonth() + 1;
                Tomorrow = LocalDate.of(LocalDate.now().getYear(), Today.getMonthValue(), tomorrow);
            }
            LocalTime RightNow = LocalTime.now();
            for (Branch branch : b.getListOfAllBranches()) {
                String[] arrOfTime = b.getFinishHour(branch.getAddress(), Tomorrow.getDayOfWeek().getValue(), 0).split(":", 2);
                TimeToCheck = LocalTime.of(Integer.parseInt(arrOfTime[0]), Integer.parseInt(arrOfTime[1]));
                if (RightNow.isBefore(TimeToCheck)) {
                    if (!s.isExist(branch.getAddress(), Tomorrow, ShiftType.Morning)) {
                        UrgentMessages.add("Sorry to inform you, but we found a problem for tomorrow arrangement!" + "\n" + "Problem Details : " + "\n" + "Branch : "
                                + branch.getAddress() + " doesn't have a shift arrangement in 24 hours from now" + "\n" + "Please create an arrangement as soon as possible");
                    }
                }
                if (!s.isExist(branch.getAddress(), Tomorrow, ShiftType.Evening)) {
                    UrgentMessages.add("Sorry to inform you, but we found a problem for tomorrow arrangement!" + "\n" + "Problem Details : " + "\n" + "Branch : "
                            + branch.getAddress() + " doesn't have a shift arrangement in 24 hours from now" + "\n" + "Please create an arrangement as soon as possible");
                }
            }
        } catch (SQLException e) {
        }
        return UrgentMessages;
    }

    static void Start() {
        ArrayList<Boolean> haveBeenRead = new ArrayList<>();
        BranchController branchController;
        WorkerController workerController;
        BranchShiftController shiftController;
        DriverController driverController;
        DriverShiftController driverShiftController;
        int numOfWarnings;
        branchController = BranchController.getInstance();
        workerController = WorkerController.getInstance();
        shiftController = BranchShiftController.getInstance();
        driverController = DriverController.getInstance();
        driverShiftController = DriverShiftController.getInstance();
        try {
            ArrayList<String> UrgentMessages = UrgentMessagesForTheHR();
            numOfWarnings = UrgentMessages.size();
            for (int i = 0; i < numOfWarnings; i++) {
                haveBeenRead.add(false);
            }
        } catch (Exception e) {
            return;
        }
        String timeOfTheDay = null;
        if (LocalTime.now().isBefore(LocalTime.NOON))
            timeOfTheDay = "Morning";
        if (LocalTime.now().isBefore(LocalTime.of(18, 0)) && LocalTime.now().isAfter(LocalTime.NOON))
            timeOfTheDay = "Afternoon";
        if (LocalTime.now().isBefore(LocalTime.of(20, 0)) && LocalTime.now().isAfter(LocalTime.of(18, 0)))
            timeOfTheDay = "Evening";
        if (LocalTime.now().isBefore(LocalTime.of(23, 59)) && LocalTime.now().isAfter(LocalTime.of(20, 0)))
            timeOfTheDay = "Night";
        System.out.println("**************************");
        System.out.println("Hello, good " + timeOfTheDay + " please notice that you have " + numOfWarnings + " urgent messages to read");
        Scanner IntScanner = new Scanner(System.in);
        Scanner StringScanner = new Scanner(System.in);
        Scanner DoubleScanner = new Scanner(System.in);
        LocalDate NewWorkerHireDate = null;
        SimpleWorkerType NewWorkerType = null, RemoverIDWorkerType = null, AdderIDWorkerType = null;
        ShiftType ShiftTime = null;
        int choice = 3;
        int ShiftTimeEdit, DayChoice, ShiftChoice, WorkerTypeChoice;
        int ch = 1;
        while (ch != 0) {
            try {
                System.out.println("choose one of the following options");
                System.out.println("1. Watch massages");
                System.out.println("2. Show all branches");
                System.out.println("3. Show all workers");
                System.out.println("4. Edit worker details ");
                System.out.println("5. Edit next week work hours ");
                System.out.println("6. Create new shift arrangement");
                System.out.println("7. Sign a new worker");
                System.out.println("8. Add a new branch");
                System.out.println("9. Change between shift workers");
                System.out.println("10. Promote simple worker to shift manager");
                System.out.println("11. Search for worker info");
                System.out.println("12. Search for a shift info");
                System.out.println("13. End month/week");
                System.out.println("14. Show branch workers");
                System.out.println("15. Add training to Simple worker");
                System.out.println("16. Add training to Driver");
                System.out.println("17. Show upcoming transports for a specific branch");
                System.out.println("18. Exit");
                choice = IntScanner.nextInt();
                switch (choice) {
//                    case 1:
//                        int index = 0;
//                        int counter = 1;
//                        for(int i = 0; i < haveBeenRead.size(); i++){
//                            if(!haveBeenRead.get(i))
//                                index = i;
//                        }
//                        for(int i = index + 1; i < numOfWarnings; i++){
//                            System.out.println("Message number " + counter);
//                            counter++;
//                            System.out.println(UrgentMessagesForTheHR(branchController, shiftController).get(i));
//                        }
//                        numOfWarnings = 0;
//                        break;
                    case 2:
                        branchController.ShowAllBranches();
                        break;
                    case 3:
                        System.out.println("Branch Workers:");
                        workerController.ShowAllWorkers();
                        System.out.println("\nDrivers:");
                        driverController.ShowAllDrivers();
                        break;
                    case 4:
                        boolean isDriver = false;
                        // edit a specific worker employment conditions \ wage
                        System.out.println("Please enter the worker ID ");
                        String id = StringScanner.nextLine();
                        if (!UserInterface.InputValidationCheck(id)) {
                            System.out.println("Please enter a valid ID");
                            break;
                        }
                        if (!workerController.isExist(id)) {
                            if (!DriverController.getInstance().isExist(id)) {
                                System.out.println("There is not such a worker");
                                break;
                            }
                            isDriver = true;
                        }
                        System.out.println("choose one of the following options");
                        System.out.println("1. add employment condition ");
                        System.out.println("2. edit wage");

                        int choice1 = IntScanner.nextInt();
                        switch (choice1) {
                            case 1:
                                System.out.println("Enter conditions");
                                if (isDriver) {
                                    driverController.setEmploymentCondition(id, StringScanner.nextLine());
                                    break;
                                }
                                workerController.setEmploymentCondition(id, StringScanner.nextLine());
                                break;
                            case 2:
                                double wage = 0;
                                System.out.println("Enter a new wage");
                                try {
                                    wage = DoubleScanner.nextDouble();
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid wage");
                                    break;
                                }
                                if (isDriver) {
                                    driverController.setWage(id, wage);
                                    break;
                                }
                                workerController.setWage(id, wage);
                                break;
                            default:
                                System.out.println("This option does not exist");
                                break;
                        }
                        break;
                    case 5:
                        //change the work hours for next week
                        while (true) {
                            System.out.println("For which branch do you want to edit the work hours for the next week ?");
                            String branchAddress = StringScanner.nextLine();
                            if (!branchController.isExist(branchAddress)) {
                                System.out.println("No such branch");
                                break;
                            }
                            System.out.println("Which hours do you want to edit ?");
                            System.out.println("1. Start hours");
                            System.out.println("2. Finish hours");
                            try {
                                ShiftTimeEdit = IntScanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid hour");
                                break;
                            }
                            System.out.println(" Please choose which day you want change the start hours (Format Sunday - 1, Monday - 2...)");
                            try {
                                DayChoice = IntScanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid day");
                                break;
                            }
                            System.out.println(" Please choose which shift you want change the start hours (Format Morning - 0, Evening - 1)");
                            try {
                                ShiftChoice = IntScanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid hour");
                                break;
                            }
                            System.out.println(" Please enter the new hour (Format h:m)");
                            String HourChoice = StringScanner.nextLine();

                            switch (ShiftTimeEdit) {
                                case 1:
                                    branchController.setStartHour(branchAddress, DayChoice, ShiftChoice, HourChoice);
                                    break;
                                case 2:
                                    branchController.setFinishHour(branchAddress, DayChoice, ShiftChoice, HourChoice);
                                    break;
                                default:
                                    System.out.println("This option does not exist");
                                    break;
                            }
                            branchController.ShowSchedule(branchAddress);
                            break;
                        }
                        break;
                    case 6:
                        System.out.println("Please select job for the new worker from the following options :");
                        System.out.println("1. Create shift for drivers");
                        System.out.println("2. Create shift in a branch");
                        int ArrangementChoice = 0;
                        String branchAddress = null;
                        ArrangementChoice = IntScanner.nextInt();
                        if (ArrangementChoice == 2) {
                            //create a new branch shift arrangement for a specific branch
                            System.out.println("Please enter the branch ID");
                            branchAddress = StringScanner.nextLine();
                            branchController.findBranch(branchAddress);
                        }
                        //Find the date and type of the shift.
                        ShiftType oppositeType;
                        System.out.println("Please enter the shift date by the following format DD/MM");
                        String date = StringScanner.nextLine();
                        String[] arrOfStr = date.split("/", 2);
                        if (!UserInterface.InputValidationCheck(arrOfStr[0]) || !UserInterface.InputValidationCheck(arrOfStr[1])) {
                            System.out.println("Please enter a valid date");
                            break;
                        }
                        int day = Integer.parseInt(arrOfStr[0]);
                        int month = Integer.parseInt(arrOfStr[1]);
                        if (day < LocalDate.now().getDayOfMonth() && month < LocalDate.now().getMonthValue()) {
                            System.out.println("Please enter a correct date");
                            break;
                        }
                        int year = LocalDate.now().getYear();
                        LocalDate AvbDate = LocalDate.of(year, month, day);
                        System.out.println("Please select the shift type Morning/Evening");
                        String type = StringScanner.nextLine();
                        ShiftType time = null;
                        if (Objects.equals(type, "Morning")) {
                            time = ShiftType.Morning;
                            oppositeType = ShiftType.Evening;
                        } else if (Objects.equals(type, "Evening")) {
                            time = ShiftType.Evening;
                            oppositeType = ShiftType.Morning;
                        } else {
                            System.out.println("no such an option");
                            break;
                        }
                        //set the start and the finish hours
                        System.out.println("When does this shift starts? (Format h:m)");
                        String HourChoice = StringScanner.nextLine();
                        String[] arrOfHour = HourChoice.split(":", 2);
                        if (!UserInterface.InputValidationCheck(arrOfHour[0]) || !UserInterface.InputValidationCheck(arrOfHour[1])) {
                            System.out.println("Please enter a valid hour");
                            break;
                        }
                        int hour = Integer.parseInt(arrOfHour[0]);
                        int min = Integer.parseInt(arrOfHour[1]);
                        LocalTime StartTime = LocalTime.of(hour, min);
                        System.out.println("When does this shift ends? (Format h:m)");
                        HourChoice = StringScanner.nextLine();
                        arrOfHour = HourChoice.split(":", 2);
                        if (!UserInterface.InputValidationCheck(arrOfHour[0]) || !UserInterface.InputValidationCheck(arrOfHour[1])) {
                            System.out.println("Please enter a valid hour");
                            break;
                        }
                        hour = Integer.parseInt(arrOfHour[0]);
                        min = Integer.parseInt(arrOfHour[1]);
                        LocalTime FinishTime = LocalTime.of(hour, min);
                        switch (ArrangementChoice) {
                            case 1:
                                break;
                            case 2:
                                System.out.println("future Transports in the "+date);
                                TransportWorkersController.getInstance().displayAllTransportsBySiteAndDate(branchAddress,AvbDate.toString());
                                //print all the workers that can work in this shift
                                System.out.println("Optional workers for this shift are:");
                                workerController.showWorkerAvailability(branchAddress, AvbDate, time);
                                // Add the shift manager of this shift, the shift must have shift manager
                                System.out.println("What is the shift manager ID ?");
                                String ShiftManagerID = StringScanner.nextLine();
                                if (!UserInterface.InputValidationCheck(ShiftManagerID)) {
                                    System.out.println("Please enter a valid ID");
                                    break;
                                }
                                if (!workerController.isExist(ShiftManagerID)) {
                                    System.out.println("there is no such a worker with the following id :" + ShiftManagerID + " in this branch");
                                    break;
                                }
                                if (!workerController.isShiftManager(ShiftManagerID)) {
                                    System.out.println("This Worker is not a shift manager!");
                                    break;
                                }
                                // the shift manager cant work twice in the same day
                                //Shift PastShift = branch.searchShiftInTheArrangement(AvbDate, oppositeType);
                                if (shiftController.isExist(branchAddress, AvbDate, oppositeType) && shiftController.isWorkerInShift(branchAddress, ShiftManagerID, AvbDate, oppositeType)) {
                                    System.out.println("This Shift manager is already working this day");
                                    break;
                                }
                                //print the shift manager details
                                System.out.println(workerController.findWorker(ShiftManagerID).getFirstName() + " " + workerController.findWorker(ShiftManagerID).getLastName() + " Shifts this week: " + workerController.findWorker(ShiftManagerID).getNumberOfShiftsPerWeek());
                                //List of all type of workers
                                SimpleWorkerType[] typeList = {SimpleWorkerType.Cashier, SimpleWorkerType.Usher, SimpleWorkerType.Cleaner, SimpleWorkerType.GeneralWorker, SimpleWorkerType.StockKeeper};
                                int numberOfThisType = 0;
                                ArrayList<String> ListOfIDs = new ArrayList<>();
                                boolean InputMismatchFound = false;
                                boolean IDInputMismatchFound = false;
                                // add all require workers by their type
                                for (int i = 0; i < typeList.length; i++) {
                                    // HR manager decide how many workers he needs from each type
                                    System.out.println("How many " + typeList[i] + "s" + " do you need in this shift?");
                                    try {
                                        numberOfThisType = IntScanner.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Please enter a valid amount of workers");
                                        InputMismatchFound = true;
                                        break;
                                    }
                                    int counter = 0;
                                    int j = 0;
                                    //add workers of this type
                                    while (j < numberOfThisType) {
                                        System.out.println("Please enter " + typeList[i] + " ID");
                                        String SimpleWorkerID = StringScanner.nextLine();
                                        if (!UserInterface.InputValidationCheck(SimpleWorkerID)) {
                                            System.out.println("Please enter a valid ID");
                                            IDInputMismatchFound = true;
                                            break;
                                        }
                                        if (workerController.MoreShiftThen(SimpleWorkerID, 6)) {
                                            System.out.println("This worker already works 6 shifts this week!");
                                            continue;
                                        }
                                        if (shiftController.isExist(branchAddress, AvbDate, oppositeType) && shiftController.isWorkerInShift(branchAddress, SimpleWorkerID, AvbDate, oppositeType)) {
                                            System.out.println("This worker is already working this day");
                                            continue;
                                        }

                                        System.out.println(workerController.findWorker(SimpleWorkerID).getFirstName() + " " + workerController.findWorker(SimpleWorkerID).getLastName() + " Shifts this week: " + workerController.findWorker(SimpleWorkerID).getNumberOfShiftsPerWeek());
                                        j++;
                                        ListOfIDs.add(SimpleWorkerID);
                                    }
                                    if (IDInputMismatchFound)
                                        break;
                                }
                                if (InputMismatchFound || IDInputMismatchFound)
                                    break;
                                shiftController.AssignNewShift(AvbDate, time, ShiftManagerID, branchAddress, ListOfIDs, StartTime, FinishTime);
                                System.out.println("Shift has been successfully created");
                                shiftController.printShift(branchAddress, AvbDate, time);
                                break;
                        }
                        break;
                    case 7:
                        //add new worker to the system
                        isDriver = false;
                        System.out.println("Please enter the new worker first name");
                        String NewWorkerFirstName = StringScanner.nextLine();
                        if (!UserInterface.InputIntValidationCheck(NewWorkerFirstName)) {
                            System.out.println("Please enter a valid first name");
                            break;
                        }
                        System.out.println("Please enter the new worker last name");
                        String NewWorkerLastName = StringScanner.nextLine();
                        if (!UserInterface.InputIntValidationCheck(NewWorkerLastName)) {
                            System.out.println("Please enter a valid Last name");
                            break;
                        }
                        System.out.println("Please enter the new worker ID");
                        String NewWorkerID = StringScanner.nextLine();
                        if (!UserInterface.InputValidationCheck(NewWorkerID)) {
                            System.out.println("Please enter a valid ID");
                            break;
                        }
                        if (workerController.SearchWorker(NewWorkerID) != null) {
                            System.out.println("This worker already exist in our system");
                            break;
                        }
                        System.out.println("Please choose password for the new worker");
                        String NewWorkerPassword = StringScanner.nextLine();
                        System.out.println("Please enter the new worker bank account number");
                        int NewWorkerBankAccount;
                        try {
                            NewWorkerBankAccount = IntScanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid bank account number");
                            break;
                        }
                        System.out.println("Please enter the new worker base wage");
                        double NewWorkerWage;
                        try {
                            NewWorkerWage = DoubleScanner.nextDouble();
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid wage");
                            break;
                        }
                        System.out.println("Please enter the new worker hire date in the following format : DD/MM/YYYY");
                        String NewWorkerHireDateStr = StringScanner.nextLine();
                        String[] arrOfHireDate = NewWorkerHireDateStr.split("/", 3);
                        if (!UserInterface.InputValidationCheck(arrOfHireDate[0]) || !UserInterface.InputValidationCheck(arrOfHireDate[1]) || !UserInterface.InputValidationCheck(arrOfHireDate[2])) {
                            System.out.println("Please enter a valid date");
                            break;
                        }
                        int HireDay = Integer.parseInt(arrOfHireDate[0]);
                        int HireMonth = Integer.parseInt(arrOfHireDate[1]);
                        int HireYear = Integer.parseInt(arrOfHireDate[2]);
                        NewWorkerHireDate = LocalDate.of(HireYear, HireMonth, HireDay);
                        System.out.println("Please choose employment condition for the new worker");
                        String NewWorkerEmploymentCondition = StringScanner.nextLine();
                        System.out.println("Please select job for the new worker from the following options :");
                        System.out.println("1. Cashier");
                        System.out.println("2. StockKeeper");
                        System.out.println("3. GeneralWorker");
                        System.out.println("4. Cleaner");
                        System.out.println("5. Usher");
                        System.out.println("6. driver");
                        try {
                            WorkerTypeChoice = IntScanner.nextInt();
                            switch (WorkerTypeChoice) {
                                case 1:
                                    NewWorkerType = SimpleWorkerType.Cashier;
                                    break;
                                case 2:
                                    NewWorkerType = SimpleWorkerType.StockKeeper;
                                    break;
                                case 3:
                                    NewWorkerType = SimpleWorkerType.GeneralWorker;
                                    break;
                                case 4:
                                    NewWorkerType = SimpleWorkerType.Cleaner;
                                    break;
                                case 5:
                                    NewWorkerType = SimpleWorkerType.Usher;
                                    break;
                                case 6:
                                    isDriver = true;
                                    LicenseType NewDriverLicenseType = null;
                                    boolean ValidInput = false;
                                    while (!ValidInput) {
                                        System.out.println("Please enter the new driver license type (Light/Medium/Heavy)");
                                        String NewDriverLicenseTypeSTR = StringScanner.nextLine();
                                        if (Objects.equals(NewDriverLicenseTypeSTR, "Light")) {
                                            ValidInput = true;
                                            NewDriverLicenseType = LicenseType.Light;
                                            break;
                                        }
                                        if (Objects.equals(NewDriverLicenseTypeSTR, "Medium")) {
                                            ValidInput = true;
                                            NewDriverLicenseType = LicenseType.Medium;
                                            break;
                                        }
                                        if (Objects.equals(NewDriverLicenseTypeSTR, "Heavy")) {
                                            ValidInput = true;
                                            NewDriverLicenseType = LicenseType.Heavy;
                                            break;
                                        }
                                        System.out.println("Please enter one option from above");
                                    }
                                    boolean MoreTrainingsToAssign = true;
                                    boolean ProblemFound = false;
                                    TrainingType DType = TrainingType.DRY;
                                    TrainingType RType = TrainingType.REFRIGERATED;
                                    TrainingType FType = TrainingType.FROZEN;
                                    ArrayList<TrainingType> AllNewDriverTrainings = new ArrayList<>();
                                    while (MoreTrainingsToAssign) {
                                        System.out.println("Please enter the new driver training type (REFRIGERATED/FROZEN/DRY)");
                                        String NewDriverTrainingTypeSTR = StringScanner.nextLine();
                                        if (Objects.equals(NewDriverTrainingTypeSTR, "DRY")) {
                                            if (AllNewDriverTrainings.contains(DType)) {
                                                System.out.println("This driver already has this type of training");
                                                ProblemFound = true;
                                            } else {
                                                AllNewDriverTrainings.add(DType);
                                            }
                                        }
                                        if (Objects.equals(NewDriverTrainingTypeSTR, "REFRIGERATED")) {
                                            if (AllNewDriverTrainings.contains(RType)) {
                                                System.out.println("This driver already has this type of training");
                                                ProblemFound = true;
                                            } else {
                                                AllNewDriverTrainings.add(RType);
                                            }
                                        }
                                        if (Objects.equals(NewDriverTrainingTypeSTR, "FROZEN")) {
                                            if (AllNewDriverTrainings.contains(FType)) {
                                                System.out.println("This driver already has this type of training");
                                                ProblemFound = true;
                                            } else {
                                                AllNewDriverTrainings.add(FType);
                                            }
                                        }
                                        if (!ProblemFound) {
                                            while (true) {
                                                System.out.println("Does this driver have more trainings ?");
                                                System.out.println("1. Yes");
                                                System.out.println("2. No");
                                                int MoreTrainings = IntScanner.nextInt();
                                                if (MoreTrainings == 1) {
                                                    MoreTrainingsToAssign = true;
                                                    break;
                                                } else if (MoreTrainings == 2) {
                                                    MoreTrainingsToAssign = false;
                                                    break;
                                                } else {
                                                    System.out.println("Please enter a valid option");
                                                }
                                            }
                                        }
                                        ProblemFound = false;
                                    }
                                    driverController.createNewDriver(NewWorkerFirstName, NewWorkerLastName, NewWorkerID, NewWorkerBankAccount, NewWorkerWage, NewWorkerHireDate, NewWorkerEmploymentCondition, NewDriverLicenseType, AllNewDriverTrainings);
                                    driverController.PrintDriver(NewWorkerID);
                                    break;
                                default:
                                    System.out.println("This option does not exist");
                                    break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a number");
                            break;
                        }
                        if (!isDriver) {
                            System.out.println("Which branch does the new worker belongs to ?");
                            String NewWorkerBranch = StringScanner.nextLine();
                            if (!branchController.isExist(NewWorkerBranch)) {
                                System.out.println("This branch doesn't exist");
                                break;
                            }
                            workerController.createNewWorker(NewWorkerBranch, NewWorkerFirstName, NewWorkerLastName, NewWorkerID,  NewWorkerBankAccount, NewWorkerWage, NewWorkerHireDate, NewWorkerEmploymentCondition, NewWorkerType);
                            workerController.PrintWorker(NewWorkerID);
                        }
                        break;
                    case 8:
                        //add new branch
                        System.out.println("What is the new branch address?");
                        id = StringScanner.nextLine();
                        branchController.createBranch(id);
                        System.out.println("New branch has been created");
                        break;
                    case 9:
                        //replace two workers in an existing shift arrangement
                        System.out.println("Which branch does the worker you want to replace belongs to ?");
                        branchAddress = StringScanner.nextLine();
                        if (!branchController.isExist(branchAddress)) {
                            System.out.println("No such branch");
                            break;
                        }
                        System.out.println("Please select the shift date you want to change (Format : DD/MM/YYYY)");
                        String DateToChangeStr = StringScanner.nextLine();
                        String[] arrOfChangeDate = DateToChangeStr.split("/", 3);
                        if (!UserInterface.InputValidationCheck(arrOfChangeDate[0]) || !UserInterface.InputValidationCheck(arrOfChangeDate[1]) || !UserInterface.InputValidationCheck(arrOfChangeDate[2])) {
                            System.out.println("Please enter a valid date");
                            break;
                        }
                        int ShiftDay = Integer.parseInt(arrOfChangeDate[0]);
                        int ShiftMonth = Integer.parseInt(arrOfChangeDate[1]);
                        if (ShiftDay < LocalDate.now().getDayOfMonth() && ShiftMonth < LocalDate.now().getMonthValue()) {
                            System.out.println("Please enter a correct date");
                            break;
                        }
                        int ShiftYear = Integer.parseInt(arrOfChangeDate[2]);
                        LocalDate ShiftDate = LocalDate.of(ShiftYear, ShiftMonth, ShiftDay);
                        System.out.println("Please select a shift type Morning/Evening");
                        type = StringScanner.nextLine();
                        if (Objects.equals(type, "Morning"))
                            ShiftTime = ShiftType.Morning;
                        if (Objects.equals(type, "Evening"))
                            ShiftTime = ShiftType.Evening;
                        System.out.println("Please select the worker ID you want to remove from the shift");
                        String IDToRemove = StringScanner.nextLine();
                        if (!UserInterface.InputValidationCheck(IDToRemove)) {
                            System.out.println("Please enter a valid ID");
                            break;
                        }
                        shiftController.RemoveWorkerFromShift(branchAddress, IDToRemove, ShiftDate, ShiftTime);
                        System.out.println("Please select the worker ID you want to add to the shift");
                        String IDToAdd = StringScanner.nextLine();
                        if (!UserInterface.InputValidationCheck(IDToAdd)) {
                            System.out.println("Please enter a valid ID");
                            break;
                        }
                        shiftController.addWorkerToShift(branchAddress, IDToAdd, ShiftDate, ShiftTime);
                        System.out.println("The change was submitted");

                        break;
                    case 10:
                        //make from simple worker a new shift manager
                        System.out.println("Please enter the worker's ID");
                        id = StringScanner.nextLine();
                        if (!UserInterface.InputValidationCheck(id) || !workerController.isExist(id)) {
                            System.out.println("This worker does no exist");
                            break;
                        }
                        if (workerController.isShiftManager(id)) {
                            System.out.println("This worker is already a Shift Manager ");
                            break;
                        }
                        workerController.PromoteWorkerToShiftManager(id);
                        System.out.println("Congratulation this worker is now a Shift Manager");
                        break;
                    case 11:
                        // print worker details
                        isDriver = false;
                        System.out.println("Please enter the ID of the worker you are looking for");
                        String WorkerID = StringScanner.nextLine();
                        if (!UserInterface.InputValidationCheck(WorkerID)) {
                            System.out.println("Please enter a valid ID");
                            break;
                        }
                        if (!workerController.isExist(WorkerID)) {
                            if (!driverController.isExist(WorkerID)) {
                                System.out.println("This worker does not exist");
                                break;
                            }
                            isDriver = true;
                        }
                        if (isDriver) {
                            driverController.PrintDriver(WorkerID);
                            break;
                        }
                        workerController.PrintWorker(WorkerID);
                        break;
                    case 12:
                        isDriver = false;
                        // prints the details of a specific shift in a specific branch
                        System.out.println("Choose one of the following options");
                        System.out.println("1. Branch shift");
                        System.out.println("2. Drivers shift");
                        int Choice = IntScanner.nextInt();
                        switch (Choice) {
                            case 1:
                                break;
                            case 2:
                                isDriver = true;
                                break;
                            default:
                                System.out.println("This option does not exist");
                                break;
                        }
                        System.out.println("Please enter the date of the shift that you are looking for (follow the next format DD/MM/YYYY)");
                        String DateOfShiftToPrintStr = StringScanner.nextLine();
                        String[] arrOfShiftToPrint = DateOfShiftToPrintStr.split("/", 3);
                        if (!UserInterface.InputValidationCheck(arrOfShiftToPrint[0]) || !UserInterface.InputValidationCheck(arrOfShiftToPrint[1]) || !UserInterface.InputValidationCheck(arrOfShiftToPrint[2])) {
                            System.out.println("Please enter a valid date");
                            break;
                        }
                        ShiftDay = Integer.parseInt(arrOfShiftToPrint[0]);
                        ShiftMonth = Integer.parseInt(arrOfShiftToPrint[1]);
                        ShiftYear = Integer.parseInt(arrOfShiftToPrint[2]);
                        LocalDate DateOfShiftToPrint = LocalDate.of(ShiftYear, ShiftMonth, ShiftDay);
                        System.out.println("Please select the shift type Morning/Evening");
                        String TypeOfShiftToPrintStr = StringScanner.nextLine();
                        ShiftType shiftType;
                        if (TypeOfShiftToPrintStr.equals("Morning")) {
                            shiftType = ShiftType.Morning;
                        } else if (TypeOfShiftToPrintStr.equals("Evening")) {
                            shiftType = ShiftType.Evening;
                        } else {
                            System.out.println("There is no such a evening shift at" + DateOfShiftToPrint);
                            break;
                        }
                        if (isDriver) {
                            driverShiftController.printShift(DateOfShiftToPrint, shiftType);
                            break;
                        }
                        System.out.println("Please enter the ID of the branch you are looking for");
                        branchAddress = StringScanner.nextLine();
                        if (!branchController.isExist(branchAddress)) {
                            System.out.println("There is not such a branch");
                            break;
                        }
                        shiftController.PrintShiftDetails(DateOfShiftToPrint, shiftType, branchAddress);
                        break;
                    case 13:
                        //This method reset branch details who need to be reset and allow end of the week/month option
                        System.out.println("Please select the right option to reset your worker stats accordingly ");
                        System.out.println("1. End the current week");
                        System.out.println("2. End the current month");
                        try {
                            int End = IntScanner.nextInt();
                            switch (End) {
                                case 1:
                                    workerController.EndWeek();
                                    driverController.EndWeek();
                                    System.out.println("The week has ended");
                                    break;
                                case 2:
                                    //calculate the salaries and add bonus
                                    isDriver = false;
                                    ArrayList<String> RewardedWorkersID = new ArrayList<>();
                                    ArrayList<Double> BonusesAmount = new ArrayList<>();
                                    ArrayList<String> RewardedDriversID = new ArrayList<>();
                                    ArrayList<Double> BonusesAmountForDrivers = new ArrayList<>();
                                    boolean AddBonusToMoreWorkers = false;
                                    int RewardMoreWorkersChoice = 0;
                                    double BonusAmount = 0, TotalSalary = 0;
                                    String RewardedWorkerIDStr = "";
                                    while (!AddBonusToMoreWorkers) {
                                        System.out.println("Do you want to reward one of your workers with a bonus this month ?");
                                        System.out.println("1. Yes");
                                        System.out.println("2. No");
                                        try {
                                            int BonusChoice = IntScanner.nextInt();
                                            switch (BonusChoice) {
                                                case 1:
                                                    System.out.println("Please enter the ID of the worker you want to reward");
                                                    RewardedWorkerIDStr = StringScanner.nextLine();
                                                    if ((!workerController.isExist(RewardedWorkerIDStr)) && (!driverController.isExist(RewardedWorkerIDStr))) {
                                                        System.out.println("No such a worker");
                                                        break;
                                                    }
                                                    if (workerController.isExist(RewardedWorkerIDStr)) {
                                                        RewardedWorkersID.add(RewardedWorkerIDStr);
                                                    }
                                                    if (driverController.isExist(RewardedWorkerIDStr)) {
                                                        RewardedWorkersID.add(RewardedWorkerIDStr);
                                                        isDriver = true;
                                                    }
                                                    System.out.println("Please select the bonus amount");
                                                    BonusAmount = DoubleScanner.nextDouble();
                                                    if (!isDriver) {
                                                        BonusesAmount.add(BonusAmount);
                                                    } else {
                                                        BonusesAmountForDrivers.add(BonusAmount);
                                                    }
                                                    //TotalSalary = workerController.calculateWage(RewardedWorkerIDStr,BonusAmount, true);
                                                    System.out.println("Do you want to reward more workers?");
                                                    System.out.println("1. Yes");
                                                    System.out.println("2. No");
                                                    RewardMoreWorkersChoice = IntScanner.nextInt();
                                                    switch (RewardMoreWorkersChoice) {
                                                        case 1:
                                                            break;
                                                        case 2:
                                                            AddBonusToMoreWorkers = true;
                                                            workerController.EndMonth(RewardedWorkersID, BonusesAmount);
                                                            System.out.println("Drivers : ");
                                                            driverController.EndMonth(RewardedDriversID, BonusesAmountForDrivers);
                                                            break;
                                                        default:
                                                            System.out.println("Please enter a valid option");
                                                            break;
                                                    }
                                                    break;
                                                case 2:
                                                    AddBonusToMoreWorkers = true;
                                                    workerController.EndMonth(RewardedWorkersID, BonusesAmount);
                                                    System.out.println("Drivers : ");
                                                    driverController.EndMonth(RewardedDriversID, BonusesAmountForDrivers);
                                                    break;
                                                default:
                                                    System.out.println("Please enter a valid option");
                                                    break;
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please enter a number");
                                            break;
                                        }
                                    }
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a number");
                            break;
                        }
                        break;
                    case 14:
                        System.out.println("Please enter the ID of the branch you are looking for");
                        String BranchAddress = StringScanner.nextLine();
                        if (!branchController.isExist(BranchAddress)) {
                            System.out.println("There is no such a branch");
                            break;
                        }
                        workerController.showBranchWorkersByBranch(BranchAddress);
                        break;
                    case 15:
                        System.out.println("Please enter the worker ID");
                        id = StringScanner.nextLine();
                        if (!UserInterface.InputValidationCheck(id)) {
                            System.out.println("Please enter a valid ID");
                            break;
                        }
                        if (!workerController.isExist(id)) {
                            System.out.println("This worker does not exist in our system");
                            break;
                        }
                        System.out.println("Please select new job for the worker from the following options :");
                        System.out.println("1. Cashier");
                        System.out.println("2. StockKeeper");
                        System.out.println("3. GeneralWorker");
                        System.out.println("4. Cleaner");
                        System.out.println("5. Usher");
                        try {
                            WorkerTypeChoice = IntScanner.nextInt();
                            switch (WorkerTypeChoice) {
                                case 1:
                                    NewWorkerType = SimpleWorkerType.Cashier;
                                    break;
                                case 2:
                                    NewWorkerType = SimpleWorkerType.StockKeeper;
                                    break;
                                case 3:
                                    NewWorkerType = SimpleWorkerType.GeneralWorker;
                                    break;
                                case 4:
                                    NewWorkerType = SimpleWorkerType.Cleaner;
                                    break;
                                case 5:
                                    NewWorkerType = SimpleWorkerType.Usher;
                                    break;
                                default:
                                    System.out.println("This option does not exist");
                                    break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a number");
                            break;
                        }
                        if (workerController.isTrainingExist(NewWorkerType, id)) {
                            System.out.println("This worker already have this training");
                            break;
                        }
                        workerController.addTraining(id, NewWorkerType);
                        System.out.println("Training successfully has been added");
                        break;
                    case 16:
                        System.out.println("Please enter the Driver ID");
                        id = StringScanner.nextLine();
                        if (!UserInterface.InputValidationCheck(id)) {
                            System.out.println("Please enter a valid ID");
                            break;
                        }
                        if (!driverController.isExist(id)) {
                            System.out.println("This Driver already exist in our system");
                            break;
                        }
                        System.out.println("Please select new Training for the Driver from the following options :");
                        System.out.println("1. REFRIGERATED");
                        System.out.println("2. FROZEN");
                        System.out.println("3. DRY");
                        TrainingType DriverTraining;
                        try {

                            WorkerTypeChoice = IntScanner.nextInt();
                            switch (WorkerTypeChoice) {
                                case 1:
                                    if (!driverController.isTrainingExist(id, TrainingType.REFRIGERATED)) {
                                        driverController.addTraining(id, TrainingType.REFRIGERATED);
                                        System.out.println("Training successfully has been added");
                                        break;
                                    } else {
                                        System.out.println("This driver already has the " + TrainingType.REFRIGERATED + " training");
                                    }
                                    break;
                                case 2:
                                    if (!driverController.isTrainingExist(id, TrainingType.FROZEN)) {
                                        driverController.addTraining(id, TrainingType.FROZEN);
                                        System.out.println("Training successfully has been added");
                                        break;
                                    } else {
                                        System.out.println("This driver already has the " + TrainingType.FROZEN + " training");
                                    }
                                    break;
                                case 3:
                                    if (!driverController.isTrainingExist(id, TrainingType.DRY)) {
                                        driverController.addTraining(id, TrainingType.DRY);
                                        System.out.println("Training successfully has been added");
                                        break;
                                    } else {
                                        System.out.println("This driver already has the " + TrainingType.DRY + " training");
                                    }
                                    break;
                                default:
                                    System.out.println("This option does not exist");
                                    break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a number");
                            break;
                        }
                        break;
                    case 17:
                        System.out.println("Please enter the ID of the branch you are looking for");
                        BranchAddress = StringScanner.nextLine();
                        if (!branchController.isExist(BranchAddress)) {
                            System.out.println("There is no such a branch");
                            break;
                        }
                        System.out.println("Please enter the date of the shift that you are looking for (follow the next format DD/MM/YYYY)");
                        String DateToCheckTransports = StringScanner.nextLine();
                        String[] arrOfCheckDate = DateToCheckTransports.split("/", 3);
                        if (!UserInterface.InputValidationCheck(arrOfCheckDate[0]) || !UserInterface.InputValidationCheck(arrOfCheckDate[1]) || !UserInterface.InputValidationCheck(arrOfCheckDate[2])) {
                            System.out.println("Please enter a valid date");
                            break;
                        }
                        ShiftDay = Integer.parseInt(arrOfCheckDate[0]);
                        ShiftMonth = Integer.parseInt(arrOfCheckDate[1]);
                        ShiftYear = Integer.parseInt(arrOfCheckDate[2]);
                        LocalDate DateToCheckTransport = LocalDate.of(ShiftYear, ShiftMonth, ShiftDay);
                        System.out.println("Please select the shift type Morning/Evening");
                        String TypeOfShiftToCheckStr = StringScanner.nextLine();
                        if (TypeOfShiftToCheckStr.equals("Morning")) {
                            //shiftController.PrintShiftDetails(DateToCheckTransport, ShiftType.Morning, BranchAddress);
                        } else if (TypeOfShiftToCheckStr.equals("Evening")) {
                            //shiftController.PrintShiftDetails(DateToCheckTransport, ShiftType.Evening, BranchAddress);
                        }
                        break;
                    case 18:
                        System.out.println("Good bye, have a nice day!");
                        ch = 0;
                        break;
                    default:
                        System.out.println("This option does not exist." + "\n" + "Please try again and re-enter a valid option");
                        break;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            System.out.println();
        }
    }
}

/*
        System.out.println("1. Watch massages");
        System.out.println("2. Show all branches");
        System.out.println("3. Show all workers");
        System.out.println("4. Edit worker details ");
        System.out.println("5. Edit next week work hours ");
        System.out.println("6. Create new shift arrangement");
        System.out.println("7. Sign a new worker");
        System.out.println("8. Add a new branch");
        System.out.println("9. Change between shift workers");
        System.out.println("10. Promote simple worker to shift manager");
        System.out.println("11. Search for worker info");
        System.out.println("12. Search for a shift info");
        System.out.println("13. End month/week");
        System.out.println("14. Show branch workers");
        System.out.println("15. Add training to Simple worker");
        System.out.println("16. Add training to Driver");
System.out.println("17. Show upcoming transports for a specific branch");
System.out.println("18. Exit");

 */