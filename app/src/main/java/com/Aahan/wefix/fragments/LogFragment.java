package com.Aahan.wefix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.Aahan.wefix.ui.MainActivity;
import com.Aahan.wefix.R;
import com.Aahan.wefix.storage.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;

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

//        if (SharedPrefManager.getInstance(getActivity()).getDelear().getPlusMunber().equals("YES")) {
        viewPagerAdapter.addFragment(new CallLogFragment(), "Open Log");
        viewPagerAdapter.addFragment(new CloseLogFragment(), "Closed Log");
//        }
//        viewPagerAdapter.addFragment(new WarrantyLogFragment(), "In Warranty Log");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}