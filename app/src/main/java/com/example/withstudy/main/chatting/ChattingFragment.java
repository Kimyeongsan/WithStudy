package com.example.withstudy.main.chatting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;

import java.util.ArrayList;


public class ChattingFragment extends Fragment {

    private RecyclerView recyclerChatRoom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chatting, container, false);

        recyclerChatRoom = root.findViewById(R.id.recycler_chat_room);
        recyclerChatRoom.setHasFixedSize(true);
        recyclerChatRoom.setLayoutManager(new LinearLayoutManager(root.getContext()));

        ArrayList<String> testData = new ArrayList<>();  // chatting room 임시 data
        testData.add("Chatting Test 1");
        testData.add("Chatting Test 2");
        testData.add("Chatting Test 3");
        testData.add("Chatting Test 4");
        testData.add("Chatting Test 5");

        recyclerChatRoom.setAdapter(new ChatRoomAdapter(testData));

        return root;
    }
}