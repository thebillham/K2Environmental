package nz.co.k2.k2e.ui.samples.asbestos.bulk;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.Fra;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.jobs.jobmain.JobViewModel;

public class AsbestosBulkSampleFragment extends BaseFragment<FragmentSampleAsbestosBulkBinding, JobViewModel> {

    private String uuid;

    FragmentSampleAsbestosBulkBinding fragmentSampleAsbestosBulkBinding;
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
        return R.layout.fragment_sample_asbestos_bulk;
    }

    @Override
    public JobViewModel getViewModel() {
        mJobViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(JobViewModel.class);
        return mJobViewModel;
    }

//
//    // newInstance constructor for creating fragment with arguments
//    public static AsbestosBulkSampleFragment newInstance(int page, String title) {
//        AsbestosBulkSampleFragment asbestosBulkSampleFragment = new AsbestosBulkSampleFragment();
//        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
//        asbestosBulkSampleFragment.setArguments(args);
//        return asbestosBulkSampleFragment;
//    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uuid = getArguments().getString("sample_uuid", "");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentSampleAsbestosBulkBinding = getViewDataBinding();
    }
}
