package com.example.withstudy.main.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;

import java.util.ArrayList;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ItemViewHolder> {
    private ArrayList<ChatData> chatdata = new ArrayList<>();
    private OnItemClickListener mListener = null;                  // listener 객체
    private Context context;

    // listener interface
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    // OnItemClickListener 객체 설정
    public void setOnItemClickListener(ChatRoomAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    // context 설정
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);

        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    //     Item 항목 하나하나씩 bind, 즉 보여주는 메소드 : 추가 예정
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

    }


    // RecyclerView의 총 개수 가져오기
    @Override
    public int getItemCount() {
        return chatdata.size();
    }

    // 외부에서 item 항목 추가
    public void addItem(ChatData data) {
        chatdata.add(data);
    }

    // item 항목 가져오기
    public ChatData getItem(int pos) { return chatdata.get(pos); }

    // 외부에서 모든 item 항목 삭제
    public void delAllItem() {
        chatdata.clear();
    }

    // subView 셋팅
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;

        ItemViewHolder(View itemView) {
            super(itemView);

            // ClickListener 설정
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos;

                    pos = getAdapterPosition();

                    // listener 객체의 메서드 호출
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(v, pos);
                    }
                }
            });

            imageView = (ImageView) itemView.findViewById(R.id.img_room_main);
            textView1 = (TextView) itemView.findViewById(R.id.text_room_name);
            textView2 = (TextView) itemView.findViewById(R.id.text_room_unread);
        }

//        수정 준비 중
//        void onBind(StudyItemData data) {
//            textView1.setText(data.getTitle());
//            textView2.setText(data.getAddress());
//
//            // 등록한 이미지가 존재할 때만
//            if (data.getRef() != null) {
//                GlideApp.with(context)
//                        .load(data.getRef())
//                        .into(imageView);
//            }
//        }

    }
}