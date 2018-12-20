package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import pt.uc.dei.cm.myfinances.SharedPreferencesHelper;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.local_backup_auto) Switch localBackupAuto;
    @BindView(R.id.drive_backup_auto) Switch driveBackupAuto;
    @BindView(R.id.show_percentages) Switch showPercentages;
    @BindView(R.id.show_subtitles) Switch showSubtitles;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(SharedPreferencesHelper.SHARED_PREFS, MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SharedPreferencesHelper.LOCAL_BACKUP, localBackupAuto.isChecked());
        editor.putBoolean(SharedPreferencesHelper.DRIVE_BACKUP, driveBackupAuto.isChecked());
        editor.putBoolean(SharedPreferencesHelper.SHOW_PERCENTAGES, showPercentages.isChecked());
        editor.putBoolean(SharedPreferencesHelper.SHOW_SUBTITLES, showSubtitles.isChecked());
    }
}
