package nz.co.k2.k2e.ui.jobs.myjobs;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class JobsFragmentProvider {

    @ContributesAndroidInjector(modules = JobsFragmentModule.class)
    abstract JobsFragment provideJobsFragmentFactory();
}
