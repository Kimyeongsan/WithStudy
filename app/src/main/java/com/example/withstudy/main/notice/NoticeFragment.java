package com.example.withstudy.main.notice;

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

public class NoticeFragment extends Fragment {

    private RecyclerView recyclerNotice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notice, container, false);

        recyclerNotice = root.findViewById(R.id.recycler_notice);
        recyclerNotice.setHasFixedSize(true);
        recyclerNotice.setLayoutManager(new LinearLayoutManager(root.getContext()));

        ArrayList<String> testData = new ArrayList<>();  // Notice 임시 data
        testData.add("With Study Together");
        testData.add("With Study Together");
        testData.add("With Study Together");
        testData.add("With Study Together");
        testData.add("With Study Together");

        recyclerNotice.setAdapter(new NoticeAdapter(testData));

        return root;
    }
}