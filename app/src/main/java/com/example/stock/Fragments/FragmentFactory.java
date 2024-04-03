package com.example.stock.Fragments;

import androidx.fragment.app.Fragment;

/**
 * Factory class for creating fragments based on the fragment type.
 */
public class FragmentFactory {
    public Fragment createFragment(FragmentType type, String symbol) {
        switch (type) {
            case DAY:
                return new DayFragment(symbol);
            case MONTH:
                return new MonthFragment(symbol);
            case YEAR:
                return new YearFragment(symbol);
            default:
                return null;
        }
    }
}
