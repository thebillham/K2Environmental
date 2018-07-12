package nz.co.k2.k2e.ui.samples.asbestos.bulk;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import nz.co.k2.k2e.ViewModelProviderFactory;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.ui.jobs.jobmain.JobViewModel;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Module
public class AsbestosBulkSampleFragmentModule {

    @Provides
    JobViewModel jobViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new JobViewModel(dataManager, schedulerProvider);
    }

    @Provides
    @Named("CheckLinearLayout")
    LinearLayoutManager provideLinearLayoutManager(AsbestosBulkSampleFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    @Named("JobFragment")
    ViewModelProvider.Factory provideJobViewModel(JobViewModel jobViewModel) {
        return new ViewModelProviderFactory<>(jobViewModel);
    }
}
