package SuperLee.HumenResource.BusinessLayer;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.HashMap;

public abstract class  GenericWorker {
    private String FirstName;
    private String LastName;
    private String Password ;
    private String UserName;
    private String ID;
    private int BankAccount;
    private double Wage;
    private String EmploymentCondition;
    private LocalDate HireDate;
    private int NumberOfShiftsPerWeek;
    private double WorkHours;
    // map of all the constraints that the worker submitted
    GenericWorker(String firstName, String lastName, String id, String password, int bankAccount, double wage, LocalDate hireDate, String employmentCondition){
        this.FirstName = firstName;
        this.LastName = lastName;
        this.ID = id;
        this.BankAccount = bankAccount;
        this.EmploymentCondition = employmentCondition;
        this.HireDate = hireDate;
        this.Wage = wage;
        this.Password = password;
        this.EmploymentCondition = employmentCondition;
        this.WorkHours = 0;
        this.NumberOfShiftsPerWeek = 0;
    }
    public String getFirstName(){
        return this.FirstName;
    }
    public String getLastName(){
        return LastName;
    }
    public String getID() {
        return ID;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getPassword() {
        return Password;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public double getWage() {
        return Wage;
    }
    public void setWage(double wage) {
        Wage = wage;
    }
    public int getBankAccount() {
        return BankAccount;
    }
    public void setBankAccount(int bankAccount) {
        BankAccount = bankAccount;
    }
    public String getEmploymentCondition() {
        return EmploymentCondition;
    }
    public void setEmploymentCondition(String employmentCondition) {
        EmploymentCondition = EmploymentCondition +" "+ employmentCondition;
    }
    public int getNumberOfShiftsPerWeek() {
        return NumberOfShiftsPerWeek;
    }
    public void setNumberOfShiftsPerWeek(int numberOfShiftsPerWeek) {
        NumberOfShiftsPerWeek = numberOfShiftsPerWeek;
    }
    public LocalDate getHireDate() {
        return HireDate;
    }
    public double getWorkHours() {
        return WorkHours;
    }
    public void setWorkHours(double workHours) {
        WorkHours = workHours;
    }

    public String toString(){
        return String.format("name: " + this.FirstName + " " + this.LastName + "\n" + "ID: " + ID + "\n" +
                "Bank account: "+getBankAccount() + "\n" + "Wage: "+ Wage + "\n" + "HireDate: " + this.HireDate + "\n" +
                "Number Of Shifts this Week: " + this.NumberOfShiftsPerWeek + "\n" + "Work Hours: " + this.WorkHours +
                "\n" + "Employment Condition: " + this.EmploymentCondition);
    }
    public double CalculateMonthlyWageWithBonus(double bonus){
        double totalWage = Wage *WorkHours + bonus;
        return totalWage;
    }
    public double CalculateMonthlyWage(){
        double totalWage = Wage*WorkHours;
        return totalWage;
    }
    public void AddShift(){this.NumberOfShiftsPerWeek++;}



}

