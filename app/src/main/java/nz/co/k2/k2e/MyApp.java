package nz.co.k2.k2e;

import android.app.Activity;
import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import nz.co.k2.k2e.di.component.DaggerAppComponent;

public class MyApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector(){
        return activityDispatchingAndroidInjector;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        // Set up Dagger 2

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);

//        AppLogger.init();

        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG){
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }
//                // List of modules that are part of the component
//                .appModule(new AppModule(this, "https://www.k2.co.nz"))
////                .netModule(new NetModule("https://www.k2.co.nz"))
//                .build();
    }

//    public AppComponent getAppComponent(){
//        return mAppComponent;
//    }
}
