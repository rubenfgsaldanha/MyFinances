package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import petrov.kristiyan.colorpicker.ColorPicker;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditCategoryactivity extends AppCompatActivity {

    @BindView(R.id.colorButton) Button colorButton;
    @BindView(R.id.add_label) EditText label;
    @BindView(R.id.btnSaveEditCategory) Button btnSaveEditCategory;
    @BindView(R.id.btnDeleteCategory) Button btnDeleteCategory;

    private MyFinancesApplication app;
    Categories c;
    int newColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_categoryactivity);
        ButterKnife.bind(this);


        app = (MyFinancesApplication) getApplicationContext();

        //gets loan id
        Intent intent = getIntent();
        String labelCat = intent.getStringExtra("label");

        //sets the date to the transaction date
        c = app.getDb().databaseDao().getCategoryByName(labelCat);
        colorButton.setBackgroundColor(c.getColor());
        label.setText(c.getLabel());
        newColor=c.getColor();
    }


    @OnClick(R.id.colorButton)
    public void editColor(View view) {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {

                newColor=color;
            }

            @Override
            public void onCancel(){
                // put code
            }
        });
    }


    @OnClick(R.id.btnSaveEditCategory)
    public void saveCategory(View view) {
        String myLabel=label.getText().toString();
        app.getDb().databaseDao().updateCat(myLabel,newColor, c.getCatId());
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.btnDeleteCategory)
    public void deleteCategory(){
        String myLabel=label.getText().toString();
        app.getDb().databaseDao().deleteCategory(myLabel);
        finish();
    }
}
