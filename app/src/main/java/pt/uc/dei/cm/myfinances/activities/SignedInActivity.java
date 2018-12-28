package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.fragments.GraphsFragment;
import pt.uc.dei.cm.myfinances.fragments.HomeFragment;
import pt.uc.dei.cm.myfinances.fragments.LoanFragment;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignedInActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        LoanFragment.OnFragmentInteractionListener, GraphsFragment.OnFragmentInteractionListener{

    private static final String TAG = "SignedInActivity";

    @BindView(R.id.activity_signed_in) View mRootView;
    @BindView(R.id.navigation) BottomNavigationView navigation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        ButterKnife.bind(this);

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
            case R.id.action_bar_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.categories:
                startActivity(new Intent(this, CategoriesActivity.class));
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
