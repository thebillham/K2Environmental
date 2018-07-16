package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.samples.asbestos.bulk.AsbestosBulkSampleFragment;

public class JobFragment extends BaseFragment<FragmentJobmainBinding, JobViewModel>{

    FragmentJobmainBinding fragmentJobmainBinding;
//    @Inject
//    @Named("JobLinearLayout")
//    LinearLayoutManager mLayoutManager;
    @Inject
    @Named("JobFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    JobViewModel mJobViewModel;

    FragmentPagerAdapter fragmentPagerAdapter;
    ViewPager viewPager;

    FloatingActionMenu menuJobMain;

    FloatingActionButton fabTask;
    FloatingActionButton fabBulk;
    FloatingActionButton fabAir;

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
        mJobViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(JobViewModel.class);
        return mJobViewModel;
    }
// TODO: https://medium.com/corebuild-software/android-repository-pattern-using-rx-room-bac6c65d7385

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jobNumber = getArguments().getString("jobNumber", "");
        mJobViewModel.loadJobFromDb(jobNumber);
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

        menuJobMain = getActivity().findViewById(R.id.menu_jobmain);
        menuJobMain.setVisibility(View.VISIBLE);

        fabTask = getActivity().findViewById(R.id.fab_newtask);
        fabBulk = getActivity().findViewById(R.id.fab_newbulk);
        fabAir = getActivity().findViewById(R.id.fab_newair);

        fabTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuJobMain.close(true);
                Toast.makeText(getActivity(),"Create New Task", Toast.LENGTH_SHORT).show();
            }
        });

        fabBulk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuJobMain.close(true);
                // Go to Add Sample screen
                Bundle args = new Bundle();
                args.putString("sample_uuid","1234567890");
                AsbestosBulkSampleFragment asbestosBulkSampleFragment = new AsbestosBulkSampleFragment();
                asbestosBulkSampleFragment.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContent, asbestosBulkSampleFragment)
                        .addToBackStack("asbestosbulksample")
                        .commit();
            }
        });

        fabAir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuJobMain.close(true);
                Toast.makeText(getActivity(),"Create New Asbestos Air Sample", Toast.LENGTH_SHORT).show();
            }
        });

        viewPager = view.findViewById(R.id.jobmain_viewpager);
        fragmentPagerAdapter = new JobMainPagerAdapter(this.getChildFragmentManager());

        viewPager.setAdapter(fragmentPagerAdapter);

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("BenD", "Try upload edited job to API");
        getCompositeDisposable().add(mJobViewModel.getDataManager().pushJob(mJobViewModel.currentJob.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(insertRow -> {
                    Log.d("BenD", insertRow.toString());
                }, throwable -> {
                    throwable.getStackTrace();
                }));
    }

    @Override
    public void onDestroyView() {
//        AHBottomNavigation bottomNavigation = getActivity().findViewById(R.id.bottom_navigation);
//        bottomNavigation.removeAllItems();
        menuJobMain.close(false);
        menuJobMain.setVisibility(View.GONE);
        super.onDestroyView();
    }

}
