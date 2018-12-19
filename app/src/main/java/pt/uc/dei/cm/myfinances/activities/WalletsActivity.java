package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.adapters.WalletAdapter;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class WalletsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private static final int STARTACT_CODE = 1001;

    @BindView(R.id.act_wallet_layout) View mRootView;
    @BindView(R.id.fab_add_wallet) FloatingActionButton fabAddWallet;
    @BindView(R.id.recycle_view_wallets) RecyclerView walletsList;

    MyFinancesApplication app;

    private WalletAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * We use a recycler view to list the wallets
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets);
        ButterKnife.bind(this);

        app = (MyFinancesApplication) getApplicationContext();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        walletsList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        walletsList.setLayoutManager(mLayoutManager);


    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Wallet> wallets = app.getDb().databaseDao().getWallets();

        // specify an adapter (see also next example)
        adapter = new WalletAdapter(getApplicationContext(),this::onItemClick, this::onItemLongClick,wallets);
        walletsList.setAdapter(adapter);
    }

    //starts Activity to add a wallet
    @OnClick(R.id.fab_add_wallet)
    public void addWallet(){
        Intent addWallet = new Intent(this,AddWalletActivity.class);
        startActivityForResult(addWallet,STARTACT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == STARTACT_CODE){
                Snackbar.make(mRootView, R.string.add_wallet_suc,Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView text = view.findViewById(R.id.wallet_item_text);
        String name = text.getText().toString();
        Intent editWallet = new Intent(this, EditWalletActivity.class);
        editWallet.putExtra("wallet_name", name);
        startActivity(editWallet);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //

        return true;
    }
}
