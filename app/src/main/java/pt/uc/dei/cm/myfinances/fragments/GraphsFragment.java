package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

/*
* Here we present a pie graph of the transactions for the current wallet
* We used a 3rd party library called: hellocharts-android -> https://github.com/lecho/hellocharts-android
*/

public class GraphsFragment extends androidx.fragment.app.Fragment {
    private final String TAG = "GraphsFragment";
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.pie_chart) PieChartView pieChart;

    public GraphsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_graphs, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawPiwChart();
    }

    //Here we draw the pie chart
    private void drawPiwChart(){
        //get the transactions from the current wallet
        MyFinancesApplication app = (MyFinancesApplication) getActivity().getApplicationContext();
        ArrayList<Transaction> transactions = app.getCurrentWallet().getTransactions();

        /*Now we create a hashmap with the name of the category and the value of the transaction
        * We use a hashmap because it has instant access
        */
        if(transactions.size() > 0){
            HashMap<String,Double> aux = new HashMap<>();

            for (int i=0; i<transactions.size(); i++){
                Transaction t = transactions.get(i);
                //if there's already a transaction of a certain category,
                // we calculate the sum of the values of those transactions
                // and update the value in the hashmap
                if(aux.containsKey(t.getCategory())){
                    double oldAmount = aux.get(t.getCategory());
                    double newAmount = oldAmount + t.getAmount();
                    aux.put(t.getCategory(),newAmount);
                }
                else{
                    aux.put(t.getCategory(),t.getAmount());
                }
            }

            //now we create a list with the transaction to present the pie chart
            List pieData =  new ArrayList<>();
            for(Map.Entry<String,Double> entry : aux.entrySet()){
                 int value = (int) Math.round(entry.getValue());
                 int color = app.getCategories().get(entry.getKey());
                 //here we add a value, color and a label
                 pieData.add(new SliceValue(value,color).setLabel(entry.getKey()));
            }

            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true).setValueLabelTextSize(14);
            pieChartData.setHasCenterCircle(true).setCenterCircleScale(0.42f);
            pieChart.setPieChartData(pieChartData);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
