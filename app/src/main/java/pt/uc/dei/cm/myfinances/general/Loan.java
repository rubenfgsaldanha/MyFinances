package pt.uc.dei.cm.myfinances.general;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Loan {

    @PrimaryKey(autoGenerate = true)
    private int loanId;

    @ColumnInfo(name = "loan_day")
    private int day;

    @ColumnInfo(name = "loan_month")
    private int month;

    @ColumnInfo(name = "loan_year")
    private int year;

    @ColumnInfo(name = "is_lender")
    private boolean isLender;

    @ColumnInfo(name = "loan_amount")
    private double loanAmount;

    @ColumnInfo(name = "third_party")
    private String thirdParty;

    /*@ColumnInfo(name = "due_day")
    private int dueday;

    @ColumnInfo(name = "due_month")
    private int duemonth;

    @ColumnInfo(name = "due_year")
    private int dueyear;*/

    @ColumnInfo(name = "payed")
    private boolean payed;

    @ColumnInfo(name="wallet_name")
    private String wallet;



    public Loan(String wallet, int day, int month, int year,  boolean isLender, double loanAmount, String thirdParty, boolean payed) { //int dueday, int duemonth, int dueyear,
        this.wallet=wallet;
        this.day = day;
        this.month = month;
        this.year = year;

        this.isLender = isLender;
        this.loanAmount = loanAmount;
        this.thirdParty = thirdParty;
        this.payed = payed;
        /*this.dueday = dueday;
        this.duemonth = duemonth;
        this.dueyear = dueyear;*/

    }
    public String getWallet() {    //BEING CALLED IN TRANS???
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }



    public boolean isLender() {
        return isLender;
    }

    public void setLender(boolean lender) {
        isLender = lender;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /*public int getDueday() {
        return dueday;
    }

    public void setDueday(int dueday) {
        this.dueday = dueday;
    }

    public int getDuemonth() {
        return duemonth;
    }

    public void setDuemonth(int duemonth) {
        this.duemonth = duemonth;
    }

    public int getDueyear() {
        return dueyear;
    }

    public void setDueyear(int dueyear) {
        this.dueyear = dueyear;
    }*/

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }


    public String getDateString(){
        return ""+day+"/"+month+"/"+year;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loan_id=" + loanId +
                ", loan_day=" + day +
                ", loan_month=" + month +
                ", loan_year=" + year +
                ", third_party='" + thirdParty +
                ", is_lender='" + isLender  +
                ", loan_amount=" + loanAmount +
                ", payed=" + payed +
                /*", due_day=" + dueday +
                ", due_month=" + duemonth +
                ", due_year=" + dueyear +*/
                '}';
    }


}
