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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.Calendar;

public class EditLoanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.payed) RadioButton payed;
    @BindView(R.id.not_payed) RadioButton notPaid;
    @BindView(R.id.btn_edit_loan_date) Button btnLoanDate;
    @BindView(R.id.edit_loan_amount) EditText loanAmount;
    @BindView(R.id.editthirdP) EditText thirdP;
    @BindView(R.id.button_save_loan) Button btnSaveLoan;
    @BindView(R.id.button_delete_loan) Button btnDeleteLoan;
    @BindView(R.id.editlenderRadioGroup) RadioGroup lenderRadioGroup;
    @BindView(R.id.isPaidRadioGroup) RadioGroup isPaidGroup;
    @BindView(R.id.editlender) RadioButton radioLender;
    @BindView(R.id.editlendee) RadioButton radioLendee;

    private ArrayAdapter<CharSequence> adapter;
    private boolean lender;
    private int[] loanDate;
    private boolean pay;
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
        new GetLoan().execute(id);
    }

    private String getLoanDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, l.getDay());
        c.set(Calendar.MONTH, l.getMonth());
        c.set(Calendar.YEAR, l.getYear());

        loanDate = new int[]{l.getDay(),l.getMonth(),l.getYear()};

        return l.getDay()+"/"+l.getMonth()+"/"+l.getYear();
    }


    @OnClick(R.id.btn_edit_loan_date)
    public void pickDate(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    @OnClick(R.id.button_save_loan)
    public void saveLoan(){
        Double amount = Double.parseDouble(loanAmount.getText().toString());
        String thirdParty = thirdP.getText().toString();

        Loan loan= new Loan(app.getCurrentWallet().getName(), loanDate[0], loanDate[1], loanDate[2],/*dueDate[0], dueDate[1], dueDate[2],*/
                lender, amount, thirdParty, pay);

        if(lender){
            amount = - amount;
            loan.setLoanAmount(amount);
        }

        if (pay != l.isPayed()){    ///CONFIRMAR QUE SAO ESTAS AS CONTAS!!
            new DeleteLoan(null).execute();
        }
        else{
            new SaveEditLoan(loan,amount).execute();
        }
    }

    @OnClick(R.id.button_delete_loan)
    public void deleteLoan(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.delete_loan_ques))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    new DeleteLoan(dialog).execute();
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
            case R.id.payed:
                if(checked){
                    pay=true;
                }
                break;
            case R.id.not_payed:
                if(checked){
                    pay = false;
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        loanDate = new int[]{dayOfMonth, month+1, year};
        btnLoanDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }

    private class GetLoan extends AsyncTask<Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... id) {
            l = app.getDb().databaseDao().getLoanByID(id[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (l.isPayed()){
                payed.setChecked(true);
                pay=true;
            }
            else{
                notPaid.setChecked(true);
                pay=false;
            }
            //sets the transaction amount and comment
            if(l.getLoanAmount() < 0){
                double auxAmount = l.getLoanAmount() * (-1);
                loanAmount.setText(""+auxAmount);
                radioLender.setChecked(true);
                lender = true;
            }
            else{
                loanAmount.setText(""+l.getLoanAmount());
                radioLendee.setChecked(true);
                lender = false;
            }
            thirdP.setText(""+l.getThirdParty());

            String date = getLoanDate();
            btnLoanDate.setText(date);
        }
    }

    private class SaveEditLoan extends AsyncTask<Void, Void, Void>{
        Loan loan;
        double amount;

        SaveEditLoan(Loan lo, double a){
            super();
            loan = lo;
            amount = a;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //checks if the user edited the loan amount
            if(amount != l.getLoanAmount()){
                app.getCurrentWallet().updateWalletBalance( (l.getLoanAmount()*(-1)) + amount);
                app.getDb().databaseDao().updateWalletBalance(app.getCurrentWallet().getBalance(), app.getCurrentWallet().getName());
            }

            app.getDb().databaseDao().updateLoan(l.getLoanId(), loan.getDay(), loan.getMonth(), loan.getYear(),/* loan.getDueyear(), loan.getDuemonth(), loan.getDueday(),*/
                    loan.getThirdParty(), loan.getLoanAmount(), loan.isLender(), loan.getWallet());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private class DeleteLoan extends AsyncTask<Void, Void, Void>{
        DialogInterface dialogInterface=null;

        DeleteLoan(DialogInterface dialog){
            dialogInterface = dialog;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            app.getDb().databaseDao().deleteLoan(l.getLoanId());
            app.getCurrentWallet().updateWalletBalance(l.getLoanAmount()*(-1));
            app.getDb().databaseDao().updateWalletBalance(app.getCurrentWallet().getBalance(), app.getCurrentWallet().getName());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(dialogInterface != null){
                dialogInterface.dismiss();
            }
            setResult(RESULT_OK);
            finish();
        }
    }
}
