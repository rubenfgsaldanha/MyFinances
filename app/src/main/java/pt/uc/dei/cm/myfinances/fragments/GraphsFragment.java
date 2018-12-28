package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.SharedPreferencesHelper;
import pt.uc.dei.cm.myfinances.adapters.TransactionAdapter;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

/*
 * Here we present a pie graph of the transactions for the current wallet
 * We used a 3rd party library called: hellocharts-android -> https://github.com/lecho/hellocharts-android
 */

public class GraphsFragment extends androidx.fragment.app.Fragment {
    private final String TAG = "GraphsFragment";
    private OnFragmentInteractionListener mListener;

    private MyFinancesApplication app;
    private List<Transaction> transactions;
    private int currentMonthNum;
    private int currentYearNum;

    private DecimalFormat df2 = new DecimalFormat(".##");   //this is to only have 2 decimal numbers
    private SharedPreferences sharedPreferences;
    private boolean showPercentage = false;
    private boolean showSubtitle = false;
    private String currency;

    @BindView(R.id.pie_chart) PieChartView pieChart;
    @BindView(R.id.overall) TextView overallValue;
    @BindView(R.id.previous_month) ImageButton previousMonth;
    @BindView(R.id.next_month) ImageButton nextMonth;
    @BindView(R.id.current_month) TextView currentMonth;
    @BindView(R.id.noRecords) TextView noDataFound;

    public GraphsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MyFinancesApplication) getActivity().getApplicationContext();

        sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesHelper.SHARED_PREFS, Context.MODE_PRIVATE);

        currency = sharedPreferences.getString(SharedPreferencesHelper.CURRENCY, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_graphs, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentMonthNum = Calendar.getInstance().get(Calendar.MONTH);
        currentYearNum = Calendar.getInstance().get(Calendar.YEAR);

        currentMonth.setText(getCurrentMonth());
        transactions = app.getDb().databaseDao().getTransactionsByMonth(currentMonthNum + 1, currentYearNum, app.getCurrentWallet().getName());
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
        currentMonth.setText("" + (currentMonthNum + 1) + "/" + currentYearNum);
        drawPieChart();
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
        currentMonth.setText("" + (currentMonthNum + 1) + "/" + currentYearNum);
        drawPieChart();
    }

    //Here we draw the pie chart
    private void drawPieChart() {
        double totalAmount = 0;     //balance of the current month
        double totalMoney = 0;      //money that was moved around

        /*Now we create a hashmap with the name of the category and the value of the transaction
         * We use a hashmap because it has instant access
         */
        if (transactions.size() > 0) {
            HashMap<String, Double> aux = new HashMap<>();

            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                totalAmount += t.getAmount();
                totalMoney += Math.abs(t.getAmount());
                //if there's already a transaction of a certain category,
                // we calculate the sum of the values of those transactions
                // and update the value in the hashmap
                if (aux.containsKey(t.getCategory())) {
                    double oldAmount = aux.get(t.getCategory());
                    double newAmount = oldAmount + t.getAmount();
                    aux.put(t.getCategory(), newAmount);
                } else {
                    aux.put(t.getCategory(), t.getAmount());
                }
            }

            //now we create a list with the transaction to present the pie chart
            List pieData = new ArrayList<>();
            for (Map.Entry<String, Double> entry : aux.entrySet()) {
                int value = (int) Math.round(entry.getValue());
                ////////////////////int color = app.getCategories().get(entry.getKey());

                //calculate percentage and create subtitle if the user wishes
                String label = "";
                if(showSubtitle && showPercentage){
                    double percentage = (Math.abs(entry.getValue()) / totalMoney) * 100;
                    String percString = df2.format(percentage);
                    label = entry.getKey() + ": " + percString + " %";
                }
                else if(showPercentage){
                    double percentage = (Math.abs(entry.getValue()) / totalMoney) * 100;
                    String percString = df2.format(percentage);
                    label = percString + "%";
                }
                else if(showSubtitle){
                    label = entry.getKey();
                }

                //here we add a value, color and a label
                ///////////////////pieData.add(new SliceValue(value, color).setLabel(label));
            }

            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true).setValueLabelTextSize(14);
            pieChartData.setHasCenterCircle(true).setCenterCircleScale(0.42f);
            pieChart.setPieChartData(pieChartData);
            noDataFound.setText("");

            String value = df2.format(totalAmount);
            if(currency != null){
                overallValue.setText(value+currency);
            }
            else{
                overallValue.setText(value);
            }
        } else {
            PieChartData pieChartData = new PieChartData();
            pieChartData.setHasLabels(true).setValueLabelTextSize(14);
            pieChartData.setHasCenterCircle(true).setCenterCircleScale(0.42f);
            pieChart.setPieChartData(pieChartData);
            noDataFound.setText(getString(R.string.no_data_found));
            overallValue.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        showPercentage = sharedPreferences.getBoolean(SharedPreferencesHelper.SHOW_PERCENTAGES, false);
        showSubtitle = sharedPreferences.getBoolean(SharedPreferencesHelper.SHOW_SUBTITLES, false);

        //drawPieChart();
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
