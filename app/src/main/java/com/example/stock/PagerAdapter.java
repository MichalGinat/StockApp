package com.example.stock;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.stock.Fragments.DayFragment;
import com.example.stock.Fragments.MonthFragment;
import com.example.stock.Fragments.YearFragment;

public class PagerAdapter extends FragmentStateAdapter {
    private static final String[] TAB_TITLES = new String[]{"Day", "Month", "Year"};

    private String symbol;
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity, String symbol) {
        super(fragmentActivity);
        this.symbol = symbol;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the appropriate fragment based on the position
        switch (position) {
            case 0:
                return new DayFragment(symbol);
            case 1:
                return new MonthFragment(symbol);
            case 2:
                return new YearFragment(symbol);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        // Total number of tabs
        return 3;
    }

    public String getTabTitle(int position) {
        return TAB_TITLES[position];
    }
}
