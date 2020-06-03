package com.example.withstudy.main.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ChattingFragment extends Fragment {

    private RecyclerView recyclerChatRoom;
    private ChatRoomAdapter chatRoomAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chatting, container, false);

        recyclerChatRoom = root.findViewById(R.id.recycler_chat_room);

        initialize(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {

        // 새로 데이터 추가
        setData();
    }

    private void initialize(View root) {


        // Item 항목을 둘 RecyclerView 및 LinearLayout 설정
        chatRoomAdapter = new ChatRoomAdapter();

        recyclerChatRoom.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        chatRoomAdapter.setOnItemClickListener(new ChatRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ChatData chatItem;
                StudyData studyData = null;
                Intent intent;

//                 클릭한 가입한 스터디 채팅방 항목 가져오기
//                chatItem = ChatRoomAdapter.getItem(pos);

                // 클릭한 채팅 화면으로 이동해야함
                intent = new Intent(getActivity(), ChatRoomActivity.class);

                intent.putExtra("studyName", studyData.getStudyName());

                startActivity(intent);
            }
        });

        recyclerChatRoom.setAdapter(chatRoomAdapter);
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
                                                            ChatData chatData;
                                                            StudyData studyData;
                                                            StudyItemData studyItemData;
                                                            StorageReference studyIconRef;

                                                            // 디비에서 가입한 스터디 가져오기
                                                            studyData = data.getValue(StudyData.class);
                                                            chatData = data.getValue(ChatData.class);

                                                            studyItemData = new StudyItemData();

                                                            studyItemData.setTitle(studyData.getStudyName());

                                                            if(!studyData.getIconUri().equals("")) {
                                                                studyIconRef = FirebaseStorage.getInstance().getReferenceFromUrl(studyData.getIconUri());

                                                                studyItemData.setRef(studyIconRef);
                                                                chatRoomAdapter.setContext(getContext());
                                                            }

                                                            // 리싸이클러 뷰에 추가
                                                            chatRoomAdapter.addItem(chatData);

                                                            // 앱상 전반적인 데이터에 해당 스터디 추가
                                                            ManagementData.getInstance().addJoinStudy(studyData);
                                                        }

                                                        // 변경된 값 표시
                                                        chatRoomAdapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                }
                );
    }
}