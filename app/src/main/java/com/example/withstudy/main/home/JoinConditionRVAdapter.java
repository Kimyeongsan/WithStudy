package com.example.withstudy.main.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.JoinConditionData;

import java.util.ArrayList;

public class JoinConditionRVAdapter extends RecyclerView.Adapter<JoinConditionRVAdapter.ItemViewHolder> {
    // adapter에 들어갈 list
    private ArrayList<JoinConditionData> listData = new ArrayList<>();

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        // LayoutInflater를 이용하여 join_condition_item.xml을 inflate
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_condition_item, parent, false);

        return new ItemViewHolder(view);
    }

    // Item 항목 하나하나씩 bind, 즉 보여주는 메소드
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    // RecyclerView의 총 개수 가져오기
    @Override
    public int getItemCount() {
        return listData.size();
    }

    // 외부에서 item 항목 추가
    public void addItem(JoinConditionData data) {
        listData.add(data);
    }

    // subView 셋팅
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1;
        private TextView textView2;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = (TextView)itemView.findViewById(R.id.joinConditionTitleText);
            textView2 = (TextView)itemView.findViewById(R.id.joinConditionContentText);
        }

        void onBind(JoinConditionData data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
        }
    }
}
