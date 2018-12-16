package pt.uc.dei.cm.myfinances;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;

import androidx.room.Room;
import pt.uc.dei.cm.myfinances.database.MyFinancesDatabase;
import pt.uc.dei.cm.myfinances.general.Wallet;

public class MyFinancesApplication extends Application {
    private HashMap<String,Integer> categories = new HashMap<>();
    private Wallet currentWallet;

    private MyFinancesDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        openDB();

        Log.d("MyFinancesApplication", "OnCreate");
    }

    public void openDB(){
        //TODO: put on a background thread
        db = Room.databaseBuilder(getApplicationContext(), MyFinancesDatabase.class,
                "MyFinances").allowMainThreadQueries().build();
        Log.d("MyFinancesApplication", "Database opened!");
    }

    public void closeDB(){
        db.close();
        Log.d("MyFinancesApplication", "Database closed!");
    }

    public HashMap<String, Integer> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, Integer> categories) {
        this.categories = categories;
    }

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
