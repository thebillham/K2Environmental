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

import io.reactivex.Observable;
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

    public Observable<Boolean> forceRefresh;

    public WfmViewModel(DataManager dataManager,
                               SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        wfmItemsLiveData = new MutableLiveData<>();
        loadWfmJobs(null);

    }

    public void addWfmItemsToList(List<WfmItemViewModel> wfmItems) {
        wfmItemViewModels.clear();
        wfmItemViewModels.addAll(wfmItems);
    }

    public void writeJobsToDb(List<WfmJob> wfmJobs) {
        Log.d("BenD", wfmJobs.size() + " WFMJobs in list");
        getDataManager().saveAllWfmJobs(wfmJobs)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        if (wfmJobs != null) {
                            Log.d("BenD", "wfmJobList size: " + wfmJobs.size());
                            wfmItemsLiveData.setValue(getViewModelList(wfmJobs));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getStackTrace();
                    }
                });
    }

    // Decide whether to load from the room database or api
//    @SuppressLint("CheckResult")
    public void loadWfmJobs(String jobNumber){
        getCompositeDisposable().add(
        getDataManager().isWfmListEmpty()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(isEmpty -> {
                    if(isEmpty) {
                        // Database is empty, refresh
                        loadWfmJobsFromApi(null, jobNumber);
                    } else {
                        if (jobNumber == null){ loadWfmJobsFromDb(); }
                    }
                }, throwable -> {
                    // handle error
                }));
    }

    // This function gets WfmJobs from the API and saves to DB
    public void loadWfmJobsFromApi(SwipeRefreshLayout swipeRefreshLayout, String jobNumber) {
            Log.d("BenD","wfmJobs is empty, look for API");
            setIsLoading(true);
            getCompositeDisposable().add(getDataManager()
                    .getWfmApiCall(jobNumber)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(wfmResponse -> {
                        if (wfmResponse != null) {
                            writeJobsToDb(wfmResponse);
                            if (swipeRefreshLayout != null){ swipeRefreshLayout.setRefreshing(false); }
                        }
                        setIsLoading(false);
                    }, throwable -> {
                        setIsLoading(false);
                        getNavigator().handleError(throwable);
                    }));
    }

    public void loadWfmJobsFromDb() {
        Log.d("BenD","wfmJobs retrieved from database");
        getCompositeDisposable().add(getDataManager()
                .getAllWfmJobs()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(wfmJobList -> {
                    if (wfmJobList != null) {
                        Log.d("BenD", "wfmJobList size: " + wfmJobList.size());
                        wfmItemsLiveData.setValue(getViewModelList(wfmJobList));
                    }
                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }


    // This function takes a jobnumber and converts the WFM Job to a new Base Job, it then
    // returns to the main job screen
    @SuppressLint("CheckResult")
    public void addNewJobToList(String jobNumber, Activity activity) {
        Log.d("BenD", "Add new job " + jobNumber);
        getDataManager().getWfmApiCall(jobNumber)
                .toObservable()
                .subscribeOn(getSchedulerProvider().io())
                /**
                 * Get WFM Response and save to DB
                 */

                .flatMap(wfmJobs -> {
                    Log.d("BenD", "Flatmap save to DB: " + wfmJobs.get(0).getJobNumber());
                    return getDataManager().saveAllWfmJobs(wfmJobs).toObservable();
                })
                /**
                 * Knowing that the WfmJob has been saved to the DB, we can retrieve it and create
                 * a BaseJob from it
                 */
                .flatMap(id -> {
                    Log.d("BenD", "Flatmap get job with this id: " + id);
                    return getDataManager().getWfmJobById(id).toObservable();
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
                    return getDataManager().insertJob(baseJob).toObservable();
                })
                .observeOn(getSchedulerProvider().ui())
                .subscribe(id -> {
                    Log.d("BenD","ID: " + id);
                    ((FragmentActivity)activity).getSupportFragmentManager().popBackStackImmediate();
                    // TODO add more information in the toast here
                    Toast.makeText(activity, "Job has been added to your jobs", Toast.LENGTH_LONG).show();
                }, throwable -> {

                });
    }

        // This function creates a new BaseJob object and returns to the job screen
    public void getWfmJobByNumber(String jobNumber, BaseJob baseJob) {

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
