package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.fragments.CategoriesFragment;
import pt.uc.dei.cm.myfinances.fragments.GraphsFragment;
import pt.uc.dei.cm.myfinances.fragments.HomeFragment;
import pt.uc.dei.cm.myfinances.fragments.LoanFragment;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignedInActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        CategoriesFragment.OnFragmentInteractionListener, GraphsFragment.OnFragmentInteractionListener{

    private static final String TAG = "SignedInActivity";

    @BindView(R.id.activity_signed_in) View mRootView;
    @BindView(R.id.navigation) BottomNavigationView navigation;

    private FirebaseUser currentUser;

    //Only used to send back to LoginActivity
    @NonNull
    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, SignedInActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        ButterKnife.bind(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //put the first fragment
        Fragment aux = getSupportFragmentManager().findFragmentById(R.id.activity_signed_in);

        if(aux == null){
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_signed_in, homeFragment,"HomeFragment").commit();
        }
        else{
            System.out.println("Fragment exists");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu,menu);

        //Our super action bar also has a new icon
        ActionBar ab = getSupportActionBar();

        //change the app icon in the app bar
        ab.setHomeAsUpIndicator(R.drawable.ic_financeslogo);

        //Show the icon - selecting "home" returns a single level
        ab.setDisplayHomeAsUpEnabled(true);

        //ab.setTitle("ViewPager Example");
        return super.onCreateOptionsMenu(menu);
    }

    //options for action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.drive_sync:
                startActivity(new Intent(this, BackupOrRestoreActivity.class));
                return true;
            case R.id.wallets:
                Wallets();
                return true;
            case R.id.action_bar_export:
                Toast.makeText(getApplicationContext(),"Export",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_bar_about:
                Toast.makeText(getApplicationContext(),"About",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.categories:
                categories();
            case R.id.action_log_off:
                askSignOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //starts wallets activity
    private void Wallets(){
        Intent startWalletsActivity =  new Intent(this, WalletsActivity.class);
        startActivity(startWalletsActivity);
    }

    //alert dialog to ask if user wants to log out
    private void askSignOut(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        signOut();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    //log the user out
    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                startActivity(LoginActivity.createIntent(SignedInActivity.this));
                                finish();
                            } else {
                                Log.w(TAG, "signOut:failure", task.getException());
                                showSnackbar(R.string.sign_out_failed);
                            }
                        }
                );
    }


    //options for bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                home();
                return true;
            case R.id.navigation_graphs:
                graphs();
                return true;
            case R.id.navigation_loans:
                loans();
                return true;
        }
        return false;
    };

    //fragment home
    private void home(){
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_signed_in,homeFragment);
        ft.addToBackStack("ToHome");
        ft.commit();
    }

    //fragment graph
    private void graphs(){
        GraphsFragment graphs = new GraphsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_signed_in,graphs);
        ft.addToBackStack("ToGraphs");
        ft.commit();
    }

    //fragment categories
    private void categories(){
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_signed_in,categoriesFragment);
        ft.addToBackStack("ToCategories");
        ft.commit();
    }
    // fragment loans
    private  void loans(){
        LoanFragment loanFragment = new LoanFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_signed_in,loanFragment);
        ft.addToBackStack("ToHome");
        ft.commit();
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onFragmentInteraction(Uri uri){}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        MyFinancesApplication app = (MyFinancesApplication) getApplicationContext();
        app.closeDB();
    }
}
