package pt.uc.dei.cm.myfinances.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import pt.uc.dei.cm.myfinances.myfinances.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.uc.dei.cm.myfinances.util.ConfigurationUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.root) View mRootView;
    private boolean useGoogle = true;
    private boolean useFacebook = true;

    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Check if it's possible to login with google and facebook. at this moment facebook isn't working
        if (ConfigurationUtils.isGoogleMisconfigured(this)) {
            useGoogle = false;
            showSnackbar(R.string.configuration_required);
        }

        if(ConfigurationUtils.isFacebookMisconfigured(this)){
            useFacebook = false;
            System.out.println("---------------------------------------No to Facebook");
        }

        signIn();
    }

    //This is taken care of by FirebaseAuthUI
    /*basically it takes the providers (function addProviders()) with which you can login.
    For now it's only google or email*/
    private void signIn() {
        AuthUI.SignInIntentBuilder builder =  AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(addProviders())
                .setLogo(R.drawable.finances_logo_resized)
                .setIsSmartLockEnabled(true, true);

        startActivityForResult(builder.build(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startSignedInActivity(null);
            finish();
        }
    }


    //If all went well, we logged in, otherwise it presents an error message
    private void handleSignInResponse(int resultCode, @Nullable Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startSignedInActivity(response);
            finish();
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }

    private void startSignedInActivity(@Nullable IdpResponse response) {
        Intent intent = new Intent(this,SignedInActivity.class);
        startActivity(intent);
    }

    //Function that gets the providers with which you can login
    private List<IdpConfig> addProviders() {
        List<IdpConfig> addProviders = new ArrayList<>();

        if(useGoogle){
            addProviders.add(new IdpConfig.GoogleBuilder().build());
        }
        if(useFacebook){
            addProviders.add(new IdpConfig.FacebookBuilder().build());
        }
        addProviders.add(new IdpConfig.EmailBuilder()
                .setRequireName(true)
                .setAllowNewAccounts(true)
                .build());

        return addProviders;
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
