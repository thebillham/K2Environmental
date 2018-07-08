package nz.co.k2.k2e.ui.jobs.jobmain;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import nz.co.k2.k2e.ui.base.BaseViewModel;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsItemViewModel;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsNavigator;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

@Singleton
public class JobViewModel extends BaseViewModel<JobsNavigator> {

    public final ObservableField<BaseJob> currentJob = new ObservableField<>();
//    public final ObservableField<String> jobNumber = new ObservableField<>();
//    public final ObservableField<String> address = new ObservableField<>();
//    public final ObservableField<String> clientName = new ObservableField<>();
//    public final ObservableField<String> type = new ObservableField<>();
//    public final ObservableField<Integer> typeIcon = new ObservableField<>();
//    public final ObservableField<String> state = new ObservableField<>();

    public JobViewModel(DataManager dataManager,
                        SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
//        TODO https://www.bignerdranch.com/blog/two-way-data-binding-on-android-observing-your-view-with-xml/

//        Log.d("BenD", type + ": " + String.valueOf(typeIcon.get()));
    }


    // This function gets JobsJobs from the API and saves to DB
    //TODO Sync Job with APi


    public void loadJobFromDb(String jobNum) {
        getCompositeDisposable().add(getDataManager()
                .getJobByJobNumber(jobNum)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(job -> {
                        currentJob.set(job);
//                        jobNumber.set(job.getJobNumber());
//                        address.set(job.getAddress());
//                        clientName.set(job.getClientName());
//                        type.set(job.getJobType());
//                        if (type.get() == null){
//                        this.typeIcon.set(R.drawable.ic_my_jobs);
//                    } else if (type.get().toLowerCase().contains("asbestos")){
//                        this.typeIcon.set(R.drawable.ic_job_asbestos);
//                    } else if (type.get().toLowerCase().contains("meth")){
//                        this.typeIcon.set(R.drawable.ic_job_meth);
//                    } else if (type.get().toLowerCase().contains("noise")) {
//                        this.typeIcon.set(R.drawable.ic_job_noise);
//                    } else if (type.get().toLowerCase().contains("bio")){
//                        this.typeIcon.set(R.drawable.ic_job_bio);
//                    } else if (type.get().toLowerCase().contains("stack")){
//                        this.typeIcon.set(R.drawable.ic_job_stack);
//                    } else {
//                        this.typeIcon.set(R.drawable.ic_my_jobs);
//                    }
                }, throwable -> {
                    throwable.getStackTrace();
                }));
    }

}
