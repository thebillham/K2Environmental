package nz.co.k2.k2e.data.local.db;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
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
    public Single<List<WfmJob>> getAllWfmJobs() {
        return Single.fromCallable(new Callable<List<WfmJob>>() {
            @Override
            public List<WfmJob> call() throws Exception {
                return mAppDatabase.wfmJobDao().loadAll();
            }
        });
    }

    @Override
    public Single<WfmJob> getWfmJobByNumber(String jobNumber){
        return Single.fromCallable(new Callable<WfmJob>() {
            @Override
            public WfmJob call() throws Exception {
                return mAppDatabase.wfmJobDao().findByJobNumber(jobNumber);
            }
        });
    }

    @Override
    public Single<WfmJob> getWfmJobById(Long id) {
        return Single.fromCallable(new Callable<WfmJob>() {
            @Override
            public WfmJob call() throws Exception {
                return mAppDatabase.wfmJobDao().findById(id);
            }
        });
    }

    // Insert list into local database
    @Override
    public Single<Long> saveAllWfmJobs(List<WfmJob> wfmJobs) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Log.d("BenD", "saveAllWfmJobs");
                //        Long row;
                if (wfmJobs.size() == 1) {
                    // Only one item in list, must be an insert
                    Log.d("BenD", "Job saved " + wfmJobs.get(0).getJobNumber());
                    return mAppDatabase.wfmJobDao().insert(wfmJobs.get(0));
                } else {
                    mAppDatabase.wfmJobDao().deleteAll();
                    Long[] ids;
                    ids = mAppDatabase.wfmJobDao().insertAll(wfmJobs);
                    Log.d("BenD", " jobs replaced");
                    return ids[0];
                }
            }
        });
    }

    // Returns true if there are no jobs in the local database
    @Override
    public Single<Boolean> isWfmListEmpty() {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return mAppDatabase.wfmJobDao().loadAll().isEmpty();
            }
        });
    }

    @Override
    public Single<Long> insertWfmJob(WfmJob wfmJob) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mAppDatabase.wfmJobDao().insert(wfmJob);
            }
        });
    }

    @Override
    public Observable<List<BaseJob>> getAllJobs() {
        return Observable.fromCallable(new Callable<List<BaseJob>>() {
            @Override
            public List<BaseJob> call() throws Exception {
                return mAppDatabase.jobDao().loadAll();
            }
        });    }

    @Override
    public Single<Long> saveAllJobs(List<BaseJob> jobs) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                mAppDatabase.jobDao().deleteAll();
                Long[] ids = mAppDatabase.jobDao().insertAll(jobs);
                return ids[0];
            }
        });
    }

    @Override
    public Single<Long> insertJob(BaseJob job) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mAppDatabase.jobDao().insert(job);
            }
        });
    }

    @Override
    public Single<BaseJob> getJobByJobNumber(String jobNumber) {
        return Single.fromCallable(new Callable<BaseJob>() {
            @Override
            public BaseJob call() throws Exception {
                return mAppDatabase.jobDao().findByJobNumber(jobNumber);
            }
        });
    }

    @Override
    public Single<BaseJob> getJobByUuid(String uuid) {
        return Single.fromCallable(new Callable<BaseJob>() {
            @Override
            public BaseJob call() throws Exception {
                return mAppDatabase.jobDao().findByUuid(uuid);
            }
        });
    }

    @Override
    public Single<Boolean> isJobListEmpty() {
        return null;
    }

    @Override
    public Completable deleteJob(String jobNumber) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAppDatabase.jobDao().deleteJob(jobNumber);
            }
        });
    }

    //
    // JOBS
    //


}
