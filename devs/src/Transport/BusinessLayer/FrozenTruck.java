package SuperLee.Transport.BusinessLayer;

public class FrozenTruck extends Truck {
    public FrozenTruck(int licenseNumber, String model, double netWeight, double maxWeight) {
        super(licenseNumber, model, netWeight, maxWeight);
    }

    public FrozenTruck() {}


    @Override
    public TrainingType getTrainingType() {
        return TrainingType.FROZEN;
    }

    @Override
    public String toString() {
        return "Frozen-Truck details: " +
                "licenseNumber:'" + licenseNumber + '\'' +
                ", model:'" + model + '\'' +
                ", netWeight:" + netWeight +
                ", maxWeight:" + maxWeight;
    }
}
