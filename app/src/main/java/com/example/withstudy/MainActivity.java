package com.example.withstudy;
import android.os.Bundle;

import com.example.withstudy.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabs.getTabAt(1).setIcon(R.drawable.ic_searched);
        tabs.getTabAt(2).setIcon(R.drawable.ic_chatting);
        tabs.getTabAt(3).setIcon(R.drawable.ic_notifications);
        tabs.getTabAt(4).setIcon(R.drawable.ic_menu);

    }
}
