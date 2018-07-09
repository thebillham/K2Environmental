package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsNavigator;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsViewModel;

public class JobFragment extends BaseFragment<FragmentJobmainBinding, JobViewModel>
        implements JobsNavigator{

    FragmentJobmainBinding mFragmentJobBinding;
    @Inject
    @Named("JobLinearLayout")
    LinearLayoutManager mLayoutManager;
    @Inject
    @Named("JobFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    JobViewModel mJobViewModel;

    // For taking picture
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
//        Integer pos = getArguments().getInt("jobNumber", 0);
        String jobNumber = getArguments().getString("jobNumber", "");
        mJobViewModel.loadJobFromDb(jobNumber);
        mJobViewModel.setNavigator(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
//        view.findViewById(R.id.)
        ImageButton jobTitlePhotoBtn = (ImageButton) view.findViewById(R.id.jobTitlePhoto);
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            jobTitlePhotoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickJobTitlePhoto();
                }
            });
        } else {
            jobTitlePhotoBtn.setEnabled(false);
        }
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
        mFragmentJobBinding.setViewModel(mJobViewModel);

        // Immediate Binding
        // When a variable or observable changes, the binding will be scheduled to change before
        // the next frame. There are times, however, when binding must be executed immediately.
        // To force execution, use the executePendingBindings() method.
        mFragmentJobBinding.executePendingBindings();
    }

    private void onClickJobTitlePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        Toast.makeText(getActivity(),"Take a photo",Toast.LENGTH_SHORT).show();
    }
}
