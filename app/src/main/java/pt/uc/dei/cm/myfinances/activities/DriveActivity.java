package pt.uc.dei.cm.myfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

public class DriveActivity extends AppCompatActivity {

    @BindView(R.id.checkboxBackupDrive) CheckBox backupAutomatically;
    @BindView(R.id.btnDriveBackup) Button btnDriveBackup;
    @BindView(R.id.btnRestoreDrive) Button btnDriveRestore;

    private MyFinancesApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        ButterKnife.bind(this);

        app = (MyFinancesApplication) getApplicationContext();
    }


    @OnClick(R.id.btnDriveBackup)
    public void driveBackup(){
        //
    }


    @OnClick(R.id.btnRestoreDrive)
    public void restoreDrive(){
        //
    }
}
