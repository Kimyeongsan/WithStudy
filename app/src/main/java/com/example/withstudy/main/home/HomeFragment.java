package com.example.withstudy.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.ManagementData;
import com.example.withstudy.main.data.StudyData;
import com.example.withstudy.main.data.StudyItemData;
import com.example.withstudy.main.data.UserData;
import com.example.withstudy.ui.studyroom.StudyItemRVAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                System.out.println("가입 스터디 클릭");
            }
        });

        joinStudyRV.setAdapter(joinStudyRVAdapter);
        //////////////////////////////////////////////////

        //////////////////////////////////////////////////
        // Item 항목 설정
        setData();
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
        List<String> joinStudyTitles;
        List<String> joinStudyLocations;

        // 유저 정보 가져오기
        userData = ManagementData.getInstance().getUserData();

        joinStudyTitles = new ArrayList<String>();
        //joinStudyLocations = new ArrayList<String>();

        joinStudyLocations = Arrays.asList("테스트1", "테스트2");

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

                        studyData = data.getValue(StudyData.class);

                        // 가입한 스터디 이름과 위치 추가
                        joinStudyTitles.add(studyData.getStudyName());
                    }

                    // 리싸이클러 뷰에 띄워주기
                    for(int i = 0; i < joinStudyTitles.size(); i++) {
                        StudyItemData data;

                        data = new StudyItemData();

                        data.setTitle(joinStudyTitles.get(i));
                        data.setLocation(joinStudyLocations.get(i));

                        // Adapter에 추가
                        joinStudyRVAdapter.addItem(data);
                    }

                    joinStudyRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }
        );
    }
}