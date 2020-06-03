package com.example.withstudy.main.chatting;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.StudyData;


import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ItemViewHolder> {
    private ArrayList<StudyData> mDataset = new ArrayList<>();

    public void setDataSet(ArrayList<StudyData> myDataset) {
        mDataset = myDataset;
    }

    public void addItem(StudyData studyData) {
        mDataset.add(studyData);
    }

    public void clear() {
        mDataset.clear();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView1;
        private View view;

        ItemViewHolder(View itemView) {
            super(itemView);

            // ClickListener 설정
            view = itemView;

            imageView = itemView.findViewById(R.id.img_room_main);
            textView1 = itemView.findViewById(R.id.text_room_name);
        }
    }


    public ChatRoomAdapter() { }

    @Override
    public ChatRoomAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);

        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final StudyData data = mDataset.get(position);
        holder.textView1.setText(data.getStudyName());

        final StudyData finalData = data;

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                // 클릭한 채팅 화면으로 이동해야함
                intent = new Intent(holder.view.getContext(), ChatRoomActivity.class);

                intent.putExtra("studyName", finalData.getStudyName());
                intent.putExtra("studyUID", finalData.studyId);

                holder.view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mDataset == null)
            return 0;
        return mDataset.size();
    }
}