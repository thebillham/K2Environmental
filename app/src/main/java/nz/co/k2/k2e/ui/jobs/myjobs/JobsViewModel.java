package nz.co.k2.k2e.ui.jobs.myjobs;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import nz.co.k2.k2e.ui.base.BaseViewModel;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Singleton
public class JobsViewModel extends BaseViewModel<JobsNavigator> {

    private final ObservableList<JobsItemViewModel> jobsItemViewModels = new ObservableArrayList<>();

    private final MutableLiveData<List<JobsItemViewModel>> jobsItemsLiveData;

    public Observable<Boolean> forceRefresh;

    public JobsViewModel(DataManager dataManager,
                        SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        jobsItemsLiveData = new MutableLiveData<>();
        loadJobsFromDb();

    }

    public void addJobsItemsToList(List<JobsItemViewModel> jobsItems) {
        jobsItemViewModels.clear();
        jobsItemViewModels.addAll(jobsItems);
    }

//    public void writeJobsToDb(JobsResponse jobsResponse){
//        // Convert to List of JobsJobs first
//        ArrayList<BaseJob> jobs = new ArrayList<BaseJob>();
//        for (JobsResponse.Job job : jobsResponse.getJobs()){
//            JobsJob dbjob = new JobsJob().convertFromSource(job);
//            jobsJobs.add(dbjob);
//        }
//        Log.d("BenD",jobsJobs.size() + " WFMJobs in list");
//
//        // Write to DB on separate thread
//        Completable.fromAction(new Action() {
//            @Override
//            public void run() throws Exception {
//                getDataManager().saveAllJobs(jobs);
//            }
//        }).subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(new CompletableObserver() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        // Subscribe
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d("BenD",jobsJobs.size() + " jobs jobs saved - OnCOmplete");
//                        loadJobsJobsFromDb();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        getNavigator().handleError(e);
//                        Log.d("BenD","error: " + e);
//                    }
//                });
//    }

    // Decide whether to load from the room database or api
//    @SuppressLint("CheckResult")
//    public void loadJobsJobs(){
//        getDataManager().isJobsListEmpty()
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(isEmpty -> {
//                    if(isEmpty) {
//                        // Database is empty, refresh
//                        loadJobsJobsFromApi(null);
//                    } else {
//                        loadJobsJobsFromDb();
//                    }
//                }, throwable -> {
//                    // handle error
//                }, () -> {
//                    // handle on complete
//                });
//    }

    // This function gets JobsJobs from the API and saves to DB
    //TODO Sync Jobs with APi
//    public void loadJobsJobsFromApi(SwipeRefreshLayout swipeRefreshLayout) {
//        Log.d("BenD","jobsJobs is empty, look for API");
//        setIsLoading(true);
//        getCompositeDisposable().add(getDataManager()
//                .getJobsApiCall()
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(jobsResponse -> {
//                    if (jobsResponse != null) {
//                        writeJobsToDb(jobsResponse);
//                        if (swipeRefreshLayout != null){ swipeRefreshLayout.setRefreshing(false); }
//                    }
//                    setIsLoading(false);
//                }, throwable -> {
//                    setIsLoading(false);
//                    getNavigator().handleError(throwable);
//                }));
//    }

    public void loadJobsFromDb() {
        Log.d("BenD","Jobs retrieved from database");
        getCompositeDisposable().add(getDataManager()
                .getAllJobs()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(jobsList -> {
                    if (jobsList != null) {
                        jobsItemsLiveData.setValue(getViewModelList(jobsList));
                    }
                }, throwable -> {
                    throwable.getStackTrace();
                }));
    }

    public void deleteJob(String jobNumber) {
        getDataManager()
                .deleteJob(jobNumber)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe();
    }

    public ObservableList<JobsItemViewModel> getJobsItemViewModels() {
        return jobsItemViewModels;
    }

    public MutableLiveData<List<JobsItemViewModel>> getJobsRepos() {
        return jobsItemsLiveData;
    }

    public List<JobsItemViewModel> getViewModelList(List<BaseJob> jobsJobList) {
        List<JobsItemViewModel> jobsItemViewModels = new ArrayList<>();
        for (BaseJob job : jobsJobList) {
            jobsItemViewModels.add(new JobsItemViewModel(
                    job.getJobNumber(), job.getAddress(),
                    job.getClientName(), job.getJobType()));
        }
        return jobsItemViewModels;
    }
}
