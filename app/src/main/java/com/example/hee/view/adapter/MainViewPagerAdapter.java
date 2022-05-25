package com.example.hee.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hee.fragment.MyListFragment;
import com.example.hee.fragment.PostListFragment;
import com.example.hee.fragment.SelectFragment;

import java.util.Arrays;
import java.util.List;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList = Arrays.asList(new MyListFragment(),new PostListFragment(),new SelectFragment());

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) { return fragmentList.get(position); }

    @Override
    public int getItemCount() { return fragmentList.size(); }
}
