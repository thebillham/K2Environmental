package nz.co.k2.k2e.ui.jobs.wfmjobs;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.reactivex.Observable;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import nz.co.k2.k2e.ui.base.BaseViewModel;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

public class WfmViewModel extends BaseViewModel<WfmNavigator> {

    private final ObservableList<WfmItemViewModel> wfmItemViewModels = new ObservableArrayList<>();

    private final MutableLiveData<List<WfmItemViewModel>> wfmItemsLiveData;

    public Observable<Boolean> forceRefresh;

    public Boolean success;

    public WfmViewModel(DataManager dataManager,
                               SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        wfmItemsLiveData = new MutableLiveData<>();
        getWfmList(false);
    }

    public void addWfmItemsToList(List<WfmItemViewModel> wfmItems) {
        wfmItemViewModels.clear();
        wfmItemViewModels.addAll(wfmItems);
    }

    public Boolean getWfmList(Boolean forceRefresh){
        return getCompositeDisposable().add(getDataManager().getWfmList(forceRefresh)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(wfmList -> {
                    if (wfmList.isEmpty()){
                        Log.d("BenD","Empty wfmList");
                        success = false;
                    } else {
                        wfmItemsLiveData.setValue(getViewModelList(wfmList));
                        getDataManager().saveAllWfmJobs(wfmList);
                        success = true;
                    }
                }));
    }

    // This function takes a jobnumber and converts the WFM Job to a new Base Job, it then
    // returns to the main job screen
    public Boolean addNewJobToList(String jobNumber) {
        Log.d("BenD", "Add new job " + jobNumber);
        return getCompositeDisposable()
                .add(getDataManager().getWfmApiCall(jobNumber)
                .subscribeOn(getSchedulerProvider().io())
                /**
                 * Get WFM Response and save to DB
                 */

                .flatMap(wfmJobs -> {
                    Log.d("BenD", "Flatmap save to DB: " + wfmJobs.get(0).getJobNumber());
                    return getDataManager().saveAllWfmJobs(wfmJobs);
                })
                /**
                 * Knowing that the WfmJob has been saved to the DB, we can retrieve it and create
                 * a BaseJob from it
                 */
                .flatMap(id -> {
                    Log.d("BenD", "Flatmap get job with this id: " + id);
                    return getDataManager().getWfmJobById(id);
                })
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
                    return getDataManager().insertJob(baseJob);
                })
                .observeOn(getSchedulerProvider().ui())
                .subscribe());
    }

    // gets job by number and adds to
    public Boolean getWfmJobByNumber(String jobNumber) {
        return getCompositeDisposable()
                .add(getDataManager().getWfmApiCall(jobNumber)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(wfmJobs -> {
                        if (wfmJobs.get(0).getStatus().equals("ERROR")) {
                            success = false;
                        } else {
                            getDataManager().saveAllWfmJobs(wfmJobs);
                            success = true;
                        }
                    }
                ));
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
