package nz.co.k2.k2e.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import nz.co.k2.k2e.R;
import nz.co.k2.k2e.ui.navdrawer.NavDrawerActivity;

public class GoogleLoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = GoogleLoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
//    private ProgressDialog mProgressDialog;
    private GoogleSignInClient mGoogleSignInClient;
//    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_googlelogin);
        setContentView(R.layout.activity_splash);
        // Configure sign-in to request the User's ID, email address and basic profile
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified in the gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (extras.getBoolean("logout")) {
                signOut(R.string.signout_message);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in
        // Check for existing Google Sign In Account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Do check email is registered with K2
            // Signed in successfully, show NavDrawer
            updateUI(account, 0);
        } catch (ApiException e){
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null, 0);
        }
    }

    private void signIn() {
        Log.d("BenD","signIn");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(int message){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        GoogleLoginActivity.this.updateUI(null, message);
                    }
                });
    }

    private void updateUI(@Nullable GoogleSignInAccount account, int message) {
        if (account != null) {
            Log.d("BenD","Account is not null");
            // Check user is registered with K2
            // TODO Replace dummy function with proper list
            if (account.getEmail().equals("thebillham@gmail.com")) {
                Log.d("BenD","Account is on the list");
                // Todo Put completable here that will load the users preferences and jobs
                Intent intent = new Intent(this, NavDrawerActivity.class);
                intent.putExtra("email",account.getEmail());
                // If logged in, start downloading MyJobs, not WFM jobs yet
                startActivity(intent);

            } else {
                Log.d("BenD","Account is not on the list (" + account.getEmail() + ")");
                signOut(R.string.unregistered_message);
            }
        } else {
            Log.d("BenD","Account is null");
            // No valid user logged in, set up the google sign in view
            setContentView(R.layout.activity_googlelogin);

            if (message != 0) {
                Snackbar.make(findViewById(R.id.googleLoginInCoordinatorLayout), message, Snackbar.LENGTH_LONG).show();
//                Snackbar snack = new Snackbar.make(findViewById(R.id.googleLoginInCoordinatorLayout), message, Snackbar.LENGTH_LONG);
//                TextView view = snack.getView().findViewById(android.support.design.R.id.snackbar_text);
//                view.setTextColor(getResources().getColor(R.color.white));
//                snack.show();
            }

            findViewById(R.id.btn_sign_in).setOnClickListener(this);
            // Set sign-in button dimensions
            SignInButton signInButton = findViewById(R.id.btn_sign_in);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("BenD","onClick");
        signIn();
    }
}