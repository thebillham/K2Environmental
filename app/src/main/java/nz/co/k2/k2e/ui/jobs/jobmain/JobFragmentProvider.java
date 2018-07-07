package nz.co.k2.k2e.ui.jobs.jobmain;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsFragment;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsFragmentModule;

@Module
public abstract class JobFragmentProvider {

    @ContributesAndroidInjector(modules = JobFragmentModule.class)
    abstract JobFragment provideJobFragmentFactory();
}
