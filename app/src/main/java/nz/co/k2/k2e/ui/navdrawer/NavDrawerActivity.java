package nz.co.k2.k2e.ui.navdrawer;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.ActivityNavDrawerBinding;
import nz.co.k2.k2e.databinding.NavHeaderNavDrawerBinding;
import nz.co.k2.k2e.ui.base.BaseActivity;
import nz.co.k2.k2e.ui.calendar.CalendarFragment;
import nz.co.k2.k2e.ui.custom.RoundedImageView;
import nz.co.k2.k2e.ui.equipment.EquipmentFragment;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsFragment;
import nz.co.k2.k2e.ui.lab.AsbestosLabFragment;
import nz.co.k2.k2e.ui.login.GoogleLoginActivity;
import nz.co.k2.k2e.ui.mydetails.MyDetailsFragment;
import nz.co.k2.k2e.ui.notifications.NotificationsFragment;
import nz.co.k2.k2e.ui.settings.AppSettingsFragment;
import nz.co.k2.k2e.ui.tasks.CurrentTasksFragment;
import nz.co.k2.k2e.ui.training.TrainingFragment;

public class NavDrawerActivity extends BaseActivity<ActivityNavDrawerBinding, NavDrawerViewModel>
        implements HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener,
CurrentTasksFragment.OnFragmentInteractionListener,
AsbestosLabFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener,
CalendarFragment.OnFragmentInteractionListener, MyDetailsFragment.OnFragmentInteractionListener,
TrainingFragment.OnFragmentInteractionListener, EquipmentFragment.OnFragmentInteractionListener,
AppSettingsFragment.OnFragmentInteractionListener {
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
    @Inject
    @Named("NavDrawer")
    ViewModelProvider.Factory mViewModelFactory;
    private ActivityNavDrawerBinding activityNavDrawerBinding;
    private NavDrawerViewModel mNavDrawerViewModel;
    private DrawerLayout mDrawer;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_nav_drawer;
    }

    // Floating Action Button Menus

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public NavDrawerViewModel getViewModel() {
        mNavDrawerViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NavDrawerViewModel.class);
        return mNavDrawerViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityNavDrawerBinding = getViewDataBinding();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mNavDrawerViewModel.getDataManager()
                    .updateUserInfo(extras.getString("displayName", "K2 Technician")
                            , extras.getString("email")
                            , extras.getString("photoUrl"));
            Log.d("BenD", "saved user info for " + extras.getString("displayName"));
        } else {
            Log.d("BenD", "extras is null");
        }

        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup nav drawer header to receive user details
        NavHeaderNavDrawerBinding navHeaderNavDrawerBinding =
                DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_nav_drawer, activityNavDrawerBinding.navView, false);
        activityNavDrawerBinding.navView.addHeaderView(navHeaderNavDrawerBinding.getRoot());
        navHeaderNavDrawerBinding.setViewModel(mNavDrawerViewModel);

        activityNavDrawerBinding.navView.inflateHeaderView(R.layout.nav_header_nav_drawer);

        Fragment fragment = null;
        Class fragmentClass = JobsFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


// Todo get profile image and name etc. to work
        // Set profile image
        if (mNavDrawerViewModel.getDataManager().getCurrentUserProfilePicUrl() != null) {
            Log.d("BenD", "set profile pic" + mNavDrawerViewModel.getDataManager().getCurrentUserProfilePicUrl());
            View view = activityNavDrawerBinding.navView.getHeaderView(0);
            RoundedImageView imageView = view.findViewById(R.id.iv_profile_pic);
            Glide.with(this)
                    .load(mNavDrawerViewModel.getDataManager().getCurrentUserProfilePicUrl())
//                    .placeholder(R.drawable.ic_mindorks_logo)
//                    .error(R.drawable.common_google_signin_btn_icon_dark)
                    .into(imageView);
        } else {
            Log.d("BenD", "current user profile pic is null");
        }

        Log.d("BenD", "Email: " + mNavDrawerViewModel.getDataManager().getCurrentUserEmail());
        Log.d("BenD", "Name: " + mNavDrawerViewModel.getDataManager().getCurrentUserName());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNavDrawerViewModel.onNavMenuCreated();

        mDrawer = activityNavDrawerBinding.drawerLayout;
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, NavDrawerActivity.class);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_my_jobs) {
            // Jobs fragment
            // Display current jobs
//            fragmentClass = WfmFragment.class;
            fragmentClass = JobsFragment.class;
        } else if (id == R.id.nav_current_tasks) {
            // Current tasks fragment
            fragmentClass = CurrentTasksFragment.class;
        } else if (id == R.id.nav_lab) {
            // Asbestos lab information
            fragmentClass = AsbestosLabFragment.class;
        } else if (id == R.id.nav_notifications) {
            fragmentClass = NotificationsFragment.class;
        } else if(id ==R.id.nav_calendar) {
            fragmentClass = CalendarFragment.class;
        } else if (id == R.id.nav_my_details) {
            fragmentClass = MyDetailsFragment.class;
        } else if (id == R.id.nav_my_training) {
            fragmentClass = TrainingFragment.class;
        } else if (id == R.id.nav_equipment) {
            fragmentClass = EquipmentFragment.class;
        } else if (id == R.id.nav_app_settings) {
            fragmentClass = AppSettingsFragment.class;
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, GoogleLoginActivity.class);
            intent.putExtra("logout",true);
            startActivity(intent);
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("BenD", "Fragment being commited " + fragmentClass.getName());

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.flContent, fragment)
            .commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void lockDrawer() {
        if (mDrawer != null) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void unlockDrawer() {
        if (mDrawer != null) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    /*
    * CAMERA FUNCTIONS
     */

    // Storage for camera image URI components
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    // Required for camera operations in order to save the image file on resume.
    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
        if (mCapturedImageURI != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI_KEY, mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_URI_KEY)) {
            mCapturedImageURI = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI_KEY));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Getters and setters.
     */

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public Uri getCapturedImageURI() {
        return mCapturedImageURI;
    }

    public void setCapturedImageURI(Uri mCapturedImageURI) {
        this.mCapturedImageURI = mCapturedImageURI;
    }
}
