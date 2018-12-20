package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static pt.uc.dei.cm.myfinances.MyFinancesApplication.DATABASE_NAME;

@RuntimePermissions
public class BackupOrRestoreActivity extends AppCompatActivity {

    @BindView(R.id.activity_backup_or_restore) View mRootView;
    @BindView(R.id.btnConnectGoogleDrive) Button btnConnectDrive;
    @BindView(R.id.btnLocalBackup) Button btnLocalBackup;
    @BindView(R.id.btnRestoreFile) Button btnRestoreFile;
    @BindView(R.id.checkboxBackup) CheckBox checkBoxBackup;

    private MyFinancesApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_or_restore);
        ButterKnife.bind(this);

        app = (MyFinancesApplication) getApplicationContext();
    }


    @OnClick(R.id.btnConnectGoogleDrive)
    public void connectToGoogleDrive(){
        BackupOrRestoreActivityPermissionsDispatcher.startDriveActivityWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.GET_ACCOUNTS})
    public void startDriveActivity(){
        startActivity(new Intent(this, DriveActivity.class));
    }


    @OnClick(R.id.btnLocalBackup)
    public void localBackup(){
        app.closeDB();

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.alertDialogBackup))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    BackupOrRestoreActivityPermissionsDispatcher.doBackUpWithPermissionCheck(this);
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();

        app.openDB();
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void doBackUp(){
        //gets Database path
        String dbPath = getDatabasePath(DATABASE_NAME).getAbsolutePath();

        //gets Internal Storage path
        String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        String backupPath = internalPath + "/MyFinances/LocalBackup";
        String backupDBPath = backupPath + "/MyFinances.db";

        File backup = new File(backupPath);
        File currentDB = new File(dbPath);

        File backupBD = new File(backupDBPath);
        if(!backup.exists()){
            backup.mkdir();
        }

        try {
            FileUtils.copyFile(currentDB, backupBD);
            showSnackbar(R.string.backup_success);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btnRestoreFile)
    public void restoreFromFile(){
        app.closeDB();

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.alertDialogRestore))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    if(verifyFile()){
                        BackupOrRestoreActivityPermissionsDispatcher.doRestoreWithPermissionCheck(this);
                        dialog.dismiss();
                    }
                    else{
                        showSnackbar(R.string.file_not_found);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();

        app.openDB();
    }


    public boolean verifyFile(){
        //gets the file to restore path
        String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileToRestore = internalPath + "/MyFinancesBackup/MyFinances.db";
        File file = new File(fileToRestore);

        return file.exists() ? true : false;
    }


    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void doRestore(){
        //gets Database path
        String dbPath = getDatabasePath(DATABASE_NAME).getAbsolutePath();

        //gets the file to restore path
        String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileToRestore = internalPath + "/MyFinances/LocalBackup/MyFinances.db";
        File fileRestore = new File(fileToRestore);

        //delete current DB file
        File dbFile = new File(dbPath);
        dbFile.delete();

        //copy the backup file to the DB directory
        try {
            FileUtils.copyFile(fileRestore, dbFile);
            showSnackbar(R.string.restore_success);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForPersmissions(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed if you want to do a local backup/restore.")
                .setPositiveButton("Ok", (dialog, which) -> request.proceed())
                .setNegativeButton("Cancel", (dialog, which) -> request.cancel())
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BackupOrRestoreActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }


    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
