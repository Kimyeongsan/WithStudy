package com.example.withstudy.main.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.DetailItemData;

import java.util.ArrayList;

public class ItemRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.ItemViewHolder> {
    private ArrayList<DetailItemData> listData = new ArrayList<>(); // adapter에 들어갈 list
    private OnItemClickListener mListener = null;                  // listener 객체
    private int option; // 어떤 액티비티인지 명시

    ItemRVAdapter(int option) {
        this.option = option;
    }

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
        if(option == Constant.DETAIL_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item, parent, false);
        }
        else if(option == Constant.BASIC_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_item, parent, false);
        }

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
    public void addItem(DetailItemData data) {
        listData.add(data);
    }

    // item 항목 가져오기
    public DetailItemData getItem(int pos) { return listData.get(pos); }

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

            if(option == Constant.BASIC_ITEM) {
                textView1 = (TextView) itemView.findViewById(R.id.basicTitleText);
                textView2 = (TextView) itemView.findViewById(R.id.basicResultText);
            } else if(option == Constant.DETAIL_ITEM) {
                textView1 = (TextView) itemView.findViewById(R.id.detailTitleText);
                textView2 = (TextView) itemView.findViewById(R.id.detailResultText);
                textView3 = (TextView) itemView.findViewById(R.id.detailContentText);
            }
        }

        void onBind(DetailItemData data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getResult());

            if(option == Constant.DETAIL_ITEM) {
                textView3.setText(data.getContent());
            }
        }
    }
}
