package SuperLee.Transport.BusinessLayer;

public class DryTruck extends Truck {
    public DryTruck(int licenseNumber, String model, double netWeight, double maxWeight) {
        super(licenseNumber, model, netWeight, maxWeight);
    }





    @Override
    public TrainingType getTrainingType() {
        return TrainingType.DRY;
    }

    @Override
    public String toString() {
        return "Dry-Truck details: " +
                "licenseNumber:'" + licenseNumber + '\'' +
                ", model:'" + model + '\'' +
                ", netWeight:" + netWeight +
                ", maxWeight:" + maxWeight;
    }
}
