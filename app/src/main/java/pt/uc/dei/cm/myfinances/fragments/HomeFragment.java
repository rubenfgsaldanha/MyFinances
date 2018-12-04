package pt.uc.dei.cm.myfinances.fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.activities.AddTransactionActivity;
import pt.uc.dei.cm.myfinances.adapters.TransactionAdapter;
import pt.uc.dei.cm.myfinances.db.DataBaseHelper;
import pt.uc.dei.cm.myfinances.db.Provider;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends ListFragment{
    private static final String TAG = "HomeFragment";
    private static final int START_ACT_CODE = 1000;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.fragmentHome) View mRootView;
    @BindView(R.id.fab_add_transaction) FloatingActionButton fabAddTransaction;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.previous_month) ImageButton previousMonth;
    @BindView(R.id.next_month) ImageButton nextMonth;
    @BindView(R.id.current_month) TextView currentMonth;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionAdapter adapter;
    private int currentMonthNum;
    private int currentYearNum;

    private static final String[] PROJECTION = new String[]{
            Provider.Constants._ID, Provider.Constants.TRANSACTION_WALLET_NAME,
            Provider.Constants.TRANSACTION_DATE, Provider.Constants.TRANSACTION_CATEGORY,
            Provider.Constants.TRANSACTION_COMMENT, Provider.Constants.TRANSACTION_AMOUNT};
    private Cursor current;
    private AsyncTask task = null;

    MyFinancesApplication app;
    DecimalFormat df2 = new DecimalFormat(".##");   //this is to only have 2 decimal numbers


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

        /*
        * For now we create a default Wallet and load the categories to the MyFinancesApplication class
        * This need to be changed when we have a DB
        */
        app = (MyFinancesApplication) getActivity().getApplicationContext();
        if(app.getWallets().size() == 0){
            defaultWallet();
        }

        loadCategories();

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentMonthNum = Calendar.getInstance().get(Calendar.MONTH);
        currentYearNum = Calendar.getInstance().get(Calendar.YEAR);

        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(getActivity(), R.layout.transaction_list_item,
                        current, new String[]{DataBaseHelper.TRANSACTION_DATE,
                        DataBaseHelper.TRANSACTION_CATEGORY, DataBaseHelper.TRANSACTION_COMMENT,
                        DataBaseHelper.TRANSACTION_AMOUNT},
                        new int[]{R.id.textDate, R.id.transaction_item_text_Category,
                        R.id.transaction_item_text_Comment, R.id.transaction_balance},
                        0);

        setListAdapter(adapter);

        if (current == null) {
            task = new LoadCursorTask(getActivity(), null).execute();
        }


        currentMonth.setText(getCurrentMonth());

        getListView().setOnItemLongClickListener((parent, view1, position, id) -> {
            //TODO: delete transaction
            return  true;
        });
    }


    //TODO Comunicar com BD para saber as wallets existentes
    //for now create a default wallets
    private void defaultWallet(){
        Wallet wallet1 =  new Wallet("Wallet1",0);
        app.getWallets().add(wallet1);
        app.setCurrentWallet(wallet1);
    }


    //loads the categories to an hashmap<String,Integer>.
    //In the hasmap we save the name of the category and a color
    private void loadCategories(){
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
    }


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
        currentMonth.setText(""+(currentMonthNum+1)+"/"+currentYearNum);
    }


    //starts activity to add a transaction
    @OnClick(R.id.fab_add_transaction)
    public void addItem(){
        Intent startAddTransaction =  new Intent(getActivity(), AddTransactionActivity.class);
        startActivityForResult(startAddTransaction, START_ACT_CODE);
    }


    @Override
    public void onResume() {
        super.onResume();

        String value = df2.format(app.getCurrentWallet().getBalance());
        balance.setText(getString(R.string.balance)+" "+value);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //TODO: edit a transaction
    }

    //

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == START_ACT_CODE){
                //insert values into DB
                insertTransaction(data.getStringExtra("wallet_name"), data.getStringExtra("transaction_date"),
                        data.getStringExtra("transaction_category"), data.getStringExtra("transaction_comment"),
                        data.getDoubleExtra("transaction_amount",-1));

                Snackbar.make(mRootView, R.string.add_trans_suc, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void insertTransaction(String walletName, String date, String category, String comment, Double amount){
        ContentValues cv = new ContentValues();

        //first we'll see if there's an equal transaction already
        String selection = "transaction_wallet_name = " +walletName+ " AND transaction_date = " +date+ " AND transaction_category = " +category+
                " AND transaction_comment = " +comment+ " AND transaction_amount = " +amount;
        task = new LoadCursorTask(getActivity(), selection);

        if(current.getCount() != 0){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Warning")
                    .setMessage("We found an exact transaction in the same day, with the same category, comment and amount. " +
                            "To avoid duplications, you can either, press Ok to add this amount to the other transaction's amount, " +
                            "or you can press Cancel to delete this transaction.")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        cv.put(DataBaseHelper.TRANSACTION_AMOUNT,amount);
                        task = new UpdateTask(getActivity(),selection,new String[]{DataBaseHelper.TRANSACTION_AMOUNT}).execute(cv);
                    })
                    .setNegativeButton("Cancel",null).show();
        }
        else{
            cv.put(DataBaseHelper.TRANSACTION_WALLET_NAME, walletName);
            cv.put(DataBaseHelper.TRANSACTION_DATE, date);
            cv.put(DataBaseHelper.TRANSACTION_CATEGORY, category);
            cv.put(DataBaseHelper.TRANSACTION_COMMENT, comment);
            cv.put(DataBaseHelper.TRANSACTION_AMOUNT, amount);

            task = new InsertTask(getActivity(), null).execute(cv);
        }
    }


    //Allow fragment to communicate with activity and other fragments
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    abstract private class BaseTask<T> extends AsyncTask<T, Void, Cursor> {
        final ContentResolver resolver;
        String selection=null;

        BaseTask(Context ctxt, String selection) {
            super();
            this.selection = selection;
            resolver = ctxt.getContentResolver();
        }

        @Override
        public void onPostExecute(Cursor result) {
            ((CursorAdapter) getListAdapter()).changeCursor(result);
            current = result;
            task = null;
        }

        protected Cursor doQuery() {
            Cursor result = resolver.query(Provider.Constants.CONTENT_URI_TRANSACTION,
                    PROJECTION, selection, null, null);
            result.getCount();

            return (result);
        }
    }

    private class LoadCursorTask extends BaseTask<Void> {
        LoadCursorTask(Context ctxt, String selection) {
            super(ctxt, selection);
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            return (doQuery());
        }
    }

    private class InsertTask extends BaseTask<ContentValues> {
        InsertTask(Context ctxt, String selection) {
            super(ctxt, selection);
        }

        @Override
        protected Cursor doInBackground(ContentValues... values) {
            resolver.insert(Provider.Constants.CONTENT_URI_TRANSACTION, values[0]);

            return (doQuery());
        }
    }

    private class UpdateTask extends BaseTask<ContentValues> {
        String []whereArgs;
        UpdateTask(Context ctxt, String selection, String[] args) {
            super(ctxt, selection);
            whereArgs = args;
        }

        @Override
        protected Cursor doInBackground(ContentValues... values) {
            resolver.update(Provider.Constants.CONTENT_URI_TRANSACTION, values[0], selection, whereArgs);

            return (doQuery());
        }
    }

    private class RemoveTask extends BaseTask<String> {
        RemoveTask(Context ctxt, String selection) {
            super(ctxt, selection);
        }

        @Override
        protected Cursor doInBackground(String... id) {
            //String where = DataBaseHelper.ROWID + "=" + id[0];
            //resolver.delete(Provider.Constants.CONTENT_URI_TRANSACTION, where, null);

            return (doQuery());
        }
    }
}