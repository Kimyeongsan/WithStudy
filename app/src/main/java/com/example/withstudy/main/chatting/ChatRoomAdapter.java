package com.example.withstudy.main.chatting;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;

import java.util.ArrayList;


class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {
    private ArrayList<String> list;

    public ChatRoomAdapter(ArrayList<String> list){
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView name;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            name = v.findViewById(R.id.text_room_name);

        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(list.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.view.getContext(),ChatRoomActivity.class);
                intent.putExtra("chatName",list.get(position));
                holder.view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}