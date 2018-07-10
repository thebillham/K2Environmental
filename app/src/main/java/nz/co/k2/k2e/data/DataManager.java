package nz.co.k2.k2e.data;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import nz.co.k2.k2e.data.local.db.DbHelper;
import nz.co.k2.k2e.data.local.prefs.PreferencesHelper;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import nz.co.k2.k2e.data.remote.ApiHelper;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmHelper;


public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper, WfmHelper {
    String userEmail="";

    public String getUserEmail();

    public void setUserEmail(String email);

    public Single<Long> pushJob(BaseJob baseJob);
//
    void updateUserInfo(
            String userName,
            String email,
            String profilePicPath);

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }

    }
}
