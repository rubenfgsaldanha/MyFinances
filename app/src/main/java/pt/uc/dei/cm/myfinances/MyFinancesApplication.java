package pt.uc.dei.cm.myfinances;

import android.app.Application;

import java.util.ArrayList;

import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.general.Wallet;

public class MyFinancesApplication extends Application {
    private ArrayList<Wallet> wallets =  new ArrayList<>();
    private ArrayList<Transaction> transactions =  new ArrayList<>();

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
