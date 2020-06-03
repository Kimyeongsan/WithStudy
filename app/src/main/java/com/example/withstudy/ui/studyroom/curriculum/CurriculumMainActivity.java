package com.example.withstudy.ui.studyroom.curriculum;

import android.content.Intent;
import android.os.Bundle;

import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.ManagementData;
import com.example.withstudy.main.data.PostItemData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.withstudy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurriculumMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_curriculum_main);

        initailize();
    }

    // 필요한 항목 초기화
    private void initailize() {
        Button completeBtn;
        ImageView backIV;

        completeBtn = (Button)findViewById(R.id.curriculum_completeBtn);
        backIV = (ImageView)findViewById(R.id.backFromCurriculumIV);

        // Click Listener 추가
        completeBtn.setOnClickListener(this);
        backIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 뒤로가기
            case R.id.backFromCurriculumIV:
                finish();

                break;

            // 완료 버튼
            case R.id.curriculum_completeBtn:
                // 커리큘럼 내용 추가해야 함
                Intent intent;
                DatabaseReference curriculumRef;
                PostItemData post;
                EditText contentET;
                SimpleDateFormat dateFormat;
                Date date;
                String studyId;

                intent = getIntent();

                studyId = intent.getStringExtra("studyId");

                // 해당 스터디의 'curriculum'에 게시글 추가
                contentET = (EditText) findViewById(R.id.curriculum_ET);

                // 날짜 형식 설정
                dateFormat = new SimpleDateFormat("yyyy년 M월 d일");
                date = new Date();

                // 데이터베이스 참조 위치(해당 스터디방의 Curriculum) 설정
                curriculumRef = FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_STUDYROOM);
                curriculumRef = curriculumRef.child(studyId).child(Constant.DB_CHILD_CURRICULUM);

                post = new PostItemData();

                // 작성자, 작성일, 내용 설정
                post.setWriter(ManagementData.getInstance().getUserData().getUser_Name());
                post.setDate(dateFormat.format(date));
                post.setContent(contentET.getText().toString());

                // 데이터베이스에 등록
                curriculumRef.setValue(post);

                finish();

                break;
        }
    }
}
