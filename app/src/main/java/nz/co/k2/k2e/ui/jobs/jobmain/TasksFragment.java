package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainTasksBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.base.BaseViewModel;

public class TasksFragment extends BaseFragment {

    private String title;
    private int page;

    FragmentJobmainTasksBinding fragmentJobmainTasksBinding;
    @Inject
    @Named("TasksLinearLayout")
    LinearLayoutManager mLayoutManager;
    @Inject
    @Named("TasksFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    JobViewModel mJobViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jobmain_tasks;
    }

    @Override
    public BaseViewModel getViewModel() {
        return mJobViewModel;
    }

    // newInstance constructor for creating fragment with arguments
    public static TasksFragment newInstance(int page, String title) {
        TasksFragment tasksFragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        tasksFragment.setArguments(args);
        return tasksFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
//        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);
        return view;
    }
}
