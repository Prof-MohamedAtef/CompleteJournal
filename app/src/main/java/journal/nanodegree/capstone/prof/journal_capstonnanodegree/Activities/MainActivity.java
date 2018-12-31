package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;

/**
 * Created by Prof-Mohamed Atef on 12/31/2018.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SignInButton Gbtn_sign_in;
    GoogleSignInAccount GAccessToken;
    SessionManagement sessionManagement;
    GoogleApiClient mGoogleApiClient;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    String Google_personName;
    String Google_personPhotoUrl;
    String Google_email, Google_AccessToken,GoogleUserID;
    Uri PhotoUri;
    GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 007;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();
        sessionManagement=new SessionManagement(getApplicationContext());
        if (isConnected()){
            mAuth=FirebaseAuth.getInstance();
            mAuthListener=new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    if (user!=null){
                        //signed In
                        Toast.makeText(getApplicationContext(), "Successful Sign In", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    }else {
                        Log.d(LOG_TAG, "onAuthStateChanged:signed_out:" );
                    }
                }
            };
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            Gbtn_sign_in=(SignInButton)findViewById(R.id.btn_sign_in);
            Gbtn_sign_in.setSize(SignInButton.SIZE_STANDARD);
            Gbtn_sign_in.setScopes(gso.getScopeArray());
            Gbtn_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        }else if(!isConnected()){
            Toast.makeText(getApplicationContext(), "Connection Disabled", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isInternetConnected;

    private boolean checkConnection() {
        return isInternetConnected=isConnected();
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); //NetworkApplication.getInstance().getApplicationContext()/
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork!=null){
            return isInternetConnected= activeNetwork.isConnected();
        }else
            return isInternetConnected=false;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GAccessToken = result.getSignInAccount();
            if (GAccessToken!=null){
                Google_personName= GAccessToken .getDisplayName();
                if (GAccessToken.getPhotoUrl() != null) {
                    Google_personPhotoUrl = GAccessToken.getPhotoUrl().toString();
                }
                Google_email = GAccessToken .getEmail();
                Google_AccessToken=GAccessToken.getIdToken();
                GoogleUserID= GAccessToken.getId();
                SharedPrefAndDiaryEntryRedirectGoogleDetails();
            }
        } else {
        }
    }

    private void SharedPrefAndDiaryEntryRedirectGoogleDetails() {
        sessionManagement.createLoginSession(Google_personName,Google_email,Google_personPhotoUrl,Google_AccessToken, GoogleUserID);
        sessionManagement.createLoginSessionType("G");
        Intent intent_create=new Intent(this,MainActivity.class);
        startActivity(intent_create);
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isConnected()){
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GAccessToken= result.getSignInAccount();
                    GoogleSignInResult result1=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result1);
                }
            }
        }else  if (!isConnected()){
            Toast.makeText(getApplicationContext(), "Connection Disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isConnected()){
            if (mGoogleApiClient!=null){
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
//             If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//             and the GoogleSignInResult will be available instantly.
                    Log.d(LOG_TAG, "Got cached sign-in");
                    GoogleSignInResult result = opr.get();
                    handleSignInResult(result);
                } else {
//             If the user has not previously signed in on this device or the sign-in has expired,
//             this asynchronous branch will attempt to sign in the user silently.  Cross-device
                    // single sign-on will occur in this branch.
                    showProgressDialog();
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            hideProgressDialog();
                            handleSignInResult(googleSignInResult);
                        }
                    });
                }
            }

        }else if (isInternetConnected==false){
            Toast.makeText(getApplicationContext(), "Connection Disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
