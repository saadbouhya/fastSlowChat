package com.example.slowvf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.slowvf.Adapters.MypagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import android.os.Bundle;

public class MainActivityChat extends AppCompatActivity {

    int[] tabIcons = {R.drawable.ic_baseline_email_24, R.drawable.ic_baseline_sent_24, R.drawable.ic_baseline_chat_24};
    String[] tabTitles = {"REÇUS", "EMIS", "CHAT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_main);

        ViewPager2 viewPager = findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new MypagerAdapter(getSupportFragmentManager(), getLifecycle()));

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setIcon(tabIcons[position]);
                    tab.setText(tabTitles[position]);
                }
        ).attach();

    }
}