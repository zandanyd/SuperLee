package SuperLee.Transport.BusinessLayer;

public enum LicenseType {
    Light(1000), Medium(2000), Heavy(5000);
    private double weightLimit;

    LicenseType(double weightLimit) {
        this.weightLimit = weightLimit;
    }

    public double getWeightLimit(){
        return weightLimit;
    }
}
