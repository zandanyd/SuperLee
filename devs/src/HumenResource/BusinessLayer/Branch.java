package SuperLee.HumenResource.BusinessLayer;

public class Branch {

    //Unique address for each branch
    private String address;

    Branch(String address)  {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String name) {
        address = name;
    }

    public String toString(){
        return String.format("Branch Address: " + address );
    }

}



