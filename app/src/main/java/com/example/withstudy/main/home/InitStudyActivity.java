package com.example.withstudy.main.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.withstudy.R;
import com.google.android.material.textfield.TextInputEditText;

public class InitStudyActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_study);

        initialize();
    }

    private void initialize() {
        Button backFromInitStudyBtn, makeStudyBtn;

        backFromInitStudyBtn = (Button)findViewById(R.id.backFromInitStudyBtn);
        makeStudyBtn = (Button)findViewById(R.id.makeStudyBtn);

        // Click Listener 추가
        backFromInitStudyBtn.setOnClickListener(this);
        makeStudyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.backFromInitStudyBtn: // 뒤로가기 버튼
                onBackPressed();

                break;

            case R.id.makeStudyBtn: // 시작하기 버튼
                TextInputEditText studyNameText;

                // 모임명을 입력했는지 확인
                studyNameText = (TextInputEditText)findViewById(R.id.studyName);

                // 기본 입력 메세지나 첫글자가 공백이면 다시 입력하게 하기
                if(studyNameText.getText().equals(R.string.InputStudyNameText) || studyNameText.getText().charAt(0) == ' ') {
                    Toast.makeText(getApplicationContext(), "모임명을 입력해주세요", Toast.LENGTH_LONG).show();

                    break;
                }

                // activity_make_study 레이아웃으로 변경하기 위한 intent 설정
                Intent intent = new Intent(InitStudyActivity.this, MakeStudyActivity.class);

                // 모임명 전달
                intent.putExtra("studyName", studyNameText.getText());

                startActivity(intent);

                break;
        }
    }
}
