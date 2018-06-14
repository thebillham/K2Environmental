package nz.co.k2.k2e.data;

import java.util.List;

import io.reactivex.Observable;
import nz.co.k2.k2e.data.local.db.DbHelper;
import nz.co.k2.k2e.data.local.prefs.PreferencesHelper;

import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.remote.ApiHelper;


public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper {
    String userEmail="";

    public String getUserEmail();

    public void setUserEmail(String email);
//
    void updateUserInfo(
            String userName,
            String email,
            String profilePicPath);

    Observable<List<WfmJob>> getWfmList(Boolean forceRefresh);

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
