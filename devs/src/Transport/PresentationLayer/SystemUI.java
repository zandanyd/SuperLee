package SuperLee.Transport.PresentationLayer;
import SuperLee.HumenResource.BusinessLayer.*;
import SuperLee.Transport.BusinessLayer.*;
import SuperLee.TransportWorkersController;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SystemUI {

    public static void start() throws SQLException {
        // Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
        // then press Enter. You can now see whitespace characters in your code.

        TransportManagment transportManagment = TransportManagment.getInstance();
//        TransportWorkersController transportWorkersController = TransportWorkersController.getInstance();


        Scanner scanner = new Scanner(System.in);
        int choice = 0;
//            TransportManagment transportManagment = new TransportManagment(truckController, siteController, ordersController, transportDocumentController);

        while (choice != 6) {
            System.out.println("Please choose an option:");
            System.out.println("1. Transport operations menu");
            System.out.println("2. Orders operations menu");
            System.out.println("3. Trucks operations menu");
            System.out.println("4. Fill data");
            System.out.println("5. Quit");

            // validate input to make sure user enters a valid integer choice
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid choice, please enter a valid integer.");
                scanner.next();
            }
            choice = scanner.nextInt();
            if (choice < 1 || choice > 6) {
                System.out.println("Invalid choice, please try again.");
                continue;
            }
            switch (choice) {
                case 1:
                    int docChoice = 0;
                    while (docChoice != 6) {
                        System.out.println("Transport operations:");
                        System.out.println("1. Order a new transport (creating a transport document)");
                        System.out.println("2. Start a new transport");
                        System.out.println("3. Manage existing transport");
                        System.out.println("4. Finish transport");
                        System.out.println("5. Information on transportation history");
                        System.out.println("6. Return to main menu");

                        // validate input to make sure user enters a valid integer choice
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid choice, please enter a valid integer.");
                            scanner.next();
                        }
                        docChoice = scanner.nextInt();

                        switch (docChoice) {
                            case 1:
                                System.out.println("Creating new transportation document");
                                createTransportDocument(transportManagment);
                                break;
                            case 2:
                                System.out.println("Start a new transport");
                                newTransport(transportManagment);
                                break;
                            case 3:
                                System.out.println("Manage existing transport");
                                manageTransport(transportManagment);
                                break;

                            case 4:
                                System.out.println("Finish transport");
                                finishTransport(transportManagment);
                                break;
                            case 5:
                                transportInfo(transportManagment);
                                break;
                            default:
                                System.out.println("Invalid choice, please try again.");
                                break;
                        }
                    }
                    break;
                case 2:
                    System.out.println("1. Enter a new order");
                    System.out.println("2. Return to main menu");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid choice, please enter a valid integer.");
                        scanner.next();
                    }
                    docChoice = scanner.nextInt();
                    switch (docChoice) {
                        case 1:
                            addOrderFromSupplier(transportManagment);
                        case 2:
                            break;
                    }
                    break;
                case 3:
                    System.out.println("Trucks menu");
                    System.out.println("1. Enter a new truck");
                    System.out.println("2. Return to main menu");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid choice, please enter a valid integer.");
                        scanner.next();
                    }
                    docChoice = scanner.nextInt();
                    switch (docChoice) {
                        case 1:
                            addTruckToSystem(transportManagment);
                        case 2:
                            break;
                    }
                    break;
                case 4:
                    transportManagment.fillDataTests();
                    continue;
                case 5:
                    break;
            }
        }
    }


    // ------- functions ------
    public static void createTransportDocument(TransportManagment transportManagment) throws SQLException {
        OrdersController ordersController = OrdersController.getInstance();
        TruckController truckController = TruckController.getInstance();
        TransportDocumentController transportDocumentController = TransportDocumentController.getInstance();
        DriverController driverController = DriverController.getInstance();
        TransportWorkersController transportWorkersController = TransportWorkersController.getInstance();
        SiteController siteController = SiteController.getInstance();

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (choice != 4) {
            System.out.println("Choose the type of items you want to transport (enter a number between 1-4): ");
            System.out.println("1. Dry");
            System.out.println("2. Cooling");
            System.out.println("3. Freezing");
            System.out.println("4. Return to the main menu");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input! Please try again");
                input.next();
                continue;
            }
            choice = input.nextInt();
            if (choice < 0 || choice > 4) {
                System.out.println("Invalid input! Please try again");
                continue;
            }
            break;
        }
        // choose a date for transport
        Scanner scanner = new Scanner(System.in);
        LocalDate transportDate = null;
        String startTime = null;
        String shiftType = null;

        while (transportDate == null || startTime == null) {
            System.out.print("Enter the date for transport (yyyy-MM-dd): ");
            String inputDate = scanner.nextLine();

            try {
                transportDate = LocalDate.parse(inputDate, DateTimeFormatter.ISO_DATE);
                if (transportDate.isBefore(LocalDate.now())) {
                    System.out.println("The transport date cannot be in the past. Please enter a future date.");
                    transportDate = null;
                } else {
                    System.out.print("Enter the start time for transport (HH:mm): ");
                    String inputTime = scanner.nextLine();
                    startTime = String.valueOf(LocalTime.parse(inputTime, DateTimeFormatter.ofPattern("HH:mm")));

                    LocalDateTime transportDateTime = LocalDateTime.of(transportDate, LocalTime.parse(startTime));
                    if (transportDateTime.isBefore(LocalDateTime.now())) {
                        System.out.println("The start time cannot be in the past. Please enter a future time.");
                        startTime = null;
                    }
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date or time format. Please enter the date in the format yyyy-MM-dd and the time in the format HH:mm.");
            }
        }
        startTime = transportDate + " " + startTime + ":00";
//            System.out.println("Transport time: " + transportDate);
        System.out.println("Transport time: " + startTime);

        Truck[] trucks;
        List<Truck> truckList;
        TrainingType type = TrainingType.DRY;
        ShippingArea area;

        //--------
        //Truck chosenTruck = null;
        int truckID = 0;
        //---------

//            Driver chosenDriver = null;
        String driverID = null;
        Scanner StringScanner  = new Scanner(System.in);
        Site source = null;
        String addressSource = null;
        ArrayList<Integer> ordersToTransport = new ArrayList<>();
        switch (choice) {
            case 1:
            case 2:
            case 3:
                if (choice == 2) {
                    type = TrainingType.REFRIGERATED;
                }
                if (choice == 3) {
                    type = TrainingType.FROZEN;
                }

                Scanner input2 = new Scanner(System.in);
                int subChoice = 0;
                while (subChoice != 6) {
                    System.out.println("Main menu: ");
                    System.out.println("1. Choosing a truck");
                    System.out.println("2. Choosing a driver");
                    System.out.println("3. Selecting orders from suppliers to enter in the transport document");
                    System.out.println("4. Select a source");
                    System.out.println("5. Finish the order");
                    System.out.println("6. Quit");
                    if (!input2.hasNextInt()) {
                        System.out.println("Invalid input.6 Please try again");
                        input2.next();
                        continue;
                    }
                    subChoice = input2.nextInt();
                    if (subChoice <= 0 || subChoice > 6) {
                        System.out.println("Invalid choice. Please try again");
                        continue;
                    }
                    switch (subChoice) {

                        case 1:
                            // choosing a truck called --chosenTruck--
                            int choice_2;
                            if (truckID != 0) {
                                System.out.println("You have already selected a truck! \nTo select a new truck enter 1, to return to the main menu enter 0 : ");
                                if (!input.hasNextInt()) {
                                    System.out.println("Invalid input. Please try again");
                                    input.next();
                                    continue;
                                }
                                int choice4 = input.nextInt();
                                if (choice4 == 0) {
                                    continue;
                                }
                            }
                            if (!truckController.checkAvailableTrucksByType(type, transportDate)) {
                                System.out.println("There are no trucks available for this type of transport.");
                                break;
                            }
                            while (true) {
                                System.out.println("Choose the action you want: ");
                                System.out.println("1. Displaying all available trucks according to the transport type you selected:");
                                System.out.println("2. Back to the transport document menu");
                                choice_2 = input.nextInt();
                                switch (choice_2) {
                                    case 1:
                                        truckController.printAvailableTrucks(type, transportDate);
                                        while (true) {
                                            System.out.println("Enter the license number of the truck you want: ");
                                            truckID = input.nextInt();
                                            if (!truckController.truckExist(truckID)) {
                                                System.out.println("Invalid truck license number!");
                                                System.out.println("If you want to try again enter 1, to return to the menu enter 0");
                                                int anotherChoice = input.nextInt();
                                                if (anotherChoice == 1) {
                                                    continue;
                                                }
                                                break;
                                            } else {
                                                int flag = 0;
                                                if (driverID != null) { // Checking whether the driver can drive the selected truck
                                                    if (transportWorkersController.checkDriverToTruck(truckController.getTruckByLicense(truckID), driverController.findDriver(driverID)) == 1) {
                                                        System.out.println("This driver does not have appropriate training for the selected truck.");
                                                        flag = 1;
                                                    } else if (transportWorkersController.checkDriverToTruck(truckController.getTruckByLicense(truckID), driverController.findDriver(driverID)) == 2) {
                                                        System.out.println("The driver you selected does not have a suitable license to drive the selected truck!");
                                                        flag = 1;
                                                    }
                                                }
                                                if (flag != 0) { // this driver isn't sutible
                                                    System.out.println("If you want to try again enter 1, to return to the menu enter 0");
                                                    int anotherChoice = input.nextInt();
                                                    if (anotherChoice == 1) {
                                                        continue;
                                                    }
                                                    break;
                                                }
                                                System.out.println("The selected truck: ");
                                                truckController.printTruck(truckID);
                                                break;
                                            }
                                        }
                                        break;
                                    case 2:
                                        break;
                                }
                                break;
                            }
                            break;
                        case 2:
                            // Choosing a driver, called --chosenDriver--

                            if (driverID != null) {
                                System.out.println("A driver has already been selected for this transport document! \nTo select a new driver enter 1, " +
                                        "to return to the main menu enter 0 : ");
                                if (!input.hasNextInt()) {
                                    System.out.println("Invalid input. Please try again");
                                    input.next();
                                    continue;
                                }
                                int choice3 = input.nextInt();
                                if (choice3 == 0) {
                                    continue;
                                }
                            }
                            System.out.println("Please select the shift type Morning/Evening");
                             shiftType = scanner.nextLine();
                            ShiftType time = null;
                            if (Objects.equals(shiftType, "Morning")) {
                                time = ShiftType.Morning;
                            } else if (Objects.equals(shiftType, "Evening")) {
                                time = ShiftType.Evening;
                            } else {
                                System.out.println("no such an option");
                                break;
                            }
                            while (true) {
                                System.out.println("Choose a driver: ");
                                if (!transportWorkersController.CheckAvailableDriverByDate(transportDate, time)) {
                                    System.out.println("There are no drivers available for this type of transport at the moment");
                                    break;
                                }

                                transportWorkersController.getAllAvailableDriverByDate(transportDate, time); // TODO ido dvir
                                while (true) {
                                    System.out.println("Enter the driver's ID number: ");
                                    driverID = StringScanner.nextLine();
                                    if (!driverController.isExist(driverID)) {
                                        System.out.println("Invalid driver ID!");
                                        System.out.println("If you want to try again enter 1, to return to the menu enter 0");
                                        int anotherChoice = input.nextInt();
                                        if (anotherChoice == 1) {
                                            continue;
                                        }
                                        break;
                                    } else {
                                        int flag = 0;
                                        if (truckID != 0) { // Checking whether the driver can drive the selected truck
                                            if (transportWorkersController.checkDriverToTruck(truckController.getTruckByLicense(truckID), driverController.findDriver(driverID)) == 1) {
                                                System.out.println("This driver does not have appropriate training for the selected truck.");
                                                driverID = null;
                                                flag = 1;
                                            } else if (transportWorkersController.checkDriverToTruck(truckController.getTruckByLicense(truckID), driverController.findDriver(driverID)) == 2) {
                                                System.out.println("The driver you selected does not have a suitable license to drive the selected truck!");
                                                driverID = null;
                                                flag = 1;
                                            }

                                        }
                                        if (flag != 0) { // this driver isn't sutible
                                            System.out.println("If you want to try again enter 1, to return to the menu enter 0");
                                            int anotherChoice = input.nextInt();
                                            if (anotherChoice == 1) {
                                                continue;
                                            }
                                            break;
                                        }
                                        System.out.println("The selected driver: ");
                                        driverController.printDriver(driverID);
                                        break;
                                    }

                                }
                                break;
                            }
                            break;

                        case 3:
                            while (true) {
                                // choose transport-orders to insert to the transport-document , an array called --ordersToTransport--
                                System.out.println("Order selection menu: ");
                                System.out.println("1. Selection of transport orders without limitation ");
                                System.out.println("2. Selection of transport orders by shipping area ");
                                System.out.println("3. Back to the transport document menu ");
                                if (!input.hasNextInt()) { // Checking  "!input.hasNext()" first
                                    System.out.println("Invalid input. Please try again");
                                    input.next();
                                    continue;
                                }
                                int opt = input.nextInt();
                                if (opt < 1 || opt > 3) {
                                    System.out.println("Invalid input. Please try again");
                                    input.next();
                                    continue;
                                }
                                ArrayList<Integer> ordersIDList = new ArrayList<>();
                                ArrayList<Integer> selectedOrders1;
                                switch (opt) {
                                    case 1:
                                        ordersIDList = ordersController.getAvailableOrdersIDByType(type);
                                        selectedOrders1 = selectOrdersToTransportDoc(type, ordersIDList, startTime);
                                        if (selectedOrders1 != null) {
                                            ordersToTransport.addAll(selectedOrders1);
//                                            for (Integer transportOrder : selectedOrders1) {
//                                                ordersController.moveOrderToSelected(transportOrder, type);
//                                            }
                                            selectedOrders1 = null;
                                        }
                                        int flag = 0;
                                        if (ordersToTransport == null) {
                                            flag = 0;
                                        } else {
                                            flag = 1;
                                        }
                                        break;
                                    case 2:
                                        Scanner in2 = new Scanner(System.in);
                                        System.out.println("Enter 1-3 according to the shipping area you would like the transport to go to ");
                                        System.out.println("1. South");
                                        System.out.println("2. Central");
                                        System.out.println("3. North");
                                        System.out.println("4. Back to the transport document menu");
                                        if (!in2.hasNextInt()) {
                                            System.out.println("Invalid input. Please try again");
                                            input.next();
                                            continue;
                                        }
                                        int opt2 = in2.nextInt();
                                        if (opt2 < 1 || opt2 > 4) {
                                            System.out.println("Invalid input. Please try again");
                                            input.next();
                                            continue;
                                        }
                                        ArrayList<Integer> selectedOrders;
                                        switch (opt2) {
                                            case 1:
                                                area = ShippingArea.SOUTH;
                                                ordersIDList = ordersController.getAvailableOrdersByAreaAndType(type, siteController.getSitesByArea(area));
                                                selectedOrders = selectOrdersToTransportDoc(type, ordersIDList, startTime);
                                                if (selectedOrders != null) {
                                                    ordersToTransport.addAll(selectedOrders);
//                                                    for (Integer transportOrder : selectedOrders) {
//                                                        ordersController.moveOrderToSelected(transportOrder, type);
//                                                    }
                                                }

                                                break;
                                            case 2:
                                                area = ShippingArea.CENTRAL;
                                                ordersIDList = ordersController.getAvailableOrdersByAreaAndType(type, siteController.getSitesByArea(area));
                                                selectedOrders = selectOrdersToTransportDoc(type, ordersIDList, startTime);
                                                if (selectedOrders != null) {
                                                    ordersToTransport.addAll(selectedOrders);
//                                                    for (Integer transportOrder : selectedOrders) {
//                                                        ordersController.moveOrderToSelected(transportOrder, type);
//                                                    }
                                                }
                                                break;
                                            case 3:
                                                area = ShippingArea.NORTH;
                                                ordersIDList = ordersController.getAvailableOrdersByAreaAndType(type, siteController.getSitesByArea(area));
                                                selectedOrders = selectOrdersToTransportDoc(type, ordersIDList, startTime);
                                                if (selectedOrders != null) {
                                                    ordersToTransport.addAll(selectedOrders);
//                                                    for (Integer transportOrder : selectedOrders) {
//                                                        ordersController.moveOrderToSelected(transportOrder, type);
//                                                    }
                                                }

                                                break;
                                            case 4:
                                                break;
                                        }
                                    case 3:
                                        break;
                                }
                                break;
                            }
                            break;
                        case 4:
                            if (addressSource != null) {
                                Scanner in4 = new Scanner(System.in);
                                System.out.println("You have already selected a source, to select a new source enter 1 otherwise enter 0.");
                                if (!in4.hasNextInt()) {
                                    in4.next();
                                    continue;
                                }
                                int opt = in4.nextInt();
                                if (opt != 1)
                                    continue;
                            }
                            Scanner in3 = new Scanner(System.in);
                            System.out.println("Enter 1-3 according to the shipping area you want to select a source");
                            System.out.println("1. South");
                            System.out.println("2. Central");
                            System.out.println("3. North");
                            System.out.println("4. Back to the transport document menu");
                            if (!in3.hasNextInt()) {
                                System.out.println("Invalid input. Please try again");
                                input.next();
                                continue;
                            }
                            int opt2 = in3.nextInt();
                            if (opt2 < 1 || opt2 > 4) {
                                System.out.println("Invalid input. Please try again");
                                input.next();
                                continue;
                            }
                            in3.nextLine();
                            switch (opt2) {
                                case 1:
                                    area = ShippingArea.SOUTH;
                                    if (siteController.getSitesByArea(area).size() == 0) {
                                        System.out.println("There are no sites in the area you selected");
                                        break;
                                    }
                                    siteController.printSitesByArea(area);
                                    System.out.println("Enter the address of the source you want to select: ");
                                    addressSource = in3.nextLine();
                                    if (!siteController.checkSiteExist(addressSource)) {
                                        System.out.println("Invalid input");
                                        break;
                                    }
                                    System.out.println("The source you selected: \n" + siteController.getSiteByAddress(addressSource).toString());
                                    break;
                                case 2:
                                    area = ShippingArea.CENTRAL;
                                    if (siteController.getSitesByArea(area).size() == 0) {
                                        System.out.println("There are no sites in the area you selected");
                                        break;
                                    }
                                    siteController.printSitesByArea(area);
                                    System.out.println("Enter the address of the source you want to select: ");
                                    addressSource = in3.nextLine();
                                    if (!siteController.checkSiteExist(addressSource)) {
                                        System.out.println("Invalid input");
                                        break;
                                    }
                                    System.out.println("The source you selected: \n" + siteController.getSiteByAddress(addressSource).toString());
                                    break;
                                case 3:
                                    area = ShippingArea.NORTH;
                                    if (siteController.getSitesByArea(area).size() == 0) {
                                        System.out.println("There are no sites in the area you selected");
                                        break;
                                    }
                                    siteController.printSitesByArea(area);
                                    System.out.println("Enter the address of the source you want to select: ");
                                    addressSource = in3.nextLine();
                                    if (!siteController.checkSiteExist(addressSource)) {
                                        System.out.println("Invalid input");
                                        break;
                                    }
                                    System.out.println("The source you selected: \n" + siteController.getSiteByAddress(addressSource).toString());
                                    break;
                                case 4:
                                    break;
                            }
                            break;
                        case 5:
                            if (truckID == 0) {
                                System.out.println("The order can not be saved, you have not selected a truck!");
                                continue;
                            }
                            if (driverID == null) {
                                System.out.println("The order can not be saved, you have not selected a driver!");
                                continue;
                            }
                            if (ordersToTransport.size() == 0) {
                                System.out.println("The order can not be saved, you have not selected orders from suppliers!");
                                continue;
                            }
                            if (addressSource == null) {
                                System.out.println("The order can not be saved, you have not selected a source!");
                                continue;
                            } else {
                                transportManagment.prepareToTransportDocument(truckID, driverID, ordersToTransport, type);
                                int docID = transportDocumentController.addTransportDocument(truckID, driverID, ordersController.convertToOrdersByIDAndType(ordersToTransport, type), siteController.getSiteByAddress(addressSource), transportManagment.getSitesFromOrders(ordersController.convertToOrdersByIDAndType(ordersToTransport, type)), type, startTime);
                                transportDocumentController.printTransportDoc(docID);
                                TransportWorkersController.getInstance().addDriverToShift(transportDate, shiftType, driverID);
                                return;
                            }
                    }
                }
        }
    }



    // Function that return true if the string consist of only integer and shorter than the param -"int ordersLength"
    public static boolean checkStringFromUser(String testString, int ordersLength){ // orderLength = the orders array length
        String[] parts = testString.split(",");
        boolean validInput = true;
        for (String part : parts) {
            int index;
            try {
                index = Integer.parseInt(part.trim()) - 1;
            } catch (NumberFormatException e) {
                validInput = false;
                break;
            }
            if (index < 0 || index >= ordersLength) {
                validInput = false;
                break;
            }
        }
        return validInput;
    }

    public static ArrayList<Integer> selectOrdersToTransportDoc(TrainingType type, List<Integer> ordersList, String transportDate) throws SQLException {
        OrdersController ordersController = OrdersController.getInstance();
        SiteController siteController = SiteController.getInstance();
        TransportWorkersController transportWorkersController = TransportWorkersController.getInstance();
        // choose transport-orders to insert to the transport-document , an array called --ordersToSend--
        Integer[] ordersByType;
        ordersByType = (ordersList != null && !ordersList.isEmpty()) ? ordersList.toArray(new Integer[0]) : null;
        if (ordersByType == null){
            System.out.println("There are no orders from suppliers in the system of the requested type");
            return null;
        }
        System.out.println("Please select the orders you would like to attach to the transport form:");
//            for (int i = 0; i < ordersByType.length; i++) {
//                System.out.println((i + 1) + ". " + ordersByType[i]);
//            }
        ArrayList<String> address_sites = transportWorkersController.getAvailableSitesByDate(transportDate);
        if(!ordersController.displayOrders(type, address_sites)) {
            System.out.println("There are no destinations available for transport on the date received");
            return null;
        }

        Scanner inputString = new Scanner(System.in);
        String userString = "";
        ArrayList<Integer> ordersToSend = new ArrayList<>();
        while (true) {
            System.out.print("Enter the order numbers (comma-separated): ");
            userString = inputString.nextLine();

            String[] parts = userString.split(",");
            boolean validInput = true;

            if (checkStringFromUser(userString, ordersByType.length)) {
                for (String part : parts) {
                    int index = Integer.parseInt(part.trim()) - 1;
                    ordersToSend.add(ordersByType[index]);
                }
                break;
            }else {
                System.out.println("Invalid input!");
                Scanner inputInt = new Scanner(System.in);
                System.out.println("To return to the main enter 0, to try again 1: ");
                if(!inputInt.hasNextInt()){
                    return null;
                } else {
                    int choice = inputInt.nextInt();
                    if(choice == 0)
                        return null;
                    else
                        continue;
                }

            }
        }
        System.out.println("Orders to send:");
        ordersController.printOrders(ordersToSend, type, siteController.getAllSites());
//        inputString.close();
        return ordersToSend;
    }


    public static void newTransport(TransportManagment transportManagment) {
        TransportDocumentController transportDocumentController = TransportDocumentController.getInstance();
        Scanner input = new Scanner(System.in);
        int choice = 0;
        int docId = 0;
        if (!transportDocumentController.checkIfExistPendingDocument()){
            System.out.println("There are no pending transport forms in the system.");
            return;
        }
        while (choice != 2) {
            System.out.println("These are all the transport documents in the system, enter" +
                    " (at the end of displaying the documents) the ID number of the form you want the transport to perform.");
            transportDocumentController.printAllPendingTransportDoc();
            System.out.println("Enter the ID number (Integer): ");
            if (!input.hasNextInt()) {
                input.next();
                System.out.println("Invalid input.");
                return;
            }
            docId = input.nextInt();
            int transportID = transportManagment.startTransport(docId);
            if (transportID == -1) {
                System.out.println("There is no pending transport document for this ID number. Try again");
                System.out.println("For retry enter 1, otherwise enter 0.");
                if (!input.hasNext()) {
                    input.next();
                    System.out.println("Invalid input.");
                    return;
                }
                choice = input.nextInt();
                if (choice != 1) {
                    return;
                }
            }
            else {
                System.out.println("The transport has started");
                transportManagment.printTransport(transportID);
                return;
            }
        }
    }

    public static void manageTransport(TransportManagment transportManagment) throws SQLException {
        TruckController truckController = TruckController.getInstance();
        SiteController siteController = SiteController.getInstance();
        TransportsController transportsController = TransportsController.getInstance();
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        int ID = 0;
        int mainChoice = 0;
        if(!transportsController.checkIfExistTransportInProcess()){
            System.out.println("There are no transports in process in the system.");
            return;
        }
        while (mainChoice != 3) {
            System.out.println("Select an option:");
            System.out.println("1. View all transports in process and then select a transport ID to manage");
            System.out.println("2. Select by transport ID number");
            System.out.println("3. Back to main menu");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input: please enter a number.");
                scanner.nextLine(); // Clear the input buffer
                continue;
            }
            mainChoice = scanner.nextInt();
            switch (mainChoice) {
                case 1:
                    transportsController.printAllTransportInProcess();
                case 2:
                    System.out.println("Select a transport ID number (Integer) that you would like to manage");
                    if (!scanner.hasNextInt()) {
                        scanner.next();
                        System.out.println("Invalid input.");
                        continue;
                    }
                    ID = scanner.nextInt();
                    if (transportsController.getTransportById(ID) == null) {
                        System.out.println("There is no transport form for this ID number. Try again");
                        continue;
                    }
                    mainChoice = 3;
                    break;

                case 3:
                    return;
            }

        }
        while (choice != 2) {
            System.out.println("Select an option:");
            System.out.println("1. Update truck weight");
            System.out.println("2. Back to main menu");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input: please enter a number.");
                scanner.nextLine(); // Clear the input buffer
                continue;
            }
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter new truck weight: ");

                    if (!scanner.hasNextDouble()) {
                        System.out.println("Invalid input. Try again");
                        scanner.nextLine(); // Clear the input buffer
                        continue;
                    }
                    double newWeight = scanner.nextDouble();
                    if (!transportsController.updateCurrentTruckWeight(ID, newWeight)) {
                        System.out.println("Alert: new weight exceeds maximum weight allowed.");
                    } else {
                        System.out.println("Truck weight updated to " + newWeight + " kg.");
                        return;
                    }
                    while (choice != 5) {
                        System.out.println("\nThe transport needs to go back to redesign! Choose one of the following options: ");
                        System.out.println("1. Replace one of the destinations");
                        System.out.println("2. Remove one of the destinations");
                        System.out.println("3. Replace the truck");
                        System.out.println("4. Remove some products from the truck");
                        System.out.println("5. Back to main menu");

                        if (scanner.hasNextInt()) {
                            int subChoice = scanner.nextInt();

                            switch (subChoice) {
                                case 1:
                                    System.out.println("Enter the destination address you want to exchange: ");
                                    scanner.nextLine();
                                    String address1 = scanner.nextLine();
                                    if (!transportManagment.checkAddressExistInTransport(ID, address1)) {
                                        System.out.println("Entering an address that does not exist in this transport, try again");
                                        break;
                                    }
                                    System.out.println("Select the area from which you would like to take new orders (enter number 1-3): ");
                                    System.out.println("1. SOUTH");
                                    System.out.println("2. CENTRAL");
                                    System.out.println("3. NORTH");
                                    if(!scanner.hasNextInt()){
                                        System.out.println("Invalid input");
                                        scanner.next(); // Clean the input
                                        break;
                                    }
                                    ShippingArea area = ShippingArea.NORTH;
                                    int choiceArea = scanner.nextInt();
                                    switch (choiceArea){
                                        case 1:
                                            area = ShippingArea.SOUTH;
                                            break;
                                        case 2:
                                            area = ShippingArea.CENTRAL;
                                            break;
                                        case 3:
                                            break;
                                    }
                                    //TODO ido and dvir function (get availble sites)
                                    if(!transportManagment.printOrdersOfEachSite_ByTransportType(ID, area)){
                                        System.out.println("There are no orders available from this shipping area.");
                                        break;
                                    }
                                    System.out.println("These are all the orders according to the destinations in the area you selected.");
                                    System.out.println("Enter the address of the destination from which you would like to select new orders: ");
                                    scanner.nextLine();
                                    String address2 = scanner.nextLine();
                                    if (!siteController.checkSiteExist(address2)) {
                                        System.out.println("Entering an address that does not exist, try again");
                                        break;
                                    }
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                                    // Format the date and time using the defined format
                                    LocalDateTime temp = LocalDateTime.now(); // Initialization with zeros
                                    String dateTimeStr = temp.format(formatter); // date for real Time exchange
                                    // Add the orders the user wants from this site address.
                                    String dateForTest = transportsController.getTransportById(ID).getTransportDocument().getStartTransportTime(); // date for test.
                                    ArrayList<Integer> newOrders = selectOrdersToTransportDoc(transportsController.getTransportById(ID).getTrainingRequired(),transportManagment.getOrdersOfSiteBy_trainingTypeTransport(ID, area, address2), dateForTest);
                                    if(newOrders == null){
                                        System.out.println("Failed to replace destinations");
                                        break;
                                    }
                                    transportManagment.replaceSitesOfTransport(ID, address1, newOrders);
                                    System.out.println("The exchange of destination is complete, the transport after the update: ");
                                    transportManagment.printTransport(ID);
                                    break;

                                case 2:
                                    System.out.println("Enter the destination address you want to remove: ");
                                    scanner.nextLine();
                                    String address = scanner.nextLine();
                                    if (!siteController.checkSiteExist(address)) {
                                        System.out.println("Entering an address that does not exist, try again");
                                        break;
                                    }
                                    transportManagment.removeDestinationOfTransport(ID, address);
                                    System.out.println("Destination removal is complete, the transport after the update: ");
                                    transportManagment.printTransport(ID);
                                    break;

                                case 3:
                                    System.out.println("Replacing truck");
                                    if(transportManagment.getExistTrucksDuringTransport(ID, newWeight) == null){
                                        System.out.println("There are no available trucks suitable for this transport.");
                                        break;
                                    }
                                    transportManagment.printSuitableTrucksToTransport(ID, newWeight);
                                    System.out.println("Enter the license number of the truck you want to select");
                                    if (!scanner.hasNextInt()) {
                                        System.out.println("Invalid input. Try again");
                                        scanner.nextLine(); // Clear the input buffer
                                        break;
                                    }
                                    int truckLicense = scanner.nextInt();
                                    if(truckController.getTruckByLicense(truckLicense) == null){
                                        System.out.println("Invalid license truck number.");
                                        break;
                                    }
                                    if(!transportManagment.replaceTrucksDuringTransport(ID, truckController.getTruckByLicense(truckLicense))){
                                        System.out.println("The driver of this transport does not have an appropriate license for the truck you selected, The exchange failed.");
                                        break;
                                    }
                                    System.out.println("The exchange was completed successfully. Your transport now: ");
                                    transportManagment.printTransport(ID);
                                    break;
                                case 4:
                                    while (true) {
                                        System.out.print("Enter order number (or 'done' to finish): ");
                                        String orderInput = scanner.next();
                                        if (orderInput.equals("done")) {
                                            break;
                                        }
                                        if (!orderInput.matches("\\d+")) {
                                            System.out.println("Invalid input. please enter a number");
                                            continue;
                                        }
                                        int orderNumber = Integer.parseInt(orderInput);
                                        if (transportManagment.getOrderByNumDuringTransport(ID, orderNumber) == null) {
                                            System.out.println("Invalid ID number. Try again");
                                            continue;
                                        }
                                        System.out.print("Enter item name: ");
                                        String itemName = scanner.next();
                                        System.out.println("Enter item amount: ");
                                        scanner.nextLine(); /////////////
                                        if(!scanner.hasNextInt()){
                                            System.out.println("Invalid input. Try again");
                                            scanner.next();
                                            continue;
                                        }
                                        int amount = scanner.nextInt();
                                        int result = transportManagment.removeItemsFromTransportByOrderNum(ID, orderNumber, itemName, amount);
                                        if(result == 1){
                                            System.out.println("Invalid order number. Try again");
                                            continue;
                                        }
                                        if(result == 2){
                                            System.out.println("Invalid Item name. Try again");
                                            continue;
                                        }
                                        if(result == 3){
                                            System.out.println("You chose to remove a larger quantity than the product has. Try again");
                                            continue;
                                        }
                                        System.out.println("Quantity removal from this product has been successfully completed");
                                        System.out.println("Your order now: ");
                                        transportManagment.printOrderByTransportID(ID, orderNumber);
                                    }
//                                    break;
                                case 5:
                                    return;
                                default:
                                    System.out.println("Invalid choice.");
                            }

                            System.out.println("To proceed with transport rescheduling operations enter 1, otherwise enter 0:");
                            if (!scanner.hasNextInt()){
                                scanner.next();
                                return;
                            }
                            if(scanner.nextInt() != 1){
                                return;
                            }
                        } else {
                            System.out.println("Invalid input: please enter a number.");
                            scanner.nextLine(); // Clear the input buffer
                        }
                    }

                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }

    public static void finishTransport(TransportManagment transportManagment)  {
        TransportsController transportsController = TransportsController.getInstance();
        if(!transportsController.checkIfExistTransportInProcess()){
            System.out.println("There are no transports in progress in the system");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("1. Print all transports still on the way");
            System.out.println("2. Finish transport");
            System.out.println("3. return to main menu ");
            if(!scanner.hasNextInt()){
                System.out.println("Invalid input");
                scanner.next();
                continue;
            }
            int answer = scanner.nextInt();
            switch (answer){
                case 1:

                    transportsController.printAllTransportInProcess();
                    break;
                case 2:
                    System.out.println("Enter the transport number (ID) you would like to finish: ");
                    if(!scanner.hasNextInt()){
                        System.out.println("Invalid input");
                        scanner.next();
                        continue;
                    }
                    int transportID = scanner.nextInt();
                    if (!transportsController.finishTransport(transportID)){
                        System.out.println("Invalid ID. Try again");
                        break;
                    }
                    System.out.println("This transport is over.");
                    return;

                case 3:
                    return;
            }
        }

    }
    public static void transportInfo(TransportManagment transportManagment){
        TransportsController transportsController = TransportsController.getInstance();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter transport number: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input");
                scanner.next();
                continue;
            }
            int ID = scanner.nextInt();
            if(transportsController.getTransportById(ID) == null){
                System.out.println("No transport exists for this ID number.");
                return;
            }
            transportManagment.printTransport(ID);
            return;
        }
    }

    public static void addOrderFromSupplier(TransportManagment transportManagment){
        OrdersController ordersController = OrdersController.getInstance();
        Scanner scanner = new Scanner(System.in);
        String contact_phone = null;
        String supplierName = null;
        String strorageType = null;
        String address = null;
        String date = null;
        int supplierNumber = 0;
        int orderNumber = 0;
        while (true){
            System.out.print("Enter order number (or 'done' to finish): ");
            String orderInput = scanner.next();
            if (orderInput.equals("done")) {
                return;
            }
            if (!orderInput.matches("\\d+")) {
                System.out.println("Invalid input. please enter a number");
                continue;
            }
            orderNumber = Integer.parseInt(orderInput);
            scanner.nextLine();
            System.out.print("Enter supplier name: ");
            supplierName = scanner.nextLine();

            System.out.print("Enter supplier number: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.next();
            }
            supplierNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter address: ");
            address = scanner.nextLine();

            System.out.print("Enter date (format: dd/mm/yyyy): ");
            date = scanner.nextLine();


            System.out.print("Enter contact phone number: ");
            contact_phone = scanner.nextLine();
            break;
        }

        ArrayList<Item> items = new ArrayList<Item>();
        boolean addItem = true;
        while (addItem) {
            // Prompt user for item name
            System.out.print("Enter item name: ");
            String itemName = scanner.nextLine();

            System.out.println("Enter storage type (Dry/Cool/Frozen): ");
            strorageType = scanner.nextLine();
            // Prompt user for item quantity
            int itemQuantity=0;
            do {
                System.out.print("Enter item quantity: ");
                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input. Please enter an integer: ");
                    scanner.next();
                }
                itemQuantity = scanner.nextInt();
            } while (itemQuantity <= 0);
            scanner.nextLine();
            // Create new Item object and add to items list
            Item newItem = new Item(itemName, strorageType, itemQuantity);
            items.add(newItem);
            System.out.print("Add another item? (Y/N): ");
            String addMore = scanner.nextLine();
            addItem = addMore.equalsIgnoreCase("Y");
        }

        // Create and return new TransportOrder object
        ordersController.createTransportOrder(orderNumber,  supplierName, supplierNumber, address,  date,  contact_phone, items );
        System.out.println("The order was successfully received");

    }


    public static void addTruckToSystem(TransportManagment transportManagment) {
        TruckController truckController = TruckController.getInstance();
        Scanner scanner = new Scanner(System.in);
        // Get truck license number
        int licenseNumber = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter truck license number: ");
            String input = scanner.nextLine();
            if (input.matches("\\d+")) {
                licenseNumber = Integer.parseInt(input);
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter an integer value.");
            }
        }
        if(truckController.truckExist(licenseNumber)){
            System.out.println("A truck with this license number already exists in the system.");
            return;

        }

        // Get truck model
        System.out.print("Enter truck model: ");
        String model = scanner.nextLine();

        // Get truck net weight
        double netWeight = 0;
        validInput = false;
        while (!validInput) {
            System.out.print("Enter truck net weight: ");
            String input = scanner.nextLine();
            if (input.matches("\\d+(\\.\\d+)?")) {
                netWeight = Double.parseDouble(input);
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter a decimal value.");
            }
        }

        // Get truck max weight
        double maxWeight = 0;
        validInput = false;
        while (!validInput) {
            System.out.print("Enter truck max weight: ");
            String input = scanner.nextLine();
            if (input.matches("\\d+(\\.\\d+)?")) {
                maxWeight = Double.parseDouble(input);
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter a decimal value.");
            }
        }

        // Get truck type
        String truckType = null;
        validInput = false;
        while (!validInput) {
            System.out.print("Enter truck type (DRY, COOL, FROZEN): ");
            truckType = scanner.nextLine().toUpperCase();
            if (truckType.equals("DRY") || truckType.equals("COOL") || truckType.equals("FROZEN")) {
                validInput = true;
            } else {
                System.out.println("Invalid truck type. Please enter DRY, COOL, or FROZEN.");
            }
        }

        // Create truck object based on type

        switch (truckType) {
            case "DRY":
                break;
            case "COOL":
                break;
            case "FROZEN":
                break;
        }
        Boolean check = truckController.createTruck(licenseNumber, model,netWeight, maxWeight, truckType); // TODO add condition
        // Add truck to list
        if (!check) {
            System.out.println("Reception of the truck was not successful. Try again later");
            return;
        }
        System.out.println("Truck added successfully: " );
        truckController.printTruck(licenseNumber);
    }

}
