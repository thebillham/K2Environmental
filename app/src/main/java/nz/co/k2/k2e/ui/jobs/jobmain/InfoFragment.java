package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainInfoBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.utils.CameraUtils;

import static android.app.Activity.RESULT_OK;

public class InfoFragment extends BaseFragment<FragmentJobmainInfoBinding, JobViewModel> {

    FragmentJobmainInfoBinding fragmentJobmainInfoBinding;
//    @Inject
//    @Named("InfoLinearLayout")
//    LinearLayoutManager mLayoutManager;
    @Inject
    @Named("JobFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    JobViewModel mJobViewModel;

    // Image view for showing our image.
    private ImageView mImageView;
    private String sitePhotoFileName;
    String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jobmain_info;
    }

    @Override
    public JobViewModel getViewModel() {
        mJobViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(JobViewModel.class);
        return mJobViewModel;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getCompositeDisposable().add(mJobViewModel.getDataManager().updateJob(mJobViewModel.currentJob.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(insertRow -> {
                    Log.d("BenD", insertRow.toString());
                }, throwable -> {
                    throwable.getStackTrace();
                }));
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mImageView = view.findViewById(R.id.image_title_photo);
        ImageButton jobTitlePhotoBtn = view.findViewById(R.id.btn_title_photo);

        // Load site photo image if one already taken
        if (mJobViewModel.currentJob.get().getSitePhotoFileName() != "") {
            Glide.with(mImageView.getContext()).load(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + mJobViewModel.currentJob.get().getSitePhotoFileName()).into(mImageView);
        }

        jobTitlePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sitePhotoFileName = CameraUtils.getFileName("J_" + mJobViewModel.currentJob.get().getJobNumber());
                File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                mCurrentPhotoPath = storageDir.getAbsolutePath() + "/" + sitePhotoFileName;
                Intent cameraIntent = CameraUtils.dispatchTakePictureIntent(getActivity(), mCurrentPhotoPath);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
        return view;
    }

    // newInstance constructor for creating fragment with arguments
    public static InfoFragment newInstance(int page, String title) {
        InfoFragment infoFragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        infoFragment.setArguments(args);
        return infoFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentJobmainInfoBinding = getViewDataBinding();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(mCurrentPhotoPath);
            if (imgFile.exists()){
                mJobViewModel.currentJob.get().setSitePhotoFileName(sitePhotoFileName);
                Log.d("BenD", "Job site photo file name: " + mJobViewModel.currentJob.get().getSitePhotoFileName());
                CameraUtils.setPic(mImageView, mCurrentPhotoPath);
//                forget this until i can get it working (adding to gallery)
//                CameraUtils.galleryAddPic(getActivity(), mCurrentPhotoPath);
            }
        }
    }
}
