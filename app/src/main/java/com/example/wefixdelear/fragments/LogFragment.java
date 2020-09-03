package com.example.wefixdelear.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wefixdelear.MainActivity;
import com.example.wefixdelear.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class LogFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);

        MainActivity.ViewPagerAdapter viewPagerAdapter = new MainActivity.ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(new CallLogFragment(), "Open Log");
        viewPagerAdapter.addFragment(new CloseLogFragment(), "Closed Log");
//        viewPagerAdapter.addFragment(new CancelledLogFragment(), "Cancelled Log");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}