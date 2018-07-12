package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import nz.co.k2.k2e.ViewModelProviderFactory;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Module
public class InfoFragmentModule {

    @Provides
    JobViewModel jobViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new JobViewModel(dataManager, schedulerProvider);
    }

    @Provides
    @Named("InfoLinearLayout")
    LinearLayoutManager provideLinearLayoutManager(InfoFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    @Named("InfoFragment")
    ViewModelProvider.Factory provideJobViewModel(JobViewModel jobViewModel) {
        return new ViewModelProviderFactory<>(jobViewModel);
    }
}
