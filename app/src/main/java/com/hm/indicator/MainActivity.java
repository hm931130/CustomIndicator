package com.hm.indicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hm.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;

    private List<String> mTitles = Arrays.asList("好友1", "通知2", "收藏3", "好友4", "通知5", "收藏6", "好友7", "通知8", "收藏9");
    private List<VpSimpleFragment> framentList = new ArrayList<>();
    private FragmentPagerAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initViews();
        initData();
        mIndicator.setVisibleCount(5);
        mIndicator.setTitles(mTitles);
        mViewPager.setAdapter(myAdapter);
        mIndicator.setViewPager(mViewPager, 2);

    }

    private void initViews() {

        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
    }

    private void initData() {

        for (String title : mTitles) {
            VpSimpleFragment fragment = VpSimpleFragment.getInstance(title);
            framentList.add(fragment);
        }
        myAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return framentList.get(position);
            }

            @Override
            public int getCount() {
                return framentList.size();
            }
        };
    }
}
