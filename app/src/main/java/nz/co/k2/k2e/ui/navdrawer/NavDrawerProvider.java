package nz.co.k2.k2e.ui.navdrawer;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmFragment;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmFragmentModule;

@Module
public abstract class NavDrawerProvider {

    @ContributesAndroidInjector(modules = NavDrawerModule.class)
    abstract NavDrawerActivity provideNavDrawerFactory();
}
