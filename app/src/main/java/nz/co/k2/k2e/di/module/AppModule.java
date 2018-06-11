package nz.co.k2.k2e.di.module;

import android.app.Application;
import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nz.co.k2.k2e.BuildConfig;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.data.AppDataManager;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.data.local.db.AppDatabase;
import nz.co.k2.k2e.data.local.db.AppDbHelper;
import nz.co.k2.k2e.data.local.db.DbHelper;
import nz.co.k2.k2e.data.local.prefs.AppPreferencesHelper;
import nz.co.k2.k2e.data.local.prefs.PreferencesHelper;
//import nz.co.k2.k2e.data.remote.ApiHeader;
import nz.co.k2.k2e.data.remote.ApiHelper;
import nz.co.k2.k2e.data.remote.AppApiHelper;
import nz.co.k2.k2e.di.ApiInfo;
import nz.co.k2.k2e.di.DatabaseInfo;
import nz.co.k2.k2e.di.PreferenceInfo;
import nz.co.k2.k2e.utils.AppConstants;
import nz.co.k2.k2e.utils.rx.AppSchedulerProvider;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Module
public class AppModule {

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
//        return BuildConfig.API_KEY;
        return "asdfhpaoiuhv\\$89a0syf";
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, dbName).fallbackToDestructiveMigration()
                .build();
    }

//    @Provides
//    @Singleton
//    CalligraphyConfig provideCalligraphyDefaultConfig() {
//        return new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build();
//    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

//    @Provides
//    @Singleton
//    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@ApiInfo String apiKey,
//                                                           PreferencesHelper preferencesHelper) {
//        return new ApiHeader.ProtectedApiHeader(
//                apiKey,
//                preferencesHelper.getCurrentUserId(),
//                preferencesHelper.getAccessToken());
//    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }
}
