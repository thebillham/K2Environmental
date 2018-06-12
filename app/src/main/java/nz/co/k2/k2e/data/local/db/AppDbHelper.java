package nz.co.k2.k2e.data.local.db;

import android.databinding.ObservableBoolean;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;

@Singleton
public class AppDbHelper implements DbHelper {

    private final AppDatabase mAppDatabase;

    @Inject
    public AppDbHelper(AppDatabase appDatabase) {
        this.mAppDatabase = appDatabase;
    }
    //
    // WFM JOBS
    //

    // Get all Wfm Jobs from local database
    @Override
    public Observable<List<WfmJob>> getAllWfmJobs() {
        return Observable.fromCallable(() -> mAppDatabase.wfmJobDao().loadAll());
    }

    @Override
    public Single<WfmJob> getWfmJobByNumber(String jobNumber){
        return Single.fromCallable(() -> mAppDatabase.wfmJobDao().findByJobNumber(jobNumber));
    }

    @Override
    public Single<WfmJob> getWfmJobById(Long id) {
        return Single.fromCallable(() -> mAppDatabase.wfmJobDao().findById(id));
    }

    // Insert list into local database
    @Override
    public Single<Long> saveAllWfmJobs(List<WfmJob> wfmJobs) {
        return Single.fromCallable(() -> {
            Log.d("BenD","saveAllWfmJobs");
            //        Long row;
            if (wfmJobs.size() == 1){
                // Only one item in list, must be an insert
                Log.d("BenD","Job saved " + wfmJobs.get(0).getJobNumber());
                return mAppDatabase.wfmJobDao().insert(wfmJobs.get(0));
            } else {
                mAppDatabase.wfmJobDao().deleteAll();
                Long[] ids;
                ids = mAppDatabase.wfmJobDao().insertAll(wfmJobs);
                Log.d("BenD", " jobs replaced");
                return ids[0];
            }
        });
    }

    // Returns true if there are no jobs in the local database
    @Override
    public Single<Boolean> isWfmListEmpty() {
        return Single.fromCallable(() -> mAppDatabase.wfmJobDao().loadAll().isEmpty());
    }

    @Override
    public Single<Long> insertWfmJob(WfmJob wfmJob) {
        return Single.fromCallable(() -> mAppDatabase.wfmJobDao().insert(wfmJob));
    }

    @Override
    public Observable<List<BaseJob>> getAllJobs() {
        return Observable.fromCallable(() -> mAppDatabase.jobDao().loadAll());    }

    @Override
    public Single<Long> saveAllJobs(List<BaseJob> jobs) {
        return Single.fromCallable(() -> {
            mAppDatabase.jobDao().deleteAll();
            Long[] ids = mAppDatabase.jobDao().insertAll(jobs);
            return ids[0];
        });
    }

    @Override
    public Single<Long> insertJob(BaseJob job) {
        return Single.fromCallable(() -> mAppDatabase.jobDao().insert(job));
    }

    @Override
    public Single<BaseJob> getJobByJobNumber(String jobNumber) {
        return Single.fromCallable(() -> mAppDatabase.jobDao().findByJobNumber(jobNumber));
    }

    @Override
    public Single<BaseJob> getJobByUuid(String uuid) {
        return Single.fromCallable(() -> mAppDatabase.jobDao().findByUuid(uuid));
    }

    @Override
    public Single<Boolean> isJobListEmpty() {
        return null;
    }

    //
    // JOBS
    //


}
