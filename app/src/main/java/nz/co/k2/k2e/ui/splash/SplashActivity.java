//package nz.co.k2.k2e.ui.splash;
//
//import android.arch.lifecycle.ViewModelProvider;
//import android.arch.lifecycle.ViewModelProviders;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.firebase.auth.FirebaseAuth;
//
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import dagger.android.AndroidInjection;
//import dagger.android.DispatchingAndroidInjector;
//import io.reactivex.Completable;
//import io.reactivex.CompletableObserver;
//import io.reactivex.Scheduler;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Action;
//import io.reactivex.schedulers.Schedulers;
//import nz.co.k2.k2e.BR;
//import nz.co.k2.k2e.R;
//
//import nz.co.k2.k2e.data.DataManager;
//import nz.co.k2.k2e.databinding.ActivitySplashBinding;
//import nz.co.k2.k2e.ui.base.BaseActivity;
//import nz.co.k2.k2e.ui.login.GoogleLoginActivity;
//import nz.co.k2.k2e.ui.navdrawer.NavDrawerActivity;
//import nz.co.k2.k2e.ui.navdrawer.NavDrawerViewModel;
//import nz.co.k2.k2e.utils.rx.SchedulerProvider;
//
//public class SplashActivity extends AppCompatActivity {
////
////    @Inject
////    @Named ("Splash")
////    SplashViewModel mSplashViewModel;
//    private GoogleApiClient mGoogleApiClient;
//    @Inject
//    DataManager dataManager;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        checkSignIn();
//        preLoad();
//    }
//
//    public static Intent newIntent(Context context) {
//        return new Intent(context, SplashActivity.class);
//    }
//
//    public void openLoginActivity() {
//        Intent intent = GoogleLoginActivity.newIntent(SplashActivity.this);
//        startActivity(intent);
//        finish();
//    }
//
//    public void openMainActivity() {
//        Intent intent = NavDrawerActivity.newIntent(SplashActivity.this);
//        startActivity(intent);
//        finish();
//    }
//
//    public void checkSignIn(){
//        FirebaseAuth mAuth;
//        mAuth = FirebaseAuth.getInstance();
//
////        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////                .requestIdToken(getString(R.string.default_web_client_id))
////                .requestEmail()
////                .build();
////
////        mGoogleApiClient = new GoogleApiClient.Builder(this)
////                .enableAutoManage(this, this)
////                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
////                .build();
//
//    }
//
//    // If logged in, start downloading MyJobs, not WFM jobs yet
//    public void preLoad() {
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                openLoginActivity();
//            }
//      }, 3000);
//    }
//}
