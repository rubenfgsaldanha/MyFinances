package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.fragments.DatePickerFragment;
import pt.uc.dei.cm.myfinances.general.Loan;
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
import android.widget.Toast;

import java.util.Calendar;

public class AddLoanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.btn_loan_date) Button btnLoanDate;
    @BindView(R.id.add_loan_amount) EditText loanAmount;
    @BindView(R.id.thirdP) EditText thirdP;
    @BindView(R.id.button_save_loan) Button btnSaveLoan;
    @BindView(R.id.lenderRadioGroup) RadioGroup lenderRadioGroup;

    private ArrayAdapter<CharSequence> adapter;
    private boolean lender;
    private int[] loanDate;
    //private int[] dueDate;

    private MyFinancesApplication app;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);
        ButterKnife.bind(this);


        app = (MyFinancesApplication) getApplicationContext();
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        loanDate = new int[]{day,month,year};
        //dueDate=  new int[]{day,month,year};
        return day+"/"+month+"/"+year;
    }

    @Override
    protected void onStart() {
        super.onStart();

        String date = getCurrentDate();
        btnLoanDate.setText(date);
        //btnDueDate.setText(date);
    }

    @OnClick(R.id.btn_loan_date)
    public void pickLoanDate(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }


    @OnClick(R.id.button_save_loan)
    public void saveLoan(){
        //check if a radio button has been clicked
        if(lenderRadioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(this,"Please select the loan type",Toast.LENGTH_SHORT).show();
        }
        else{
            Double amount = Double.parseDouble(loanAmount.getText().toString());
            String strThirdP = thirdP.getText().toString();

            //checks if it's an expense or income
            Loan l;
            if(lender){
                amount = - amount;
            }
            l = new Loan(app.getCurrentWallet().getName(), loanDate[0], loanDate[1], loanDate[2],
                    lender, amount, strThirdP, false); //dueDate[0], dueDate[1], dueDate[2],

            app.getDb().databaseDao().insertLoan(l);
            app.getCurrentWallet().updateWalletBalance(l.getLoanAmount());
            app.getDb().databaseDao().updateWalletBalance(app.getCurrentWallet().getBalance(), app.getCurrentWallet().getName());

            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();

        loanDate = new int[]{dayOfMonth, month+1, year};

        btnLoanDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.lendee:
                if(checked){
                    lender = false;
                }
                break;
            case R.id.lender:
                if(checked){
                    lender = true;
                }
                break;
        }
    }
}
