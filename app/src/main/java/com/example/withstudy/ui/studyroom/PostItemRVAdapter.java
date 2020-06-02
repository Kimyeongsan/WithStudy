package com.example.withstudy.ui.studyroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.PostItemData;

import java.util.ArrayList;

public class PostItemRVAdapter extends RecyclerView.Adapter<PostItemRVAdapter.ItemViewHolder> {
    private ArrayList<PostItemData> listData = new ArrayList<>(); // adapter에 들어갈 list
    private OnItemClickListener mListener = null;                  // listener 객체

    // listener interface
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    // OnItemClickListener 객체 설정
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = null;

        // LayoutInflater를 이용하여 item.xml을 inflate
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

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
    public void addItem(PostItemData data) {
        listData.add(data);
    }

    // 외부에서 item 항목 전체 제거
    public void delAllItem() {
        listData.clear();
    }

    // item 항목 가져오기
    public PostItemData getItem(int pos) { return listData.get(pos); }

    // subView 셋팅
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;

        ItemViewHolder(View itemView) {
            super(itemView);

            // ClickListener 설정
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos;

                    pos = getAdapterPosition();

                    // listener 객체의 메서드 호출
                    if(pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(v, pos);
                    }
                }
            });

            textView1 = (TextView) itemView.findViewById(R.id.postWriter);
            textView2 = (TextView) itemView.findViewById(R.id.postDate);
            textView3 = (TextView) itemView.findViewById(R.id.postContent);
        }

        void onBind(PostItemData data) {
            textView1.setText(data.getWriter());
            textView2.setText(data.getDate());
            textView3.setText(data.getContent());
        }
    }
}

