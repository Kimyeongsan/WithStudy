package com.example.withstudy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.withstudy.main.data.UserData;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    public String id, name, emailId, passwd;

    private SignInButton mSignInButton;

    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference myRef;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_sign_in);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);



        Button sign_up =findViewById(R.id.sign_up_button);
        sign_up.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View view){
                        Intent intent=new Intent(getApplicationContext(), SignUpActivity.class);
                        startActivity(intent);
                    }
                }
        );

        Button d_sign_in =findViewById(R.id.d_sign_in);
        d_sign_in.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View view){
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
        );


        Button sign_in =findViewById(R.id.login_button);
        sign_in.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View view){
                        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user =  mFirebaseAuth.getCurrentUser();
        userID = user.getUid();


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:

//                String emailID = emailId;
//                String paswd = passwd;
//                String userID = id;
//                String userName = name;
//
//                if (!userID.equals("") && !userName.equals("") && !emailId.equals("") && !passwd.equals("")) {
//
//                    UserData userInfor = new UserData(userID, userName, emailID, paswd);
//                    myRef.child("users").child(userID).setValue(userInfor);
//                    toastMessage("New Information has been saved.");
//
//                }
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //만약 users에 구글 이메일과 같은 아이디가 없다면? --> 여기서 유저 닉네임이 아니라 유저 이메일이 uid가 되는게
                        //원착상 맞겠죠?

                        //isSuccessful 일 때 파이어베이스 데이터베이스에 해당 값들을 이메일 로그인하고 똑같은 콯드를 넣어주면 되겠지?

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign-In failed
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}