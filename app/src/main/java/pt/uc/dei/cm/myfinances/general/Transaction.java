package pt.uc.dei.cm.myfinances.general;

import java.util.Date;

public class Transaction {
    private String category;
    private double amount;
    private Date date;
    private String comment;

    public Transaction() {
    }

    public Transaction(String category, double amount, Date date, String comment) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.comment = comment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
                "category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                '}';
    }
}
