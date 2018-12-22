package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import pt.uc.dei.cm.myfinances.SharedPreferencesHelper;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch localBackupAuto;
    private Switch driveBackupAuto;
    private Switch showPercentages;
    private Switch showSubtitles;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        localBackupAuto = findViewById(R.id.local_backup_auto);
        driveBackupAuto = findViewById(R.id.drive_backup_auto);
        showPercentages = findViewById(R.id.show_percentages);
        showSubtitles = findViewById(R.id.show_subtitles);

        sharedPreferences = getSharedPreferences(SharedPreferencesHelper.SHARED_PREFS, MODE_PRIVATE);

        localBackupAuto.setChecked(sharedPreferences.getBoolean(SharedPreferencesHelper.LOCAL_BACKUP,false));
        driveBackupAuto.setChecked(sharedPreferences.getBoolean(SharedPreferencesHelper.DRIVE_BACKUP, false));
        showPercentages.setChecked(sharedPreferences.getBoolean(SharedPreferencesHelper.SHOW_PERCENTAGES, false));
        showSubtitles.setChecked(sharedPreferences.getBoolean(SharedPreferencesHelper.SHOW_SUBTITLES, false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SharedPreferencesHelper.LOCAL_BACKUP, localBackupAuto.isChecked());
        editor.putBoolean(SharedPreferencesHelper.DRIVE_BACKUP, driveBackupAuto.isChecked());
        editor.putBoolean(SharedPreferencesHelper.SHOW_PERCENTAGES, showPercentages.isChecked());
        editor.putBoolean(SharedPreferencesHelper.SHOW_SUBTITLES, showSubtitles.isChecked());
        editor.apply();
    }
}
