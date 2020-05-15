package com.example.withstudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText phone_join;
    private EditText pwd_join;
    private EditText user_name;
    private EditText user_date;
    private Button btn;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        phone_join = (EditText) findViewById(R.id.sign_up_phone);
        pwd_join = (EditText) findViewById(R.id.sign_up_pwd);
//        user_name = (EditText) findViewById(R.id.sign_up_name);
//        user_date = (EditText) findViewById(R.id.sign_up_date);
        btn = (Button) findViewById(R.id.sign_up_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = phone_join.getText().toString().trim();
                String pwd = pwd_join.getText().toString().trim();
//                String name = user_name.getText().toString().trim();
//                String date = user_date.getText().toString().trim();

//                switch(v.getId()) {
//                    case R.id.sign_up_delate: // 뒤로가기 버튼
//                        onBackPressed();
//
//                        break;
//                }

                firebaseAuth.createUserWithEmailAndPassword(phone_number, pwd)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }

        });


    }



}