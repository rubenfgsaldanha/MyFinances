package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.adapters.TransactionAdapter;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;


public class HomeFragment extends androidx.fragment.app.Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "HomeFragment";
    private OnFragmentInteractionListener mListener;

    private RecyclerView transactionsList;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionAdapter adapter;

    MyFinancesApplication app;


    @BindView(R.id.fab_add_transaction) FloatingActionButton fabAddTransaction;

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
        transactionsList = rootView.findViewById(R.id.recycle_view_transactions);

        defaultTransactions();

        // specify an adapter (see also next example)
        adapter = new TransactionAdapter(getContext(),this, app.getTransactions());
        transactionsList.setAdapter(adapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        transactionsList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        transactionsList.setLayoutManager(mLayoutManager);

        return rootView;
    }

    private void defaultTransactions(){
        Transaction t1 = new Transaction("Food",15.1,null,"KFC");
        Transaction t2 = new Transaction("Clothes",17.1,null,"Jeans");
        Transaction t3 = new Transaction("Technology",1.75,null,"HMDI Adapter");
        Transaction t4 = new Transaction("Salary",915.65,null,"Paycheck");
        app = (MyFinancesApplication) getActivity().getApplicationContext();
        app.getTransactions().add(t1);
        app.getTransactions().add(t2);
        app.getTransactions().add(t3);
        app.getTransactions().add(t4);
    }

    @OnClick(R.id.fab_add_transaction)
    public void addItem(){
        Toast.makeText(getActivity().getApplicationContext(),"Now we add something",Toast.LENGTH_SHORT).show();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
