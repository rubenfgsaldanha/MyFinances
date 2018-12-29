package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
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
import pt.uc.dei.cm.myfinances.activities.AddLoanActivity;
import pt.uc.dei.cm.myfinances.activities.AddTransactionActivity;
import pt.uc.dei.cm.myfinances.activities.EditLoanActivity;
import pt.uc.dei.cm.myfinances.activities.EditTransactionActivity;
import pt.uc.dei.cm.myfinances.adapters.LoanAdapter;
import pt.uc.dei.cm.myfinances.adapters.TransactionAdapter;
import pt.uc.dei.cm.myfinances.general.Loan;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static android.app.Activity.RESULT_OK;


public class LoanFragment extends androidx.fragment.app.Fragment implements AdapterView.OnItemClickListener {

    private static final int START_ACT_ADD_CODE = 2000;
    private static final int START_ACT_EDIT_CODE = 2001;
    @BindView(R.id.loanFragment) View mRootView;
    @BindView(R.id.recycle_view_loans) RecyclerView loansList;
    @BindView(R.id.fab_add_loan) FloatingActionButton fabAddLoam;

    @BindView(R.id.past_month) ImageButton pastMonth;
    @BindView(R.id.following_month) ImageButton followingMonth;
    @BindView(R.id.now_month) TextView nowMonth;

    private RecyclerView.LayoutManager mLayoutManager;
    private LoanAdapter adapter;

    private OnFragmentInteractionListener mListener;

    private int currentMonthNum;
    private int currentYearNum;


    private MyFinancesApplication app;

    private List<Loan> loans;


    public LoanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.fragment_loan, container, false);

        ButterKnife.bind(this, mRootView);

        app = (MyFinancesApplication) getActivity().getApplicationContext();

        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentMonthNum = Calendar.getInstance().get(Calendar.MONTH);
        currentYearNum = Calendar.getInstance().get(Calendar.YEAR);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        loansList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        loansList.setLayoutManager(mLayoutManager);

        nowMonth.setText(getCurrentMonth());
    }


    @OnClick(R.id.past_month)
    public void goToPastMonth(){
        if(currentMonthNum > 0){
            currentMonthNum--;
        }
        else{
            currentMonthNum = 11;
            currentYearNum--;
        }

        new GetLoans2().execute();
    }

    @OnClick(R.id.following_month)
    public void goToNextMonth(){
        if(currentMonthNum < 11){
            currentMonthNum++;
        }
        else{
            currentMonthNum = 0;
            currentYearNum++;
        }

        new GetLoans2().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetLoans1().execute();
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
        TextView t = view.findViewById(R.id.loan_id);

        Intent editLoan = new Intent(getActivity(), EditLoanActivity.class);
        editLoan.putExtra("id",t.getText().toString());
        startActivityForResult(editLoan,START_ACT_EDIT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case START_ACT_ADD_CODE:
                    Snackbar.make(mRootView, R.string.add_loan_suc, Snackbar.LENGTH_SHORT).show();
                    break;
                case START_ACT_EDIT_CODE:
                    //Snackbar.make(mRootView, R.string.add_loan_suc, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //starts activity to add a loan
    @OnClick(R.id.fab_add_loan)
    public void addItem(){
        Intent startAddLoan =  new Intent(getActivity(), AddLoanActivity.class);
        startActivityForResult(startAddLoan, START_ACT_ADD_CODE);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private String getCurrentMonth(){
        Calendar c = Calendar.getInstance();
        return ""+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
    }

    abstract private class BaseTask<T> extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            loans = app.getDb().databaseDao().getLoansPerMonth(currentMonthNum+1, currentYearNum, app.getCurrentWallet().getName());
            return null;
        }
    }

    private class GetLoans1 extends BaseTask<Void>{
        GetLoans1(){
            super();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new LoanAdapter(getContext(),LoanFragment.this::onItemClick, loans);
            loansList.setAdapter(adapter);
        }
    }

    private class GetLoans2 extends BaseTask<Void>{
        GetLoans2(){
            super();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new LoanAdapter(getContext(),LoanFragment.this::onItemClick, loans);
            loansList.setAdapter(adapter);
            nowMonth.setText(""+(currentMonthNum+1)+"/"+currentYearNum);
        }
    }
}
