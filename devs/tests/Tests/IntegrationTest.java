 package Tests;

 import SuperLee.HumenResource.BusinessLayer.*;
 import SuperLee.HumenResource.DataLayer.ConstraintDataMapper;
 import SuperLee.HumenResource.DataLayer.DriverDataMapper;
 import SuperLee.Transport.BusinessLayer.*;
 import SuperLee.TransportWorkersController;
 import org.junit.jupiter.api.Test;

 import java.time.LocalDate;
 import java.util.ArrayList;

 import static org.junit.jupiter.api.Assertions.assertEquals;

class TransportWorkersControllerTest {
    @Test
    public void getAllAvailableDriverByDate() throws Exception {
        LocalDate Day = LocalDate.of(2023,5,23);
        String ID = "302287130";
        String FirstName = "Noam";
        String LastName = "Cohen";
        String password = "1234";
        String username = "302287130";
        int bankAccount = 2451;
        double wage = 50;
        String employmentCondition = "Student";
        LocalDate HireDate = LocalDate.of(2022, 3, 12);
        LicenseType license = LicenseType.Medium;
        ArrayList<TrainingType> allTraining = new ArrayList<>();
        allTraining.add(TrainingType.FROZEN);
        DriverController.getInstance().createNewDriver(FirstName,LastName,ID,bankAccount,wage,HireDate,employmentCondition,license,allTraining);
        DriverController.getInstance().AddConstraint(ID, Day, ShiftType.Morning);
        assertEquals(true, TransportWorkersController.getInstance().CheckAvailableDriverByDate(Day, ShiftType.Morning));
        DriverDataMapper.getInstance().delete(ID);
    }



    @Test
    public void getAvailableSiteByDate() throws Exception {
        Site newSite1 = new Site("Eqron City", "000", "000", ShippingArea.SOUTH);
        BranchWorker worker = WorkerController.getInstance().findWorker("111111111");
        LocalDate date = LocalDate.of(2023,12,12);
        WorkerController.getInstance().AddConstraint("111111111",  date ,ShiftType.Evening);
        WorkerController.getInstance().AddConstraint("444444444",  date ,ShiftType.Evening);
        String datestr = "2023-12-12 18:00";
        assertEquals(true, TransportWorkersController.getInstance().getAvailableSiteByDate(datestr, "Eqron City"));
        ConstraintDataMapper.getInstance().deleteConstraint("111111111",date, ShiftType.Evening);
        ConstraintDataMapper.getInstance().deleteConstraint("444444444",date, ShiftType.Evening);
    }

    @Test
    public void checkDriverToTruck() throws Exception{
        Truck truck = TruckController.getInstance().getTruckByLicense(1);
        Driver driver = DriverController.getInstance().findDriver("1");
        assertEquals(true, TransportWorkersController.getInstance().checkLicenseDriverToTruck(driver, truck));

    }

    @Test
    void existDocsWithDriverOnShift() throws Exception {
        boolean existDocs = TransportDocumentController.getInstance().checkIfExistPendingDocument();
        assertEquals(true, existDocs);
    }

}
//
