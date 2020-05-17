package com.example.withstudy;
import android.os.Bundle;

import com.example.withstudy.main.menu.MenuFragment;
import com.example.withstudy.main.SectionsPagerAdapter;
import com.example.withstudy.main.chatting.ChattingFragment;
import com.example.withstudy.main.home.HomeFragment;
import com.example.withstudy.main.notice.NoticeFragment;
import com.example.withstudy.main.search.SearchFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        addTabs(viewPager);
        setTabIcons();
    }

    private void setTabIcons() {
        tabs.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabs.getTabAt(1).setIcon(R.drawable.ic_search);
        tabs.getTabAt(2).setIcon(R.drawable.ic_chatting);
        tabs.getTabAt(3).setIcon(R.drawable.ic_notifications);
        tabs.getTabAt(4).setIcon(R.drawable.ic_menu);
    }

    private void addTabs(ViewPager viewPager) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        sectionsPagerAdapter.addFrag(new HomeFragment());
        sectionsPagerAdapter.addFrag(new SearchFragment());
        sectionsPagerAdapter.addFrag(new ChattingFragment());
        sectionsPagerAdapter.addFrag(new NoticeFragment());
        sectionsPagerAdapter.addFrag(new MenuFragment());

        viewPager.setAdapter(sectionsPagerAdapter);
    }
}
