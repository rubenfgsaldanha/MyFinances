package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import petrov.kristiyan.colorpicker.ColorPicker;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCategoryActivity extends AppCompatActivity {

    @BindView(R.id.colorButton) Button colorButton;
    @BindView(R.id.add_label) EditText label;
    @BindView(R.id.btnSaveCategory) Button btnSaveCategory;

    private MyFinancesApplication app;

    private int myColor= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ButterKnife.bind(this);

        app = (MyFinancesApplication) getApplicationContext();
    }


    @OnClick(R.id.btnSaveCategory)
    public void saveCategory(View view) {
        new InsertCategory().execute();
    }


    @OnClick(R.id.colorButton)
    public void openColor(View view) {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                myColor=color;
            }

            @Override
            public void onCancel(){
                // put code
            }
        });
    }

    private class InsertCategory extends AsyncTask<Void, Void, Void>{
        String myLabel;
        Categories category;
        @Override
        protected void onPreExecute() {
            myLabel = label.getText().toString();
            category = new Categories(myLabel,myColor);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            app.getDb().databaseDao().insertinit(category);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(RESULT_OK);
            finish();
        }
    }
}

