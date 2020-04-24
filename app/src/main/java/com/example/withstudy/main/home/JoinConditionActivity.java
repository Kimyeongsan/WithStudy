package com.example.withstudy.main.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.withstudy.R;
import com.example.withstudy.main.data.JoinConditionData;

import java.util.Arrays;
import java.util.List;

public class JoinConditionActivity extends AppCompatActivity implements View.OnClickListener{
    private JoinConditionRVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join_condition);

        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        RecyclerView rv;
        LinearLayoutManager linearLayoutManager;

        rv = findViewById(R.id.joinConditionRV);

        linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);

        rvAdapter = new JoinConditionRVAdapter();

        rv.setAdapter(rvAdapter);

        // Item 항목 설정
        setData();
    }

    // RecyclerView의 Item 항목 설정
    private void setData() {
        List<String> titles;
        List<String> contents;

        titles = Arrays.asList("성별", "나이");
        contents = Arrays.asList("제한없음", "제한없음");

        // List의 값들을 JoinConditionData 객체에 설정
        for(int i = 0; i < titles.size(); i++) {
            JoinConditionData data;

            data = new JoinConditionData();

            data.setTitle(titles.get(i));
            data.setContent(contents.get(i));

            // Adapter에 추가
            rvAdapter.addItem(data);
        }

        // Adapter의 값이 변경됨을 알려줌으로써 변경된 값 표시
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.backFromJoinConditionBtn:         // 뒤로가기 버튼
                onBackPressed();

                break;
        }
    }
}
