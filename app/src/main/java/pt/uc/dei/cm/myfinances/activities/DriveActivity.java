package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.google.drive.REST;
import pt.uc.dei.cm.myfinances.google.drive.UT;
import pt.uc.dei.cm.myfinances.myfinances.R;
import pt.uc.dei.cm.myfinances.util.MimeUtils;

import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import java.io.File;
import java.util.ArrayList;

import static pt.uc.dei.cm.myfinances.MyFinancesApplication.DATABASE_NAME;

public class DriveActivity extends AppCompatActivity implements REST.ConnectCBs {
    private static final int REQ_ACCPICK = 1000;
    private static final int REQ_CONNECT = 1001;

    private static boolean mBusy;

    @BindView(R.id.activity_drive) View mRootView;
    @BindView(R.id.checkboxBackupDrive) CheckBox backupAutomatically;
    @BindView(R.id.btnDriveBackup) Button btnDriveBackup;
    @BindView(R.id.btnRestoreDrive) Button btnDriveRestore;
    @BindView(R.id.connected_user) TextView connectedUser;

    private MyFinancesApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        ButterKnife.bind(this);

        app = (MyFinancesApplication) getApplicationContext();

        if (savedInstanceState == null) {
            UT.init(this);
            if (!REST.init(this)) {
                startActivityForResult(AccountPicker.newChooseAccountIntent(null,
                        null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null),
                        REQ_ACCPICK);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        REST.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        REST.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_account: {
                connectedUser.setText("");
                startActivityForResult(AccountPicker.newChooseAccountIntent(
                        null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},
                        true, null, null, null, null), REQ_ACCPICK);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        switch (request) {
            case REQ_CONNECT:
                if (result == RESULT_OK)
                    REST.connect();
                else {
                    UT.lg("act result - NO AUTH");
                    suicide(R.string.err_auth_nogo);  //---------------------------------->>>
                }
                break;
            case REQ_ACCPICK:
                if (data != null && data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME) != null)
                    UT.AM.setEmail(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                if (!REST.init(this)) {
                    UT.lg("act result - NO ACCOUNT");
                    suicide(R.string.err_auth_accpick); //---------------------------------->>>
                }
                break;
        }
        super.onActivityResult(request, result, data);
    }

    @Override
    public void onConnOK() {
        connectedUser.setText("Account: "+UT.AM.getEmail());
    }

    @Override
    public void onConnFail(Exception ex) {
        if (ex == null) {
            UT.lg("connFail - UNSPECD 1");
            suicide(R.string.err_auth_dono);
            return;  //---------------------------------->>>
        }
        if (ex instanceof UserRecoverableAuthIOException) {
            UT.lg("connFail - has res");
            startActivityForResult((((UserRecoverableAuthIOException) ex).getIntent()), REQ_CONNECT);
        } else if (ex instanceof GoogleAuthIOException) {
            UT.lg("connFail - SHA1?");
            if (ex.getMessage() != null) suicide(ex.getMessage());  //--------------------->>>
            else suicide(R.string.err_auth_sha);  //---------------------------------->>>
        } else {
            UT.lg("connFail - UNSPECD 2");
            suicide(R.string.err_auth_dono);  //---------------------------------->>>
        }
    }


    @OnClick(R.id.btnDriveBackup)
    public void driveBackup(){
        //startActivity(new Intent(this,MainActivity.class));
        createTree(DATABASE_NAME);
    }


    @OnClick(R.id.btnRestoreDrive)
    public void restoreDrive(){
        Toast.makeText(this,"Drive Restore",Toast.LENGTH_SHORT).show();
    }


    private void createTree(final String titl) {
        if (titl != null && !mBusy) {

            new AsyncTask<Void, String, Void>() {
                private String findOrCreateFolder(String prnt, String titl) {
                    ArrayList<ContentValues> cvs = REST.search(prnt, titl, UT.MIME_FLDR);
                    String id, txt;
                    if (cvs.size() > 0) {
                        txt = "found ";
                        id = cvs.get(0).getAsString(UT.GDID);
                    } else {
                        id = REST.createFolder(prnt, titl);
                        txt = "created ";
                    }
                    if (id != null)
                        txt += titl;
                    else
                        txt = "failed " + titl;
                    publishProgress(txt);
                    return id;
                }

                @Override
                protected Void doInBackground(Void... params) {
                    mBusy = true;
                    String rsid = findOrCreateFolder("root", UT.MYROOT);
                    if (rsid != null) {
                        rsid = findOrCreateFolder(rsid, "Backups");
                        if (rsid != null) {
                            //File fl = UT.str2File("This is a teste", "tmp");
                            File fl = new File(getDatabasePath(DATABASE_NAME).getAbsolutePath());
                            String id = null;
                            if (fl != null) {
                                String mime = MimeUtils.guessMimeTypeFromExtension("odb");
                                id = REST.createFile(rsid, titl, mime, fl);
                                fl.delete();
                            }
                            if (id != null)
                                publishProgress("created " + titl);
                            else
                                publishProgress("failed " + titl);
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void nada) {
                    super.onPostExecute(nada);
                    mBusy = false;
                    showSnackbar(R.string.backup_drive_success);
                }
            }.execute();
        }
    }


    private void suicide(int rid) {
        UT.AM.setEmail(null);
        Toast.makeText(this, rid, Toast.LENGTH_LONG).show();
        finish();
    }


    private void suicide(String msg) {
        UT.AM.setEmail(null);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        finish();
    }


    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
