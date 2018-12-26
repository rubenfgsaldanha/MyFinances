package pt.uc.dei.cm.myfinances;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

import androidx.room.Room;
import pt.uc.dei.cm.myfinances.database.MyFinancesDatabase;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.general.Wallet;

public class MyFinancesApplication extends Application {
    //private HashMap<String,Integer> categories = new HashMap<>();
    private Wallet currentWallet;

    private MyFinancesDatabase db;
    public static final String DATABASE_NAME = "MyFinances.db";

    @Override
    public void onCreate() {
        super.onCreate();

        openDB();

        Log.d("MyFinancesApplication", "OnCreate");
    }

    public void openDB(){
        //TODO: put on a background thread
        db = Room.databaseBuilder(getApplicationContext(), MyFinancesDatabase.class,
                DATABASE_NAME).allowMainThreadQueries().build();
        Log.d("MyFinancesApplication", "Database opened!");
        Categories init = new Categories(1,"comida",1);
        Categories sec = new Categories(2,"bebida",2);

        db.databaseDao().insertinit(init);
        db.databaseDao().insertinit(sec);

        Log.d("MyFinancesApplication", "-----------------------------------------------------------------------HERE----------------------------------------");

    }

    public void closeDB(){
        db.close();
        Log.d("MyFinancesApplication", "Database closed!");
    }

    /*public HashMap<String, Integer> getCategories() {
        return categories;
    }*/

    /*public void setCategories(HashMap<String, Integer> categories) {
        this.categories = categories;
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
