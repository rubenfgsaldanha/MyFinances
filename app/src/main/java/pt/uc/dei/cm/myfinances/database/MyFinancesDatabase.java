package pt.uc.dei.cm.myfinances.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import pt.uc.dei.cm.myfinances.general.Transaction;

@Database(entities = {Transaction.class}, version = 1)
public abstract class MyFinancesDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
}
