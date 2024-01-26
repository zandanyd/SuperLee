package SuperLee.Transport.BusinessLayer;

public class CoolTruck extends Truck {
    public CoolTruck(int licenseNumber, String model, double netWeight, double maxWeight) {
        super(licenseNumber, model, netWeight, maxWeight);
    }

    public CoolTruck() {}


    @Override
    public TrainingType getTrainingType() {
        return TrainingType.REFRIGERATED;
    }

    @Override
    public String toString() {
        return "Cool-Truck details: " +
                "licenseNumber:'" + licenseNumber + '\'' +
                ", model:'" + model + '\'' +
                ", netWeight:" + netWeight +
                ", maxWeight:" + maxWeight;
    }
}