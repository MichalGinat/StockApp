package com.example.stock;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.stock.Fragments.DayFragment;
import com.example.stock.Fragments.MonthFragment;
import com.example.stock.Fragments.YearFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return the appropriate fragment based on the position
        switch (position) {
            case 0:
                return new DayFragment();
            case 1:
                return new MonthFragment();
            case 2:
                return new YearFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Total number of tabs
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Set tab titles
        switch (position) {
            case 0:
                return "Day";
            case 1:
                return "Month";
            case 2:
                return "Year";
            default:
                return null;
        }
    }
}
