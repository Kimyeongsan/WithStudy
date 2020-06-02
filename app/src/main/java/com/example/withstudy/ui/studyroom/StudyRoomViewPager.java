package com.example.withstudy.ui.studyroom;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.withstudy.R;

public class StudyRoomViewPager extends AppCompatActivity {

    private int  MAX_PAGE = 3;

    Fragment current_fragment = new Fragment();   //현재 Viewpager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_room_viewpager);

        ViewPager viewPager = findViewById(R.id.study_view_pager);
        viewPager.setAdapter(new adapter(getSupportFragmentManager()));
    }

    private class adapter extends FragmentPagerAdapter {
        public adapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if(position<0 || MAX_PAGE<=position)
                return null;
            switch (position){
                case 0:
                    current_fragment  = new StudyRoomFragment();
                    break;

                case 1:
                    current_fragment = new DuesFragment();
                    break;

                case 2:
                    current_fragment = new CalendarFragment();
                    break;
            }
            return current_fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }
}
