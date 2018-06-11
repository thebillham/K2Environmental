//package nz.co.k2.k2e.ui.splash;
//
//import android.arch.lifecycle.ViewModelProvider;
//
//import javax.inject.Named;
//
//import dagger.Module;
//import dagger.Provides;
//import nz.co.k2.k2e.ViewModelProviderFactory;
//import nz.co.k2.k2e.data.DataManager;
//import nz.co.k2.k2e.utils.rx.SchedulerProvider;
//
//@Module
//public class SplashModule {
//
//    @Provides
//    @Named("Splash")
//    ViewModelProvider.Factory splashViewModelProvider(SplashViewModel splashViewModel) {
//        return new ViewModelProviderFactory<>(splashViewModel);
//    }
////
////    @Provides
////    NavDrawerViewModel provideNavDrawerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
////        return new NavDrawerViewModel(dataManager, schedulerProvider);
////    }
////
////    @Provides
////    NavDrawerPagerAdapter provideNavDrawerPagerAdapter(NavDrawerActivity activity) {
////        return new NavDrawerPagerAdapter(activity.getSupportFragmentManager());
////    }
//
//    @Provides
//    SplashViewModel provideSplashViewModel(DataManager dataManager,
//                                                 SchedulerProvider schedulerProvider) {
//        return new SplashViewModel(dataManager, schedulerProvider);
//    }
//}
