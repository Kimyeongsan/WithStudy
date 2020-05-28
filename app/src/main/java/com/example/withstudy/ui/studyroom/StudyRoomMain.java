package com.example.withstudy.ui.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.StudyData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudyRoomMain extends AppCompatActivity implements View.OnClickListener {
    private StudyData studyData;
    private String studyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_room_main);

        initialize();
    }

    private void initialize() {
        DatabaseReference db;
        Intent intent;
        TextView studyNameTV, visibleTV, memberCountTV;

        ///////////////////////////////////
        // TextView 가져오기
        studyNameTV = findViewById(R.id.studyRoom_studyNameTV);
        visibleTV = findViewById(R.id.studyRoom_visibleTV);
        memberCountTV = findViewById(R.id.studyRoom_memberCountTV);
        ///////////////////////////////////

        intent = getIntent();

        studyName = intent.getStringExtra("studyName");

        db = FirebaseDatabase.getInstance().getReference();
        db.child(Constant.DB_CHILD_STUDYROOM).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Database에서 해당 스터디 찾기
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    // 찾았으면 스터디 정보 저장
                    if (studyName.equals(ds.getValue(StudyData.class).getStudyName())) {
                        studyData = ds.getValue(StudyData.class);

                        break;
                    }
                }

                // 찾았으면 정보 설정
                if(studyData != null) {
                    String visible = "";

                    studyNameTV.setText(studyData.getStudyName());

                    switch(studyData.isVisible()) {
                        case Constant.VISIBLE:
                            visible = "공개";

                            break;

                        case Constant.NAMEVISIBLE:
                            visible = "모임명 공개";

                            break;

                        case Constant.INVISIBLE:
                            visible = "비공개";

                            break;
                    }

                    visibleTV.setText(visible);
                    memberCountTV.setText(Integer.toString(studyData.getMemberCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            // 뒤로가기
            case R.id.backFromStudyRoomMain:
                break;

            // 글쓰기
            case R.id.writePostBtn:
                break;
        }
    }
}
