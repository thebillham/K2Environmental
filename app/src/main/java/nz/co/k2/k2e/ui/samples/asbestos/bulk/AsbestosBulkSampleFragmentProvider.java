package nz.co.k2.k2e.ui.samples.asbestos.bulk;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import nz.co.k2.k2e.ui.jobs.jobmain.JobFragmentModule;

@Module
public abstract class AsbestosBulkSampleFragmentProvider {

    @ContributesAndroidInjector(modules = AsbestosBulkSampleFragmentModule.class)
    abstract AsbestosBulkSampleFragment provideAsbestosBulkSampleFragmentFactory();
}
