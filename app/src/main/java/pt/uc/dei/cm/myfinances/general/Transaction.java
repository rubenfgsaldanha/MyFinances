package pt.uc.dei.cm.myfinances.general;

import java.util.Calendar;

public class Transaction {
    private Calendar date;
    private String category;
    private boolean isExpense;
    private double amount;
    private String comment;

    public Transaction() {
    }

    public Transaction(Calendar date, String category, boolean isExpense, double amount, String comment) {
        this.date = date;
        this.category = category;
        this.isExpense = isExpense;
        this.amount = amount;
        this.comment = comment;
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

    public Calendar getDate() {
        return date;
    }

    public String getDateString(){
        return ""+date.get(Calendar.DAY_OF_MONTH)+"/"+(date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.YEAR);
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", category='" + category + '\'' +
                ", isExpense=" + isExpense +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                '}';
    }
}
