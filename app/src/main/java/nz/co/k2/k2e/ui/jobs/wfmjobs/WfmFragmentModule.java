package nz.co.k2.k2e.ui.jobs.wfmjobs;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import nz.co.k2.k2e.ViewModelProviderFactory;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Module
public class WfmFragmentModule {

    @Provides
    WfmViewModel wfmViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Log.d("BenD","provides viewmodel");
        return new WfmViewModel(dataManager, schedulerProvider);
    }

    @Provides
    @Named("WfmLinearLayout")
    LinearLayoutManager provideLinearLayoutManager(WfmFragment fragment) {
        Log.d("BenD","provides linear layout");
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    WfmAdapter providewfmAdapter(WfmViewModel wfmViewModel) {
        Log.d("BenD","provides wfmAdapter");
        return new WfmAdapter(new ArrayList<>(), wfmViewModel);
    }

    @Provides
    @Named("WfmFragment")
    ViewModelProvider.Factory providewfmViewModel(WfmViewModel wfmViewModel) {
        return new ViewModelProviderFactory<>(wfmViewModel);
    }
}
