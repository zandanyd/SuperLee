package SuperLee.Transport.BusinessLayer;
public class Item {

    private String name;
    private String storageType;
    private int amount;


    public Item(String name, String storageType, int amount){
        this.name = name;
        this.storageType = storageType;
        this.amount = amount;
    }
    public Item(String name, String storageType){
        this.name = name;
        this.storageType = storageType;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void addToAmount(int val){
        this.amount += val;
    }
    public int getAmount(){
        return amount;
    }

    public String getStorageType() {
        return storageType;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", storageType='" + storageType + '\'' +
                ", amount=" + amount +
                '}';
    }

}
