package nz.co.k2.k2e.ui.navdrawer;

import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.ui.base.BaseViewModel;
import nz.co.k2.k2e.utils.rx.SchedulerProvider;

public class NavDrawerViewModel extends BaseViewModel<NavDrawerNavigator> {
    private final ObservableField<String> userEmail = new ObservableField<>();

    private final ObservableField<String> userName = new ObservableField<>();

    private final ObservableField<String> userProfilePicUrl = new ObservableField<>();

    public NavDrawerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public ObservableField<String> getUserEmail() {
        return userEmail;
    }

    public ObservableField<String> getUserName() {
        return userName;
    }

    public ObservableField<String> getUserProfilePicUrl() {
        return userProfilePicUrl;
    }

    public void onNavMenuCreated() {
        final String currentUserName = getDataManager().getCurrentUserName();
        if (!TextUtils.isEmpty(currentUserName)) {
            userName.set(currentUserName);
        }

        final String currentUserEmail = getDataManager().getCurrentUserEmail();
        if (!TextUtils.isEmpty(currentUserEmail)) {
            userEmail.set(currentUserEmail);
        }

        final String profilePicUrl = getDataManager().getCurrentUserProfilePicUrl();
        if (!TextUtils.isEmpty(profilePicUrl)) {
            userProfilePicUrl.set(profilePicUrl);
        }
        Log.d("BenD", userEmail.get() + userName.get());
    }
}
