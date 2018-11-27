package pt.uc.dei.cm.myfinances.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class help to create the tables for the database, which has the following structure:
 *
 * TABLE Wallet: id, wallet_name, balance
 * TABLE Transaction: id, wallet_name, date, type, amount
 *
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";
    private static final String DATABASE_NAME = "MyFinances.db";
    public static final String ROWID = "_id";
    public static final String WALLET_NAME = "wallet_name";

    public static final String TABLE_WALLET = "wallets";
    public static final String WALLET_BALANCE = "wallet_balance";

    public static final String TABLE_TRANSACTION = "transactions";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_CATEGORY = "transaction_category";
    public static final String TRANSACTION_COMMENT = "transaction_comment";
    public static final String TRANSACTION_AMOUNT = "transaction_amount";

    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Cursor c1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='wallets'", null);
        Cursor c2 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='transactions'", null);

        try{
            if(c1.getCount() == 0){
                db.execSQL("CREATE TABLE "+TABLE_WALLET+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+WALLET_NAME+" TEXT, "+WALLET_BALANCE+" REAL);");
            }

            if(c2.getCount() == 0){
                db.execSQL("CREATE TABLE "+TABLE_TRANSACTION+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+WALLET_NAME+" TEXT, "+TRANSACTION_DATE+" TEXT, " +
                        ""+TRANSACTION_CATEGORY+" TEXT, "+TRANSACTION_COMMENT+" TEXT, "+TRANSACTION_AMOUNT+" REAL);");
            }
        }catch (Exception e){
            Log.e(TAG, "Error creating tables for DB");
            e.printStackTrace();
        }
        finally {
            c1.close();
            c2.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database, which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WALLET);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TRANSACTION);
        onCreate(db);
    }
}
