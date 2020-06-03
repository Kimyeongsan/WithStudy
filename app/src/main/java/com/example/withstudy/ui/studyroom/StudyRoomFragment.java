package com.example.withstudy.ui.studyroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.ManagementData;
import com.example.withstudy.main.data.PostItemData;
import com.example.withstudy.main.data.StudyData;
import com.example.withstudy.ui.studyroom.curriculum.CurriculumMainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudyRoomFragment extends Fragment {
    private PostItemRVAdapter postRVAdapter;
    private StudyData studyData;
    private String studyName;
    private String studyId; // 스터디 고유값
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_study_room_main, container, false);

        initialize();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 고유 값 찾은 다음부터
        if(studyId != null) {
            refresh();
        }
    }


    private void initialize() {
        DatabaseReference db;
        Intent intent;
        TextView studyNameTV, visibleTV, memberCountTV, ruleWriterTV;
        Button writePostBtn, curriculumBtn;
        ImageButton backFromStudyRoomMainBtn;

        //////////////////////////////////////////////////
        // Item 항목을 둘 RecyclerView 및 LinearLayout 설정
        initAllRecyclerView();
        //////////////////////////////////////////////////

        ///////////////////////////////////
        // TextView 가져오기
        studyNameTV = root.findViewById(R.id.studyRoom_studyNameTV);
        visibleTV = root.findViewById(R.id.studyRoom_visibleTV);
        memberCountTV = root.findViewById(R.id.studyRoom_memberCountTV);
        ruleWriterTV = root.findViewById(R.id.studyRoomMain_ruleWriterTV);
        ///////////////////////////////////

        ///////////////////////////////////
        // Button 종류 가져오기
        writePostBtn = (Button)root.findViewById(R.id.writePostBtn);
        curriculumBtn = (Button)root.findViewById(R.id.studyRoom_curriculumBtn);
        backFromStudyRoomMainBtn = (ImageButton)root.findViewById(R.id.backFromStudyRoomMain);
        ///////////////////////////////////

        intent = getActivity().getIntent();

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

                    // Post 아이템 항목 및 커리큘럼 항목 설정
                    refresh();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Click Listener 추가
        writePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                // activity_post_write 레이아웃으로 변경하기 위한 intent 설정
                intent = new Intent(getContext(), PostWriteActivity.class);

                // 스터디 명과 스터디 고유 값 전달
                intent.putExtra("studyName", studyName);
                intent.putExtra("studyId", studyId);

                startActivity(intent);
            }
        });

        curriculumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 스터디 생성자만 작성가능
                if(ManagementData.getInstance().getUserData().getUser_Name().equals(studyData.getPresident())) {
                    Intent intent;

                    // activity_curriculum_main 레이아웃으로 변경하기 위한 intent 설정
                    intent = new Intent(getContext(), CurriculumMainActivity.class);

                    // 스터디 고유 값 전달
                    intent.putExtra("studyId", studyId);

                    startActivity(intent);
                }
            }
        });

        backFromStudyRoomMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void refresh() {
        // 어댑터의 모든 item 항목 삭제후 변환된 걸 알리기
        postRVAdapter.delAllItem();
        postRVAdapter.notifyDataSetChanged();

        // 새로 데이터 추가
        setData();

        // 커리큘럼 항목 새로고침
        curriculumRefresh();
    }

    private void curriculumRefresh() {
        TextView curriculumWriterTV, curriculumDateTV, curriculumContentTV;

        curriculumWriterTV = (TextView)root.findViewById(R.id.studyRoomMain_ruleWriterTV);
        curriculumDateTV = (TextView)root.findViewById(R.id.studyRoom_ruleDateTV);
        curriculumContentTV = (TextView)root.findViewById(R.id.studyRoom_ruleContentTV);

        // 데이터베이스에서 해당 스터디방의 커리큘럼 가져오기
        FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_STUDYROOM).child(studyId).child(Constant.DB_CHILD_CURRICULUM)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // 커리큘럼 데이터 가져오기
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PostItemData postItem;

                        postItem = dataSnapshot.getValue(PostItemData.class);

                        // 없으면 빠져나오기
                        if(postItem == null)
                            return;

                        curriculumWriterTV.setText(postItem.getWriter() + " 님이 만든 커리큘럼");
                        curriculumDateTV.setText(postItem.getDate());
                        curriculumContentTV.setText(postItem.getContent());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    // 모든 RecyclerView 및 adapter 초기화, click listener 추가
    private void initAllRecyclerView() {
        RecyclerView postRV;

        postRV = root.findViewById(R.id.studyRoom_PostRV);

        postRVAdapter   = new PostItemRVAdapter();

        postRV.setLayoutManager(new LinearLayoutManager(root.getContext()));

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
}
