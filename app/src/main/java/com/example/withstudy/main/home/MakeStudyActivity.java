package com.example.withstudy.main.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.StudyInfo;

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
        Button backFromMakeMeetingBtn;
        Button setMinMemberBtn, setJoinConditionBtn, setMeetingDurationBtn, setMeetingFrequencyBtn;
        TextView setVisibleMeetingText;

        backFromMakeMeetingBtn = (Button)findViewById(R.id.backFromMakeStudyBtn);
        setMinMemberBtn = (Button)findViewById(R.id.setMinMemberBtn);
        setJoinConditionBtn = (Button)findViewById(R.id.setJoinConditionBtn);
        setMeetingDurationBtn = (Button)findViewById(R.id.setMeetingDurationBtn);
        setMeetingFrequencyBtn = (Button)findViewById(R.id.setMeetingFrequencyBtn);
        setVisibleMeetingText = (TextView)findViewById(R.id.setVisibleMeetingText);

        // Click Listener 추가
        backFromMakeMeetingBtn.setOnClickListener(this);
        setMinMemberBtn.setOnClickListener(this);
        setJoinConditionBtn.setOnClickListener(this);
        setMeetingDurationBtn.setOnClickListener(this);
        setMeetingFrequencyBtn.setOnClickListener(this);
        setVisibleMeetingText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.backFromMakeStudyBtn:     // 뒤로가기 버튼
                onBackPressed();

                break;

            case R.id.setMinMemberBtn:          // 최소인원 설정 버튼
                break;

            case R.id.setJoinConditionBtn:      // 가입조건 설정 버튼
                break;

            case R.id.setMeetingDurationBtn:    // 모임 지속기간 설정 버튼
                break;

            case R.id.setMeetingFrequencyBtn:   // 모임 빈도 설정 버튼
                break;

            case R.id.setVisibleMeetingText:    // 공개 모임 설정 텍스트
                break;
        }
    }

    // 스터디 방 생성
    private void makeStudy() {
        StudyInfo studyRoom;

        // 옵션 설정한거에 맞게 스터디 방 생성
        //studyRoom = new StudyInfo(...)
    }
}
