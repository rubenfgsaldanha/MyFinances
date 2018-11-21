package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.adapters.WalletAdapter;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WalletsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.fab_add_wallet) FloatingActionButton fabAddWallet;
    /*@BindView(R.id.recycle_view_wallets)*/ RecyclerView walletsList;

    MyFinancesApplication app;

    private WalletAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets);

        walletsList = findViewById(R.id.recycle_view_wallets);

        defaultWallet();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        walletsList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        walletsList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = new WalletAdapter(getApplicationContext(),this,app.getWallets());
        walletsList.setAdapter(adapter);
    }


    //TODO Comunicar com BD para saber as wallets existentes
    //for now create a default wallets
    private void defaultWallet(){
        Wallet wallet1 =  new Wallet("Wallet1",0);
        Wallet wallet2 =  new Wallet("Wallet2",5);
        Wallet wallet3 =  new Wallet("Wallet3",12.45);
        app = (MyFinancesApplication) getApplicationContext();
        app.getWallets().add(wallet1);
        app.getWallets().add(wallet2);
        app.getWallets().add(wallet3);
    }

    @OnClick(R.id.fab_add_wallet)
    public void addWallet(){
        Toast.makeText(getApplicationContext(),"Add wallet",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),"Item "+position+" clicked", Toast.LENGTH_SHORT).show();
    }
}
