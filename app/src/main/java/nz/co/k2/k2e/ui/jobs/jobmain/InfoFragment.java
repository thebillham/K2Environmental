package nz.co.k2.k2e.ui.jobs.jobmain;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentJobmainInfoBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.base.BaseViewModel;
import nz.co.k2.k2e.ui.navdrawer.NavDrawerActivity;

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

    private String title;
    private int page;

    // Activity result key for camera
    static final int REQUEST_TAKE_PHOTO = 11111;

    // Image view for showing our image.
    private ImageView mImageView;

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mImageView = view.findViewById(R.id.image_title_photo);
//        mThumbView = view.findViewById(R.id.image_title_thumb);
        ImageButton jobTitlePhotoBtn = view.findViewById(R.id.btn_title_photo);
//        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);

        jobTitlePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    /**
     * Start the camera by dispatching a camera intent.
     */
    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(context, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                ((NavDrawerActivity)getActivity()).setCapturedImageURI(fileUri);
                ((NavDrawerActivity)getActivity()).setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        ((NavDrawerActivity)getActivity()).getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * The activity returns with the photo.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            addPhotoToGallery();

            // Show the full sized image.
            Log.d("BenD", mImageView.toString());
            setFullImageFromFilePath(((NavDrawerActivity)getActivity()).getCurrentPhotoPath(), mImageView);
//            setFullImageFromFilePath(((NavDrawerActivity)getActivity()).getCurrentPhotoPath(), mThumbView);
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Creates the image file to which the image must be saved.
     * @return
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        ((NavDrawerActivity)getActivity()).setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    /**
     * Add the picture to the photo gallery.
     * Must be called on all camera images or they will
     * disappear once taken.
     */
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(((NavDrawerActivity)getActivity()).getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }


    /**
     * Scale the photo down and fit it to our image views.
     *
     * "Drastically increases performance" to set images using this technique.
     * Read more:http://developer.android.com/training/camera/photobasics.html
     */
    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentJobmainInfoBinding = getViewDataBinding();
    }
    // https://stackoverflow.com/questions/6448856/android-camera-intent-how-to-get-full-sized-photo
}
