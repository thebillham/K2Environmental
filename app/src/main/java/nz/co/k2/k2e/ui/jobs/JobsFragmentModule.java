package nz.co.k2.k2e.ui.jobs;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import nz.co.k2.k2e.ViewModelProviderFactory;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Module
public class JobsFragmentModule {

    @Provides
    JobsViewModel JobsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new JobsViewModel(dataManager, schedulerProvider);
    }

    @Provides
    @Named("JobsLinearLayout")
    LinearLayoutManager provideLinearLayoutManager(JobsFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    JobsAdapter provideJobsAdapter() {
        return new JobsAdapter(new ArrayList<>());
    }

    @Provides
    @Named("JobsFragment")
    ViewModelProvider.Factory providewfmViewModel(JobsViewModel wfmViewModel) {
        return new ViewModelProviderFactory<>(wfmViewModel);
    }
}
