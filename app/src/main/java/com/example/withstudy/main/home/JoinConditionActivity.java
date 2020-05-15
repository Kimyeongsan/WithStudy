package com.example.withstudy.main.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.JoinConditionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinConditionActivity extends AppCompatActivity implements View.OnClickListener{
    private JoinConditionRVAdapter rvAdapter;
    private AlertDialog.Builder builder;
    private int birthYear;
    private char gender;

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
        Button backFromJoinConditionBtn;

        //////////////////////////////////////////////////
        // AlertDialog Builder 초기화
        builder = new AlertDialog.Builder(this);
        //////////////////////////////////////////////////

        //////////////////////////////////////////////////
        // Item 항목을 둘 RecyclerView 및 LinearLayout 설정
        rv = findViewById(R.id.joinConditionRV);

        linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);

        rvAdapter = new JoinConditionRVAdapter();

        // RecyclerView Item Click Listener추가
        rvAdapter.setOnItemClickListener(new JoinConditionRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                AlertDialog alertDialog;
                final String[] items;
                final ArrayList<String> selectedItem;

                switch(pos) {
                    case 0: // 성별

                        items = getResources().getStringArray(R.array.GENDER_CONDITION);
                        selectedItem = new ArrayList<String>();
                        selectedItem.add(items[0]);

                        builder.setTitle("가입조건 성별");

                        builder.setSingleChoiceItems(R.array.GENDER_CONDITION, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                selectedItem.clear();
                                selectedItem.add(items[pos]);
                            }
                        });

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                JoinConditionData data;

                                data = rvAdapter.getItem(0);
                                data.setContent(selectedItem.get(0));

                                // 변경된 값 표시
                                rvAdapter.notifyDataSetChanged();

                                // gender 값 변경
                                switch(data.getContent()) {
                                    case "제한없음":
                                        gender = Constant.ALLGENDER;

                                        break;
                                    case "남성만":
                                        gender = Constant.MALE;

                                        break;
                                    case "여성만":
                                        gender = Constant.FEMALE;

                                        break;
                                }
                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                System.out.println("취소");
                            }
                        });

                        break;
                    case 1: // 나이
                        int year;

                        year = 2008;
                        items = new String[50];

                        items[0] = "제한없음";

                        for(int i = 1; i < 50; i++) {
                            year -= 1;
                            items[i] = Integer.toString(year);
                        }

                        selectedItem = new ArrayList<String>();
                        selectedItem.add(items[0]);

                        builder.setTitle("나이 설정(태어난 해)");

                        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                selectedItem.clear();
                                selectedItem.add(items[pos]);
                            }
                        });

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                JoinConditionData data;

                                data = rvAdapter.getItem(1);
                                data.setContent(selectedItem.get(0));

                                // 변경된 값 표시
                                rvAdapter.notifyDataSetChanged();

                                // birthYear 값 변경
                                if(data.getContent().equals("제한없음")) {
                                    birthYear = 0;
                                }
                                else {
                                    birthYear = Integer.parseInt(data.getContent());
                                }
                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                System.out.println("취소");
                            }
                        });

                        break;
                }

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        rv.setAdapter(rvAdapter);

        // Item 항목 설정
        setData();
        //////////////////////////////////////////////////

        //////////////////////////////////////////////////
        // Button 설정
        backFromJoinConditionBtn = findViewById(R.id.backFromJoinConditionBtn);

        // Click Listener 추가
        backFromJoinConditionBtn.setOnClickListener(this);
        //////////////////////////////////////////////////
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
                String str;

                str = "성별: " + gender + ", 나이: " + birthYear + "년생 이후";

                // 가입조건 설정 텍스트 변경
                onBackPressed();

                break;
        }
    }
}