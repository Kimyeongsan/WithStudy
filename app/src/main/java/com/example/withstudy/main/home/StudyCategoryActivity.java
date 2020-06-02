package com.example.withstudy.main.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withstudy.R;
import com.example.withstudy.main.data.DetailItemData;

public class StudyCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_study_category);

        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        Button categoryBtn1, categoryBtn2, categoryBtn3, categoryBtn4, categoryBtn5, categoryBtn6, categoryBtn7, categoryBtn8, categoryBtn9;
        Button nextBtn;
        ImageView backIV;

        intent = new Intent(StudyCategoryActivity.this, MakeStudyActivity.class);

        categoryBtn1 = (Button)findViewById(R.id.studyCategory_Btn1);
        categoryBtn2 = (Button)findViewById(R.id.studyCategory_Btn2);
        categoryBtn3 = (Button)findViewById(R.id.studyCategory_Btn3);
        categoryBtn4 = (Button)findViewById(R.id.studyCategory_Btn4);
        categoryBtn5 = (Button)findViewById(R.id.studyCategory_Btn5);
        categoryBtn6 = (Button)findViewById(R.id.studyCategory_Btn6);
        categoryBtn7 = (Button)findViewById(R.id.studyCategory_Btn7);
        categoryBtn8 = (Button)findViewById(R.id.studyCategory_Btn8);
        categoryBtn9 = (Button)findViewById(R.id.studyCategory_Btn9);
        nextBtn = (Button)findViewById(R.id.studyCategory_nextBtn);

        backIV = (ImageView)findViewById(R.id.backFromStudyCategory);

        categoryBtn1.setOnClickListener(this);
        categoryBtn2.setOnClickListener(this);
        categoryBtn3.setOnClickListener(this);
        categoryBtn4.setOnClickListener(this);
        categoryBtn5.setOnClickListener(this);
        categoryBtn6.setOnClickListener(this);
        categoryBtn7.setOnClickListener(this);
        categoryBtn8.setOnClickListener(this);
        categoryBtn9.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        backIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextView selectedCategoryTV;

        selectedCategoryTV = (TextView)findViewById(R.id.studyCategory_selectedCategoryTV);

        switch(v.getId()) {
            // 이전 버튼
            case R.id.backFromStudyCategory:
                onBackPressed();

                break;

            // 토익
            case R.id.studyCategory_Btn1:
            // 영어회화
            case R.id.studyCategory_Btn2:
            // 공무원
            case R.id.studyCategory_Btn3:
            // 정보처리기사
            case R.id.studyCategory_Btn4:
            // 편입
            case R.id.studyCategory_Btn5:
            // 코딩
            case R.id.studyCategory_Btn6:
            // 승무원
            case R.id.studyCategory_Btn7:
            // 수능대비
            case R.id.studyCategory_Btn8:
                selectedCategoryTV.setText("선택 분야: " + ((Button)v).getText().toString());

                intent.putExtra("category", ((Button)v).getText().toString());

                break;

            // +(직접 입력)
            case R.id.studyCategory_Btn9:
                AlertDialog.Builder builder;
                AlertDialog alertDialog;
                View dialogView;
                TextView titleTV;
                EditText categoryET;

                builder = new AlertDialog.Builder(this);

                dialogView = getLayoutInflater().inflate(R.layout.input_dialog, null);

                titleTV = (TextView)dialogView.findViewById(R.id.dialog_titleTV);
                titleTV.setText("모임 종류(분야)를 입력해주세요.");

                categoryET = (EditText)dialogView.findViewById(R.id.dialog_ET);
                categoryET.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(dialogView);
                builder.setTitle("기타 모임");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        String inputCategory;

                        inputCategory = categoryET.getText().toString();

                        // 공백이면 안됨
                        if(inputCategory.equals("")) {
                            Toast.makeText(getApplicationContext(), "분야를 입력해주세요.", Toast.LENGTH_LONG).show();
                        } else {
                            selectedCategoryTV.setText("선택 분야: " + inputCategory);

                            intent.putExtra("category", inputCategory);
                        }
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        System.out.println("취소");
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();

                break;

            // 다음
            case R.id.studyCategory_nextBtn:
                Intent data;
                String studyName, iconUri;
                double latitude, longitude;

                // 아직 분야 선택을 안했으면 나가기
                if(selectedCategoryTV.getText().toString().equals("모임의 종류를 선택하세요."))
                    return;

                data = getIntent();
                studyName = data.getExtras().getString("studyName");
                iconUri = data.getExtras().getString("iconUri");
                latitude = data.getExtras().getDouble("latitude");
                longitude = data.getExtras().getDouble("longitude");

                // 이전 액티비티의 값 전달
                // 모임명 및 이미지를 선택 했을 시 이미지 Uri 전달
                intent.putExtra("studyName", studyName);

                if(iconUri != null)
                    intent.putExtra("iconUri", iconUri);

                // 위치 정보 전달
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);

                startActivity(intent);
                finish();

                break;
        }
    }
}
