package pt.uc.dei.cm.myfinances.general;

import java.util.ArrayList;

public class Wallet {
    private String name;
    private ArrayList<Transaction> transactions;
    private double balance;
    private boolean isCurrentWallet;

    public Wallet() {
    }

    public Wallet(String name, double balance) {
        this.name = name;
        this.balance = balance;
        transactions = new ArrayList<>();
        isCurrentWallet = true;
    }

    public Wallet(String name, ArrayList<Transaction> transactions, double balance) {
        this.name = name;
        this.transactions = transactions;
        this.balance = balance;
        isCurrentWallet = true;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCurrentWallet() {
        return isCurrentWallet;
    }

    public void setCurrentWallet(boolean currentWallet) {
        isCurrentWallet = currentWallet;
    }

    public void updateBalance(double transaction){
        balance += transaction;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "name='" + name + '\'' +
                ", transactions=" + transactions +
                ", balance=" + balance +
                '}';
    }
}
