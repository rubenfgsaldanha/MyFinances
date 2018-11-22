package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class AddTransactionActivity extends AppCompatActivity {

    @BindView(R.id.spinner_categories) Spinner categories;
    @BindView(R.id.amount) EditText amount;
    @BindView(R.id.comment) EditText comment;
    @BindView(R.id.button_save_transaction) Button btnSaveTransaction;

    private RadioGroup radioGroup;
    private String category;
    private ArrayAdapter<CharSequence> adapter;
    private boolean expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        ButterKnife.bind(this);

        radioGroup = findViewById(R.id.radioGroup);

        adapter = ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
    }

    @OnClick(R.id.button_save_transaction)
    public void saveTransaction(){
        if(radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(this,"Please select the transaction type",Toast.LENGTH_SHORT).show();
        }
        else{
            Double transactionAmount = Double.parseDouble(amount.getText().toString());
            String strComment = comment.getText().toString();

            Transaction t;
            if(expense){
                transactionAmount = 0 - transactionAmount;
            }
            t = new Transaction(null, category, expense, transactionAmount, strComment);

            MyFinancesApplication app = (MyFinancesApplication) getApplicationContext();
            app.getCurrentWallet().getTransactions().add(t);
            app.getCurrentWallet().updateBalance(transactionAmount);

            setResult(RESULT_OK);
            finish();
        }
    }

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
}
