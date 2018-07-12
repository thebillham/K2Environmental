package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsNavigator;

public class JobFragment extends BaseFragment<FragmentJobmainBinding, JobViewModel>
        implements JobsNavigator{

    FragmentJobmainBinding fragmentJobmainBinding;
    @Inject
    @Named("JobLinearLayout")
    LinearLayoutManager mLayoutManager;
    @Inject
    @Named("JobFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    JobViewModel mJobViewModel;

    FragmentPagerAdapter fragmentPagerAdapter;
    ViewPager viewPager;

    private FloatingActionMenu menuJobMain;

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;

    //https://github.com/rexstjohn/UltimateAndroidCameraGuide/blob/master/camera/src/main/java/com/ultimate/camera/fragments/SimpleCameraIntentFragment.java
    public static JobFragment newInstance() {
        Bundle args = new Bundle();
        JobFragment fragment = new JobFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jobmain;
    }

    @Override
    public JobViewModel getViewModel() {
        return mJobViewModel;
    }
// TODO: https://medium.com/corebuild-software/android-repository-pattern-using-rx-room-bac6c65d7385

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jobNumber = getArguments().getString("jobNumber", "");
        mJobViewModel.loadJobFromDb(jobNumber);
        mJobViewModel.setNavigator(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
//        view.findViewById(R.id.)

////        https://android.jlelse.eu/ultimate-guide-to-bottom-navigation-on-android-75e4efb8105f
//        AHBottomNavigationItem bnavInfo = new AHBottomNavigationItem("Info",R.drawable.ic_info);
//        AHBottomNavigationItem bnavTasks = new AHBottomNavigationItem("Tasks",R.drawable.ic_current_tasks);
//        AHBottomNavigationItem bnavSamples = new AHBottomNavigationItem("Samples",R.drawable.ic_sample);
//        AHBottomNavigationItem bnavCheck = new AHBottomNavigationItem("Check",R.drawable.ic_check);
//
//        AHBottomNavigation bottomNavigation = getActivity().findViewById(R.id.bottom_navigation);
//
//        bottomNavigation.addItem(bnavInfo);
//        bottomNavigation.addItem(bnavTasks);
//        bottomNavigation.addItem(bnavSamples);
//        bottomNavigation.addItem(bnavCheck);
////        bottomNavigation.setCurrentItem(0);
//
//        bottomNavigation.setDefaultBackgroundColor(Color.WHITE);
//        bottomNavigation.setAccentColor(R.color.colorAccentLight);
//        bottomNavigation.setInactiveColor(R.color.gray);
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
//        bottomNavigation.setBehaviorTranslationEnabled(true);
//
//        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                viewPager.setCurrentItem(position);
//                return false;
//            }
//        });
        FloatingActionMenu fab = getActivity().findViewById(R.id.menu_jobmain);
        fab.setVisibility(View.VISIBLE);

        viewPager = view.findViewById(R.id.jobmain_viewpager);
        Log.d("BenD", "ViewPager: " + viewPager.toString());
        fragmentPagerAdapter = new JobMainPagerAdapter(getActivity().getSupportFragmentManager());
        Log.d("BenD", "FragmentPager: " + fragmentPagerAdapter.toString());

        viewPager.setAdapter(fragmentPagerAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
//        AHBottomNavigation bottomNavigation = getActivity().findViewById(R.id.bottom_navigation);
//        bottomNavigation.removeAllItems();
        FloatingActionMenu fab = getActivity().findViewById(R.id.menu_jobmain);
        fab.setVisibility(View.GONE);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuJobMain = view.findViewById(R.id.menu_jobmain);

        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        fab3 = view.findViewById(R.id.fab3);

//        final FloatingActionButton programFab1 = new FloatingActionButton(getActivity());
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    Toast.makeText(getContext(), "Fab 1" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fab2:
                    Toast.makeText(getContext(), "Fab 2" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fab3:
                    Toast.makeText(getContext(), "Fab 3" , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
