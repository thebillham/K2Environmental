package nz.co.k2.k2e.ui.jobs.jobmain;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class InfoFragmentProvider {

    @ContributesAndroidInjector(modules = InfoFragmentModule.class)
    abstract InfoFragment provideInfoFragmentFactory();
}
