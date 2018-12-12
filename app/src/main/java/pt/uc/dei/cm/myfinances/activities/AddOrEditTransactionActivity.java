package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.StringRes;
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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddOrEditTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.btn_date) Button btnDate;
    @BindView(R.id.spinner_categories) Spinner categories;
    @BindView(R.id.amount) EditText amount;
    @BindView(R.id.comment) EditText comment;
    @BindView(R.id.button_save_transaction) Button btnSaveTransaction;

    private RadioGroup radioGroup;
    private String category;
    private ArrayAdapter<CharSequence> adapter;
    private boolean expense;
    private int[] transactionDate;

    MyFinancesApplication app;

    /*
    * We use a recycler view to list the transactions
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_transaction);
        ButterKnife.bind(this);

        radioGroup = findViewById(R.id.radioGroup);

        //create a spinner with the categories defined in the categories.xml file
        adapter = ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        app = (MyFinancesApplication) getApplicationContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String date = getCurrentDate();

        btnDate.setText(date);
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        transactionDate = new int[]{day,month,year};

        return day+"/"+month+"/"+year;
    }

    @OnClick(R.id.btn_date)
    public void pickDate(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    @OnClick(R.id.button_save_transaction)
    public void saveTransaction(){
        //check if a radio button has been clicked
        if(radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(this,"Please select the transaction type",Toast.LENGTH_SHORT).show();
        }
        else{
            Double transactionAmount = Double.parseDouble(amount.getText().toString());
            String strComment = " -> "+comment.getText().toString();

            //checks if it's an expense or income
            Transaction t;
            if(expense){
                transactionAmount = 0 - transactionAmount;
            }
            t = new Transaction(transactionDate[0], transactionDate[1], transactionDate[2],
                    category, strComment, transactionAmount, expense, app.getCurrentWallet().getName());

            app.getDb().databaseDao().insertTransaction(t);
            app.getCurrentWallet().updateWalletBalance(t.getAmount());
            app.getDb().databaseDao().updateWalletBalance(app.getCurrentWallet().getBalance(), app.getCurrentWallet().getName());

            setResult(RESULT_OK);
            finish();
        }
    }

    //when the user clicks a category from the spinner we save it
    @OnItemSelected(R.id.spinner_categories)
    public void selectedCategory(AdapterView<?> parent, View view, final int position, long id){
        category = adapter.getItem(position).toString();
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
        Calendar c = Calendar.getInstance();

        transactionDate = new int[]{dayOfMonth, month+1, year};

        btnDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}
