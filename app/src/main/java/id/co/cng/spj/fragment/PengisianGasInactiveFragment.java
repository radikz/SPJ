package id.co.cng.spj.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.co.cng.spj.MainActivity;
import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;

public class PengisianGasInactiveFragment extends Fragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("PENGISIAN GAS (INACTIVE)");
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public PengisianGasInactiveFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.isi_gas, container, false);

//        View view = inflater.inflate(R.layout.fragment_library, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.pager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
//        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
//        viewPager = (ViewPager)view.findViewById(R.id.pager);
//
//        viewPager.setAdapter(new CustomFragmentPageAdapter(getChildFragmentManager()));
//        tabLayout.setupWithViewPager(viewPager);
//
//        adapter.addFragment(new FragmentOne(), "FRAG1");
//
//        return view;

    }

    public void onResume(){
        super.onResume();
        // Set title bar
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Filling1Fragment(), "FILLING 1");
        adapter.addFragment(new Filling2Fragment(), "FILLING 2");
        adapter.addFragment(new Filling3Fragment(), "FILLING 3");
        adapter.addFragment(new Filling4Fragment(), "FILLING 4");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}