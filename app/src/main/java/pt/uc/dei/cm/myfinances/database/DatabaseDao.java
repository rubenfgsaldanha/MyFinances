package pt.uc.dei.cm.myfinances.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import pt.uc.dei.cm.myfinances.general.Transaction;

@Dao
public interface DatabaseDao {
    @Query("SELECT * FROM `transaction` WHERE transaction_month = :month AND transaction_year = :year")
    List<Transaction> getTransactionsByMonth(int month, int year);

    @Insert
    void insertAll(Transaction... transactions);
}
