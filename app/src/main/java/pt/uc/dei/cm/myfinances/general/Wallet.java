package pt.uc.dei.cm.myfinances.general;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Wallet {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "wallet_name")
    private String name;

    @ColumnInfo(name = "wallet_balance")
    private double balance;

    @ColumnInfo(name = "is_current")
    private boolean isCurrentWallet;        //in the database, it convert to an int: 1 is true and 0 is false


    public Wallet(String name, double balance) {
        this.name = name;
        this.balance = balance;
        //transactions = new ArrayList<>();
        isCurrentWallet = true;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void updateWalletBalance(double amount){
        balance += amount;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", isCurrentWallet=" + isCurrentWallet +
                '}';
    }
}
