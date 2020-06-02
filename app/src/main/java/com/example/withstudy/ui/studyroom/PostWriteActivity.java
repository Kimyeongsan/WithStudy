package com.example.withstudy.ui.studyroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.ManagementData;
import com.example.withstudy.main.data.PostItemData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostWriteActivity extends AppCompatActivity implements View.OnClickListener {
    private String studyName;
    private String studyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        initialize();
    }

    private void initialize() {
        Intent intent;
        TextView studyNameTV;
        Button postWriteCompleteBtn;
        ImageView postWriteExitIV;

        intent = getIntent();

        studyName = intent.getStringExtra("studyName");
        studyId = intent.getStringExtra("studyId");

        studyNameTV = (TextView)findViewById(R.id.postWrite_studyName);
        postWriteCompleteBtn = (Button)findViewById(R.id.postWriteCompleteBtn);
        postWriteExitIV = (ImageView)findViewById(R.id.postWriteExit);

        studyNameTV.setText(studyName);

        // Click Listener 추가
        postWriteCompleteBtn.setOnClickListener(this);
        postWriteExitIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // X버튼
            case R.id.postWriteExit:
                finish();

                break;

            // 글쓰기 완료 버튼
            case R.id.postWriteCompleteBtn:
                // 해당 스터디의 'posts'에 게시글 추가
                DatabaseReference postRef;
                PostItemData post;
                EditText contentET;
                SimpleDateFormat dateFormat;
                Date date;

                contentET = (EditText) findViewById(R.id.postWrite_contentET);

                // 날짜 형식 설정
                dateFormat = new SimpleDateFormat("yyyy년 M월 d일");
                date = new Date();

                // 데이터베이스 참조 위치(해당 스터디방의 Post) 설정
                postRef = FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_STUDYROOM);
                postRef = postRef.child(studyId).child(Constant.DB_CHILD_POST).push();

                post = new PostItemData();

                // 작성자, 작성일, 내용 설정
                post.setWriter(ManagementData.getInstance().getUserData().getUser_Name());
                post.setDate(dateFormat.format(date));
                post.setContent(contentET.getText().toString());

                // 데이터베이스에 등록
                postRef.setValue(post);

                finish();

                break;
        }
    }
}
