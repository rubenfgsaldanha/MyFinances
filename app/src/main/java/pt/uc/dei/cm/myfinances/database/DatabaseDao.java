package pt.uc.dei.cm.myfinances.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import pt.uc.dei.cm.myfinances.general.Loan;
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

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    Transaction getTransactionByID(int id);

    @Insert
    void insertTransaction(Transaction... transactions);

    @Query("UPDATE `transaction` SET transaction_day = :day, transaction_month = :month, transaction_year = :year, " +
            "transaction_category = :category, transaction_comment = :comment, transaction_amount = :amount, " +
            "expense = :isExpense, wallet_name = :wallet_name WHERE id = :id")
    void updateTransaction(int id, int day, int month, int year, String category, String comment, double amount, boolean isExpense, String wallet_name);

    @Query("DELETE FROM `transaction` WHERE id = :id")
    void deleteTransaction(int id);

    @Query("DELETE FROM `transaction` WHERE wallet_name = :wallet_name")
    void deleteAllTransactionsFromWallet(String wallet_name);




    /***** operations for Loans *****/

    @Query("SELECT * FROM loan WHERE wallet_name = :walletName AND loan_month= :currentMonth and loan_year= :currentYear")//" //ORDER BY /*due_day,*/ loan_day ASC") /*(due_month=:currentMonth and due_year= :currentYear) or */
    List<Loan> getLoansPerMonth(int currentMonth, int currentYear, String walletName);

    @Query("UPDATE loan SET payed='True' WHERE loanId= :id")  //PODERA HAVER PORBLEMAS COM TRUE ENTRE ''
    void isPayed(long id);

    @Insert
    void insertLoan(Loan... loans);

    /*@Query("Insert into loan values ()")
    void insertLoan (Wallet w, int lday, int lmonth, int lyear,int dday, int dmonth, int dyear,
    boolean lender, double amount, String strThirdP, boolean payed);*/

    @Query("SELECT * FROM `loan` WHERE loanId = :id")
    Loan getLoanByID(int id);

    @Query("UPDATE `loan` SET loan_day = :day, loan_month = :month, loan_year = :year," + /* due_day = :dueday, due_month = :duemonth, due_year = :dueyear,*/
            " third_party = :thirdParty, loan_amount = :loanAmount, " +
            "is_lender = :lender, wallet_name = :wallet WHERE loanId = :loanId")
    void updateLoan(int loanId, int day, int month, int year, /*int dueyear, int duemonth, int dueday, */ String thirdParty, double loanAmount, boolean lender, String wallet);

    @Query("DELETE FROM `loan` WHERE loanId = :id")
    void deleteLoan(int id);





    /*@Query("SELECT * FROM wallet WHERE is_current = 1")
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
    void deleteWallet(Wallet... wallets);*/
}

