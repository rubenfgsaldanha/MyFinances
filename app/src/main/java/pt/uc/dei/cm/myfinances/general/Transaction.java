package pt.uc.dei.cm.myfinances.general;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "transaction_day")
    private int day;

    @ColumnInfo(name = "transaction_month")
    private int month;

    @ColumnInfo(name = "transaction_year")
    private int year;

    @ColumnInfo(name = "transaction_category")
    private String category;

    @ColumnInfo(name = "transaction_comment")
    private String comment;

    @ColumnInfo(name = "transaction_amount")
    private double amount;

    @ColumnInfo(name = "expense")
    private boolean isExpense;

    @ColumnInfo(name = "wallet_name")
    private String walletName;

    public Transaction(int day, int month, int year, String category, String comment,
                       double amount, boolean isExpense, String walletName) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.category = category;
        this.comment = comment;
        this.amount = amount;
        this.isExpense = isExpense;
        this.walletName = walletName;
    }

    public String getDateString(){
        return ""+day+"/"+month+"/"+year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", category='" + category + '\'' +
                ", comment='" + comment + '\'' +
                ", amount=" + amount +
                ", isExpense=" + isExpense +
                ", walletName='" + walletName + '\'' +
                '}';
    }
}
