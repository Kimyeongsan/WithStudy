
package com.example.withstudy.ui.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.Arrays;
import java.util.List;

public class StudyRoomMain extends AppCompatActivity implements View.OnClickListener {
    private int REQUEST_POST = 1;
    private PostItemRVAdapter postRVAdapter;
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

        //////////////////////////////////////////////////
        // Item 항목을 둘 RecyclerView 및 LinearLayout 설정
        initAllRecyclerView();
        //////////////////////////////////////////////////

        //////////////////////////////////////////////////
        // Item 항목 설정
        setData();
        //////////////////////////////////////////////////

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
                    memberCountTV.setText("멤버 " + Integer.toString(studyData.getMemberCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        List<String> postWriters, postDates, postContents;

        postWriters = Arrays.asList("작성자1", "작성자2");
        postDates = Arrays.asList("작성일1", "작성일2");
        postContents = Arrays.asList("내용1", "내용2");

        // List의 값들을 PostItemData 객체에 설정
        for(int i = 0; i < postWriters.size(); i++) {
            PostItemData data;

            data = new PostItemData();

            data.setWriter(postWriters.get(i));
            data.setDate(postDates.get(i));
            data.setContent(postContents.get(i));

            // Adapter에 추가
            postRVAdapter.addItem(data);
        }

        // Adapter의 값이 변경됨을 알려줌으로써 변경된 값 표시
        postRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            // 뒤로가기
            case R.id.backFromStudyRoomMain:
                break;

            // 글쓰기
            case R.id.writePostBtn:
                Intent intent;

                // activity_post_write 레이아웃으로 변경하기 위한 intent 설정
                intent = new Intent(StudyRoomMain.this, PostWriteActivity.class);

                // 모임명 전달
                intent.putExtra("studyName", studyName);

                startActivityForResult(intent, REQUEST_POST);

                break;
        }
    }
}