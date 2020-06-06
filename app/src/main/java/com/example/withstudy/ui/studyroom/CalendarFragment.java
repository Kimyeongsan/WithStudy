package com.example.withstudy.ui.studyroom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.withstudy.R;
import com.example.withstudy.main.data.Constant;
import com.example.withstudy.main.data.ManagementData;
import com.example.withstudy.main.data.PostItemData;
import com.example.withstudy.main.data.StudyData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private CalendarAdapter calendarAdapter;
    private StudyData studyData = null;
    private String studyName;
    private String studyId; // 스터디 고유값
    private View root;

    private TextView tvYear;
    private TextView tvDate;

    private GridAdapter gridAdapter;
    private GridView gridView;
    private Calendar mCal;

    private ArrayList<String> dayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_calendar, container, false);

        tvYear = root.findViewById(R.id.tv_year);
        tvDate = root.findViewById(R.id.tv_date);
        gridView = root.findViewById(R.id.gridview);

        // 오늘에 날짜를 세팅 해준다.

        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date));
        tvYear.setText(curMonthFormat.format(date) + "월");

        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance(); //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);

        int dayNum = mCal.get(Calendar.DAY_OF_WEEK); //1일 - 요일 매칭 시키기 위해 공백 add

        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(root.getContext(), dayList);

        gridView.setAdapter(gridAdapter);

        initAllRecyclerView();
        initialize();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 고유 값 찾은 다음부터
        if(studyId != null) {
            refresh();
        }
    }

    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add(" " + (i + 1));

        }
    }


    private class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;

        private int date;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setdate(int num) {
            date = num;
        }

        public int getdate() {
            return date;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        // 수정 준비
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);
                convertView.setTag(holder);

                // 일자 누르면 페이지 이동
                // Click Listener 추가
                holder.tvItemGridView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        ArrayList<StudyData> joinStudys;

                        // 가입한 멤버만 가능
                        joinStudys = ManagementData.getInstance().getJoinStudys();

                        // 모임에 가입되어있을 때만 작동
                        for(StudyData study : joinStudys) {
                            if(study.getStudyName().equals(studyName)) {
                                // calendar write 레이아웃으로 변경하기 위한 intent 설정
                                intent = new Intent(getContext(), CalendarWriteActivity.class);

                                // 스터디 명과 스터디 고유 값 전달
                                intent.putExtra("studyName", studyName);
                                intent.putExtra("studyId", studyId);

                                startActivity(intent);

                                return;
                            }
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));

            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.colorBlack));
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvItemGridView;
    }

    private void refresh() {
        // 어댑터의 모든 item 항목 삭제후 변환된 걸 알리기
        calendarAdapter.delAllItem();
        calendarAdapter.notifyDataSetChanged();

        // 새로 데이터 추가
        setData();
    }
    private void initialize() {
        Intent intent;
        DatabaseReference db;
        intent = getActivity().getIntent();

        studyName = intent.getStringExtra("studyName");

        db = FirebaseDatabase.getInstance().getReference();
        db.child(Constant.DB_CHILD_STUDYROOM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Database에서 해당 스터디 찾기
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // 찾았으면 스터디 정보 저장
                    if (studyName.equals(ds.getValue(StudyData.class).getStudyName())) {
                        ArrayList<StudyData> joinStudys;

                        // 스터디 고유 값 저장해두기
                        studyId = ds.getKey();
                        studyData = ds.getValue(StudyData.class);

                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // 모든 RecyclerView 및 adapter 초기화, click listener 추가
    private void initAllRecyclerView() {
        RecyclerView calendar_recycler;

        calendar_recycler = root.findViewById(R.id.calendar_recycle);

        calendarAdapter = new CalendarAdapter();

        calendar_recycler.setLayoutManager(new LinearLayoutManager(root.getContext()));

        initPostRVAdapter();

        calendar_recycler.setAdapter(calendarAdapter);
    }

    private void initPostRVAdapter() {
        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
            }
        });
    }

    // RecyclerView의 Item 항목 설정
    private void setData() {
        // 해당 스터디방의 게시글 가져와서 띄우기
        FirebaseDatabase.getInstance().getReference()
                .child(Constant.DB_CHILD_STUDYROOM)
                .child(studyId)
                .child(Constant.DB_CHILD_CALENDAR)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data : dataSnapshot.getChildren()) {
                            PostItemData postItemData;

                            postItemData = data.getValue(PostItemData.class);

                            postItemData.setCl_title(postItemData.getCl_title());
                            postItemData.setCl_contnet(postItemData.getCl_contnet());
                            postItemData.setWhen(postItemData.getWhen());

                            // Adapter에 추가
                            calendarAdapter.addItem(postItemData);
                        }

                        // 변경된 값 표시
                        calendarAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}