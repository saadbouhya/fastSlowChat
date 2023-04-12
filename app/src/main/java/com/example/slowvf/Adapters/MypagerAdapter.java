package com.example.slowvf.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.slowvf.Fragments.ChatFragment;
import com.example.slowvf.Fragments.SentFragment;
import com.example.slowvf.Fragments.ReceivedFragment;

public class MypagerAdapter extends FragmentStateAdapter {

    public MypagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);


    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: //Page number 1
                return ReceivedFragment.newInstance();
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
