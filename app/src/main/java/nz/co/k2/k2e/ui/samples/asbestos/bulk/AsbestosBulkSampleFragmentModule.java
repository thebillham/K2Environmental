package nz.co.k2.k2e.ui.samples.asbestos.bulk;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import nz.co.k2.k2e.ViewModelProviderFactory;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Module
public class AsbestosBulkSampleFragmentModule {

    @Provides
    AsbestosBulkSampleViewModel asbestosBulkSampleViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new AsbestosBulkSampleViewModel(dataManager, schedulerProvider);
    }

    @Provides
    @Named("AsbestosBulkSampleLinearLayout")
    LinearLayoutManager provideLinearLayoutManager(AsbestosBulkSampleFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    @Named("AsbestosBulkSampleFragment")
    ViewModelProvider.Factory provideAsbestosBulkSampleViewModel(AsbestosBulkSampleViewModel asbestosBulkSampleViewModel) {
        return new ViewModelProviderFactory<>(asbestosBulkSampleViewModel);
    }
}
