package com.example.withstudy.ui.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.PostItemData;
import com.example.withstudy.main.data.StudyData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudyRoomMain extends AppCompatActivity implements View.OnClickListener {
    private PostItemRVAdapter postRVAdapter;
    private StudyData studyData;
    private String studyName;
    private String studyId; // 스터디 고유값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_room_main);

        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 고유 값 찾은 다음부터
        if(studyId != null) {
            refresh();
        }
    }

    private void refresh() {
        // 어댑터의 모든 item 항목 삭제후 변환된 걸 알리기
        postRVAdapter.delAllItem();
        postRVAdapter.notifyDataSetChanged();

        // 새로 데이터 추가
        setData();
    }

    private void initialize() {
        DatabaseReference db;
        Intent intent;
        TextView studyNameTV, visibleTV, memberCountTV, ruleWriterTV;
        Button writePostBtn;
        ImageButton backFromStudyRoomMainBtn;

        //////////////////////////////////////////////////
        // Item 항목을 둘 RecyclerView 및 LinearLayout 설정
        initAllRecyclerView();
        //////////////////////////////////////////////////

        ///////////////////////////////////
        // TextView 가져오기
        studyNameTV = findViewById(R.id.studyRoom_studyNameTV);
        visibleTV = findViewById(R.id.studyRoom_visibleTV);
        memberCountTV = findViewById(R.id.studyRoom_memberCountTV);
        ruleWriterTV = findViewById(R.id.studyRoomMain_ruleWriterTV);
        ///////////////////////////////////

        ///////////////////////////////////
        // Button 종류 가져오기
        writePostBtn = (Button)findViewById(R.id.writePostBtn);
        backFromStudyRoomMainBtn = (ImageButton)findViewById(R.id.backFromStudyRoomMain);
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
                        // 스터디 고유 값 저장해두기
                        studyId = ds.getKey();

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
                    memberCountTV.setText("멤버 " + Integer.toString(studyData.getMemberCount()));
                    ruleWriterTV.setText(studyData.getPresident() + " 님이 만든 규칙");

                    // Post 아이템 항목 설정
                    setData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Click Listener 추가
        writePostBtn.setOnClickListener(this);
        backFromStudyRoomMainBtn.setOnClickListener(this);
    }

    // 모든 RecyclerView 및 adapter 초기화, click listener 추가
    private void initAllRecyclerView() {
        RecyclerView postRV;

        postRV = findViewById(R.id.studyRoom_PostRV);

        postRVAdapter   = new PostItemRVAdapter();

        postRV.setLayoutManager(new LinearLayoutManager(this));

        initPostRVAdapter();

        postRV.setAdapter(postRVAdapter);
    }

    private void initPostRVAdapter() {
        postRVAdapter.setOnItemClickListener(new PostItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                System.out.println("Post 클릭");
            }
        });
    }

    // RecyclerView의 Item 항목 설정
    private void setData() {
        // 해당 스터디방의 게시글 가져와서 띄우기
        FirebaseDatabase.getInstance().getReference()
            .child(Constant.DB_CHILD_STUDYROOM)
            .child(studyId)
            .child(Constant.DB_CHILD_POST)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        PostItemData postItemData;

                        postItemData = data.getValue(PostItemData.class);

                        postItemData.setWriter(postItemData.getWriter());
                        postItemData.setDate(postItemData.getDate());
                        postItemData.setContent(postItemData.getContent());

                        // Adapter에 추가
                        postRVAdapter.addItem(postItemData);
                    }

                    // 변경된 값 표시
                    postRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            // 뒤로가기
            case R.id.backFromStudyRoomMain:
                finish();

                break;

            // 글쓰기
            case R.id.writePostBtn:
                Intent intent;

                // activity_post_write 레이아웃으로 변경하기 위한 intent 설정
                intent = new Intent(StudyRoomMain.this, PostWriteActivity.class);

                // 스터디 명과 스터디 고유 값 전달
                intent.putExtra("studyName", studyName);
                intent.putExtra("studyId", studyId);

                startActivity(intent);

                break;
        }
    }
}