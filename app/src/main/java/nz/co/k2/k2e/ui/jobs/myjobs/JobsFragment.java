package nz.co.k2.k2e.ui.jobs.myjobs;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobsBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.jobs.jobmain.JobFragment;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmFragment;
import nz.co.k2.k2e.ui.navdrawer.NavDrawerViewModel;

public class JobsFragment extends BaseFragment<FragmentJobsBinding, JobsViewModel>
        implements JobsNavigator, JobsAdapter.JobsAdapterListener {

    FragmentJobsBinding mFragmentJobsBinding;
    @Inject
    @Named("JobsLinearLayout")
    LinearLayoutManager mLayoutManager;
    @Inject
    JobsAdapter mJobsAdapter;
    @Inject
    @Named("JobsFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    JobsViewModel mJobsViewModel;

    public static JobsFragment newInstance() {
        Bundle args = new Bundle();
        JobsFragment fragment = new JobsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jobs;
    }

    @Override
    public JobsViewModel getViewModel() {
        mJobsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(JobsViewModel.class);
        return mJobsViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJobsViewModel.setNavigator(this);
        mJobsAdapter.setListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mJobsViewModel.loadJobsFromDb();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(v -> {
            // Go to Add Jobs screen
            JobsFragment.this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContent, new WfmFragment())
                    .addToBackStack("wfm")
                    .commit();
        });
        return view;
    }

    @Override
    public void onDestroyView()
    {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        super.onDestroyView();
    }

    @Override
    public void onJobClick(String jobNumber) {
        // Go to selected job's main page
        Bundle args = new Bundle();
        args.putString("jobNumber",jobNumber);
        JobFragment jobFragment = new JobFragment();
        jobFragment.setArguments(args);
        JobsFragment.this.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, jobFragment)
                .addToBackStack("jobmain")
                .commit();
    }

    @Override
    public void onRetryClick() {
        //
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentJobsBinding = getViewDataBinding();
        setUp();
        subscribeToLiveData();
    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFragmentJobsBinding.jobsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentJobsBinding.jobsRecycler.setItemAnimator(new DefaultItemAnimator());
        mFragmentJobsBinding.jobsRecycler.setAdapter(mJobsAdapter);

        mJobsAdapter.notifyDataSetChanged();
    }

    private void subscribeToLiveData() {
        mJobsViewModel.getJobsRepos().observe(this,
                new Observer<List<JobsItemViewModel>>() {
                    @Override
                    public void onChanged(@Nullable List<JobsItemViewModel> JobsItemViewModels) {
                        mJobsViewModel.addJobsItemsToList(JobsItemViewModels);
                    }
                });
    }
}
