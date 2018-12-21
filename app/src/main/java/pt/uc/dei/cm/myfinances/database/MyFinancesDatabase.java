package pt.uc.dei.cm.myfinances.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import pt.uc.dei.cm.myfinances.general.Loan;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.general.Wallet;

@Database(entities = {Wallet.class, Transaction.class, Loan.class}, version = 1)
public abstract class MyFinancesDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
}
