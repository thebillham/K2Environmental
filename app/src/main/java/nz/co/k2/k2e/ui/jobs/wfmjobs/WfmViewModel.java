package nz.co.k2.k2e.ui.jobs.wfmjobs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import nz.co.k2.k2e.ui.base.BaseViewModel;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

import android.support.v4.app.FragmentManager;
import android.widget.Toast;

public class WfmViewModel extends BaseViewModel<WfmNavigator> {

    private final ObservableList<WfmItemViewModel> wfmItemViewModels = new ObservableArrayList<>();

    private final MutableLiveData<List<WfmItemViewModel>> wfmItemsLiveData;

    public WfmViewModel(DataManager dataManager,
                               SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        wfmItemsLiveData = new MutableLiveData<>();
        loadWfmItems(false);
    }

    public void addWfmItemsToList(List<WfmItemViewModel> wfmItems) {
        wfmItemViewModels.clear();
        wfmItemViewModels.addAll(wfmItems);
    }

    public Completable loadWfmItems(Boolean forceRefresh) {
        return (Completable) getDataManager()
                .getWfmList(forceRefresh)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(wfmJobList -> {
                        if (!wfmJobList.isEmpty()){
                            getDataManager().saveAllWfmJobs(wfmJobList);
                            wfmItemsLiveData.setValue(getViewModelList(wfmJobList));
                        }
                });
        }

    // This function takes a jobnumber and converts the WFM Job to a new Base Job, it then
    // returns to the main job screen
    @SuppressLint("CheckResult")
    public Completable addNewJobToList(String jobNumber) {
        return (Completable) getDataManager().getWfmApiCall(jobNumber)
                .subscribeOn(getSchedulerProvider().io())
                /**
                 * Get WFM Response and save to DB
                 */
                .flatMap(wfmJobs -> { return getDataManager().saveAllWfmJobs(wfmJobs).toObservable();})
                /**
                 * Knowing that the WfmJob has been saved to the DB, we can retrieve it and create
                 * a BaseJob from it
                 */
                .flatMap(id -> { return getDataManager().getWfmJobById(id).toObservable(); })
                /**
                 * Now we have the WFM Job we just need to map it to the BaseJob
                 * and add it to the DB
                 */
                .flatMap(wfmJob -> {
                    Log.d("BenD", "Flatmap, make BaseJob with this: " + wfmJob.getJobNumber());
                    BaseJob baseJob = new BaseJob();
                    Log.d("BenD", "Copying from job: " + wfmJob.getJobNumber());
                    baseJob.setUuid(UUID.randomUUID().toString());
                    baseJob.setJobNumber(wfmJob.getJobNumber());
                    baseJob.setAddress(wfmJob.getAddress());
                    baseJob.setClientName(wfmJob.getClientName());
                    baseJob.setJobType(wfmJob.getType());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    baseJob.setLastModified(dateFormat.format(new Date()));
                    Log.d("BenD", "Address from rx: " + wfmJob.getAddress());
                    return getDataManager().insertJob(baseJob).toObservable();
                })
                .observeOn(getSchedulerProvider().ui())
                .subscribe();
    }


    public Single<WfmJob> getWfmJobByNumber(String jobNumber) {
        return getDataManager().getWfmJobByNumber(jobNumber);
    }

    public ObservableList<WfmItemViewModel> getWfmItemViewModels() {
        return wfmItemViewModels;
    }

    public MutableLiveData<List<WfmItemViewModel>> getWfmRepos() {
        return wfmItemsLiveData;
    }

    public List<WfmItemViewModel> getViewModelList(List<WfmJob> wfmJobList) {
        List<WfmItemViewModel> wfmItemViewModels = new ArrayList<>();
        for (WfmJob job : wfmJobList) {
            wfmItemViewModels.add(new WfmItemViewModel(
                    job.getJobNumber(), job.getAddress(),
                    job.getClientName(), job.getType(),
                    job.getState()));
        }
        return wfmItemViewModels;
    }
}
