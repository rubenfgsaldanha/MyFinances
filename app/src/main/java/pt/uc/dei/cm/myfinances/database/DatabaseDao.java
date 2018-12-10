package pt.uc.dei.cm.myfinances.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.general.Wallet;

@Dao
public interface DatabaseDao {

    /***** Operations for Wallets *****/
    @Query("SELECT * FROM wallet ORDER BY is_current DESC")
    List<Wallet> getWallets();

    @Query("SELECT * FROM wallet WHERE is_current = 1")
    Wallet getCurrentWallet();

    @Query("SELECT * FROM wallet WHERE wallet_name = :name")
    Wallet getWalletByName(String name);

    @Insert
    void insertWallet(Wallet... wallets);

    @Update
    void updateWallet(Wallet... w);

    @Query("UPDATE wallet SET wallet_balance = :balance WHERE wallet_name = :walletName")
    void updateWalletBalance(double balance, String walletName);

    @Query("UPDATE wallet SET is_current = :isCurrent WHERE wallet_name = :walletName")
    void updateWalletStatus(boolean isCurrent, String walletName);

    @Delete
    void deleteWallet(Wallet... wallets);


    /***** Operations for Transactions *****/
    @Query("SELECT * FROM `transaction` WHERE wallet_name = :walletName AND transaction_month = :month AND transaction_year = :year")
    List<Transaction> getTransactionsByMonth(int month, int year, String walletName);

    @Query("SELECT * FROM `transaction` WHERE wallet_name = :walletName")
    List<Transaction> getAllTransactions(String walletName);

    @Insert
    void insertTransaction(Transaction... transactions);

    @Update
    void updateTransaction(Transaction... t);

    @Delete
    void deleteTransaction(Transaction t);

    @Query("DELETE FROM `transaction` WHERE wallet_name = :wallet_name")
    void deleteAllTransactionsFromWallet(String wallet_name);
}
