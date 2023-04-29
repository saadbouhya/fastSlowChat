package com.example.slowvf.View.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.slowvf.View.Fragments.ChatFragment;

import com.example.slowvf.View.Fragments.ReceivedFragment;

import com.example.slowvf.View.Fragments.SentFragment;

import java.io.IOException;

public class MypagerAdapter extends FragmentStateAdapter {

    public MypagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);


    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: //Page number 1
                try {
                    return ReceivedFragment.newInstance();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case 1: //Page number 2
                return SentFragment.newInstance();
            case 2: //Page number 3
                return ChatFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
