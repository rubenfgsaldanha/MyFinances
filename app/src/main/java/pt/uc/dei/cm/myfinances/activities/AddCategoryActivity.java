package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import petrov.kristiyan.colorpicker.ColorPicker;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.myfinances.R;
//import top.defaults.colorpicker.ColorPickerPopup;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/*import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;*/

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
        String myLabel=label.getText().toString();
        Categories category= new Categories(myLabel,myColor);
        app.getDb().databaseDao().insertinit(category);
        setResult(RESULT_OK);
        finish();
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
}

