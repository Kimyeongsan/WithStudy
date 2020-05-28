package com.example.withstudy.main.notice;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.withstudy.R;

import java.util.ArrayList;


class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {
    private ArrayList<String> list;

    public NoticeAdapter(ArrayList<String> list){
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView name;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            name = v.findViewById(R.id.text_notice);

        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}