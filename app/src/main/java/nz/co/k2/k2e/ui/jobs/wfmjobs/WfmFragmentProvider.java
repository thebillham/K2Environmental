package nz.co.k2.k2e.ui.jobs.wfmjobs;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class WfmFragmentProvider {

    @ContributesAndroidInjector(modules = WfmFragmentModule.class)
    abstract WfmFragment provideWfmFragmentFactory();
}
