package com.example.withstudy.main.notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.StudyItemData;
import com.example.withstudy.ui.studyroom.curriculum.GlideApp;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ItemViewHolder> {
    private ArrayList<StudyItemData> mDataset = new ArrayList<>();
    private Context context;

    public void setDataSet(ArrayList<StudyItemData> myDataset) {
        mDataset = myDataset;
    }

    public void addItem(StudyItemData studyData) {
        mDataset.add(studyData);
    }

//     context 설정
    public void setContext(Context context) {
        this.context = context;
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

            imageView = itemView.findViewById(R.id.img_notice_main);
            textView1 = itemView.findViewById(R.id.text_notice);
        }
    }


    public NoticeAdapter() { }

    @Override
    public NoticeAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);

        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final StudyItemData data = mDataset.get(position);
        holder.textView1.setText(data.getTitle());

        // 등록한 이미지가 존재할 때만
        if (data.getRef() != null) {
            GlideApp.with(context)
                    .load(data.getRef())
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if(mDataset == null)
            return 0;
        return mDataset.size();
    }
}