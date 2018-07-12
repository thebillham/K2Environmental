package nz.co.k2.k2e.ui.samples.asbestos.bulk;

import android.databinding.ObservableField;

import javax.inject.Singleton;

import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.data.model.db.entities.samples.AsbestosBulkSample;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import nz.co.k2.k2e.ui.base.BaseViewModel;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsNavigator;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Singleton
public class AsbestosBulkSampleViewModel extends BaseViewModel<JobsNavigator> {

    public final ObservableField<AsbestosBulkSample> currentSample = new ObservableField<>();

    public AsbestosBulkSampleViewModel(DataManager dataManager,
                        SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    // This function gets JobsJobs from the API and saves to DB
    //TODO Sync Job with APi


//    public void loadSampleFromDb(String uuid) {
//        getCompositeDisposable().add(getDataManager()
//                .getSampleByUuid(uuid)
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(sample -> {
//                        currentSample.set(sample);
//                }, throwable -> {
//                    throwable.getStackTrace();
//                }));
//    }

}
