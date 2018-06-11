package nz.co.k2.k2e.ui.navdrawer;

import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import nz.co.k2.k2e.ViewModelProviderFactory;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Module
public class NavDrawerModule {

    @Provides
    @Named("NavDrawer")
    ViewModelProvider.Factory navDrawerViewModelProvider(NavDrawerViewModel navDrawerViewModel) {
        return new ViewModelProviderFactory<>(navDrawerViewModel);
    }
//
//    @Provides
//    NavDrawerViewModel provideNavDrawerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
//        return new NavDrawerViewModel(dataManager, schedulerProvider);
//    }
//
//    @Provides
//    NavDrawerPagerAdapter provideNavDrawerPagerAdapter(NavDrawerActivity activity) {
//        return new NavDrawerPagerAdapter(activity.getSupportFragmentManager());
//    }

    @Provides
    NavDrawerViewModel provideNavDrawerViewModel(DataManager dataManager,
                                       SchedulerProvider schedulerProvider) {
        return new NavDrawerViewModel(dataManager, schedulerProvider);
    }
}
