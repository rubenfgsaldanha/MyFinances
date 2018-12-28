package pt.uc.dei.cm.myfinances.activities;

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
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Calendar;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.btn_date) Button btnDate;
    @BindView(R.id.spinner_add_categories) Spinner cats;
    @BindView(R.id.amount) EditText amount;
    @BindView(R.id.comment) EditText comment;
    @BindView(R.id.button_save_transaction) Button btnSaveTransaction;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;

    private String category;
    private ArrayAdapter<CharSequence> adapter;
    private boolean expense;
    private int[] transactionDate;

    private MyFinancesApplication app;
    private ArrayAdapter<String> dataAdapter;

    /*
    * We use a recycler view to list the transactions
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        ButterKnife.bind(this);

        app = (MyFinancesApplication) getApplicationContext();
        // Spinner Drop down elements
        List<String> labels =  app.getDb().databaseDao().getAllLabels();

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter (this, android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        cats.setAdapter(dataAdapter);
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        transactionDate = new int[]{day,month,year};

        return day+"/"+month+"/"+year;
    }

    @Override
    protected void onStart() {
        super.onStart();

        String date = getCurrentDate();
        btnDate.setText(date);
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
            String strComment = comment.getText().toString();

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
    @OnItemSelected(R.id.spinner_add_categories)
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
        Calendar c = Calendar.getInstance();

        transactionDate = new int[]{dayOfMonth, month+1, year};

        btnDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}
