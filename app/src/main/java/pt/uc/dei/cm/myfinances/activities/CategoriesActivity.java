package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.adapters.CategoryAdapter;
import pt.uc.dei.cm.myfinances.adapters.WalletAdapter;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private static final int STARTACT_CODE = 3001;
    private static final int START_ACT_EDIT_CODE = 3002;

    @BindView(R.id.activity_categories) View mRootView;
    @BindView(R.id.fab_add_category) FloatingActionButton fabAddCategory;
    @BindView(R.id.recycle_view_categories) RecyclerView categoriesList;

    private MyFinancesApplication app;

    private CategoryAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ButterKnife.bind(this);

        app = (MyFinancesApplication) getApplicationContext();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        categoriesList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        categoriesList.setLayoutManager(mLayoutManager);
    }


    @Override
    protected void onResume() {
        super.onResume();

        List<Categories> categories = app.getDb().databaseDao().getCategories();

        // specify an adapter (see also next example)
        adapter = new CategoryAdapter(getApplicationContext(),this::onItemClick,categories);
        categoriesList.setAdapter(adapter);
    }

    //starts Activity to add a wallet
    @OnClick(R.id.fab_add_category)
    public void addCategory(){
        Intent startCategory = new Intent(this,AddCategoryActivity.class);
        startActivityForResult(startCategory,STARTACT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == STARTACT_CODE){
                Snackbar.make(mRootView, R.string.add_cat_suc,Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView t = view.findViewById(R.id.category_item_text);

        Intent editCategory = new Intent(this, EditCategoryactivity.class);
        editCategory.putExtra("label",t.getText().toString());
        startActivityForResult(editCategory,START_ACT_EDIT_CODE);
    }
}
