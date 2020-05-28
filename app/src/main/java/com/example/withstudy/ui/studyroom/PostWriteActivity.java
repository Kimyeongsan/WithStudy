package com.example.withstudy.ui.studyroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.PostItemData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostWriteActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        initialize();
    }

    private void initialize() {
        Intent intent;
        TextView studyNameTV;
        String studyName;

        intent = getIntent();

        studyName = intent.getStringExtra("studyName");

        studyNameTV = findViewById(R.id.postWrite_studyName);

        studyNameTV.setText(studyName);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // X버튼
            case R.id.postWriteExit:
                break;

            // 글쓰기 완료 버튼
            case R.id.postWriteCompleteBtn:
                DatabaseReference ref;
                PostItemData post;

                // 데이터베이스에 게시글 추가
                ref = FirebaseDatabase.getInstance().getReference();

                //post = new PostItemData();
                //post.setWriter()

                //ref.child(Constant.DB_CHILD_STUDYROOM).push().setValue(studyRoom);

                onBackPressed();

                break;
        }
    }
}
