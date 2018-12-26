package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.activities.AddTransactionActivity;
import pt.uc.dei.cm.myfinances.activities.EditTransactionActivity;
import pt.uc.dei.cm.myfinances.adapters.TransactionAdapter;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends androidx.fragment.app.Fragment implements AdapterView.OnItemClickListener{
    private static final String TAG = "HomeFragment";
    private static final int START_ACT_ADD_CODE = 1000;
    private static final int START_ACT_EDIT_CODE = 1001;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.fragmentHome) View mRootView;
    @BindView(R.id.recycle_view_transactions) RecyclerView transactionsList;
    @BindView(R.id.fab_add_transaction) FloatingActionButton fabAddTransaction;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.previous_month) ImageButton previousMonth;
    @BindView(R.id.next_month) ImageButton nextMonth;
    @BindView(R.id.current_month) TextView currentMonth;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionAdapter adapter;
    private int currentMonthNum;
    private int currentYearNum;

    MyFinancesApplication app;
    DecimalFormat df2 = new DecimalFormat(".##");   //this is to only have 2 decimal numbers

    List<Transaction> transactions;

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



        /*
        * For now we create a default Wallet and load the categories to the MyFinancesApplication class
        * This need to be changed when we have a DB
        */

        if(app.getCurrentWallet() == null){
            defaultWallet();
        }

        //loadCategories();

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
    private void defaultWallet(){
        Wallet wallet1 =  new Wallet("Wallet1",0);



        app.getDb().databaseDao().insertWallet(wallet1);
        app.setCurrentWallet(wallet1);


    }

    //loads the categories to an hashmap<String,Integer>.
    //In the hashmap we save the name of the category and a color
   /* private void loadCategories(){
        HashMap<String, Integer> map = new HashMap<>();

        //creates an array from the categories.xml file
        String[] categories = getResources().getStringArray(R.array.categories);

        map.put(categories[0],Color.BLACK);
        map.put(categories[1],Color.BLUE);
        map.put(categories[2],Color.CYAN);
        map.put(categories[3],Color.DKGRAY);
        map.put(categories[4],Color.GRAY);
        map.put(categories[5],Color.GREEN);
        map.put(categories[6],Color.LTGRAY);
        map.put(categories[7],Color.MAGENTA);
        map.put(categories[8],Color.RED);
        map.put(categories[9],Color.YELLOW);
        map.put(categories[10],getResources().getColor(R.color.purple));   //purple
        map.put(categories[11],getResources().getColor(R.color.orange));   //orange
        map.put(categories[12],getResources().getColor(R.color.brown));   //brown
        map.put(categories[13],getResources().getColor(R.color.pink));   //pink

        app.setCategories(map);
    }*/

    private String getCurrentMonth(){
        Calendar c = Calendar.getInstance();
        return ""+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
    }

    @OnClick(R.id.previous_month)
    public void goToPreviousMonth(){
        if(currentMonthNum > 0){
            currentMonthNum--;
        }
        else{
            currentMonthNum = 11;
            currentYearNum--;
        }
        transactions = app.getDb().databaseDao().getTransactionsByMonth(currentMonthNum+1, currentYearNum, app.getCurrentWallet().getName());
        adapter = new TransactionAdapter(getContext(),this::onItemClick, transactions);
        transactionsList.setAdapter(adapter);
        currentMonth.setText(""+(currentMonthNum+1)+"/"+currentYearNum);
    }

    @OnClick(R.id.next_month)
    public void goToNextMonth(){
        if(currentMonthNum < 11){
            currentMonthNum++;
        }
        else{
            currentMonthNum = 0;
            currentYearNum++;
        }
        transactions = app.getDb().databaseDao().getTransactionsByMonth(currentMonthNum+1, currentYearNum, app.getCurrentWallet().getName());
        adapter = new TransactionAdapter(getContext(),this::onItemClick, transactions);
        transactionsList.setAdapter(adapter);
        currentMonth.setText(""+(currentMonthNum+1)+"/"+currentYearNum);
    }

    //starts activity to add a transaction
    @OnClick(R.id.fab_add_transaction)
    public void addItem(){
        Intent startAddTransaction =  new Intent(getActivity(), AddTransactionActivity.class);
        startActivityForResult(startAddTransaction, START_ACT_ADD_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();

        transactions = app.getDb().databaseDao().getTransactionsByMonth(currentMonthNum+1, currentYearNum, app.getCurrentWallet().getName());
        adapter = new TransactionAdapter(getContext(),this::onItemClick, transactions);
        transactionsList.setAdapter(adapter);

        String value = df2.format(app.getDb().databaseDao().getCurrentWallet().getBalance());
        balance.setText(getString(R.string.balance)+" "+value);
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
        editTransaction.putExtra("id",t.getText().toString());
        startActivityForResult(editTransaction,START_ACT_EDIT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
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