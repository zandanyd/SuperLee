package SuperLee.Transport.BusinessLayer;

public  class Site {

    private String address;
    private String phoneNumber;
    private String contactName;
    private ShippingArea shippingArea;

    public Site(String address, String phoneNumber, String contactName, ShippingArea shippingArea) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.shippingArea = shippingArea;
    }

    public Site(String address, String phoneNumber, String contactName){
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
    }
    public Site(){};

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setShippingArea(ShippingArea shippingArea) {
        this.shippingArea = shippingArea;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public ShippingArea getShippingArea() {
        return shippingArea;
    }


    @Override
    public String toString() {
        return "Site details: " +
                "address:'" + address + '\'' +
                ", phoneNumber:" + phoneNumber +
                ", contactName=:'" + contactName + '\'' +
                ", Shipping area: " + shippingArea;
    }
}
