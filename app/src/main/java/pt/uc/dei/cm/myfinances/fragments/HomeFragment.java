package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.SharedPreferencesHelper;
import pt.uc.dei.cm.myfinances.activities.AddTransactionActivity;
import pt.uc.dei.cm.myfinances.activities.EditTransactionActivity;
import pt.uc.dei.cm.myfinances.adapters.TransactionAdapter;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends androidx.fragment.app.Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "HomeFragment";
    private static final int START_ACT_ADD_CODE = 1000;
    private static final int START_ACT_EDIT_CODE = 1001;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.fragmentHome)
    View mRootView;
    @BindView(R.id.recycle_view_transactions)
    RecyclerView transactionsList;
    @BindView(R.id.fab_add_transaction)
    FloatingActionButton fabAddTransaction;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.previous_month)
    ImageButton previousMonth;
    @BindView(R.id.next_month)
    ImageButton nextMonth;
    @BindView(R.id.current_month)
    TextView currentMonth;
    @BindView(R.id.noRecords)
    TextView noDataFound;

    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionAdapter adapter;
    private int currentMonthNum;
    private int currentYearNum;

    private MyFinancesApplication app;
    private DecimalFormat df2 = new DecimalFormat(".##");   //this is to only have 2 decimal numbers

    private List<Transaction> transactions;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //use savedInstanceState?
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        app = (MyFinancesApplication) getActivity().getApplicationContext();

        app.setCurrentWallet(app.getDb().databaseDao().getCurrentWallet());

        if (app.getCurrentWallet() == null) {
            defaultWallet();
            loadCategories();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentMonthNum = Calendar.getInstance().get(Calendar.MONTH);
        currentYearNum = Calendar.getInstance().get(Calendar.YEAR);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        transactionsList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        transactionsList.setLayoutManager(mLayoutManager);

        currentMonth.setText(getCurrentMonth());
    }

    //TODO Comunicar com BD para saber as wallets existentes
    //for now create a default wallets
    private void defaultWallet() {
        Wallet wallet1 = new Wallet("Wallet1", 0);


        app.getDb().databaseDao().insertWallet(wallet1);
        app.setCurrentWallet(wallet1);


    }

    //loads the categories to an hashmap<String,Integer>.
    //In the hashmap we save the name of the category and a color
    private void loadCategories() {
        //creates an array from the categories.xml file
        String[] categories = getResources().getStringArray(R.array.categories);

        app.getDb().databaseDao().insertinit(new Categories(categories[0], Color.RED));
        app.getDb().databaseDao().insertinit(new Categories(categories[1], Color.BLUE));
        app.getDb().databaseDao().insertinit(new Categories(categories[2], Color.CYAN));
        app.getDb().databaseDao().insertinit(new Categories(categories[3], Color.DKGRAY));
        app.getDb().databaseDao().insertinit(new Categories(categories[4], Color.GRAY));
        app.getDb().databaseDao().insertinit(new Categories(categories[5], Color.GREEN));
        app.getDb().databaseDao().insertinit(new Categories(categories[6], Color.LTGRAY));
        app.getDb().databaseDao().insertinit(new Categories(categories[7], Color.MAGENTA));
        app.getDb().databaseDao().insertinit(new Categories(categories[8], Color.YELLOW));
        app.getDb().databaseDao().insertinit(new Categories(categories[9], Color.BLACK));
        app.getDb().databaseDao().insertinit(new Categories(categories[10], getResources().getColor(R.color.purple)));   //purple
    }

    private String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return "" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
    }

    @OnClick(R.id.previous_month)
    public void goToPreviousMonth() {
        if (currentMonthNum > 0) {
            currentMonthNum--;
        } else {
            currentMonthNum = 11;
            currentYearNum--;
        }
        transactions = app.getDb().databaseDao().getTransactionsByMonth(currentMonthNum + 1, currentYearNum, app.getCurrentWallet().getName());
        adapter = new TransactionAdapter(getContext(), this::onItemClick, transactions);
        transactionsList.setAdapter(adapter);
        currentMonth.setText("" + (currentMonthNum + 1) + "/" + currentYearNum);
        verifyData();
    }

    @OnClick(R.id.next_month)
    public void goToNextMonth() {
        if (currentMonthNum < 11) {
            currentMonthNum++;
        } else {
            currentMonthNum = 0;
            currentYearNum++;
        }
        transactions = app.getDb().databaseDao().getTransactionsByMonth(currentMonthNum + 1, currentYearNum, app.getCurrentWallet().getName());
        adapter = new TransactionAdapter(getContext(), this::onItemClick, transactions);
        transactionsList.setAdapter(adapter);
        currentMonth.setText("" + (currentMonthNum + 1) + "/" + currentYearNum);
        verifyData();
    }

    //Verifies if exists transaction on the selected month
    private void verifyData() {
        if (transactions.size() == 0) {
            noDataFound.setText(R.string.no_data_found);
        } else {
            noDataFound.setText("");
        }
    }

    //starts activity to add a transaction
    @OnClick(R.id.fab_add_transaction)
    public void addItem() {
        Intent startAddTransaction = new Intent(getActivity(), AddTransactionActivity.class);
        startActivityForResult(startAddTransaction, START_ACT_ADD_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");

        transactions = app.getDb().databaseDao().getTransactionsByMonth(currentMonthNum + 1, currentYearNum, app.getCurrentWallet().getName());
        adapter = new TransactionAdapter(getContext(), this::onItemClick, transactions);
        transactionsList.setAdapter(adapter);

        verifyData();

        String value = df2.format(app.getDb().databaseDao().getCurrentWallet().getBalance());

        SharedPreferences preferences = getActivity().getSharedPreferences(SharedPreferencesHelper.SHARED_PREFS, Context.MODE_PRIVATE);
        String currency = preferences.getString(SharedPreferencesHelper.CURRENCY, null);

        if (currency != null) {
            balance.setText(getString(R.string.balance) + " " + value + currency);
        } else {
            balance.setText(getString(R.string.balance) + " " + value);
        }

        Log.d(TAG, "Still onResume");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView t = view.findViewById(R.id.transaction_id);

        Intent editTransaction = new Intent(getActivity(), EditTransactionActivity.class);
        editTransaction.putExtra("id", t.getText().toString());
        startActivityForResult(editTransaction, START_ACT_EDIT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case START_ACT_ADD_CODE:
                    Snackbar.make(mRootView, R.string.add_trans_suc, Snackbar.LENGTH_SHORT).show();
                    break;
                case START_ACT_EDIT_CODE:
                    //Snackbar.make(mRootView, R.string.add_trans_suc, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //Allow fragment to communicate with activity and other fragments
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}