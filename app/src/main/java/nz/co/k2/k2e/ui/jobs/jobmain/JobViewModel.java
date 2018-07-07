package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.LiveData;
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
import nz.co.k2.k2e.ui.jobs.myjobs.JobsItemViewModel;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsNavigator;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Singleton
public class JobViewModel extends BaseViewModel<JobsNavigator> {

    public BaseJob currentJob;
    public String mJobNumber;

    public JobViewModel(DataManager dataManager,
                        SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        currentJob = new BaseJob();
    }


    // This function gets JobsJobs from the API and saves to DB
    //TODO Sync Job with APi


    public void loadJobFromDb(String jobNumber) {
        mJobNumber = jobNumber;
        Log.d("BenD","Job " + jobNumber + " retrieved from database");
        getCompositeDisposable().add(getDataManager()
                .getJobByJobNumber(jobNumber)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(job -> {
                        currentJob=job;
//                        Log.d("BenD",currentJob.getValue().getAddress());
                }, throwable -> {
                    throwable.getStackTrace();
                }));
    }

    public BaseJob getCurrentJob(){
        return currentJob;
    }

}
