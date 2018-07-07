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
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
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
                        success = true;
                        wfmItemsLiveData.setValue(getViewModelList(wfmList));
                        Log.d("BenD","Saved all jobs " + wfmList.size());
//                        getDataManager().saveAllWfmJobs(wfmList);
                    }
                }));
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
                            success = true;
                            getDataManager().saveAllWfmJobs(wfmJobs);
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
