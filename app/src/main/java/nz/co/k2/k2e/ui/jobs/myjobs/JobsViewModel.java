package nz.co.k2.k2e.ui.jobs.myjobs;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public JobsViewModel(DataManager dataManager,
                        SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        jobsItemsLiveData = new MutableLiveData<>();
        loadJobsFromDb();
    }

    public void addJobsItemsToList(List<JobsItemViewModel> jobsItems) {
        // sort repo list by alphabetical order
        Collections.sort(jobsItems, new Comparator<JobsItemViewModel>() {
            @Override
            public int compare(JobsItemViewModel o1, JobsItemViewModel o2) {
                return o1.getJobNumber().compareTo(o2.getJobNumber());
            }
        });
        jobsItemViewModels.clear();
        jobsItemViewModels.addAll(jobsItems);
    }

    public void loadJobsFromDb() {
        Log.d("BenD","Jobs retrieved from database");
        getCompositeDisposable().add(getDataManager()
                .getAllJobs()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(jobsList -> {
                    if (jobsList != null) {
//                        jobViewModels.addAll(jobsList);
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
