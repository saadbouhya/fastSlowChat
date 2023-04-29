package com.example.slowvf.View.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.MypagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChatFragmentBaptiste extends Fragment {

    int[] tabIcons = {R.drawable.ic_baseline_email_24, R.drawable.ic_baseline_sent_24, R.drawable.ic_baseline_chat_24};
    String[] tabTitles = {"REÇUS", "EMIS", "CHAT"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel chatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);
        View root = inflater.inflate(R.layout.chat_activity_main, container, false);

        ViewPager2 viewPager = root.findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new MypagerAdapter(getChildFragmentManager(), getLifecycle()));

        TabLayout tabLayout = root.findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setIcon(tabIcons[position]);
                    tab.setText(tabTitles[position]);
                }
        ).attach();

        return root;
    }
}