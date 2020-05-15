package com.example.withstudy.main.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.StudyData;

public class MakeStudyActivity extends AppCompatActivity implements View.OnClickListener{
    short minMember, minAge;    // 최소 인원, 최소 나이
    char limitGender;           // 성별 제한

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_study);

        initialize();
    }

    private void initialize() {
        Button backFromMakeStudyBtn;
        Button setMinMemberBtn, setJoinConditionBtn, setMeetingDurationBtn, setMeetingFrequencyBtn;
        TextView setVisibleMeetingText;

        backFromMakeStudyBtn = (Button)findViewById(R.id.backFromMakeStudyBtn);
        setMinMemberBtn = (Button)findViewById(R.id.setMinMemberBtn);
        setJoinConditionBtn = (Button)findViewById(R.id.setJoinConditionBtn);
        setMeetingDurationBtn = (Button)findViewById(R.id.setMeetingDurationBtn);
        setMeetingFrequencyBtn = (Button)findViewById(R.id.setMeetingFrequencyBtn);
        setVisibleMeetingText = (TextView)findViewById(R.id.setVisibleMeetingText);

        // Click Listener 추가
        backFromMakeStudyBtn.setOnClickListener(this);
        setMinMemberBtn.setOnClickListener(this);
        setJoinConditionBtn.setOnClickListener(this);
        setMeetingDurationBtn.setOnClickListener(this);
        setMeetingFrequencyBtn.setOnClickListener(this);
        setVisibleMeetingText.setOnClickListener(this);
    }

    // 최소 인원 설정 다이얼로그 띄우기
    private void showMinMemberDialog() {

    }

    // 

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.backFromMakeStudyBtn:         // 뒤로가기 버튼
                onBackPressed();

                break;

            case R.id.completeFromMakeStudyText:    // 완료 텍스트
                makeStudy();

                break;

            case R.id.setMinMemberBtn:              // 최소인원 설정 버튼
                break;

            case R.id.setJoinConditionBtn:          // 가입조건 설정 버튼
                Intent intent;

                // activity_join_condition 레이아웃으로 변경하기 위한 intent 설정
                intent = new Intent(MakeStudyActivity.this, JoinConditionActivity.class);

                startActivity(intent);

                break;

            case R.id.setMeetingDurationBtn:        // 모임 지속기간 설정 버튼
                break;

            case R.id.setMeetingFrequencyBtn:       // 모임 빈도 설정 버튼
                break;

            case R.id.setVisibleMeetingText:        // 공개 모임 설정 텍스트
                break;
        }
    }

    // 스터디 방 생성
    private void makeStudy() {
        Intent intent;
        StudyData studyRoom;
        String studyName;

        // 데이터 수신
        intent = getIntent();

        studyName = intent.getExtras().getString("studyName");

        // 옵션 설정한거에 맞게 스터디 방 생성
        // 생성한 스터디 방은 즉시 데이터베이스에 추가되어야 한다.
        //studyRoom = new StudyData()
    }
}
