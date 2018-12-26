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
import pt.uc.dei.cm.myfinances.general.Loan;
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

import java.util.Calendar;

public class EditLoanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.btn_edit_loan_date)
    Button btnLoanDate;
    /*@BindView(R.id.btn_due_date)
    Button btnDueDate;*/
    @BindView(R.id.edit_loan_amount)
    EditText loanAmount;
    @BindView(R.id.editthirdP) EditText thirdP;
    @BindView(R.id.button_save_loan) Button btnSaveLoan;
    @BindView(R.id.editlenderRadioGroup)
    RadioGroup lenderRadioGroup;
    @BindView(R.id.editlender) RadioButton radioLender;
    @BindView(R.id.editlendee) RadioButton radioLendee;

    private ArrayAdapter<CharSequence> adapter;
    private boolean lender;
    private int[] loanDate;
    //private int[] dueDate;

    private MyFinancesApplication app;
    private Loan l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_loan);
        ButterKnife.bind(this);


        app = (MyFinancesApplication) getApplicationContext();

        //gets loan id
        Intent intent = getIntent();
        int id = Integer.parseInt(intent.getStringExtra("id"));

        //sets the date to the transaction date
        l = app.getDb().databaseDao().getLoanByID(id);

        //sets the transaction amount and comment
        if(l.getLoanAmount() < 0){
            double auxAmount = l.getLoanAmount() * (-1);
            loanAmount.setText(""+auxAmount);
        }
        else{
            loanAmount.setText(""+l.getLoanAmount());
        }
        thirdP.setText(""+l.getThirdParty());

        //checks if it's an expense or income, so at least on eof the radio buttons is checked
        if(l.getLoanAmount() < 0){
            radioLendee.setChecked(true);
            lender = false;
        }
        else{
            radioLender.setChecked(true);
            lender = true;
        }
    }

    private String getLoanDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, l.getDay());
        c.set(Calendar.MONTH, l.getMonth());
        c.set(Calendar.YEAR, l.getYear());

        loanDate = new int[]{l.getDay(),l.getMonth(),l.getYear()};

        return l.getDay()+"/"+l.getMonth()+"/"+l.getYear();
    }
    /*private String getDueDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, l.getDueday());
        c.set(Calendar.MONTH, l.getDuemonth());
        c.set(Calendar.YEAR, l.getDueyear());

        dueDate = new int[]{l.getDueday(),l.getDuemonth(),l.getDueyear()};

        return l.getDueday()+"/"+l.getDuemonth()+"/"+l.getDueyear();
    }*/

    @Override
    protected void onStart() {
        super.onStart();

        String date = getLoanDate();
        btnLoanDate.setText(date);
        /*String dueDate = getDueDate();
        btnDueDate.setText(dueDate);*/
    }

    @OnClick(R.id.btn_edit_loan_date)
    public void pickDate(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }
    /*@OnClick(R.id.btn_due_date)
    public void pickDueDate(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }*/

    @OnClick(R.id.button_save_loan)
    public void saveLoan(){
        Double amount = Double.parseDouble(loanAmount.getText().toString());
        String thirdParty = thirdP.getText().toString();

        Loan loan= new Loan(app.getCurrentWallet().getName(), loanDate[0], loanDate[1], loanDate[2],/*dueDate[0], dueDate[1], dueDate[2],*/
                lender, amount, thirdParty, false);

        if(lender){
            amount = - amount;
            loan.setLoanAmount(amount);
        }

        //checks if the user edited the loan amount
        if(amount != l.getLoanAmount()){
            app.getCurrentWallet().updateWalletBalance( (l.getLoanAmount()*(-1)) + amount);
            app.getDb().databaseDao().updateWalletBalance(app.getCurrentWallet().getBalance(), app.getCurrentWallet().getName());
        }

        app.getDb().databaseDao().updateLoan(l.getLoanId(), loan.getDay(), loan.getMonth(), loan.getYear(),/* loan.getDueyear(), loan.getDuemonth(), loan.getDueday(),*/
                loan.getThirdParty(), loan.getLoanAmount(), loan.isLender(), loan.getWallet());

        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.button_delete_loan)
    public void deleteLoan(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.delete_loan_ques))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {

                    app.getDb().databaseDao().deleteLoan(l.getLoanId());
                    app.getCurrentWallet().updateWalletBalance(app.getCurrentWallet().getBalance() - l.getLoanAmount());
                    app.getDb().databaseDao().updateWalletBalance(app.getCurrentWallet().getBalance(), app.getCurrentWallet().getName());

                    dialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                }).setNegativeButton(getString(R.string.cancel),null);

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }


    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();

        switch(v.getId()){
            case R.id.lender:
                if(checked){
                    lender = true;
                }
                break;
            case R.id.lendee:
                if(checked){
                    lender = false;
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        loanDate = new int[]{dayOfMonth, month+1, year};
        btnLoanDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}
