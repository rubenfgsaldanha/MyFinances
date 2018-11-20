package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import pt.uc.dei.cm.myfinances.fragments.CategoriesFragment;
import pt.uc.dei.cm.myfinances.fragments.HomeFragment;
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
        CategoriesFragment.OnFragmentInteractionListener{

    private static final String TAG = "SignedInActivity";

    @BindView(R.id.activity_signed_in) View mRootView;
    @BindView(R.id.navigation) BottomNavigationView navigation;

    private FirebaseUser currentUser;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.drive_sync:
                Toast.makeText(getApplicationContext(),"Drive sync",Toast.LENGTH_SHORT).show();
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
            case R.id.action_log_off:
                askSignOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                home();
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_categories:
                categories();
                Toast.makeText(getApplicationContext(), "Categories", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_graphics:
                Toast.makeText(getApplicationContext(), "Graphics", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    };


    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

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


    private void home(){
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_signed_in,homeFragment);
        ft.addToBackStack("ToHome");
        ft.commit();
    }

    private void categories(){
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_signed_in,categoriesFragment);
        ft.addToBackStack("ToCategories");
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri){}
}