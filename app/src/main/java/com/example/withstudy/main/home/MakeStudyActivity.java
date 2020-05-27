package com.example.withstudy.main.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withstudy.R;
import com.example.withstudy.main.data.BasicItemData;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.DetailItemData;
import com.example.withstudy.main.data.StudyData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MakeStudyActivity extends AppCompatActivity implements View.OnClickListener{
    private int REQUEST_JOIN = 1;
    private ItemRVAdapter joinItemRVAdapter, studyItemRVAdapter, etcItemRVAdapter;
    private int minMember;          // 최소 인원
    private int limitGender;       // 성별 제한
    private int minAge;             // 최소 나이
    private int studyDuration;      // 모임 지속기간
    private String studyFrequency;  // 모임빈도
    private int studyVisible;      // 공개여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_study);

        initialize();
    }

    private void initialize() {
        Button backFromMakeStudyBtn;
        TextView completeFromMakeStudyText;

        //////////////////////////////////////////////////
        // Study Data 기본값으로 초기화
        minMember       = 5;
        limitGender     = Constant.ALLGENDER;
        minAge          = Constant.ALLAGE;
        studyDuration   = 30;
        studyFrequency  = "월~금";
        studyVisible    = Constant.VISIBLE;
        //////////////////////////////////////////////////

        //////////////////////////////////////////////////
        // 모든 RecyclerView 및 Adapter 초기화
        initAllRecyclerView();
        //////////////////////////////////////////////////

        // Item 항목 설정
        setData();
        //////////////////////////////////////////////////

        backFromMakeStudyBtn = (Button)findViewById(R.id.backFromMakeStudyBtn);
        completeFromMakeStudyText = (TextView)findViewById(R.id.completeFromMakeStudyText);

        // Click Listener 추가
        backFromMakeStudyBtn.setOnClickListener(this);
        completeFromMakeStudyText.setOnClickListener(this);
    }

    // 모든 RecyclerView 및 adapter 초기화, click listener 추가
    private void initAllRecyclerView() {
        RecyclerView joinItemRV, studyItemRV, etcItemRV;

        joinItemRV  = findViewById(R.id.makeStudyJoinRV);
        studyItemRV = findViewById(R.id.makeStudyStudyRV);
        etcItemRV   = findViewById(R.id.makeStudyEtcRV);

        joinItemRVAdapter   = new ItemRVAdapter(Constant.DETAIL_ITEM);
        studyItemRVAdapter  = new ItemRVAdapter(Constant.BASIC_ITEM);
        etcItemRVAdapter    = new ItemRVAdapter(Constant.DETAIL_ITEM);

        joinItemRV.setLayoutManager(new LinearLayoutManager(this));
        studyItemRV.setLayoutManager(new LinearLayoutManager(this));
        etcItemRV.setLayoutManager(new LinearLayoutManager(this));

        initJoinItemRVAdapter();
        initStudyItemRVAdapter();
        initEtcItemRVAdapter();

        joinItemRV.setAdapter(joinItemRVAdapter);
        studyItemRV.setAdapter(studyItemRVAdapter);
        etcItemRV.setAdapter(etcItemRVAdapter);
    }

    private void initJoinItemRVAdapter() {
        final AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);

        joinItemRVAdapter.setOnItemClickListener(new ItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                AlertDialog alertDialog;

                switch(pos) {
                    case 0: // 최소 인원
                        View dialogView;
                        final EditText minMemberET;

                        dialogView = getLayoutInflater().inflate(R.layout.input_dialog, null);
                        minMemberET = (EditText)dialogView.findViewById(R.id.minMember);

                        builder.setView(dialogView);
                        builder.setTitle("최소 인원");

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                short inputMinMember;

                                inputMinMember = Short.parseShort(minMemberET.getText().toString());

                                // 인원 수는 2~100명 사이여야 함
                                if(inputMinMember < 2 || inputMinMember > 100) {
                                    Toast.makeText(getApplicationContext(), "2~100명까지 설정할 수 있습니다", Toast.LENGTH_LONG).show();
                                } else {
                                    DetailItemData data;

                                    minMember = inputMinMember;

                                    // 설정한 인원수 표시
                                    data = joinItemRVAdapter.getItem(0);
                                    data.setResult(Integer.toString(minMember) + "명");

                                    // 변경된 값 표시
                                    joinItemRVAdapter.notifyDataSetChanged();
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

                    case 1: // 가입 조건 설정
                        Intent intent;

                        // activity_join_condition 레이아웃으로 변경하기 위한 intent 설정
                        intent = new Intent(MakeStudyActivity.this, JoinConditionActivity.class);

                        startActivityForResult(intent, REQUEST_JOIN);

                        break;
                }

                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void initStudyItemRVAdapter() {
        final AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);

        studyItemRVAdapter.setOnItemClickListener(new ItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                AlertDialog alertDialog;
                final String[] items;
                final ArrayList<String> selectedItem;

                switch(pos) {
                    case 0: // 모임 지속기간
                        items = getResources().getStringArray(R.array.STUDY_DURATION);
                        selectedItem = new ArrayList<String>();
                        selectedItem.add(items[0]);

                        builder.setTitle("모임 지속기간");

                        builder.setSingleChoiceItems(R.array.STUDY_DURATION, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                selectedItem.clear();
                                selectedItem.add(items[pos]);
                            }
                        });

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                BasicItemData data;

                                data = studyItemRVAdapter.getItem(0);
                                data.setResult(selectedItem.get(0));

                                // 변경된 값 표시
                                studyItemRVAdapter.notifyDataSetChanged();

                                // studyDuration 값 변경
                                studyDuration = Integer.parseInt(data.getResult().replaceAll("[^0-9]", ""));
                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                System.out.println("취소");
                            }
                        });

                        break;

                    case 1: // 모임 빈도
                        items = getResources().getStringArray(R.array.STUDY_FREQUENCY);
                        selectedItem = new ArrayList<String>();
                        selectedItem.add(items[0]);

                        builder.setTitle("모임 빈도");

                        builder.setSingleChoiceItems(R.array.STUDY_FREQUENCY, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                selectedItem.clear();
                                selectedItem.add(items[pos]);
                            }
                        });

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                BasicItemData data;

                                data = studyItemRVAdapter.getItem(1);
                                data.setResult(selectedItem.get(0));

                                // 변경된 값 표시
                                studyItemRVAdapter.notifyDataSetChanged();

                                // studyFrequency 값 변경
                                studyFrequency = data.getResult();
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
    }

    private void initEtcItemRVAdapter() {
        final AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);

        etcItemRVAdapter.setOnItemClickListener(new ItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                AlertDialog alertDialog;
                final String[] items;
                final ArrayList<String> selectedItem;

                switch(pos) {
                    case 0:
                        items = getResources().getStringArray(R.array.STUDY_TYPE);
                        selectedItem = new ArrayList<String>();
                        selectedItem.add(items[0]);

                        builder.setTitle("모임 타입");

                        builder.setSingleChoiceItems(R.array.STUDY_TYPE, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                selectedItem.clear();
                                selectedItem.add(items[pos]);
                            }
                        });

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                DetailItemData data;
                                String[] studyTypeDetails;

                                // 스터디 타입 세부설명 가져오기
                                studyTypeDetails = getResources().getStringArray(R.array.STUDY_TYPE_DETAIL);

                                data = etcItemRVAdapter.getItem(0);
                                data.setTitle(selectedItem.get(0));

                                // study 값 변경
                                switch(data.getTitle()) {
                                    case "비공개 모임":
                                        data.setContent(studyTypeDetails[0]);
                                        studyVisible = Constant.INVISIBLE;

                                        break;

                                    case "모임명 공개":
                                        data.setContent(studyTypeDetails[1]);
                                        studyVisible = Constant.NAMEVISIBLE;

                                        break;

                                    case "공개 모임":
                                        data.setContent(studyTypeDetails[2]);
                                        studyVisible = Constant.VISIBLE;

                                        break;
                                }

                                // 변경된 값 표시
                                etcItemRVAdapter.notifyDataSetChanged();
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
    }

    // RecyclerView의 Item 항목 설정
    private void setData() {
        List<String> titles;
        List<String> contents;
        List<String> results;

        titles = Arrays.asList(getString(R.string.MakeStudyText2), getString(R.string.MakeStudyText4),
                getString(R.string.MakeStudyText7), getString(R.string.MakeStudyText8),
                getString(R.string.MakeStudyText10));
        contents = Arrays.asList(getString(R.string.MakeStudyText3), getString(R.string.MakeStudyText5),
                getString(R.string.MakeStudyText11));
        results = Arrays.asList("5명", "",
                "30일", "월~금");

        ///////////////////////////////////////////////////////
        // JoinItemRVAdapter 부분
        // List의 값들을 DetailItemData 객체에 설정
        for(int i = 0; i < 2; i++) {
            DetailItemData data;

            data = new DetailItemData();

            data.setTitle(titles.get(i));
            data.setContent(contents.get(i));
            data.setResult(results.get(i));

            // Adapter에 추가
            joinItemRVAdapter.addItem(data);
        }

        // Adapter의 값이 변경됨을 알려줌으로써 변경된 값 표시
        joinItemRVAdapter.notifyDataSetChanged();
        ///////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////
        // StudyItemRVAdapter 부분
        for(int i = 2; i < 4; i++) {
            BasicItemData data;

            data = new DetailItemData();

            data.setTitle(titles.get(i));
            data.setResult(results.get(i));

            // Adapter에 추가
            studyItemRVAdapter.addItem((DetailItemData)data);
        }

        // Adapter의 값이 변경됨을 알려줌으로써 변경된 값 표시
        studyItemRVAdapter.notifyDataSetChanged();
        ///////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////
        // etcItemRVAdapter 부분
        DetailItemData data;

        data = new DetailItemData();

        data.setTitle(titles.get(4));
        data.setContent(contents.get(2));

        // Adapter에 추가
        etcItemRVAdapter.addItem(data);

        // Adapter의 값이 변경됨을 알려줌으로써 변경된 값 표시
        etcItemRVAdapter.notifyDataSetChanged();
        ///////////////////////////////////////////////////////
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.backFromMakeStudyBtn:         // 뒤로가기 버튼
                onBackPressed();

                break;

            case R.id.completeFromMakeStudyText:    // 완료 텍스트
                makeStudy();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // JoinConditionActivity로 부터 온 행위 처리
        if(requestCode == REQUEST_JOIN) {
            // 받아온 행위가 정상적으로 처리된 경우
            if(resultCode == RESULT_OK) {
                DetailItemData dData;
                Date currentTime;
                SimpleDateFormat yearFormat;
                String year;
                String gender;

                gender = new String();

                currentTime = Calendar.getInstance().getTime();
                yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                year = yearFormat.format(currentTime);

                limitGender = data.getIntExtra("gender", Constant.ALLGENDER);
                minAge      = data.getIntExtra("birthYear", Constant.ALLAGE);

                // minAge를 년도가 아닌 실제 나이로 환산
                // 만약 넘겨받은 값이 0(제한없음 이면) 제한 없음으로 표시해야 함
                minAge = Integer.parseInt(year) - minAge;

                // 성별 텍스트 설정
                switch(limitGender) {
                    case Constant.MALE:
                        gender = "남성만";

                        break;
                    case Constant.FEMALE:
                        gender = "여성만";

                        break;
                    case Constant.ALLGENDER:
                        gender = "제한없음";

                        break;
                }


                // 가입 조건 텍스트 변경
                dData = joinItemRVAdapter.getItem(1);
                dData.setContent("성별 " + gender);

                if(minAge == Integer.parseInt(year)) {
                    dData.setContent(dData.getContent() + ", 나이 제한없음");
                } else {
                    dData.setContent(dData.getContent() + ", 나이 " + minAge + "살 이상");
                }

                // 변경된 값 표시
                joinItemRVAdapter.notifyDataSetChanged();
            }
        }
    }

    // 스터디 방 생성
    private void makeStudy() {
        DatabaseReference ref;
        Intent intent;
        StudyData studyRoom;
        String studyName;
//
        // 데이터 수신
        intent = getIntent();

        studyName = intent.getExtras().getString("studyName");

        // 옵션 설정한거에 맞게 스터디 방 생성
        // 생성한 스터디 방은 즉시 데이터베이스에 추가되어야 한다.
        studyRoom = new StudyData(studyName, minMember, limitGender, minAge, studyVisible, studyDuration, studyFrequency);

        // 데이터베이스에 스터디방 생성
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child(Constant.DB_CHILD_STUDYROOM).child(studyName);
        ref.child(Constant.DB_CHILD_STUDYROOM).child(studyName).child("minMember").setValue(minMember);
        ref.child(Constant.DB_CHILD_STUDYROOM).child(studyName).child("ligitGender").setValue(limitGender);
        ref.child(Constant.DB_CHILD_STUDYROOM).child(studyName).child("minAge").setValue(minAge);
        ref.child(Constant.DB_CHILD_STUDYROOM).child(studyName).child("visible").setValue(studyVisible);
        ref.child(Constant.DB_CHILD_STUDYROOM).child(studyName).child("duration").setValue(studyDuration);
        ref.child(Constant.DB_CHILD_STUDYROOM).child(studyName).child("frequency").setValue(studyFrequency);
    }
}