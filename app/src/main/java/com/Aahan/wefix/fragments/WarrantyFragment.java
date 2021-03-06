package com.Aahan.wefix.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Aahan.wefix.ui.MainActivity;
import com.Aahan.wefix.R;
import com.google.android.material.tabs.TabLayout;

public class WarrantyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_warranty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);

        MainActivity.ViewPagerAdapter viewPagerAdapter = new MainActivity.ViewPagerAdapter(getChildFragmentManager());

//        viewPagerAdapter.addFragment(new InWarrantyFragment(), "In Warranty");
        viewPagerAdapter.addFragment(new AddLogFragment(), "Out Of Warranty");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}