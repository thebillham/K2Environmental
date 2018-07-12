package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainSamplesBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.base.BaseViewModel;

public class SamplesFragment extends BaseFragment<FragmentJobmainSamplesBinding, JobViewModel> {

    private String title;
    private int page;

    FragmentJobmainSamplesBinding fragmentJobmainSamplesBinding;
//    @Inject
//    @Named("SamplesLinearLayout")
//    LinearLayoutManager mLayoutManager;
    @Inject
    @Named("JobFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    JobViewModel mJobViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jobmain_samples;
    }

    @Override
    public JobViewModel getViewModel() {
        mJobViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(JobViewModel.class);
        return mJobViewModel;
    }

    // newInstance constructor for creating fragment with arguments
    public static SamplesFragment newInstance(int page, String title) {
        SamplesFragment samplesFragment = new SamplesFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        samplesFragment.setArguments(args);
        return samplesFragment;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentJobmainSamplesBinding = getViewDataBinding();
    }
}
