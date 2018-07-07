package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsNavigator;

public class JobFragment extends BaseFragment<FragmentJobmainBinding, JobViewModel>
        implements JobsNavigator{

    FragmentJobmainBinding mFragmentJobBinding;
    @Inject
    @Named("JobLinearLayout")
    LinearLayoutManager mLayoutManager;
//    @Inject
//    @Named("JobFragment")
//    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    JobViewModel mJobViewModel;

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

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("BenD","onCreate");
        super.onCreate(savedInstanceState);
        mJobViewModel.setNavigator(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
//        Bundle args = getArguments();
        String jobNumber = getArguments().getString("jobNumber", "");
        Log.d("BenD","onCreateView");
        mJobViewModel.loadJobFromDb(jobNumber);
        return view;
    }

    public void onResume()
    {
        super.onResume();
        Log.d("BenD","onResume");
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(v -> {
//            // Go to Add Jobs screen
//            JobFragment.this.getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.flContent, new WfmFragment())
//                    .addToBackStack("wfm")
//                    .commit();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("BenD","onViewCreated");
        mFragmentJobBinding = getViewDataBinding();
        setUp();
//        subscribeToLiveData();
    }

    private void setUp() {
        Log.d("BenD", "setting up...");
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mFragmentJobBinding.jobsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Log.d("BenD","Jobs fragment set up 2 " + mFragmentJobsBinding.jobsRecycler.getLayoutManager().toString());
//        mFragmentJobsBinding.jobsRecycler.setItemAnimator(new DefaultItemAnimator());
    }

//    private void subscribeToLiveData() {
//        mJobViewModel.getJobsRepos().observe(this,
//                new Observer<List<JobsItemViewModel>>() {
//                    @Override
//                    public void onChanged(@Nullable List<JobsItemViewModel> JobsItemViewModels) {
//                        mJobViewModel.addJobsItemsToList(JobsItemViewModels);
//                    }
//                });
//    }

//    private void showAboutFragment() {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .disallowAddToBackStack()
//                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
//                .add(R.id.flContent, WfmFragment.newInstance(), WfmFragment.class.getSimpleName())
//                .commit();
//    }
}
