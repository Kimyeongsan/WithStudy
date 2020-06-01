package com.example.withstudy;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.StudyData;
import com.example.withstudy.main.data.UserData;
import com.example.withstudy.main.home.InitStudyActivity;
import com.example.withstudy.main.home.MakeStudyActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth firebaseAuth = null;
    private GoogleSignInClient googleSignInClient;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_up);

        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        Button signUpBtn, nonMemberBtn, signInBtn;
        SignInButton googleSignInBtn;
        GoogleSignInOptions gsio;

        signUpBtn = (Button)findViewById(R.id.signUpBtn);
        nonMemberBtn = (Button)findViewById(R.id.nonMemberBtn);
        signInBtn = (Button)findViewById(R.id.signInBtn);
        googleSignInBtn = (SignInButton)findViewById(R.id.googleSignInBtn);

        ////////////////////////////////////////////////////////////////////////////
        // 파이어베이스 및 구글 관련 설정
        // 파이어베이스 Auth 객체 설정
        firebaseAuth = FirebaseAuth.getInstance();

        // 구글 로그인 옵션 설정 및 클라이언트 받아오기
        gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gsio);
        ////////////////////////////////////////////////////////////////////////////

        // Click Listener 추가
        signUpBtn.setOnClickListener(this);
        nonMemberBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        googleSignInBtn.setOnClickListener(this);
    }

    // 구글 로그인 실행
    private void googleSignIn() {
        Intent intent;

        intent = googleSignInClient.getSignInIntent();

        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            // 회원가입
            case R.id.signUpBtn:
                intent = new Intent(SignInUpActivity.this, SignUpActivity.class);

                startActivity(intent);

                break;

            // 비회원
            case R.id.nonMemberBtn:
                intent = new Intent(SignInUpActivity.this, MainActivity.class);

                startActivity(intent);

                break;

            // 로그인
            case R.id.signInBtn:
                intent = new Intent(SignInUpActivity.this, SignInActivity.class);

                startActivity(intent);

                break;

            // 구글로그인
            case R.id.googleSignInBtn:
                googleSignIn();

                break;
        }
    }

    private void updateUI(FirebaseUser user) {
        // 유저 정보가 존재할 때만
        if(user != null) {
            Intent intent;

            intent = new Intent(SignInUpActivity.this, MainActivity.class);

            startActivity(intent);

            finish();
        }
    }

    // 유저 정보를 디비에 등록
    private void insertUserToDatabase() {
        DatabaseReference userRef;

        userRef = FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_USER).push();

        // 디비에 유저 생성
        userRef.setValue(userData);
    }

    // 파이어베이스에 구글계정 등록
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential;

        credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // 성공적으로 등록 했으면
                if(task.isSuccessful()) {
                    FirebaseUser user;

                    // 현재 User 가져오기
                    user = firebaseAuth.getCurrentUser();

                    // 이미 DB에 존재하는 유저면 화면만 넘기기
                    FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_USER).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                // Uid를 비교해서 같으면 디비에 등록X
                                if (data.getValue(UserData.class).getUser_Id().equals(user.getUid())) {
                                    return;
                                }
                            }

                            // 유저 정보 생성
                            userData = new UserData(user.getUid(), user.getDisplayName(), user.getEmail(), null);

                            // DB에 유저 정보 등록
                            insertUserToDatabase();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    // UI 업데이트(화면 넘기기)
                    updateUI(user);
                } else {
                    // 실패시
                    updateUI(null);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 로그인 요청이었다면
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task;

            // 로그인 계정 받아오기
            task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // 여기가 실행되면 구글 로그인이 성공적
                GoogleSignInAccount account;

                account = task.getResult(ApiException.class);

                // 파이어베이스에 등록
                firebaseAuthWithGoogle(account);
            } catch(ApiException e) {
                e.printStackTrace();
            }
        }
    }
}