package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import pt.uc.dei.cm.myfinances.SharedPreferencesHelper;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.spinner_currency) Spinner currencies;
    @BindView(R.id.show_percentages) Switch showPercentages;
    @BindView(R.id.show_subtitles) Switch showSubtitles;

    private ArrayAdapter<CharSequence> adapter;
    private String currencySymbol;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        //create a spinner with the categories defined in the categories.xml file
        adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencies.setAdapter(adapter);

        sharedPreferences = getSharedPreferences(SharedPreferencesHelper.SHARED_PREFS, MODE_PRIVATE);

        showPercentages.setChecked(sharedPreferences.getBoolean(SharedPreferencesHelper.SHOW_PERCENTAGES, false));
        showSubtitles.setChecked(sharedPreferences.getBoolean(SharedPreferencesHelper.SHOW_SUBTITLES, false));

        String aux = sharedPreferences.getString(SharedPreferencesHelper.CURRENCY, null);
        //a currency was select before
        if(aux != null){
            if(aux.equals("€")){
                //
                currencies.setSelection(1);
            }
            else if(aux.equals("$")){
                currencies.setSelection(2);
            }
            else{
                // £
                currencies.setSelection(3);
            }
        }
    }

    @OnItemSelected(R.id.spinner_currency)
    public void selectCurrency(AdapterView<?> parent, View view, final int position, long id) {
        String currency = adapter.getItem(position).toString();
        if(!currency.equals("No currency selected")){
            char[] aux = currency.toCharArray();
            currencySymbol = ""+aux[aux.length - 2];
        }
        else{
            currencySymbol = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SharedPreferencesHelper.SHOW_PERCENTAGES, showPercentages.isChecked());
        editor.putBoolean(SharedPreferencesHelper.SHOW_SUBTITLES, showSubtitles.isChecked());
        editor.putString(SharedPreferencesHelper.CURRENCY, currencySymbol);
        editor.apply();
    }
}
