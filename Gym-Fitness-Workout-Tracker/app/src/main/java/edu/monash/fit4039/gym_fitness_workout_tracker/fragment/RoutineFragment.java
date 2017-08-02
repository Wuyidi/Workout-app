package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.adapter.RoutinePagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutineFragment extends Fragment {
    // Instance variable
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public RoutineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        // Set a scroll flag for toolbar
        // https://stackoverflow.com/questions/30771156/how-to-set-applayout-scrollflags-for-toolbar-programmatically
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_routine, container, false);
        viewPager = (ViewPager) currentView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.VISIBLE);
        setupViewPager(viewPager);
        setupTabLayout(tabLayout);
        return currentView;

    }

    // Set up a view pager and add fragment
    // https://stackoverflow.com/questions/32238343/using-navigationview-with-tablayout
    private void setupViewPager(ViewPager viewPager) {
        RoutinePagerAdapter adapter = new RoutinePagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DefaultRoutineFragment(), "Recommended Routines");
        adapter.addFragment(new CustomRoutineFragment(), "Custom Routines");
        viewPager.setAdapter(adapter);
    }

    // Set up a tab
    // https://stackoverflow.com/questions/32238343/using-navigationview-with-tablayout
    private void setupTabLayout(TabLayout tabLayout) {
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        tabLayout.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
    }
}
