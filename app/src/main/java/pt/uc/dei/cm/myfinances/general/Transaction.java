package pt.uc.dei.cm.myfinances.general;

import java.util.Date;

public class Transaction {
    private Date date;
    private String category;
    private boolean isExpense;
    private double amount;
    private String comment;

    public Transaction() {
    }

    public Transaction(Date date, String category, boolean isExpense, double amount, String comment) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
