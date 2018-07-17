package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.log.LoggersKt;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.view.CameraView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainInfoBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.utils.CameraUtils;
import nz.co.k2.k2e.utils.ImageUtils;

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

    Fotoapparat fotoapparat;

    private String title;
    private int page;
    private Boolean isCamera = false;

    // Image view for showing our image.
    private ImageView mImageView;

//    private static final int PICK_IMAGE_ID = 1111;

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
        fotoapparat.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        fotoapparat.stop();
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
        CameraView cameraView = view.findViewById(R.id.image_title_camera);
        ImageView imageView = view.findViewById(R.id.image_title_photo);
        ImageButton jobTitlePhotoBtn = view.findViewById(R.id.btn_title_photo);

        fotoapparat = Fotoapparat
                .with(getActivity())
                .into(cameraView)
                .photoResolution(ResolutionSelectorsKt.highestResolution())
                .logger(LoggersKt.fileLogger(getContext()))
                .build();

        jobTitlePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (isCamera){
                        try {
                            PhotoResult photoResult = fotoapparat.takePicture();
                            photoResult.saveToFile(ImageUtils.createImageFile(getActivity()));
                            photoResult.toBitmap()
                            .whenAvailable(bitmapPhoto -> {
                                imageView.setImageBitmap(bitmapPhoto.bitmap);
                                imageView.setRotation(-bitmapPhoto.rotationDegrees);
                                return Unit.INSTANCE;
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fotoapparat.stop();
                        cameraView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        isCamera = false;
                    } else {
                        fotoapparat.start();
                        cameraView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        isCamera = true;
                    }
//                startActivityForResult(CameraUtils.getPickImageIntent(getActivity()), PICK_IMAGE_ID);
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

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        page = getArguments().getInt("someInt", 0);
//        title = getArguments().getString("someTitle");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentJobmainInfoBinding = getViewDataBinding();
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        switch(requestCode){
//            case PICK_IMAGE_ID:
//                Bitmap bitmap = null;
//                bitmap = CameraUtils.getImageFromResult(getActivity(), resultCode, data);
//                mImageView.setImageBitmap(bitmap);
//                break;
//            default:
//                super.onActivityResult(requestCode,resultCode,data);
//                break;
//        }
//    }
}
