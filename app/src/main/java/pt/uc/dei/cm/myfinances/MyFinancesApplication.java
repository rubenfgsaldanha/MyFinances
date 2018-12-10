package pt.uc.dei.cm.myfinances;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.room.Room;
import pt.uc.dei.cm.myfinances.database.MyFinancesDatabase;
import pt.uc.dei.cm.myfinances.general.Wallet;

public class MyFinancesApplication extends Application {
    //private ArrayList<Wallet> wallets =  new ArrayList<>();
    private HashMap<String,Integer> categories = new HashMap<>();
    //private ArrayList<Transaction> transactions =  new ArrayList<>();
    private Wallet currentWallet;

    private MyFinancesDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        //TODO: put on a background thread
        db = Room.databaseBuilder(getApplicationContext(), MyFinancesDatabase.class,
                "MyFinances").allowMainThreadQueries().build();

        Log.d("MyFinancesApplication", "OnCreate");
    }

    /*public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
    }*/

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

    public MyFinancesDatabase getDb() {
        return db;
    }
}
