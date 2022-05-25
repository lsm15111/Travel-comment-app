package com.example.hee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hee.FirebaseID;
import com.example.hee.R;
import com.example.hee.RecyclerViewItemClickListener;
import com.example.hee.databinding.ActivityMainBinding;
import com.example.hee.models.Post;
import com.example.hee.view.adapter.MainViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewPager();
    }

    private void initViewPager(){
        viewPagerAdapter = new MainViewPagerAdapter(this);
        binding.viewpager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(binding.tabs, binding.viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("나의여행");
                } else if (position == 1){
                    tab.setText("게시판");
                } else {
                    tab.setText("검색");
                }

            }
        }).attach();


    }
}