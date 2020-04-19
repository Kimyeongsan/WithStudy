package com.example.withstudy.main.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.withstudy.R;

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
                // activity_make_study 레이아웃으로 변경
                Intent intent = new Intent(InitStudyActivity.this, MakeStudyActivity.class);

                startActivity(intent);

                break;
        }
    }
}
