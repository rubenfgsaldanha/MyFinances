package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class AddWalletActivity extends AppCompatActivity {

    MyFinancesApplication app;

    @BindView(R.id.add_wallet_layout) View mRootView;
    @BindView(R.id.name) EditText walletName;
    @BindView(R.id.initial_balance) EditText initialBalance;
    @BindView(R.id.save) Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.save)
    public void saveWallet(){
        String name = walletName.getText().toString();
        String strInitBalance = initialBalance.getText().toString();

        Wallet w = new Wallet(name,Double.parseDouble(strInitBalance));

        app = (MyFinancesApplication) getApplicationContext();

        new InsertWallet().execute(w);
    }

    private class InsertWallet extends AsyncTask<Wallet, Void, Void>{

        @Override
        protected Void doInBackground(Wallet... w) {
            //agora vai colocar a current wallet a false e atualizar na base de dados
            app.getCurrentWallet().setCurrentWallet(false);
            app.getDb().databaseDao().updateWalletStatus(app.getCurrentWallet().isCurrentWallet(), app.getCurrentWallet().getName());

            app.getDb().databaseDao().insertWallet(w[0]);
            app.setCurrentWallet(w[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
