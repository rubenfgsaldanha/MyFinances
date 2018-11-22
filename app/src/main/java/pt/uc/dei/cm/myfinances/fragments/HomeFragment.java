package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.activities.AddTransactionActivity;
import pt.uc.dei.cm.myfinances.adapters.TransactionAdapter;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends androidx.fragment.app.Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "HomeFragment";
    private static final int START_ACT_CODE = 1000;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.fragmentHome) View mRootView;
    @BindView(R.id.recycle_view_transactions) RecyclerView transactionsList;
    @BindView(R.id.fab_add_transaction) FloatingActionButton fabAddTransaction;
    @BindView(R.id.balance) TextView balance;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionAdapter adapter;

    MyFinancesApplication app;

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
        if(app.getWallets().size() == 0){
            defaultWallet();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // specify an adapter (see also next example)
        adapter = new TransactionAdapter(getContext(),this, app.getCurrentWallet().getTransactions());
        transactionsList.setAdapter(adapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        transactionsList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        transactionsList.setLayoutManager(mLayoutManager);
    }

    //TODO Comunicar com BD para saber as wallets existentes
    //for now create a default wallets
    private void defaultWallet(){
        Wallet wallet1 =  new Wallet("Wallet1",0);
        app.getWallets().add(wallet1);
        app.setCurrentWallet(wallet1);
    }

    @OnClick(R.id.fab_add_transaction)
    public void addItem(){
        Intent startAddTransaction =  new Intent(getActivity(), AddTransactionActivity.class);
        startActivityForResult(startAddTransaction, START_ACT_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        balance.setText(getString(R.string.balance)+" "+app.getCurrentWallet().getBalance());
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
        Toast.makeText(getActivity().getApplicationContext(),"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == START_ACT_CODE){
                Snackbar.make(mRootView, R.string.add_trans_suc, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    //Allow fragment to communicate with activity and other fragments
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}