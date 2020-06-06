package com.example.withstudy.ui.studyroom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.PostItemData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarWriteActivity extends AppCompatActivity implements View.OnClickListener {
    private String studyId;
    private String studyName;
    private DatePicker datePicker;
    private Calendar calendar;

    private TextView dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_write);

        dateView = (TextView) findViewById(R.id.data_conect);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDate(year, month+1, day);

        initialize();
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view)
    {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "원하시는 날짜를 입력하세요",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(month).append(".").append(day));
    }

    private void initialize() {
        Intent intent;
        Button calendartWriteCompleteBtn;
        ImageView calendarWriteExitIV;

        intent = getIntent();

        studyName = intent.getStringExtra("studyName");
        studyId = intent.getStringExtra("studyId");

        calendartWriteCompleteBtn = (Button)findViewById(R.id.calendarComplete);
        calendarWriteExitIV = (ImageView)findViewById(R.id.calendarWriteExit);

        // Click Listener 추가
        calendartWriteCompleteBtn.setOnClickListener(this);
        calendarWriteExitIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // < 버튼
            case R.id.calendarWriteExit:
                finish();

                break;

            // 글쓰기 완료 버튼
            case R.id.calendarComplete:
                // 해당 스터디의 'calendar'에 게시글 추가
                DatabaseReference postRef;
                PostItemData calendar_post;
                EditText schedule_ET;
                EditText schedule_ET2;
                TextView dateView;
                SimpleDateFormat dateFormat;
                Date date;

                schedule_ET = (EditText) findViewById(R.id.schedule_ET);
                schedule_ET2 = (EditText) findViewById(R.id.schedule_ET2);
                dateView =  findViewById(R.id.data_conect);


                // 날짜 형식 설정
                dateFormat = new SimpleDateFormat("yyyy년 M월 d일");
                date = new Date();

                // 데이터베이스 참조 위치(해당 스터디방의 Post) 설정
                postRef = FirebaseDatabase.getInstance().getReference().child(Constant.DB_CHILD_STUDYROOM);
                postRef = postRef.child(studyId).child(Constant.DB_CHILD_CALENDAR).push();

                calendar_post = new PostItemData();

                // 작성일, 내용 설정
                calendar_post.setCl_title(schedule_ET.getText().toString());
                calendar_post.setCl_contnet(schedule_ET2.getText().toString());
                calendar_post.setWhen(dateView.getText().toString());

                // 데이터베이스에 등록
                postRef.setValue(calendar_post);

                finish();
                break;
        }
    }
}
