package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.fragments.DatePickerFragment;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.List;

public class EditTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.btn_date) Button btnDate;
    @BindView(R.id.spinner_categories) Spinner categories;
    @BindView(R.id.amount) EditText amount;
    @BindView(R.id.comment) EditText comment;
    @BindView(R.id.button_save_transaction) Button btnSaveTransaction;
    @BindView(R.id.button_delete_transaction) Button btnDeleteTransaction;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.expense) RadioButton radioExpense;
    @BindView(R.id.income) RadioButton radioIncome;

    private String category;
    private boolean expense;
    private int[] transactionDate;
    private ArrayAdapter<String> dataAdapter;

    private MyFinancesApplication app;
    private Transaction t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        ButterKnife.bind(this);

        app = (MyFinancesApplication) getApplicationContext();

        // Spinner Drop down elements
        List<String> labels =  app.getDb().databaseDao().getAllLabels();
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter (this, android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categories.setAdapter(dataAdapter);

        //gets transaction id
        Intent intent = getIntent();
        int id = Integer.parseInt(intent.getStringExtra("id"));

        //sets the selected category
        t = app.getDb().databaseDao().getTransactionByID(id);
        category = t.getCategory();

        for(int i=0; i<labels.size(); i++){
            if(labels.get(i).equals(category)){
                categories.setSelection(i);
            }
        }

        //sets the transaction amount and comment
        if(t.getAmount() < 0){
            double auxAmount = t.getAmount() * (-1);
            amount.setText(""+auxAmount);
        }
        else{
            amount.setText(""+t.getAmount());
        }
        comment.setText(""+t.getComment());

        //checks if it's an expense or income, so at least on eof the radio buttons is checked
        if(t.getAmount() < 0){
            radioExpense.setChecked(true);
            expense = true;
        }
        else{
            radioIncome.setChecked(true);
            expense = false;
        }
    }

    private String getTransactionDateDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, t.getDay());
        c.set(Calendar.MONTH, t.getMonth());
        c.set(Calendar.YEAR, t.getYear());

        transactionDate = new int[]{t.getDay(),t.getMonth(),t.getYear()};

        return t.getDay()+"/"+t.getMonth()+"/"+t.getYear();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String date = getTransactionDateDate();
        btnDate.setText(date);
    }

    @OnClick(R.id.btn_date)
    public void pickDate(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    @OnClick(R.id.button_save_transaction)
    public void saveTransaction(){
        Double transactionAmount = Double.parseDouble(amount.getText().toString());
        String strComment = comment.getText().toString();

        Transaction transaction = new Transaction(transactionDate[0], transactionDate[1], transactionDate[2],
                category, strComment, transactionAmount, expense, app.getCurrentWallet().getName());

        //checks if it's an expense or income
        if(expense){
            transactionAmount = 0 - transactionAmount;
            transaction.setAmount(transactionAmount);
        }

        //checks if the user edited the transaction amount
        if(transactionAmount != t.getAmount()){
            app.getCurrentWallet().updateWalletBalance( (t.getAmount()*(-1)) + transactionAmount);
            app.getDb().databaseDao().updateWalletBalance(app.getCurrentWallet().getBalance(), app.getCurrentWallet().getName());
        }

        app.getDb().databaseDao().updateTransaction(t.getId(), transaction.getDay(), transaction.getMonth(), transaction.getYear(),
                transaction.getCategory(), transaction.getComment(), transaction.getAmount(), transaction.isExpense(), transaction.getWalletName());

        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.button_delete_transaction)
    public void deleteTransaction(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.delete_trans_ques))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {

                    app.getDb().databaseDao().deleteTransaction(t.getId());
                    app.getCurrentWallet().updateWalletBalance(0 - t.getAmount());
                    app.getDb().databaseDao().updateWalletBalance(app.getCurrentWallet().getBalance(), app.getCurrentWallet().getName());

                    dialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                }).setNegativeButton(getString(R.string.cancel),null);

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }

    //when the user clicks a category from the spinner we save it
    @OnItemSelected(R.id.spinner_categories)
    public void selectedCategory(AdapterView<?> parent, View view, final int position, long id){
        category = dataAdapter.getItem(position).toString();
    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();

        switch(v.getId()){
            case R.id.expense:
                if(checked){
                    expense = true;
                }
                break;
            case R.id.income:
                if(checked){
                    expense = false;
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        transactionDate = new int[]{dayOfMonth, month+1, year};
        btnDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}
