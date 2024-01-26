package SuperLee.HumenResource.BusinessLayer;
import SuperLee.Transport.BusinessLayer.LicenseType;
import SuperLee.Transport.BusinessLayer.TrainingType;

import java.time.LocalDate;
import java.util.ArrayList;

public class Driver extends GenericWorker {
    private LicenseType licenseType;
    private ArrayList<TrainingType> trainings;



    public String getName(){
        return getFirstName() + " " + getLastName();
    }

    public Driver(String firstName, String lastName, String id, String password, int bankAccount, double wage, LocalDate hireDate, String employmentCondition, LicenseType license_Type, ArrayList<TrainingType> trainingsType) {
        super(firstName, lastName, id, password, bankAccount, wage, hireDate, employmentCondition);
        trainings = trainingsType;
        licenseType = license_Type;

    }

    public  LicenseType getLicenseType() {
        return licenseType;
    }

    public ArrayList<TrainingType> getTrainings() {
        return trainings;
    }


    public void setTrainings(TrainingType newTraining) {
        this.trainings.add(newTraining);
    }

    public boolean checkTraining(TrainingType trainingType) {
        return trainings.contains(trainingType);
    }

    @Override
    public String toString() {
        return String.format("Trainings: " + trainings + "\n" + super.toString() + "\n" + "License Type : " + licenseType);
    }
    public String toStringMethod_2() {
        StringBuilder trainingString = new StringBuilder();
        for (TrainingType type : trainings) {
            trainingString.append(type);
            trainingString.append(',');
        }
        if (this.trainings.size() != 0) {
            trainingString.deleteCharAt(trainingString.length() - 1);
        }
            return "Driver details: " +
                    "Name: '" + this.getName() + '\'' + ", ID : " + this.getID() +
                    ", LicenseType: " + licenseType +
                    ", Trainings passed: " + trainingString;
        }

}


