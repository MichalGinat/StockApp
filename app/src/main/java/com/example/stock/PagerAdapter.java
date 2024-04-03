package com.example.stock;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.stock.fragments.FragmentFactory;
import com.example.stock.fragments.FragmentType;

/**
 * Adapter class for managing fragments in ViewPager2 setup
 * Handles creation of DayFragment, MonthFragment, and YearFragment based on position
 * Provides tab titles for Day, Month, and Year tabs
 */
public class PagerAdapter extends FragmentStateAdapter {
    private static final String[] TAB_TITLES = new String[]{"Day", "Month", "Year"};

    private final String symbol;

    // Constructor to initialize the PagerAdapter with the symbol
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity, String symbol) {
        super(fragmentActivity);
        this.symbol = symbol;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the appropriate fragment based on the position
        FragmentType fragmentType = position == 0
                ? FragmentType.DAY
                : position == 1 ? FragmentType.MONTH : FragmentType.YEAR;
        FragmentFactory fragmentFactory = new FragmentFactory();
        return fragmentFactory.createFragment(fragmentType, symbol);
    }

    // Total number of tabs
    @Override
    public int getItemCount() {
        return 3;
    }

    // Get the title for a specific tab position
    public String getTabTitle(int position) {
        return TAB_TITLES[position];
    }
}
