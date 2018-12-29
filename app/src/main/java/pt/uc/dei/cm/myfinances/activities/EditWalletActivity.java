package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.adapters.WalletAdapter;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

public class EditWalletActivity extends AppCompatActivity {

    @BindView(R.id.add_wallet_layout) View mRootView;
    @BindView(R.id.name) EditText walletName;
    @BindView(R.id.initial_balance) EditText initialBalance;
    @BindView(R.id.save) Button btnSave;
    @BindView(R.id.delete) Button btnDelete;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;

    private boolean makeCurrent = false;
    MyFinancesApplication app;
    private String nameWallet;
    private Wallet aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);

        ButterKnife.bind(this);
        app = (MyFinancesApplication) getApplicationContext();

        Intent intent = getIntent();
        nameWallet = intent.getStringExtra("wallet_name");

        new GetWallet().execute();
    }


    @OnClick(R.id.save)
    public void saveWallet(){
        String name = walletName.getText().toString();
        String strInitBalance = initialBalance.getText().toString();

        Wallet w = new Wallet(name, Double.parseDouble(strInitBalance));
        w.setId(aux.getId());

        new SaveWallet().execute(w);
    }

    @OnClick(R.id.delete)
    public void deleteWallet(){
        //Verifica se é a current Wallet
        if(nameWallet.equals(app.getCurrentWallet().getName())){
            Toast.makeText(this, "You can't delete your current wallet!", Toast.LENGTH_LONG).show();
        }
        else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.delete_wallet_question))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        new DeleteWallet().execute(dialog);
                    }).setNegativeButton(getString(R.string.cancel),null);

            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();
        }
    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();

        switch(v.getId()){
            case R.id.no:
                if(checked){
                    makeCurrent = false;
                }
                break;
            case R.id.yes:
                if(checked){
                    makeCurrent = true;
                }
                break;
        }
    }

    private class GetWallet extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            aux = app.getDb().databaseDao().getWalletByName(nameWallet);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            walletName.setText(nameWallet);
            initialBalance.setText(String.valueOf(aux.getBalance()));

            if(nameWallet.equals(app.getCurrentWallet().getName())){
                radioGroup.setVisibility(View.GONE);
            }
        }
    }

    private class SaveWallet extends AsyncTask<Wallet, Void, Void>{
        Wallet w;

        @Override
        protected Void doInBackground(Wallet... wallets) {
            w = wallets[0];
            if(!nameWallet.equals(app.getCurrentWallet().getName())){
                w.setCurrentWallet(false);

                if(makeCurrent){
                    w.setCurrentWallet(true);
                    //agora vai colocar a current wallet a false e atualizar na base de dados
                    app.getCurrentWallet().setCurrentWallet(false);
                    app.getDb().databaseDao().updateWalletStatus(app.getCurrentWallet().isCurrentWallet(), app.getCurrentWallet().getName());
                }
            }

            app.getDb().databaseDao().updateWallet(w);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(w.isCurrentWallet()){
                app.setCurrentWallet(w);
            }

            setResult(RESULT_OK);
            finish();
        }
    }

    private class DeleteWallet extends AsyncTask<DialogInterface, Void, Void>{
        DialogInterface dialogInterface;

        @Override
        protected Void doInBackground(DialogInterface... dialogInterfaces) {
            dialogInterface = dialogInterfaces[0];
            app.getDb().databaseDao().deleteWallet(aux);
            app.getDb().databaseDao().deleteAllTransactionsFromWallet(aux.getName());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialogInterface.dismiss();
            finish();
        }
    }
}
