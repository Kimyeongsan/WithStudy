package com.example.withstudy.main.home;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.withstudy.R;

public class CalendarActivity extends Activity {
        /**
         * 연/월 텍스트뷰
         */
        private TextView tvYear;
        private TextView tvDate;
        /**
         * 그리드뷰 어댑터
         */
        private GridAdapter gridAdapter;
        /**
         * 일 저장 할 리스트
         */
        private ArrayList<String> dayList;
        ;
        /**
         * 그리드뷰
         */
        private GridView gridView;
        /**
         * 캘린더 변수
         */
        private Calendar mCal;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            tvYear = findViewById(R.id.tv_year);
            tvDate = findViewById(R.id.tv_date);
            gridView = findViewById(R.id.gridview);

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

            gridAdapter = new GridAdapter(getApplicationContext(), dayList);

            gridView.setAdapter(gridAdapter);

        }

        /**
         * 해당 월에 표시할 일 수 구함 * * @param month
         */
        private void setCalendarDate(int month) {
            mCal.set(Calendar.MONTH, month - 1);

            for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                dayList.add(" " + (i + 1));

            }
        }

        /**
         * 그리드뷰 어댑터 *
         */
        private class GridAdapter extends BaseAdapter {
            private final List<String> list;
            private final LayoutInflater inflater;
            /**
             * 생성자 * * @param context * @param list
             */
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

                    holder.tvItemGridView =  convertView.findViewById(R.id.tv_item_gridview);
                    convertView.setTag(holder);
                    holder.tvItemGridView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(CalendarActivity.this);
                            ad.setTitle("임시 설정");       // 제목 설정

                            final EditText et = new EditText(CalendarActivity.this);
                            ad.setView(et);

                            ad.setPositiveButton("스케줄 저장", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String value = et.getText().toString();
                                    int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
                                    for (int i = 1; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                                        int s = i;
                                        if (getdate() == i) {
                                            dayList.set(dayNum + i + 4, s + "\n" + value);
                                        }
                                        s++;
                                    }

                                }
                            });
                            ad.setNeutralButton("날짜저장", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setdate(Integer.parseInt(et.getText().toString()));
                                }
                            });
                            ad.show();
                        }
                    });
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tvItemGridView.setText("" + getItem(position)); //해당 날짜 텍스트 컬러,배경 변경

                mCal = Calendar.getInstance(); //오늘 day 가져옴
                Integer today = mCal.get(Calendar.DAY_OF_MONTH);
                String sToday = String.valueOf(today);

                if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                    holder.tvItemGridView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                return convertView;
            }
        }


        private class ViewHolder {
            TextView tvItemGridView;
        }
    }