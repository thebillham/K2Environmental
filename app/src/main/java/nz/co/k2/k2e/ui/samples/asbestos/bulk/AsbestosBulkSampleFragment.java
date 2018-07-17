package nz.co.k2.k2e.ui.samples.asbestos.bulk;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentSampleAsbestosBulkBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.utils.CameraUtils;

import static android.app.Activity.RESULT_OK;

public class AsbestosBulkSampleFragment extends BaseFragment<FragmentSampleAsbestosBulkBinding, AsbestosBulkSampleViewModel> {
    FragmentSampleAsbestosBulkBinding fragmentSampleAsbestosBulkBinding;
    @Inject
    @Named("AsbestosBulkSampleFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    AsbestosBulkSampleViewModel asbestosBulkSampleViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sample_asbestos_bulk;
    }

    @Override
    public AsbestosBulkSampleViewModel getViewModel() {
        asbestosBulkSampleViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(AsbestosBulkSampleViewModel.class);
        return asbestosBulkSampleViewModel;
    }

    private ImageView mImageView;
    private String sitePhotoFileName;
    String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uuid = getArguments().getString("sample_uuid", "");
        asbestosBulkSampleViewModel.sampleUuid.set(uuid);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mImageView = view.findViewById(R.id.image_title_photo);
//        mThumbView = view.findViewById(R.id.image_title_thumb);
        ImageButton jobTitlePhotoBtn = view.findViewById(R.id.btn_title_photo);

        jobTitlePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sitePhotoFileName = CameraUtils.getFileName(asbestosBulkSampleViewModel.currentSample.get().sampleid);
                sitePhotoFileName = CameraUtils.getFileName("J_jobnumber_S_samplenumber");
                File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                mCurrentPhotoPath = mCurrentPhotoPath = storageDir.getAbsolutePath() + "/" + sitePhotoFileName;
                Intent cameraIntent = CameraUtils.dispatchTakePictureIntent(getActivity(), mCurrentPhotoPath);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        Button submitButton = view.findViewById(R.id.sampleSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Sample Added", Toast.LENGTH_SHORT).show();
            }
        });

        String[] asbestosMaterials = getResources().getStringArray(R.array.asbestos_materials);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, asbestosMaterials);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(R.id.material);
        textView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentSampleAsbestosBulkBinding = getViewDataBinding();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(mCurrentPhotoPath);
            if (imgFile.exists()){
//                asbestosBulkSampleViewModel.currentSample.get().setSamplePhoto(sitePhotoFileName);
//                Log.d("BenD", "Job site photo file name: " + mJobViewModel.currentJob.get().getSitePhotoFileName());
                CameraUtils.setPic(mImageView, mCurrentPhotoPath);
//                forget this until i can get it working (adding to gallery)
                CameraUtils.galleryAddPic(getActivity(), mCurrentPhotoPath);
            }
        }
    }
}
