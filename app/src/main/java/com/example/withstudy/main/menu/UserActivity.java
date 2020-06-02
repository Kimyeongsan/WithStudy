package com.example.withstudy.main.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.withstudy.R;
import com.example.withstudy.SignInUpActivity;
import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogOut, btnSignOut;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnSignOut = findViewById(R.id.btnSignOut);

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
    }

    private void logOut() {
        firebaseAuth.getInstance().signOut();
    }

    private void signOut() {
        firebaseAuth.getCurrentUser().delete();
    }

    @Override
    // 추가 수정 예정
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogOut:
                logOut();
                Intent intent = new Intent(
                        getApplication(), SignInUpActivity.class);
                startActivity(intent);
                break;

            case R.id.btnSignOut:
                signOut();
                finishAffinity();
                break;
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}