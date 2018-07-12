package nz.co.k2.k2e.ui.jobs.jobmain;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TasksFragmentProvider {

    @ContributesAndroidInjector(modules = TasksFragmentModule.class)
    abstract TasksFragment provideTasksFragmentFactory();
}
