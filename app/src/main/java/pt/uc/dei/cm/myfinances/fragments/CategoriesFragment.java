package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.activities.AddCategoryActivity;
import pt.uc.dei.cm.myfinances.activities.AddLoanActivity;
import pt.uc.dei.cm.myfinances.activities.EditCategoryactivity;
import pt.uc.dei.cm.myfinances.adapters.CategoryAdapter;
import pt.uc.dei.cm.myfinances.adapters.TransactionAdapter;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static android.app.Activity.RESULT_OK;


public class CategoriesFragment extends androidx.fragment.app.Fragment {
    private static final int START_ACT_ADD_CODE = 2 ;
    private static final int START_ACT_EDIT_CODE = 1;
    private final String TAG = "CategoriesFragment";
    private OnFragmentInteractionListener mListener;
    private MyFinancesApplication app;
    List<Categories> categories;
    CategoryAdapter adapter;

    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.fab_add_category) FloatingActionButton fab_add_category;
    @BindView(R.id.fragmentCat) View mRootView;
    @BindView(R.id.recycle_view_categories)
    RecyclerView catgoriesList;

    public CategoriesFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this,rootView);

        app = (MyFinancesApplication) getActivity().getApplicationContext();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*catgoriesList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        catgoriesList.setLayoutManager(mLayoutManager);
        categories = app.getDb().databaseDao().getCategories();
        adapter = new CategoryAdapter(getContext(),this::onItemClick, categories);
        catgoriesList.setAdapter(adapter);*/


        return rootView;
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         TextView t = view.findViewById(R.id.category_id);

                Intent editCategory = new Intent(getActivity(), EditCategoryactivity.class);
                editCategory.putExtra("id",t.getText().toString());
                startActivityForResult(editCategory,START_ACT_EDIT_CODE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catgoriesList.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        catgoriesList.setLayoutManager(mLayoutManager);

        categories = app.getDb().databaseDao().getCategories();
        adapter = new CategoryAdapter(getContext(),this::onItemClick, categories);
        catgoriesList.setAdapter(adapter);}

    @OnClick(R.id.fab_add_category)
    public void addCategory(){
        Intent startAddLoan =  new Intent(getActivity(), AddCategoryActivity.class);
        startActivityForResult(startAddLoan, START_ACT_ADD_CODE);

        //
}
    @Override
    public void onResume() {
        super.onResume();

        categories = app.getDb().databaseDao().getCategories();
        adapter = new CategoryAdapter(getContext(),this::onItemClick, categories);
        catgoriesList.setAdapter(adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case START_ACT_ADD_CODE:
                    Snackbar.make(mRootView, R.string.add_cat_suc, Snackbar.LENGTH_SHORT).show();
                    break;
                case START_ACT_EDIT_CODE:
                    break;
            }
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
