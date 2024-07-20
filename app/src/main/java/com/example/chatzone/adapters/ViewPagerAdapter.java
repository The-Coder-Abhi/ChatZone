package com.example.chatzone.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatzone.fragments.ChatFragment;
import com.example.chatzone.fragments.FriendsFragment;
import com.example.chatzone.fragments.ProfileFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    int tabcount;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new ChatFragment();
            case 1:
                return new FriendsFragment();
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
