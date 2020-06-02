package com.example.withstudy.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.ManagementData;
import com.example.withstudy.main.data.StudyData;
import com.example.withstudy.main.data.StudyItemData;
import com.example.withstudy.main.data.UserData;
import com.example.withstudy.ui.studyroom.StudyItemRVAdapter;
import com.example.withstudy.ui.studyroom.StudyRoomViewPager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private RecyclerView joinStudyRV;
    private StudyItemRVAdapter joinStudyRVAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        joinStudyRV = root.findViewById(R.id.home_joinStudyRV);

        initialize(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {
        // 어댑터의 모든 item 항목 삭제후 변환된 걸 알리기
        joinStudyRVAdapter.delAllItem();
        joinStudyRVAdapter.notifyDataSetChanged();

        // 새로 데이터 추가
        setData();
    }

    private void initialize(View root) {
        Button addMeetingBtn; // 모임 만들기 버튼

        addMeetingBtn = (Button)root.findViewById(R.id.addMeetingBtn);

        //////////////////////////////////////////////////
        // Item 항목을 둘 RecyclerView 및 LinearLayout 설정
        joinStudyRVAdapter   = new StudyItemRVAdapter();

        joinStudyRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        joinStudyRVAdapter.setOnItemClickListener(new StudyItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                StudyItemData studyItem;
                Intent intent;

                // 클릭한 가입한 스터디 항목 가져오기
                studyItem = joinStudyRVAdapter.getItem(pos);

                // 클릭한 스터디 화면으로 이동해야함
                intent = new Intent(getActivity(), StudyRoomViewPager.class);

                intent.putExtra("studyName", studyItem.getTitle());

                startActivity(intent);
            }
        });

        joinStudyRV.setAdapter(joinStudyRVAdapter);
        //////////////////////////////////////////////////

        //////////////////////////////////////////////////
        // Item 항목 설정
        //setData();
        //////////////////////////////////////////////////

        // Click Listener 추가
        addMeetingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.addMeetingBtn:    // 모임 만들기 버튼
                // activity_init_study 레이아웃으로 변경해야 함
                Intent intent = new Intent(getActivity(), InitStudyActivity.class);

                startActivity(intent);
                break;
        }
    }

    // 가입한 스터디 목록 생성
    private void setData() {
        UserData userData;

        // 유저 정보 가져오기
        userData = ManagementData.getInstance().getUserData();

        // 가입한 목록 받아와서 띄워주기
        FirebaseDatabase.getInstance().getReference()
            .child(Constant.DB_CHILD_USER)
            .child(userData.getUser_Id())
            .child(Constant.DB_CHILD_JOINSTUDY)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        StudyData studyData;
                        StudyItemData studyItemData;
                        StorageReference studyIconRef;

                        // 디비에서 가입한 스터디 가져오기
                        studyData = data.getValue(StudyData.class);

                        studyItemData = new StudyItemData();

                        studyItemData.setTitle(studyData.getStudyName());
                        studyItemData.setAddress(studyData.getAddress());

                        if(!studyData.getIconUri().equals("")) {
                            studyIconRef = FirebaseStorage.getInstance().getReferenceFromUrl(studyData.getIconUri());

                            studyItemData.setRef(studyIconRef);
                            joinStudyRVAdapter.setContext(getContext());
                        }

                        // 리싸이클러 뷰에 추가
                        joinStudyRVAdapter.addItem(studyItemData);

                        // 앱상 전반적인 데이터에 해당 스터디 추가
                        ManagementData.getInstance().addJoinStudy(studyData);
                    }

                    // 변경된 값 표시
                    joinStudyRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }
        );
    }
}