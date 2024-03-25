package com.example.stock;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.stock.Fragments.DayFragment;
import com.example.stock.Fragments.MonthFragment;
import com.example.stock.Fragments.YearFragment;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
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
    public int getItemCount() {
        // Total number of tabs
        return 3;
    }
}
