package pt.uc.dei.cm.myfinances;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

import pt.uc.dei.cm.myfinances.general.Wallet;

public class MyFinancesApplication extends Application {
    private ArrayList<Wallet> wallets =  new ArrayList<>();
    private HashMap<String,Integer> categories = new HashMap<>();
    //private ArrayList<Transaction> transactions =  new ArrayList<>();
    private Wallet currentWallet;

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
    }

    public HashMap<String, Integer> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, Integer> categories) {
        this.categories = categories;
    }

    /*public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }*/

    public Wallet getCurrentWallet() {
        return currentWallet;
    }

    public void setCurrentWallet(Wallet currentWallet) {
        this.currentWallet = currentWallet;
    }
}
