package pt.uc.dei.cm.myfinances.general;

import java.util.Calendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Loan {

    @PrimaryKey(autoGenerate = true)
    private int loanId;

    @ColumnInfo(name = "loan_date")
    private Calendar loanDate;

    @ColumnInfo(name = "is_lender")
    private boolean isLender;

    @ColumnInfo(name = "loan_amount")
    private double loanAmount;

    @ColumnInfo(name = "thirdParty")
    private String thirdParty;

    @ColumnInfo(name = "due_date")
    private Calendar dueDate;

    @ColumnInfo(name = "payed")
    private boolean payed;

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public Calendar getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Calendar loanDate) {
        this.loanDate = loanDate;
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

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public Loan(Calendar date, boolean isLender, double amount, String thirdParty, Calendar paymentLimit, boolean payed) {
        this.dueDate = date;
        this.isLender = isLender;
        this.loanAmount = amount;
        this.thirdParty = thirdParty;
        this.dueDate = paymentLimit;
        this.payed = payed;

    }
    public String getDateString(Calendar date){
        return ""+date.get(Calendar.DAY_OF_MONTH)+"/"+(date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.YEAR);
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + loanDate +
                ", paymentLimit='" + dueDate + '\'' +
                ", isLender=" + isLender +
                ", amount=" + loanAmount +
                ", thirdParty='" + thirdParty + '\'' +
                ", payed='" + payed + '\'' +
                '}';
    }


}
