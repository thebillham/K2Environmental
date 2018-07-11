package nz.co.k2.k2e.data;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import nz.co.k2.k2e.data.local.db.DbHelper;
import nz.co.k2.k2e.data.local.prefs.PreferencesHelper;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import nz.co.k2.k2e.data.remote.ApiHelper;

@Singleton
public class AppDataManager implements DataManager {

    private final ApiHelper mApiHelper;

    private final Context mContext;

    private final DbHelper mDbHelper;

    private final Gson mGson;

    private final PreferencesHelper mPreferencesHelper;

    private String userEmail = "";

    public CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public AppDataManager(Context context, DbHelper dbHelper, PreferencesHelper preferencesHelper, ApiHelper apiHelper, Gson gson) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mGson = gson;
    }

    /**
     * User Details
     */
    @Override
    public String getCurrentUserEmail() {
        return mPreferencesHelper.getCurrentUserEmail();
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPreferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public String getCurrentUserName() {
        return mPreferencesHelper.getCurrentUserName();
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPreferencesHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPreferencesHelper.getCurrentUserProfilePicUrl();
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPreferencesHelper.setCurrentUserProfilePicUrl(profilePicUrl);
    }

    @Override
    public Set<String> getCurrentUserMyJobNumbers() {
        return mPreferencesHelper.getCurrentUserMyJobNumbers();
    }

    @Override
    public void setCurrentUserMyJobNumbers(Set<String> myJobNumbers) {
        mPreferencesHelper.setCurrentUserMyJobNumbers(myJobNumbers);
    }

    @Override
    public void deleteCurrentUserMyJobNumber(String myJobNumber) {
        mPreferencesHelper.deleteCurrentUserMyJobNumber(myJobNumber);
    }

    @Override
    public void insertCurrentUserMyJobNumber(String myJobNumber) {
        mPreferencesHelper.insertCurrentUserMyJobNumber(myJobNumber);
    }

    /**
     * WFM
     */
    @Override
    public Single<List<WfmJob>> getWfmApiCall(String jobNumber) {
        return mApiHelper.getWfmApiCall(jobNumber).map(wList -> {saveAllWfmJobs(wList);
        return wList;});
    }

    @Override
    public void apiCreateJob(BaseJob baseJob) {
        Log.d("BenD", "ApiCreateJob");
        mApiHelper.apiCreateJob(baseJob);
    }

    @Override
    public Single<BaseJob> apiGetJobByUuid(String uuid) {
        return mApiHelper.apiGetJobByUuid(uuid);
    }

    @Override
    public Single<BaseJob> apiGetJobByJobNumber(String jobNumber) {
        return mApiHelper.apiGetJobByJobNumber(jobNumber);
    }

    @Override
    public Single<List<WfmJob>> getAllWfmJobs() {
        return mDbHelper.getAllWfmJobs();
    }

    @Override
    public Single<WfmJob> getWfmJobByNumber(String jobNumber) {
        return mDbHelper.getWfmJobByNumber(jobNumber);
    }

    @Override
    public Single<WfmJob> getWfmJobById(Long id) {
        return mDbHelper.getWfmJobById(id);
    }

    @Override
    public Single<Long> saveAllWfmJobs(List<WfmJob> wfmJobs) {
        return mDbHelper.saveAllWfmJobs(wfmJobs);
    }

    @Override
    public Single<Long> insertWfmJob(WfmJob wfmJob) {
        return mDbHelper.insertWfmJob(wfmJob);
    }

    @Override
    public Single<Boolean> isWfmListEmpty() {
        return mDbHelper.isWfmListEmpty();
    }

    @Override
    public Observable<List<BaseJob>> getAllJobs() {
        return mDbHelper.getAllJobs();
    }

    @Override
    public Single<Long> saveAllJobs(List<BaseJob> jobs) {
        return mDbHelper.saveAllJobs(jobs);
    }

    @Override
    public Single<Long> insertJob(BaseJob job) {
        Log.d("BenD", "Insert job");
        return mDbHelper.insertJob(job);
    }

    @Override
    public Maybe<BaseJob> getJobByJobNumber(String jobNumber) {
        return mDbHelper.getJobByJobNumber(jobNumber);
    }

    @Override
    public Single<BaseJob> getJobByUuid(String uuid) {
        return mDbHelper.getJobByUuid(uuid);
    }

    @Override
    public Single<Boolean> isJobListEmpty() {
        return mDbHelper.isJobListEmpty();
    }

    @Override
    public Completable deleteJob(String jobNumber) {
        return mDbHelper.deleteJob(jobNumber);
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String email) {
        this.userEmail = email;
    }
//
//    @Override
//    public Flowable pushJob(BaseJob baseJob) {
//        return Flowable.merge(mDbHelper.insertJob(baseJob), mApiHelper.apiCreateJob(baseJob));
//    }

    public Single<Long> pushJob(BaseJob baseJob){
        apiCreateJob(baseJob);
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(result -> Log.d("BenD", String.valueOf(result)));
        return insertJob(baseJob);
//        return Single.merge(insertJob(baseJob),
//                apiCreateJob(baseJob)).singleOrError();
    }

    @Override
    public void updateUserInfo(String userName, String email, String profilePicPath) {
        mPreferencesHelper.setCurrentUserName(userName);
        mPreferencesHelper.setCurrentUserEmail(email);
        mPreferencesHelper.setCurrentUserProfilePicUrl(profilePicPath);
    }

    @Override
    public Single<List<WfmJob>> getWfmList(Boolean forceRefresh) {
        if (forceRefresh){
            return getWfmApiCall(null);
        } else return getAllWfmJobs()
                .flatMap(wfmList -> wfmList.isEmpty()
                                ? getWfmApiCall(null)
                                : Single.just(wfmList));
    }
}
